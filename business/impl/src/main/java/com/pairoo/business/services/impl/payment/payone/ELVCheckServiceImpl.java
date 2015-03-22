package com.pairoo.business.services.impl.payment.payone;

import java.util.HashMap;

import com.pairoo.business.api.payment.payone.ELVCheckService;
import com.pairoo.domain.payment.payone.PayOneConstants;

/**
 * Some params are configured in PMI (PayOne Merchant Portal):
 * look them up under: Konfiguration - Zahlungsportale - Pairoo.com - API-Parameter
 * 
 * @author Ralf Eichinger
 */
public class ELVCheckServiceImpl extends PayOneServiceImpl implements ELVCheckService {

    private String subAccountId;

    public ELVCheckServiceImpl(final String merchantId, final String portalId, final String subAccountId,
	    final String secretKey, final String mode) {
	super(merchantId, portalId, secretKey, mode, PayOneConstants.REQUEST_BANK_ACCOUNT_CHECK);
	this.subAccountId = subAccountId;
    }

    @Override
    public HashMap<String, String> getHashParam() {
	HashMap<String, String> result = new HashMap<String, String>();

	// generally required params to be hashed
	HashMap<String, String> hashParams = super.getHashParams();

	// use case specific params to be hashed
	// (no need to sort alphabetically, will be done in super-class)
	hashParams.putAll(getRequestParam());
	hashParams.putAll(getCheckTypeParam());
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

    @Override
    public HashMap<String, String> getCheckTypeParam() {
	HashMap<String, String> result = new HashMap<String, String>();
	String value = PayOneConstants.BANKACCOUNT_CHECK_TYPE_POS;
	result.put(PayOneConstants.KEY_BANKACCOUNT_CHECK_TYPE, value);
	return result;
    }
}
