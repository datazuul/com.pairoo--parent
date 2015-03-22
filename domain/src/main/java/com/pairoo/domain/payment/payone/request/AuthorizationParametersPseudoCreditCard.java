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
 * As this parameters are the same than the ones in PreAuthorizationParametersPseudoCreditCard,
 * no additional fields have to be added.
 * 
 * @author Ralf Eichinger
 */
public class AuthorizationParametersPseudoCreditCard extends AuthorizationParameters {
    public static final String MAP_KEY = "AuthorizationParametersPseudoCreditCard";
    
    private String pseudoCardPan;

    public AuthorizationParametersPseudoCreditCard(AuthorizationParameters params) {
	super(params);
	init();
    }

    public AuthorizationParametersPseudoCreditCard() {
	super();
	init();
    }
    
    private void init() {
	setClearingType(ClearingType.CREDIT_CARD);
    }
    
    public String getPseudoCardPan() {
	return pseudoCardPan;
    }

    public void setPseudoCardPan(String pseudoCardPan) {
	this.pseudoCardPan = pseudoCardPan;
    }
}
