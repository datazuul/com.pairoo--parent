package com.pairoo.domain.payment;

import com.datazuul.framework.domain.DomainObject;
import com.datazuul.framework.domain.geo.Country;

public class BillingAddress implements DomainObject<Long> {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String firstname;
    private String lastname;
    private String street;
    private String houseNumber;
    private String zipcode;
    private String city;
    private Country country;

    @Override
    public Long getId() {
	return id;
    }

    /**
     * @param id
     *            id of domain object
     */
    public void setId(Long id) {
	this.id = id;
    }

    /**
     * @return the firstname
     */
    public String getFirstname() {
	return firstname;
    }

    /**
     * @param firstname the firstname to set
     */
    public void setFirstname(String firstname) {
	this.firstname = firstname;
    }

    /**
     * @return the lastname
     */
    public String getLastname() {
	return lastname;
    }

    /**
     * @param lastname the lastname to set
     */
    public void setLastname(String lastname) {
	this.lastname = lastname;
    }

    /**
     * @return the street
     */
    public String getStreet() {
	return street;
    }

    /**
     * @param street the street to set
     */
    public void setStreet(String street) {
	this.street = street;
    }

    /**
     * @return the houseNumber
     */
    public String getHouseNumber() {
	return houseNumber;
    }

    /**
     * @param houseNumber the houseNumber to set
     */
    public void setHouseNumber(String houseNumber) {
	this.houseNumber = houseNumber;
    }

    /**
     * @return the zipcode
     */
    public String getZipcode() {
	return zipcode;
    }

    /**
     * @param zipcode the zipcode to set
     */
    public void setZipcode(String zipcode) {
	this.zipcode = zipcode;
    }

    /**
     * @return the city
     */
    public String getCity() {
	return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
	this.city = city;
    }

    /**
     * @return the country
     */
    public Country getCountry() {
	return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(Country country) {
	this.country = country;
    }

    public BillingAddress clone() {
	BillingAddress clone = new BillingAddress();
	clone.setCity(getCity());
	clone.setCountry(getCountry());
	clone.setFirstname(getFirstname());
	clone.setHouseNumber(getHouseNumber());
	clone.setLastname(getLastname());
	clone.setStreet(getStreet());
	clone.setZipcode(getZipcode());
	return clone;
    }
}
