package com.pairoo.business.services.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datazuul.framework.business.services.persistence.AbstractDomainObjectServiceImpl;
import com.googlecode.genericdao.search.Search;
import com.pairoo.backend.dao.FavoriteDao;
import com.pairoo.business.api.FavoriteService;
import com.pairoo.business.exceptions.AlreadyExistsException;
import com.pairoo.domain.Favorite;
import com.pairoo.domain.User;
import com.pairoo.domain.search.FavoriteSearchResult;

/**
 * @author Ralf Eichinger
 */
public class FavoriteServiceImpl extends AbstractDomainObjectServiceImpl<Long, Favorite> implements FavoriteService {

    static final Logger LOGGER = LoggerFactory.getLogger(FavoriteServiceImpl.class);
    private FavoriteDao dao;

    /**
     * Constructor needed for test handing over a mock dao.
     *
     * @param dao the data access interface
     */
    public FavoriteServiceImpl(final FavoriteDao dao) {
        super(dao);
        this.dao = dao;
    }

    @Override
    public void add(final User owner, final User target) throws AlreadyExistsException {
        // search if already favorite
        final Search search = new Search(Favorite.class);
        search.addFilterEqual("owner.id", owner.getId());
        search.addFilterEqual("target.id", target.getId());
        final List<Favorite> listFavorites = search(search);

        Favorite favorite = null;
        if (!listFavorites.isEmpty()) {
            // if already favorite: do nothing
            throw new AlreadyExistsException();
        } else {
            // if not favorite, yet
            favorite = new Favorite();
            favorite.setTimeStamp(new Date());
            favorite.setOwner(owner);
            favorite.setTarget(target);

            // 1. save
            final boolean saved = save(favorite);

            // 2. notifications?
        }
    }

    @Override
    public int countFavoritesOnline(final User user) {
        final Search search = new Search(Favorite.class);
        // owner is given user
        search.addFilterEqual("owner.id", user.getId());
        // target useraccount is online
        search.addFilterEqual("target.userAccount.online", true);

        return dao.count(search);
    }

    @Override
    public Search getFavoritesSearchCriteria(final User owner) {
        final Search search = new Search(Favorite.class);

        // owner is given user
        search.addFilterEqual("owner.id", owner.getId());

        return search;
    }

    @Override
    public List<FavoriteSearchResult> search(User user, long first, long count) {
        return dao.search(user, first, count);
    }

    @Override
    public long count(User user) {
        return dao.count(user);
    }
}
