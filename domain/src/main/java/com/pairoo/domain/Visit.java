package com.pairoo.domain;

import java.util.Date;

import com.datazuul.framework.domain.AbstractPersistentDomainObject;

/**
 * @author Ralf Eichinger
 */
public class Visit extends AbstractPersistentDomainObject {
    private static final long serialVersionUID = 1L;

    private Date timeStamp;
    private User visitedUser;
    private User visitor;

    /**
     * @return the timestamp
     */
    public Date getTimeStamp() {
	return timeStamp;
    }

    /**
     * @return the visitedUser
     */
    public User getVisitedUser() {
	return visitedUser;
    }

    /**
     * @return the visitor
     */
    public User getVisitor() {
	return visitor;
    }

    /**
     * @param timeStamp
     *            the timestamp to set
     */
    public void setTimeStamp(final Date timeStamp) {
	this.timeStamp = timeStamp;
    }

    /**
     * @param visitedUser
     *            the visitedUser to set
     */
    public void setVisitedUser(final User visitedUser) {
	this.visitedUser = visitedUser;
    }

    /**
     * @param visitor
     *            the visitor to set
     */
    public void setVisitor(final User visitor) {
	this.visitor = visitor;
    }

}
