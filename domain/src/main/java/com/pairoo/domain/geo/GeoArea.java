package com.pairoo.domain.geo;

import com.datazuul.framework.domain.AbstractPersistentDomainObject;
import com.datazuul.framework.domain.geo.Continent;
import com.datazuul.framework.domain.geo.Country;
import com.datazuul.framework.domain.geo.GeoLocation;
import com.datazuul.framework.domain.geo.Subdivision;
import com.pairoo.domain.enums.MaxDistance;

public class GeoArea extends AbstractPersistentDomainObject {
    private static final long serialVersionUID = 1L;

    private Continent continent = null;
    private Country country = Country.ANY;
    private Subdivision subdivision = null;
    private GeoLocation geoLocation = null;
    private String zipcodeStartsWith = null;
    private MaxDistance maxDistance = MaxDistance.ANY;

    /**
     * @return the continent
     */
    public Continent getContinent() {
	return continent;
    }

    /**
     * @param continent
     *            the continent to set
     */
    public void setContinent(final Continent continent) {
	this.continent = continent;
    }

    /**
     * @return the country
     */
    public Country getCountry() {
	return country;
    }

    /**
     * @param country
     *            the country to set
     */
    public void setCountry(final Country country) {
	this.country = country;
    }

    /**
     * @return the subdivision
     */
    public Subdivision getSubdivision() {
	return subdivision;
    }

    /**
     * @param subdivision
     *            the subdivision to set
     */
    public void setSubdivision(final Subdivision subdivision) {
	this.subdivision = subdivision;
    }

    /**
     * @return the geoLocation
     */
    public GeoLocation getGeoLocation() {
	return geoLocation;
    }

    /**
     * @param geoLocation
     *            the geoLocation to set
     */
    public void setGeoLocation(final GeoLocation geoLocation) {
	this.geoLocation = geoLocation;
    }

    /**
     * @return the maxDistance
     */
    public MaxDistance getMaxDistance() {
	return maxDistance;
    }

    /**
     * @param maxDistance
     *            the maxDistance to set
     */
    public void setMaxDistance(final MaxDistance maxDistance) {
	this.maxDistance = maxDistance;
    }

    /**
     * @return the zipcodeStartsWith
     */
    public String getZipcodeStartsWith() {
	return zipcodeStartsWith;
    }

    /**
     * @param zipcodeStartsWith
     *            the zipcodeStartsWith to set
     */
    public void setZipcodeStartsWith(final String zipcodeStartsWith) {
	this.zipcodeStartsWith = zipcodeStartsWith;
    }

}
