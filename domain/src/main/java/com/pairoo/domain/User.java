package com.pairoo.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.datazuul.framework.domain.AbstractPersistentDomainObject;

/**
 * @author Ralf Eichinger
 */
public class User extends AbstractPersistentDomainObject {
    private static final long serialVersionUID = 1L;

    private Address address;
    @NotNull
    @Size(min = 6, max = 256)
    private String email;
    private String firstname;
    private String lastname;
    private SearchProfile searchProfile = new SearchProfile();
    private UserAccount userAccount = new UserAccount();

    private UserProfile userProfile = new UserProfile();

    /**
     * @return the address
     */
    public Address getAddress() {
	return address;
    }

    public String getEmail() {
	return this.email;
    }

    public String getFirstname() {
	return this.firstname;
    }

    public String getLastname() {
	return this.lastname;
    }

    /**
     * @return the searchProfile
     */
    public SearchProfile getSearchProfile() {
	return searchProfile;
    }

    /**
     * @return the userAccount
     */
    public UserAccount getUserAccount() {
	return userAccount;
    }

    /**
     * @return the userprofile
     */
    public UserProfile getUserProfile() {
	return userProfile;
    }

    /**
     * @param address
     *            the address to set
     */
    public void setAddress(final Address address) {
	this.address = address;
    }

    public void setEmail(final String email) {
	this.email = email;
    }

    public void setFirstname(final String firstname) {
	this.firstname = firstname;
    }

    public void setLastname(final String lastname) {
	this.lastname = lastname;
    }

    /**
     * @param searchProfile
     *            the searchProfile to set
     */
    public void setSearchProfile(final SearchProfile searchProfile) {
	this.searchProfile = searchProfile;
    }

    /**
     * @param userAccount
     *            the userAccount to set
     */
    public void setUserAccount(final UserAccount userAccount) {
	this.userAccount = userAccount;
    }

    /**
     * @param userProfile
     *            the userprofile to set
     */
    public void setUserProfile(final UserProfile userProfile) {
	this.userProfile = userProfile;
    }

    @Override
    public String toString() {
	return ReflectionToStringBuilder.toString(this);
	// StringBuffer sb = new StringBuffer();
	// sb.append("gender: ").append(userProfile.getGender()).append(" | ");
	// sb.append("email: ").append(email).append(" | ");
	// sb.append("firstname: ").append(firstname).append(" | ");
	// sb.append("lastname: ").append(lastname);
	// return sb.toString();
    }
}
