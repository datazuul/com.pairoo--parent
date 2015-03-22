package com.pairoo.business.api;

import java.util.List;

import com.datazuul.framework.business.services.DomainObjectService;
import com.datazuul.framework.domain.geo.Country;
import com.datazuul.framework.domain.geo.Subdivision;

/**
 * @author Ralf Eichinger
 */
public interface SubdivisionService extends DomainObjectService<Long, Subdivision> {
    public static final String BEAN_ID = "subdivisionService";

    /**
     * Find Subdivisions of the given country.
     * 
     * @param country
     *            search param
     * @return matching Subdivisions
     */
    public List<Subdivision> getByCountry(Country country);

    /**
     * Gets recursively all subdvisions under a given dubdivision (inclusively).
     * 
     * @param subdivision
     *            given subdivision
     * @return list of all subdvisions
     */
    public List<Subdivision> getAllSmallestRecursively(Subdivision subdivision);
}
