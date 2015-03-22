package com.pairoo.business.api;

import com.datazuul.framework.business.services.DomainObjectService;
import com.googlecode.genericdao.search.Search;
import com.pairoo.business.exceptions.AlreadyExistsException;
import com.pairoo.domain.Favorite;
import com.pairoo.domain.User;
import com.pairoo.domain.search.FavoriteSearchResult;
import java.util.List;

/**
 * @author Ralf Eichinger
 */
public interface FavoriteService extends DomainObjectService<Long, Favorite> {

    public static final String BEAN_ID = "favoriteService";

    /**
     * Add an user as favorite
     *
     * @param user the user to be added
     */
    public void add(User owner, User target) throws AlreadyExistsException;

    /**
     * Count favorites of the given user that are online.
     *
     * @param user given user
     * @return number of online favorites
     */
    public int countFavoritesOnline(User user);

    /**
     * Create a Search for getting the favorites of the given user
     *
     * @param owner owner of the favorites
     * @return search criteria object
     */
    public Search getFavoritesSearchCriteria(User owner);

    /**
     * Searching for favorites of given user
     *
     * @param user searching user
     * @param first paging start
     * @param count paging size
     * @return list of results
     */
    public List<FavoriteSearchResult> search(final User user, final long first, final long count);

    /**
     * Count favorites of given user
     *
     * @param user searching user
     * @return number of results
     */
    public long count(User user);
}
