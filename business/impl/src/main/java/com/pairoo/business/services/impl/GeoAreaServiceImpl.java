package com.pairoo.business.services.impl;

import com.datazuul.framework.business.services.persistence.AbstractDomainObjectServiceImpl;
import com.pairoo.backend.dao.GeoAreaDao;
import com.pairoo.business.api.GeoAreaService;
import com.pairoo.domain.geo.GeoArea;

/**
 * @author ralf
 */
public class GeoAreaServiceImpl extends AbstractDomainObjectServiceImpl<Long, GeoArea> implements GeoAreaService {

    /**
     * @param dao
     *            the data access interface
     */
    public GeoAreaServiceImpl(final GeoAreaDao dao) {
	super(dao);
    }
}