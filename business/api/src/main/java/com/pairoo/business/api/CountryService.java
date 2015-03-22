package com.pairoo.business.api;

import java.util.List;

import com.datazuul.framework.domain.geo.Country;

/**
 * @author Ralf Eichinger
 */
public interface CountryService {
    public static final String BEAN_ID = "countryService";

    /**
     * @return all countries
     */
    public List<Country> getAll();

    public Country getCountryByCode(String countryCode);

    public List<Country> getSelectableCountries();
}
