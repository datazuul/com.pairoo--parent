package com.pairoo.domain.payment.payone;

import com.pairoo.domain.payment.Transaction;

public class PayOneTransaction extends Transaction {
    private static final long serialVersionUID = 1L;

    private int subAccountId; // aid
    private long payoneId; // txid
    private int payoneDebitorId; // userid
    private int errorCode;
    private String errorMessage;
    private String merchantTransactionReference;
    private String param;
    private String customerMessage;
    private String redirectUrl;

    /**
     * @return the payoneId
     */
    public long getPayoneId() {
	return payoneId;
    }

    /**
     * @param payoneId the payoneId to set
     */
    public void setPayoneId(long payoneId) {
	this.payoneId = payoneId;
    }

    /**
     * @return the payoneDebitorId
     */
    public int getPayoneDebitorId() {
	return payoneDebitorId;
    }

    /**
     * @param payoneDebitorId the payoneDebitorId to set
     */
    public void setPayoneDebitorId(int payoneDebitorId) {
	this.payoneDebitorId = payoneDebitorId;
    }

    /**
     * @return the errorCode
     */
    public int getErrorCode() {
	return errorCode;
    }

    /**
     * @param errorCode the errorCode to set
     */
    public void setErrorCode(int errorCode) {
	this.errorCode = errorCode;
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
	return errorMessage;
    }

    /**
     * @param errorMessage the errorMessage to set
     */
    public void setErrorMessage(String errorMessage) {
	this.errorMessage = errorMessage;
    }

    public int getSubAccountId() {
	return subAccountId;
    }

    public void setSubAccountId(int subAccountId) {
	this.subAccountId = subAccountId;
    }

    public String getParam() {
	return param;
    }

    public void setParam(String param) {
	this.param = param;
    }

    public String getMerchantTransactionReference() {
	return merchantTransactionReference;
    }

    public void setMerchantTransactionReference(String merchantTransactionReference) {
	this.merchantTransactionReference = merchantTransactionReference;
    }

    public String getCustomerMessage() {
	return customerMessage;
    }

    public void setCustomerMessage(String customerMessage) {
	this.customerMessage = customerMessage;
    }

    public String getRedirectUrl() {
	return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
	this.redirectUrl = redirectUrl;
    }
}
