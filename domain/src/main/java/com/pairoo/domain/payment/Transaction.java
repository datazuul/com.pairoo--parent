package com.pairoo.domain.payment;

import java.util.Date;

import com.datazuul.framework.domain.AbstractPersistentDomainObject;
import com.pairoo.domain.UserAccount;
import com.pairoo.domain.payment.enums.ClearingType;
import com.pairoo.domain.payment.enums.StatusType;

public class Transaction extends AbstractPersistentDomainObject {
    private static final long serialVersionUID = 1L;

    private UserAccount userAccount;
    private PaymentChannel paymentChannel;
    private ClearingType clearingType;
    private StatusType status;
    /**
     * amount in smallest currency unit (e.g. cent)
     */
    private int amount;
    private String currencyCode;
    private String narrativeText;
    private Date timeStamp;

    public ClearingType getClearingType() {
	return clearingType;
    }

    public void setClearingType(ClearingType clearingType) {
	this.clearingType = clearingType;
    }

    public StatusType getStatus() {
	return status;
    }

    public void setStatus(StatusType status) {
	this.status = status;
    }

    public int getAmount() {
	return amount;
    }

    public void setAmount(int amount) {
	this.amount = amount;
    }

    public String getCurrencyCode() {
	return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
	this.currencyCode = currencyCode;
    }

    public String getNarrativeText() {
	return narrativeText;
    }

    public void setNarrativeText(String narrativeText) {
	this.narrativeText = narrativeText;
    }

    public PaymentChannel getPaymentChannel() {
	return paymentChannel;
    }

    public void setPaymentChannel(PaymentChannel paymentChannel) {
	this.paymentChannel = paymentChannel;
    }

    public UserAccount getUserAccount() {
	return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
	this.userAccount = userAccount;
    }

    public Date getTimeStamp() {
	return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
	this.timeStamp = timeStamp;
    }
}
