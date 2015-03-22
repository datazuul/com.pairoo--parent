package com.pairoo.backend.dao;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.pairoo.domain.User;
import com.pairoo.domain.Visit;
import com.pairoo.domain.search.VisitSearchResult;
import java.util.List;

/**
 * @author Ralf Eichinger
 */
public interface VisitDao extends GenericDAO<Visit, Long> {

    List<VisitSearchResult> search(User user, final long first, final long count);

    public long count(User user);
}
