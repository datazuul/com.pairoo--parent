package com.pairoo.backend.db.hibernate;

import com.datazuul.framework.persistence.dao.hibernate.ExtendedGenericDaoImpl;
import com.pairoo.backend.dao.GeoAreaDao;
import com.pairoo.domain.geo.GeoArea;

/**
 * @author Ralf Eichinger
 */
public class GeoAreaDaoImpl extends ExtendedGenericDaoImpl<GeoArea, Long> implements GeoAreaDao {
}