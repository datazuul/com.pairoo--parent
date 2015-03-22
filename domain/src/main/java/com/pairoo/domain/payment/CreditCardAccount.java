package com.pairoo.domain.payment;

import java.util.Date;

/**
 * @author Ralf Eichinger
 */
public class CreditCardAccount extends PaymentChannel {
    private static final long serialVersionUID = 1L;

    private String pseudopan;
    private String holderName;
    private Date validThru;
    private String truncatedPan;

    /**
     * @return the pseudopan
     */
    public String getPseudopan() {
	return pseudopan;
    }

    /**
     * @param pseudopan the pseudopan to set
     */
    public void setPseudopan(String pseudopan) {
	this.pseudopan = pseudopan;
    }

    /**
     * @return the holderName
     */
    public String getHolderName() {
	return holderName;
    }

    /**
     * @param holderName the holderName to set
     */
    public void setHolderName(String holderName) {
	this.holderName = holderName;
    }

    /**
     * @return the validThru
     */
    public Date getValidThru() {
	return validThru;
    }

    /**
     * @param validThru the validThru to set
     */
    public void setValidThru(Date validThru) {
	this.validThru = validThru;
    }
    
    public CreditCardAccount clone() {
	CreditCardAccount clone = (CreditCardAccount) super.clone();
	clone.setHolderName(getHolderName());
	clone.setPseudopan(getPseudopan());
	clone.setTruncatedPan(getTruncatedPan());
	return clone;
    }

    public String getTruncatedPan() {
	return truncatedPan;
    }

    public void setTruncatedPan(String truncatedPan) {
	this.truncatedPan = truncatedPan;
    }

}
