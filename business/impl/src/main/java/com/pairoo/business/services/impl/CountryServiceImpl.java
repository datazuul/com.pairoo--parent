package com.pairoo.business.services.impl;

import java.util.ArrayList;
import java.util.List;

import com.datazuul.framework.domain.geo.Country;
import com.pairoo.business.api.CountryService;

/**
 * @author ralf
 */
public class CountryServiceImpl implements CountryService {
    @Override
    public List<Country> getAll() {
	final List<Country> result = new ArrayList<Country>();
	final Country[] countries = Country.values();
	for (final Country country : countries) {
	    result.add(country);
	}
	return result;
    }

    @Override
    public Country getCountryByCode(final String countryCode) {
	final Country[] countries = Country.values();
	for (final Country country : countries) {
	    if (country.getCountryCode() != null && country.getCountryCode().equalsIgnoreCase(countryCode)) {
		return country;
	    }
	}
	return null;
    }

    @Override
    public List<Country> getSelectableCountries() {
	final List<Country> countries = new ArrayList<Country>();
	countries.add(Country.ANY);
	countries.add(Country.AUSTRIA);
	countries.add(Country.CANADA);
	countries.add(Country.FRANCE);
	countries.add(Country.GERMANY);
	countries.add(Country.ITALY);
	countries.add(Country.SPAIN);
	countries.add(Country.SWITZERLAND);
	countries.add(Country.UNITED_KINGDOM);
	countries.add(Country.UNITED_STATES);
	return countries;
    }
}
