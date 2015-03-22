package com.pairoo.domain;

import java.util.Date;

import com.datazuul.framework.domain.AbstractPersistentDomainObject;
import com.pairoo.domain.enums.MembershipStatus;
import com.pairoo.domain.marketing.Promotion;
import com.pairoo.domain.payment.Transaction;

/**
 * @author Ralf Eichinger
 */
public class Membership extends AbstractPersistentDomainObject {
    private static final long serialVersionUID = 1L;

    private UserAccount userAccount;
    private Boolean acceptedTerms;
    private Date startDate;
    private Date endDate;
    private Product product = null; // has to be explicitly assigned (choose one
				    // product from DB)
    private MembershipStatus status;
    private Transaction paymentTransaction;
    private Promotion promotion;

    /**
     * @param status
     *            the Status to set
     */
    public void setStatus(final MembershipStatus status) {
	this.status = status;
    }

    /**
     * @return the status
     */
    public MembershipStatus getStatus() {
	return status;
    }

    /**
     * @param startDate
     *            the startDate to set
     */
    public void setStartDate(final Date startDate) {
	this.startDate = startDate;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
	return startDate;
    }

    /**
     * @param endDate
     *            the endDate to set
     */
    public void setEndDate(final Date endDate) {
	this.endDate = endDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
	return endDate;
    }

    /**
     * @param acceptedTerms
     *            the acceptedTerms to set
     */
    public void setAcceptedTerms(final Boolean acceptedTerms) {
	this.acceptedTerms = acceptedTerms;
    }

    /**
     * @return the acceptedTerms
     */
    public Boolean getAcceptedTerms() {
	return acceptedTerms;
    }

    public UserAccount getUserAccount() {
	return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
	this.userAccount = userAccount;
    }

    public Product getProduct() {
	return product;
    }

    public void setProduct(Product product) {
	this.product = product;
    }

    public Transaction getPaymentTransaction() {
	return paymentTransaction;
    }

    public void setPaymentTransaction(Transaction paymentTransaction) {
	this.paymentTransaction = paymentTransaction;
    }

    /**
     * @return the promotion "responsible" for this membership (may be null)
     */
    public Promotion getPromotion() {
	return promotion;
    }

    /**
     * @param promotion
     *            the promotion "responsible" for this membership (may be null)
     */
    public void setPromotion(Promotion promotion) {
	this.promotion = promotion;
    }
}
