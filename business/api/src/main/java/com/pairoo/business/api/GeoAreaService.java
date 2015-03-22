package com.pairoo.business.api;

import com.datazuul.framework.business.services.DomainObjectService;
import com.pairoo.domain.geo.GeoArea;

public interface GeoAreaService extends DomainObjectService<Long, GeoArea> {
    public static final String BEAN_ID = "geoAreaService";
}