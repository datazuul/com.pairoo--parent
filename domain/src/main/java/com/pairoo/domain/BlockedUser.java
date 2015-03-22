package com.pairoo.domain;

import java.util.Date;

import com.datazuul.framework.domain.AbstractPersistentDomainObject;

/**
 * @author Ralf Eichinger
 */
public class BlockedUser extends AbstractPersistentDomainObject {
    private static final long serialVersionUID = 1L;

    private User owner;
    private User target;
    private Date timeStamp;

    /**
     * @return the owner
     */
    public User getOwner() {
	return owner;
    }

    /**
     * @return the target
     */
    public User getTarget() {
	return target;
    }

    /**
     * @return the timestamp
     */
    public Date getTimeStamp() {
	return timeStamp;
    }

    /**
     * @param owner
     *            the owner to set
     */
    public void setOwner(final User owner) {
	this.owner = owner;
    }

    /**
     * @param target
     *            the target to set
     */
    public void setTarget(final User target) {
	this.target = target;
    }

    /**
     * @param timeStamp
     *            the timestamp to set
     */
    public void setTimeStamp(final Date timeStamp) {
	this.timeStamp = timeStamp;
    }

}
