package com.pairoo.domain.payment;

import java.util.Date;

import com.datazuul.framework.domain.AbstractPersistentDomainObject;
import com.pairoo.domain.UserAccount;
import com.pairoo.domain.payment.enums.PaymentChannelType;

/**
 * @author Ralf Eichinger
 */
public class PaymentChannel extends AbstractPersistentDomainObject {
    private static final long serialVersionUID = 1L;

    private UserAccount userAccount;
    private PaymentChannelType paymentChannelType;
    private Date startDate;
    private Date endDate;

    public PaymentChannelType getPaymentChannelType() {
	return paymentChannelType;
    }

    public void setPaymentChannelType(PaymentChannelType paymentChannelType) {
	this.paymentChannelType = paymentChannelType;
    }

    public Date getStartDate() {
	return startDate;
    }

    public void setStartDate(Date startDate) {
	this.startDate = startDate;
    }

    public Date getEndDate() {
	return endDate;
    }

    public void setEndDate(Date endDate) {
	this.endDate = endDate;
    }

    public UserAccount getUserAccount() {
	return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
	this.userAccount = userAccount;
    }

    public PaymentChannel clone() {
	PaymentChannel clone = new PaymentChannel();
	clone.setPaymentChannelType(getPaymentChannelType());
	return clone;
    }
}
