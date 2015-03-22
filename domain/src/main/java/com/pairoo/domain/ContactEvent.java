/**
 * 
 */
package com.pairoo.domain;

import java.util.Date;

import com.datazuul.framework.domain.AbstractPersistentDomainObject;
import com.pairoo.domain.enums.ContactEventType;

/**
 * @author buntschw
 * 
 */
public class ContactEvent extends AbstractPersistentDomainObject {
    private static final long serialVersionUID = 1L;

    private Date timestamp;
    private UserAccount userAccount;
    private ContactEventType contactEventType;

    /**
     * @param timestamp
     *            the timestamp to set
     */
    public void setTimestamp(Date timestamp) {
	this.timestamp = timestamp;
    }

    /**
     * @return the timestamp
     */
    public Date getTimestamp() {
	return timestamp;
    }

    /**
     * @param userAccount
     *            the userAccount to set
     */
    public void setUserAccount(UserAccount userAccount) {
	this.userAccount = userAccount;
    }

    /**
     * @return the userAccount
     */
    public UserAccount getUserAccount() {
	return userAccount;
    }

    /**
     * @param contactEventType
     *            the contactEventType to set
     */
    public void setContactEventType(ContactEventType contactEventType) {
	this.contactEventType = contactEventType;
    }

    /**
     * @return the contactEventType
     */
    public ContactEventType getContactEventType() {
	return contactEventType;
    }

}
