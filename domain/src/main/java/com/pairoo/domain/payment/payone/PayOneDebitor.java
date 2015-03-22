package com.pairoo.domain.payment.payone;

import java.util.Date;

import com.datazuul.framework.domain.DomainObject;
import com.datazuul.framework.domain.geo.Country;
import com.datazuul.framework.domain.geo.Subdivision;
import com.pairoo.domain.UserAccount;
import com.pairoo.domain.enums.SalutationType;

public class PayOneDebitor implements DomainObject<Long> {
    private static final long serialVersionUID = 1L;

    private Long id;
    private UserAccount userAccount;
    
    // person data
    private String merchantCustomerId; // customerid
    private int payOneDebitorId; // userid
    private SalutationType salutation;
    private String title;
    private String firstname;
    private String lastname;
    private String company;
    private String street;
    private String addressAddition;
    private String zipcode;
    private String city;
    private Country country;
    private Subdivision state;
    private String email;
    private String phoneNumber;
    private Date birthday;
    private String language;
    private String vatId;
    private String ip;

    /**
     * @return the merchantCustomerId
     */
    public String getMerchantCustomerId() {
	return merchantCustomerId;
    }

    /**
     * @param merchantCustomerId the merchantCustomerId to set
     */
    public void setMerchantCustomerId(String merchantCustomerId) {
	this.merchantCustomerId = merchantCustomerId;
    }

    /**
     * @return the payOneDebitorId
     */
    public int getPayOneDebitorId() {
	return payOneDebitorId;
    }

    /**
     * @param payOneDebitorId the payOneDebitorId to set
     */
    public void setPayOneDebitorId(int payOneDebitorId) {
	this.payOneDebitorId = payOneDebitorId;
    }

    /**
     * @return the salutation
     */
    public SalutationType getSalutation() {
	return salutation;
    }

    /**
     * @param salutation the salutation to set
     */
    public void setSalutation(SalutationType salutation) {
	this.salutation = salutation;
    }

    /**
     * @return the title
     */
    public String getTitle() {
	return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
	this.title = title;
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
     * @return the company
     */
    public String getCompany() {
	return company;
    }

    /**
     * @param company the company to set
     */
    public void setCompany(String company) {
	this.company = company;
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
     * @return the addressAddition
     */
    public String getAddressAddition() {
	return addressAddition;
    }

    /**
     * @param addressAddition the addressAddition to set
     */
    public void setAddressAddition(String addressAddition) {
	this.addressAddition = addressAddition;
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

    /**
     * @return the state
     */
    public Subdivision getState() {
	return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(Subdivision state) {
	this.state = state;
    }

    /**
     * @return the email
     */
    public String getEmail() {
	return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
	this.email = email;
    }

    /**
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
	return phoneNumber;
    }

    /**
     * @param phoneNumber the phoneNumber to set
     */
    public void setPhoneNumber(String phoneNumber) {
	this.phoneNumber = phoneNumber;
    }

    /**
     * @return the birthday
     */
    public Date getBirthday() {
	return birthday;
    }

    /**
     * @param birthday the birthday to set
     */
    public void setBirthday(Date birthday) {
	this.birthday = birthday;
    }

    /**
     * @return the language
     */
    public String getLanguage() {
	return language;
    }

    /**
     * @param language the language to set
     */
    public void setLanguage(String language) {
	this.language = language;
    }

    /**
     * @return the vatId
     */
    public String getVatId() {
	return vatId;
    }

    /**
     * @param vatId the vatId to set
     */
    public void setVatId(String vatId) {
	this.vatId = vatId;
    }

    /**
     * @return the ip
     */
    public String getIp() {
	return ip;
    }

    /**
     * @param ip the ip to set
     */
    public void setIp(String ip) {
	this.ip = ip;
    }

    @Override
    public Long getId() {
	return id;
    }

    /**
     * @param id id of domain object
     */
    public void setId(Long id) {
	this.id = id;
    }

    public UserAccount getUserAccount() {
	return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
	this.userAccount = userAccount;
    }

}
