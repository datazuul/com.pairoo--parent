package com.pairoo.business.api;

import java.util.List;
import java.util.Locale;

import com.datazuul.framework.business.services.DomainObjectService;
import com.googlecode.genericdao.search.Search;
import com.pairoo.domain.User;
import com.pairoo.domain.Visit;
import com.pairoo.domain.search.VisitSearchResult;

/**
 * @author Ralf Eichinger
 */
public interface VisitService extends DomainObjectService<Long, Visit> {

    public static final String BEAN_ID = "visitService";

    /**
     * Add a visit.
     *
     * @param visitedUser visited user
     * @param visitor user that visits the visited user
     * @param actionUrl landingpage url to this action
     * @param locale locale for translations
     */
    public void add(final User visitedUser, final User visitor, final String actionUrl, final Locale locale);

    /**
     * Count visits since last login of given user.
     *
     * @param visitedUser visited user
     * @return number of visits since last login
     */
    public long countVisitsSinceLastLogin(User visitedUser);

    /**
     * Create a Search for getting the visits of the given user
     *
     * @param owner owner of the favorites
     * @return search criteria object
     */
    public Search getVisitsSearchCriteria(User visitedUser);

    /**
     * Get list of visits since last login of given user.
     *
     * @param visitedUser visited user
     * @return list of visits since last login
     */
    public List<Visit> getVisitsSinceLastLogin(User visitedUser);

    /**
     * @return maximum number of thumbnails shown for new visits
     */
    public int getMaxShownThumbnails();

    /**
     * Searching for visits of given user
     *
     * @param user searching user
     * @param first paging start
     * @param count paging size
     * @return list of results
     */
    public List<VisitSearchResult> search(final User user, final long first, final long count);

    /**
     * Count visits of given user
     *
     * @param user searching user
     * @return number of results
     */
    public long count(User user);
}
