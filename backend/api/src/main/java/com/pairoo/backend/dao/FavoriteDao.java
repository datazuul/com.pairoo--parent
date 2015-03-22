package com.pairoo.backend.dao;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.pairoo.domain.Favorite;
import com.pairoo.domain.User;
import com.pairoo.domain.search.FavoriteSearchResult;
import java.util.List;

/**
 * @author Ralf Eichinger
 */
public interface FavoriteDao extends GenericDAO<Favorite, Long> {

    List<FavoriteSearchResult> search(User user, final long first, final long count);

    public long count(User user);
}
