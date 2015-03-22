package com.pairoo.backend.db.hibernate;

import com.datazuul.framework.domain.geo.GeoLocation;
import com.datazuul.framework.persistence.dao.hibernate.ExtendedGenericDaoImpl;
import com.pairoo.backend.dao.GeoLocationDao;

/**
 * @author Ralf Eichinger
 */
public class GeoLocationDaoImpl extends ExtendedGenericDaoImpl<GeoLocation, Long> implements GeoLocationDao {
}