package com.pairoo.backend.dao;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.pairoo.backend.dao.search.DaoUserSearchParams;
import com.pairoo.domain.User;
import com.pairoo.domain.search.UserSearchResult;
import java.util.List;

/**
 * @author Ralf Eichinger
 */
public interface UserDao extends GenericDAO<User, Long> {

    List<UserSearchResult> search(DaoUserSearchParams daoUserSearchParams, final long first, final long count);

    public long count(DaoUserSearchParams daoUserSearchParams);
}
