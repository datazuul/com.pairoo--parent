package com.pairoo.business.services.impl.demo;

import java.util.List;

import com.googlecode.genericdao.search.ISearch;
import com.googlecode.genericdao.search.Search;
import com.pairoo.business.api.FavoriteService;
import com.pairoo.business.exceptions.AlreadyExistsException;
import com.pairoo.domain.Favorite;
import com.pairoo.domain.User;
import com.pairoo.domain.search.FavoriteSearchResult;

/**
 * @author Ralf Eichinger
 */
public class FavoriteServiceImpl implements FavoriteService {

    /* (non-Javadoc)
     * @see com.pairoo.business.api.FavoriteService#add(com.pairoo.domain.User, com.pairoo.domain.User)
     */
    @Override
    public void add(final User owner, final User target) throws AlreadyExistsException {
        // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
     * @see de.javapro.framework.business.services.DomainObjectService#count(com.googlecode.genericdao.search.ISearch)
     */
    @Override
    public long count(final ISearch search) {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see de.javapro.framework.business.services.DomainObjectService#countAll()
     */
    @Override
    public Long countAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int countFavoritesOnline(final User user) {
        return 1;
    }

    /* (non-Javadoc)
     * @see de.javapro.framework.business.services.DomainObjectService#delete(de.javapro.framework.domain.DomainObject)
     */
    @Override
    public boolean delete(final Favorite object) {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see de.javapro.framework.business.services.DomainObjectService#findAll(int, int)
     */
    @Override
    public List<Favorite> findAll(final int offset, final int max) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.javapro.framework.business.services.DomainObjectService#get(java.io.Serializable)
     */
    @Override
    public Favorite get(final Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.pairoo.business.api.FavoriteService#getFavoritesSearchCriteria(com.pairoo.domain.User)
     */
    @Override
    public Search getFavoritesSearchCriteria(final User owner) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.javapro.framework.business.services.DomainObjectService#save(de.javapro.framework.domain.DomainObject)
     */
    @Override
    public boolean save(final Favorite object) {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see de.javapro.framework.business.services.DomainObjectService#update(de.javapro.framework.domain.DomainObject)
     */
    @Override
    public void update(final Favorite object) {
        // TODO Auto-generated method stub
    }

    @Override
    public List<FavoriteSearchResult> search(User user, long first, long count) {
        return null;
    }

    @Override
    public long count(User user) {
        return 0;
    }

    @Override
    public List<Favorite> search(ISearch is) {
        return null;
    }
}
