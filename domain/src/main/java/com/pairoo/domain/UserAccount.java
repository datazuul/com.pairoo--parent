package com.pairoo.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.datazuul.framework.domain.AbstractPersistentDomainObject;
import com.datazuul.framework.domain.Language;
import com.datazuul.framework.domain.authorization.Roles;

/**
 * @author Ralf Eichinger
 */
public class UserAccount extends AbstractPersistentDomainObject {
	private static final long serialVersionUID = 1L;

	private List<ContactEvent> contactEvent = new ArrayList<>();
	private Date lastLogin;
	private NotificationSettings notificationSettings = new NotificationSettings();
	private boolean online;

	// @Size(min = 6, max = 12)
	@NotNull
	private String password = null;
	private String passwordSalt = null;
	private Language preferredLanguage;
	private Date premiumEndDate;
	private transient Date previousLogin;
	private User user;
	private String username;

	private Roles roles;

	/**
	 * @return the contactEvent
	 */
	public List<ContactEvent> getContactEvent() {
		return contactEvent;
	}

	/**
	 * @return the lastLogin
	 */
	public Date getLastLogin() {
		return lastLogin;
	}

	/**
	 * @return the password digest
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return the password digest's salt
	 */
	public String getPasswordSalt() {
		return passwordSalt;
	}

	/**
	 * set the password digest's salt
	 * 
	 * @param passwordSalt
	 *            salt used for digesting
	 */
	public void setPasswordSalt(String passwordSalt) {
		this.passwordSalt = passwordSalt;
	}

	/**
	 * Date of login before current login.
	 * 
	 * @return the previous login
	 */
	public Date getPreviousLogin() {
		return previousLogin;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	public String getUsername() {
		return username;
	}

	/**
	 * @return the online
	 */
	public boolean isOnline() {
		return online;
	}

	/**
	 * @param contactEvent
	 *            the contactEvent to set
	 */
	public void setContactEvent(final List<ContactEvent> contactEvent) {
		this.contactEvent = contactEvent;
	}

	/**
	 * @param lastLogin
	 *            the lastLogin to set
	 */
	public void setLastLogin(final Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	/**
	 * @param online
	 *            the online to set
	 */
	public void setOnline(final boolean online) {
		this.online = online;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	/**
	 * Date of login before current login.
	 * 
	 * @param previousLogin
	 *            the previous login to set
	 */
	public void setPreviousLogin(final Date previousLogin) {
		this.previousLogin = previousLogin;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(final User user) {
		this.user = user;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public Language getPreferredLanguage() {
		return preferredLanguage;
	}

	public void setPreferredLanguage(final Language preferredLanguage) {
		this.preferredLanguage = preferredLanguage;
	}

	public NotificationSettings getNotificationSettings() {
		return notificationSettings;
	}

	public void setNotificationSettings(
			final NotificationSettings notificationSettings) {
		this.notificationSettings = notificationSettings;
	}

	public Date getPremiumEndDate() {
		return premiumEndDate;
	}

	public void setPremiumEndDate(Date premiumEndDate) {
		this.premiumEndDate = premiumEndDate;
	}

	/**
	 * @return the roles (may be null!)
	 */
	public Roles getRoles() {
		return roles;
	}

	public boolean hasRole(String role) {
		boolean result = false;
		if (getRoles() != null) {
			return getRoles().hasRole(role);
		}
		return result;
	}

	/**
	 * @param roles
	 *            the roles to set
	 */
	public void setRoles(Roles roles) {
		this.roles = roles;
	}
	
	public String getRolesCodes() {
	    return roles.getRoles();
	}
}
