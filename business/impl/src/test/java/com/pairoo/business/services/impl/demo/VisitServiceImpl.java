package com.pairoo.business.services.impl.demo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.googlecode.genericdao.search.ISearch;
import com.googlecode.genericdao.search.Search;
import com.pairoo.business.api.VisitService;
import com.pairoo.business.services.impl.DataRepository;
import com.pairoo.domain.User;
import com.pairoo.domain.Visit;
import com.pairoo.domain.search.VisitSearchResult;

/**
 * @author Ralf Eichinger
 */
public class VisitServiceImpl implements VisitService {

    private final DataRepository repo;

    public VisitServiceImpl(final DataRepository repo) {
        super();
        this.repo = repo;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.javapro.framework.business.services.DomainObjectService#count(com.
     * googlecode.genericdao.search.ISearch)
     */
    @Override
    public long count(final ISearch search) {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.javapro.framework.business.services.DomainObjectService#countAll()
     */
    @Override
    public Long countAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long countVisitsSinceLastLogin(final User visitedUser) {
        return 1;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.javapro.framework.business.services.DomainObjectService#delete(de.
     * javapro.framework.domain.DomainObject)
     */
    @Override
    public boolean delete(final Visit object) {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.javapro.framework.business.services.DomainObjectService#findAll(int,
     * int)
     */
    @Override
    public List<Visit> findAll(final int offset, final int max) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.javapro.framework.business.services.DomainObjectService#get(java.io
     * .Serializable)
     */
    @Override
    public Visit get(final Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.pairoo.business.api.VisitService#getVisitsSearchCriteria(com.pairoo
     * .domain.User)
     */
    @Override
    public Search getVisitsSearchCriteria(final User visitedUser) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Visit> getVisitsSinceLastLogin(final User visitedUser) {
        final Visit visit = new Visit();
        visit.setVisitedUser(visitedUser);
        visit.setVisitor(repo.getUserList().get(0));
        visit.setTimeStamp(new Date());
        final ArrayList<Visit> list = new ArrayList<Visit>();
        list.add(visit);
        return list;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.javapro.framework.business.services.DomainObjectService#save(de.javapro
     * .framework.domain.DomainObject)
     */
    @Override
    public boolean save(final Visit object) {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.javapro.framework.business.services.DomainObjectService#search(com
     * .googlecode.genericdao.search.ISearch)
     */
    @Override
    public List<Visit> search(final ISearch search) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.javapro.framework.business.services.DomainObjectService#update(de.
     * javapro.framework.domain.DomainObject)
     */
    @Override
    public void update(final Visit object) {
        // TODO Auto-generated method stub
    }

    @Override
    public void add(final User visitedUser, final User visitor, final String actionUrl, final Locale locale) {
        // TODO Auto-generated method stub
    }

    @Override
    public int getMaxShownThumbnails() {
        return 4;
    }

    @Override
    public List<VisitSearchResult> search(User user, long first, long count) {
        return null;
    }

    @Override
    public long count(User user) {
        return 0;
    }
}
