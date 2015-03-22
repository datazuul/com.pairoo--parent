package com.pairoo.domain.payment;

import com.pairoo.domain.payment.enums.ELVCountry;

/**
 * @author Ralf Eichinger
 */
public class BankAccount extends PaymentChannel {
    private static final long serialVersionUID = 1L;

    private ELVCountry bankCountry = ELVCountry.DE;
    private String bankAccount;
    private Integer bankCode;
    private String bankAccountHolder;

    /**
     * @return the bankCountry
     */
    public ELVCountry getBankCountry() {
	return bankCountry;
    }

    /**
     * @param bankCountry the bankCountry to set
     */
    public void setBankCountry(ELVCountry bankCountry) {
	this.bankCountry = bankCountry;
    }

    /**
     * @return the bankAccount
     */
    public String getBankAccount() {
	return bankAccount;
    }

    /**
     * In germany a bank code is between 10.000.000 and 99.999.999.
     * It has always 8 digits.
     * 
     * @param bankAccount the bankAccount to set
     */
    public void setBankAccount(String bankAccount) {
	this.bankAccount = bankAccount;
    }

    /**
     * @return the bankCode
     */
    public Integer getBankCode() {
	return bankCode;
    }

    /**
     * @param bankCode the bankCode to set
     */
    public void setBankCode(Integer bankCode) {
	this.bankCode = bankCode;
    }

    /**
     * @return the bankAccountHolder
     */
    public String getBankAccountHolder() {
	return bankAccountHolder;
    }

    /**
     * @param bankAccountHolder the bankAccountHolder to set
     */
    public void setBankAccountHolder(String bankAccountHolder) {
	this.bankAccountHolder = bankAccountHolder;
    }
    
    public BankAccount clone() {
	BankAccount clone = (BankAccount) super.clone();
	clone.setBankAccount(getBankAccount());
	clone.setBankAccountHolder(getBankAccountHolder());
	clone.setBankCode(getBankCode());
	clone.setBankCountry(getBankCountry());
	return clone;
    }
}
