package com.pairoo.domain.payment.payone.request;

import com.pairoo.domain.payment.enums.ClearingType;
import com.pairoo.domain.payment.enums.ELVCountry;

/**
 * See PayOne documentation "FinanceGate_Server_API_v_2_5_DE.pdf".
 * 
 * Parameter ( Lastschrift )
 * Parameter              Pflicht Format  Kommentar
 * bankcountry            +       Vorgabe Kontotyp/ Land
 *                                        DE: Deutschland
 *                                        AT: Österreich
 *                                        NL: Niederlande
 * bankaccount            +       AN..14  Kontonummer
 * bankcode               o       N..8    Bankleitzahl
 *                                        (Nur in DE & AT)
 * bankaccountholder      -       AN..50  Kontoinhaber
 * 
 * As this parameters are the same than the ones in PreAuthorizationParametersELV,
 * no additional fields have to be added.
 * 
 * @author Ralf Eichinger
 *
 */
public class AuthorizationParametersELV extends AuthorizationParameters {
    public static final String MAP_KEY = "AuthorizationParametersELV";
    private ELVCountry bankCountry;
    private String bankAccount;
    private int bankCode;
    private String bankAccountHolder;

    public AuthorizationParametersELV() {
	super();
	init();
    }

    public AuthorizationParametersELV(AuthorizationParameters params) {
	super(params);
	init();
    }

    private void init() {
	setClearingType(ClearingType.ELEKTRONISCHE_LASTSCHRIFT);
    }

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
     * @param bankAccount the bankAccount to set
     */
    public void setBankAccount(String bankAccount) {
	this.bankAccount = bankAccount;
    }

    /**
     * @return the bankCode
     */
    public int getBankCode() {
	return bankCode;
    }

    /**
     * @param bankCode the bankCode to set
     */
    public void setBankCode(int bankCode) {
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
}
