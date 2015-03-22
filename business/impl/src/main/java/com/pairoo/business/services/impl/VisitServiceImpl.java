package com.pairoo.business.services.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.datazuul.framework.business.services.persistence.AbstractDomainObjectServiceImpl;
import com.googlecode.genericdao.search.Search;
import com.pairoo.backend.dao.VisitDao;
import com.pairoo.backend.sao.EmailSao;
import com.pairoo.business.LocalizedStrings;
import com.pairoo.business.api.MessageService;
import com.pairoo.business.api.VisitService;
import com.pairoo.domain.Message;
import com.pairoo.domain.User;
import com.pairoo.domain.Visit;
import com.pairoo.domain.search.VisitSearchResult;

/**
 * @author Ralf Eichinger
 */
public class VisitServiceImpl extends AbstractDomainObjectServiceImpl<Long, Visit> implements VisitService {

    static final Logger LOGGER = LoggerFactory.getLogger(VisitServiceImpl.class);
    @Autowired
    private EmailSao emailSao;
    private final MessageService messageService;
    private VisitDao dao;

    /**
     * Constructor needed for test handing over a mock dao.
     *
     * @param dao the data access interface
     */
    public VisitServiceImpl(final VisitDao dao, final MessageService messageService) {
        super(dao);
        this.dao = dao;
        this.messageService = messageService;
    }

    @Override
    public void add(final User visitedUser, final User visitor, String actionUrl, final Locale locale) {
        // search for previous visit
        final Search search = new Search(Visit.class);
        search.addFilterEqual("visitedUser.id", visitedUser.getId());
        search.addFilterEqual("visitor.id", visitor.getId());
        final List<Visit> listVisits = search(search);

        boolean sameDayVisit = false;
        Visit visit;
        if (!listVisits.isEmpty()) {
            // if previous visit found: use it
            visit = listVisits.get(0);

            // check if there was a visit today before
            final Calendar now = Calendar.getInstance();
            final Calendar visitCal = Calendar.getInstance();
            visitCal.setTimeInMillis(visit.getTimeStamp().getTime());
            if (DateUtils.isSameDay(now, visitCal)) {
                sameDayVisit = true;
            }

        } else {
            // if no previous visit found: create new one
            visit = new Visit();
            visit.setVisitedUser(visitedUser);
            visit.setVisitor(visitor);
        }

        // update to timestamp of current visit
        visit.setTimeStamp(new Date());

        // 1. save
        // FIXME: NonUniqueObjectException: a different object with the same
        // identifier value was already associated with the session:
        // [com.pairoo.domain.Visit#9]
        // ProfileDetailsPage.java:138
        save(visit);

        // 2. send notification email to visited user
        // stop sending visit email if there was a visit already this
        // same day before
        if (!sameDayVisit) {
            // create message
            final User sender = new User();
            sender.setEmail(LocalizedStrings.get(LocalizedStrings.EMAIL_ADDRESS_PAIROO, locale));
            final Map<String, Object> model = new HashMap<>();
            model.put("visitedUser", visitedUser);
            model.put("visitor", visitor);
            model.put("actionUrl", actionUrl);
            final Message msg = messageService.createMessage(visitedUser, sender, locale,
                    LocalizedStrings.get(LocalizedStrings.EMAIL_USERPROFILE_VISITED_SUBJECT, locale),
                    "com/pairoo/business/templates/email-userprofile-visited-txt", model);

            try {
                emailSao.sendMail(msg);
            } catch (final Exception e) {
                e.printStackTrace();
            }

            LOGGER.info("Visited notification sent to {}", visitedUser.getEmail());
        }
    }

    @Override
    public long countVisitsSinceLastLogin(final User visitedUser) {
        final Search searchCriteria = getVisitsSearchCriteria(visitedUser);
        searchCriteria.addFilterGreaterThan("timeStamp", visitedUser.getUserAccount().getPreviousLogin());
        return count(searchCriteria);
    }

    @Override
    public Search getVisitsSearchCriteria(final User visitedUser) {
        final Search search = new Search(Visit.class);

        // visited user is given user
        search.addFilterEqual("visitedUser.id", visitedUser.getId());
        // search.setMaxResults(4);
        return search;
    }

    @Override
    public List<Visit> getVisitsSinceLastLogin(final User visitedUser) {
        final Search searchCriteria = getVisitsSearchCriteria(visitedUser);
        searchCriteria.addFilterGreaterThan("timeStamp", visitedUser.getUserAccount().getPreviousLogin());
        return search(searchCriteria);
    }

    @Override
    public int getMaxShownThumbnails() {
        return 4;
    }

    @Override
    public List<VisitSearchResult> search(User user, long first, long count) {
        return dao.search(user, first, count);
    }

    @Override
    public long count(User user) {
        return dao.count(user);
    }
}
