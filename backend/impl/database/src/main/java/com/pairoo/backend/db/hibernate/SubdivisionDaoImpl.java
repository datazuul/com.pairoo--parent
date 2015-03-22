package com.pairoo.backend.db.hibernate;

import com.datazuul.framework.domain.geo.Subdivision;
import com.datazuul.framework.persistence.dao.hibernate.ExtendedGenericDaoImpl;
import com.pairoo.backend.dao.SubdivisionDao;

/**
 * @author Ralf Eichinger
 */
public class SubdivisionDaoImpl extends ExtendedGenericDaoImpl<Subdivision, Long> implements SubdivisionDao {
}
