package com.pairoo.domain.payment.payone.request;

import com.pairoo.domain.payment.enums.ClearingType;

/**
 * See PayOne documentation "FinanceGate_Server_API_v_2_5_DE.pdf".
 * 
 * Parameter (Kreditkarte mit Pseudokartennummer)
 * Parameter              Pflicht Format  Kommentar
 * pseudocardpan          +       N..19   Pseudo-Kartenummer
 *                                        (Diese Kartennummer kann alternativ zu den
 *                                         übrigen Kartendaten übermittelt werden)
 * 
 * @author Ralf Eichinger
 */
public class PreAuthorizationParametersPseudoCreditCard extends PreAuthorizationParameters {
    private String pseudoCardPan;

    public PreAuthorizationParametersPseudoCreditCard() {
	setClearingType(ClearingType.CREDIT_CARD);
    }

    public String getPseudoCardPan() {
	return pseudoCardPan;
    }

    public void setPseudoCardPan(String pseudoCardPan) {
	this.pseudoCardPan = pseudoCardPan;
    }

}
