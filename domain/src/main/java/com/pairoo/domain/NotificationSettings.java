package com.pairoo.domain;

import com.datazuul.framework.domain.AbstractPersistentDomainObject;

/**
 * Settings of an user account about what notifications to get. Default is
 * "true" for all notifications.
 * 
 * @author Ralf Eichinger
 */
public class NotificationSettings extends AbstractPersistentDomainObject {
    private static final long serialVersionUID = 1L;

    private Boolean onNewMessage = true;
    private Boolean onNewSuggestions = true;
    private Boolean onVisit = true;
    private Boolean newsletter = true;
    private Boolean weekendSuggestions = true;
    private Boolean weeklyStatistic = true;

    public Boolean getOnNewMessage() {
	return onNewMessage;
    }

    public void setOnNewMessage(final Boolean onNewMessage) {
	this.onNewMessage = onNewMessage;
    }

    public Boolean getOnVisit() {
	return onVisit;
    }

    public void setOnVisit(final Boolean onVisit) {
	this.onVisit = onVisit;
    }

    public Boolean getOnNewSuggestions() {
	return onNewSuggestions;
    }

    public void setOnNewSuggestions(final Boolean onNewSuggestions) {
	this.onNewSuggestions = onNewSuggestions;
    }

    public Boolean getWeeklyStatistic() {
	return weeklyStatistic;
    }

    public void setWeeklyStatistic(final Boolean weeklyStatistic) {
	this.weeklyStatistic = weeklyStatistic;
    }

    public Boolean getWeekendSuggestions() {
	return weekendSuggestions;
    }

    public void setWeekendSuggestions(final Boolean weekendSuggestions) {
	this.weekendSuggestions = weekendSuggestions;
    }

    public Boolean getNewsletter() {
	return newsletter;
    }

    public void setNewsletter(final Boolean newsletter) {
	this.newsletter = newsletter;
    }

}
