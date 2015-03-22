package com.pairoo.business.services.impl.payment.payone;

import java.util.HashMap;

import com.pairoo.business.api.payment.payone.CreditCardCheckService;
import com.pairoo.domain.payment.enums.PaymentChannelType;
import com.pairoo.domain.payment.payone.PayOneConstants;

/**
 * Some params are configured in PMI (PayOne Merchant Portal):
 * look them up under: Konfiguration - Zahlungsportale - Pairoo.com - API-Parameter
 * 
 * @author Ralf Eichinger
 */
public class CreditCardCheckServiceImpl extends PayOneServiceImpl implements CreditCardCheckService {

    private String subAccountId;

    public CreditCardCheckServiceImpl(final String merchantId, final String portalId, final String subAccountId,
	    final String secretKey, final String mode) {
	super(merchantId, portalId, secretKey, mode, PayOneConstants.REQUEST_CREDITCARD_CHECK);
	this.subAccountId = subAccountId;
    }

    @Override
    public HashMap<String, String> getHashParam() {
	HashMap<String, String> result = new HashMap<String, String>();

	// generally required params to be hashed
	HashMap<String, String> hashParams = super.getHashParams();

	// use case specific params to be hashed
	hashParams.putAll(getRequestParam());
	hashParams.putAll(getStoreCardDataParam());
	hashParams.putAll(getSubAccountIdParam());

	String value = super.getHashParam(hashParams);
	result.put(PayOneConstants.KEY_HASH, value);
	return result;
    }

    @Override
    public HashMap<String, String> getRequestParam() {
	HashMap<String, String> result = new HashMap<String, String>();
	String value = stdParams.getRequest();
	result.put(PayOneConstants.KEY_REQUEST, value);
	return result;
    }

    @Override
    public HashMap<String, String> getSubAccountIdParam() {
	HashMap<String, String> result = new HashMap<String, String>();
	String value = subAccountId;
	result.put(PayOneConstants.KEY_SUBACCOUNT_ID, value);
	return result;
    }

    /* (non-Javadoc)
     * @see com.pairoo.business.api.payment.payone.CreditCardCheckService#getCardTypeParam(com.pairoo.domain.payment.enums.PaymentChannelType)
     * 
     * Vorgabe nach Kartentyp
     * V: Visa
     * M: Mastercard
     * A: Amex
     * D: Diners
     * J: JCB
     * O: Maestro International
     * U: Maestro UK
     * C: Discover
     * B: Carte Bleue
     */
    @Override
    public HashMap<String, String> getCardTypeParam(PaymentChannelType paymentChannelType) {
	HashMap<String, String> result = new HashMap<String, String>();
	String value = null;
	switch (paymentChannelType) {
	case AMERICAN_EXPRESS:
	    value = PayOneConstants.CARD_TYPE_AMERICAN_EXPRESS;
	    break;
	case MASTERCARD:
	    value = PayOneConstants.CARD_TYPE_MASTERCARD;
	    break;
	case VISA:
	    value = PayOneConstants.CARD_TYPE_VISA;
	    break;
	default:
	    break;
	}
	result.put(PayOneConstants.KEY_CARD_TYPE, value);
	return result;
    }

    @Override
    public HashMap<String, String> getStoreCardDataParam() {
	HashMap<String, String> result = new HashMap<String, String>();
	String value = PayOneConstants.STORE_CARDDATA_TRUE;
	result.put(PayOneConstants.KEY_STORE_CARDDATA, value);
	return result;
    }
}
