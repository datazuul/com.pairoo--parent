package com.pairoo.domain;

import com.datazuul.framework.domain.AbstractPersistentDomainObject;
import com.pairoo.domain.enums.Importance;

/**
 * @author Ralf Eichinger
 */
public class PersonalValues extends AbstractPersistentDomainObject {
    private static final long serialVersionUID = 1L;

    private Importance showingEmotions = Importance.NOT_IMPORTANT;
    private Importance romance = Importance.NOT_IMPORTANT;
    private Importance tenderness = Importance.NOT_IMPORTANT;
    private Importance longRelationship = Importance.NOT_IMPORTANT;
    private Importance shortRelationship = Importance.NOT_IMPORTANT;
    private Importance freedom = Importance.NOT_IMPORTANT;
    private Importance sexuality = Importance.NOT_IMPORTANT;
    private Importance differentPartners = Importance.NOT_IMPORTANT;
    private Importance faithfulness = Importance.NOT_IMPORTANT;
    private Importance respect = Importance.NOT_IMPORTANT;
    private Importance honesty = Importance.NOT_IMPORTANT;
    private Importance security = Importance.NOT_IMPORTANT;
    private Importance confidence = Importance.NOT_IMPORTANT;
    private Importance charm = Importance.NOT_IMPORTANT;

    /**
     * @return the showingEmotions
     */
    public Importance getShowingEmotions() {
	return showingEmotions;
    }

    /**
     * @param showingEmotions
     *            the showingEmotions to set
     */
    public void setShowingEmotions(final Importance showingEmotions) {
	this.showingEmotions = showingEmotions;
    }

    /**
     * @return the romance
     */
    public Importance getRomance() {
	return romance;
    }

    /**
     * @param romance
     *            the romance to set
     */
    public void setRomance(final Importance romance) {
	this.romance = romance;
    }

    /**
     * @return the tenderness
     */
    public Importance getTenderness() {
	return tenderness;
    }

    /**
     * @param tenderness
     *            the tenderness to set
     */
    public void setTenderness(final Importance tenderness) {
	this.tenderness = tenderness;
    }

    /**
     * @return the longRelationship
     */
    public Importance getLongRelationship() {
	return longRelationship;
    }

    /**
     * @param longRelationship
     *            the longRelationship to set
     */
    public void setLongRelationship(final Importance longRelationship) {
	this.longRelationship = longRelationship;
    }

    /**
     * @return the shortRelationship
     */
    public Importance getShortRelationship() {
	return shortRelationship;
    }

    /**
     * @param shortRelationship
     *            the shortRelationship to set
     */
    public void setShortRelationship(final Importance shortRelationship) {
	this.shortRelationship = shortRelationship;
    }

    /**
     * @return the freedom
     */
    public Importance getFreedom() {
	return freedom;
    }

    /**
     * @param freedom
     *            the freedom to set
     */
    public void setFreedom(final Importance freedom) {
	this.freedom = freedom;
    }

    /**
     * @return the sexuality
     */
    public Importance getSexuality() {
	return sexuality;
    }

    /**
     * @param sexuality
     *            the sexuality to set
     */
    public void setSexuality(final Importance sexuality) {
	this.sexuality = sexuality;
    }

    /**
     * @return the differentPartners
     */
    public Importance getDifferentPartners() {
	return differentPartners;
    }

    /**
     * @param differentPartners
     *            the differentPartners to set
     */
    public void setDifferentPartners(final Importance differentPartners) {
	this.differentPartners = differentPartners;
    }

    /**
     * @return the faithfulness
     */
    public Importance getFaithfulness() {
	return faithfulness;
    }

    /**
     * @param faithfulness
     *            the faithfulness to set
     */
    public void setFaithfulness(final Importance faithfulness) {
	this.faithfulness = faithfulness;
    }

    /**
     * @return the respect
     */
    public Importance getRespect() {
	return respect;
    }

    /**
     * @param respect
     *            the respect to set
     */
    public void setRespect(final Importance respect) {
	this.respect = respect;
    }

    /**
     * @return the honesty
     */
    public Importance getHonesty() {
	return honesty;
    }

    /**
     * @param honesty
     *            the honesty to set
     */
    public void setHonesty(final Importance honesty) {
	this.honesty = honesty;
    }

    /**
     * @return the security
     */
    public Importance getSecurity() {
	return security;
    }

    /**
     * @param security
     *            the security to set
     */
    public void setSecurity(final Importance security) {
	this.security = security;
    }

    /**
     * @return the confidence
     */
    public Importance getConfidence() {
	return confidence;
    }

    /**
     * @param confidence
     *            the confidence to set
     */
    public void setConfidence(final Importance confidence) {
	this.confidence = confidence;
    }

    /**
     * @return the charm
     */
    public Importance getCharm() {
	return charm;
    }

    /**
     * @param charm
     *            the charm to set
     */
    public void setCharm(final Importance charm) {
	this.charm = charm;
    }
}
