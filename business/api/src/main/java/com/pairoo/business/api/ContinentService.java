package com.pairoo.business.api;

import java.util.List;

import com.datazuul.framework.domain.geo.Continent;
import com.datazuul.framework.domain.geo.Country;

/**
 * @author Ralf Eichinger
 */
public interface ContinentService {
	public static final String BEAN_ID = "continentService";

	/**
	 * Find Countries located in the continent.
	 *
	 * @param continent
	 *            search param
	 * @return list of countries
	 */
	public List<Country> getCountries(Continent continent);
}
