package com.pairoo.business.services.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datazuul.framework.business.services.persistence.AbstractDomainObjectServiceImpl;
import com.datazuul.framework.domain.geo.Country;
import com.datazuul.framework.domain.geo.Subdivision;
import com.googlecode.genericdao.search.Search;
import com.pairoo.backend.dao.SubdivisionDao;
import com.pairoo.business.api.SubdivisionService;

/**
 * @author Ralf Eichinger
 */
public class SubdivisionServiceImpl extends AbstractDomainObjectServiceImpl<Long, Subdivision> implements
	SubdivisionService {
    static final Logger LOGGER = LoggerFactory.getLogger(SubdivisionServiceImpl.class);

    /**
     * Constructor needed for test handing over a dao.
     * 
     * @param dao
     *            the data access interface
     */
    public SubdivisionServiceImpl(final SubdivisionDao dao) {
	super(dao);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.pairoo.business.services.SubdivisionService#getByCountry(com.pairoo
     * .domain.enums.Country)
     */
    @Override
    public List<Subdivision> getByCountry(final Country country) {
	LOGGER.info("country = {}", country.getCountryCode());
	final Search search = new Search();

	// Subdivision example = new Subdivision();
	// example.setCountry(country);
	// Filter filter = dao.getFilterFromExample(example);
	// search.addFilters(filter);

	search.addFilterEqual("countryCode", country.getCountryCode());

	return ((SubdivisionDao) dao).search(search);
    }

    @Override
    public List<Subdivision> getAllSmallestRecursively(final Subdivision subdivision) {
	// FIXME implement: until now just parent is set, children to be fetched
	// all by DB accesses (too expensive...)

	return null;
    }

}
