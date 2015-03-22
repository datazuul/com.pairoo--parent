package com.pairoo.domain.payment.payone.request;

import com.pairoo.domain.payment.enums.ClearingType;
import com.pairoo.domain.payment.enums.CreditCardTransactionType;
import com.pairoo.domain.payment.enums.CreditCardType;

/**
 * See PayOne documentation "FinanceGate_Server_API_v_2_5_DE.pdf".
 * 
 * Parameter (credit card)
 * Parameter              Pflicht Format  Kommentar
 * cardpan                +       N..19   Kartenummer
 * cardtype               +       Vorgabe Kartentyp
 *                                        V: Visa
 *                                        M: Mastercard
 *                                        A: Amex
 *                                        D: Diners
 *                                        J: JCB
 *                                        O: Maestro International
 *                                        U: Maestro UK
 *                                        C: Discover
 *                                        B: Carte Bleue
 * cardexpiredate         +       N4      Verfallsdatum YYMM
 * cardcvc2               o       N..5    Kartenprüfnummer (Card Verification Code Format 2)
 * cardissuenumber        -       N..2    Kartenfolgenummer
 *                                        (nur bei Maestro UK Karten)
 * cardholder             -       AN..50  Karteninhaber
 * ecommercemode          -       Vorgabe Kreditkarten Transaktionstyp:
 *                                        internet:    eCommerce Transaktion (SSL gesichert)
 *                                        3dsecure: 3D-Secure Transaktion
 *                                                  (kann alternativ über die Riskeinstellungen
 *                                                   aktiviert werden)
 *                                        moto: Mail or Telephone Order Transaktion
 * 
 * @author Ralf Eichinger
 */
public class PreAuthorizationParametersCreditCard extends PreAuthorizationParameters {
    private String cardPan;
    private CreditCardType cardType;
    private String cardExpireDate;
    private String cardCVC2;
    private int cardIssueNumber;
    private String cardHolder;
    private CreditCardTransactionType eCommerceMode;

    public PreAuthorizationParametersCreditCard() {
	setClearingType(ClearingType.CREDIT_CARD);
    }
    
    /**
     * @return the cardPan
     */
    public String getCardPan() {
	return cardPan;
    }

    /**
     * @param cardPan the cardPan to set
     */
    public void setCardPan(String cardPan) {
	this.cardPan = cardPan;
    }

    /**
     * @return the cardType
     */
    public CreditCardType getCardType() {
	return cardType;
    }

    /**
     * @param cardType the cardType to set
     */
    public void setCardType(CreditCardType cardType) {
	this.cardType = cardType;
    }

    /**
     * @return the cardExpireDate
     */
    public String getCardExpireDate() {
	return cardExpireDate;
    }

    /**
     * @param cardExpireDate the cardExpireDate to set
     */
    public void setCardExpireDate(String cardExpireDate) {
	this.cardExpireDate = cardExpireDate;
    }

    /**
     * @return the cardCVC2
     */
    public String getCardCVC2() {
	return cardCVC2;
    }

    /**
     * @param cardCVC2 the cardCVC2 to set
     */
    public void setCardCVC2(String cardCVC2) {
	this.cardCVC2 = cardCVC2;
    }

    /**
     * @return the cardIssueNumber
     */
    public int getCardIssueNumber() {
	return cardIssueNumber;
    }

    /**
     * @param cardIssueNumber the cardIssueNumber to set
     */
    public void setCardIssueNumber(int cardIssueNumber) {
	this.cardIssueNumber = cardIssueNumber;
    }

    /**
     * @return the cardHolder
     */
    public String getCardHolder() {
	return cardHolder;
    }

    /**
     * @param cardHolder the cardHolder to set
     */
    public void setCardHolder(String cardHolder) {
	this.cardHolder = cardHolder;
    }

    /**
     * @return the eCommerceMode
     */
    public CreditCardTransactionType geteCommerceMode() {
	return eCommerceMode;
    }

    /**
     * @param eCommerceMode the eCommerceMode to set
     */
    public void seteCommerceMode(CreditCardTransactionType eCommerceMode) {
	this.eCommerceMode = eCommerceMode;
    }
}
