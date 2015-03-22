package com.pairoo.domain;

import com.datazuul.framework.domain.AbstractPersistentDomainObject;
import com.datazuul.framework.domain.geo.Country;
import com.datazuul.framework.domain.geo.GeoLocation;

/**
 * @author ralf
 */
public class Address extends AbstractPersistentDomainObject {
    private static final long serialVersionUID = 1L;

    private GeoLocation geoLocation; // contains zipcode, country, name==city if
				     // exact location found otherwise == null
    private String zipcode;
    private String street;
    private String housenr;
    private String city;
    private Country country;

    /**
     * @param zipcode
     *            the zipcode to set
     */
    public void setZipcode(String zipcode) {
	this.zipcode = zipcode;
    }

    /**
     * @return the zipcode
     */
    public String getZipcode() {
	return zipcode;
    }

    /**
     * @param street
     *            the street to set
     */
    public void setStreet(String street) {
	this.street = street;
    }

    /**
     * @return the street
     */
    public String getStreet() {
	return street;
    }

    /**
     * @param housenr
     *            the housenr to set
     */
    public void setHousenr(String housenr) {
	this.housenr = housenr;
    }

    /**
     * @return the housenr
     */
    public String getHousenr() {
	return housenr;
    }

    /**
     * @param city
     *            the city to set
     */
    public void setCity(String city) {
	this.city = city;
    }

    /**
     * @return the city
     */
    public String getCity() {
	return city;
    }

    /**
     * @param country
     *            the country to set
     */
    public void setCountry(Country country) {
	this.country = country;
    }

    /**
     * @return the country
     */
    public Country getCountry() {
	return country;
    }

    public GeoLocation getGeoLocation() {
	return geoLocation;
    }

    public void setGeoLocation(GeoLocation geoLocation) {
	this.geoLocation = geoLocation;
    }
}
