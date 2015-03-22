package com.pairoo.business.services.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.datazuul.framework.domain.geo.Continent;
import com.datazuul.framework.domain.geo.Country;
import com.pairoo.business.api.ContinentService;

/**
 * @author ralf
 */
public class ContinentServiceImpl implements ContinentService {

	/* (non-Javadoc)
	 * @see com.pairoo.business.services.ContinentService#getCountries(com.pairoo.domain.enums.Continent)
	 */
	@Override
	public List<Country> getCountries(Continent continent) {
		Country[] countries = continent.getCountries();
		if (countries == null) {
			return Collections.EMPTY_LIST;
		}
		return Arrays.asList(countries);
	}

}
