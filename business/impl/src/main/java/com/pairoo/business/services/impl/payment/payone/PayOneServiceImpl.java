package com.pairoo.business.services.impl.payment.payone;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datazuul.framework.security.HashUtil;
import com.pairoo.business.api.payment.payone.PayOneService;
import com.pairoo.domain.payment.payone.PayOneConstants;
import com.pairoo.domain.payment.payone.request.StandardParameters;

/**
 * Some params are configured in PMI (PayOne Merchant Portal):
 * look them up under: Konfiguration - Zahlungsportale - Pairoo.com - API-Parameter
 * 
 * @author Ralf Eichinger
 */
public abstract class PayOneServiceImpl implements PayOneService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PayOneServiceImpl.class);

    final StandardParameters stdParams;

    protected PayOneServiceImpl(final String merchantId, final String portalId, final String secretKey,
	    final String mode, final String request) {
	String encoding = PayOneConstants.ENCODING_UTF8;
	stdParams = new StandardParameters(merchantId, portalId, secretKey, mode, request, encoding);
    }

    @Override
    public HashMap<String, String> getEncodingParam() {
	HashMap<String, String> result = new HashMap<String, String>();
	String value = PayOneConstants.ENCODING_UTF8;
	result.put(PayOneConstants.KEY_ENCODING, value);
	return result;
    }

    @Override
    public HashMap<String, String> getMerchantIdParam() {
	HashMap<String, String> result = new HashMap<String, String>();
	String value = stdParams.getMerchantAccountId();
	result.put(PayOneConstants.KEY_MERCHANT_ID, value);
	return result;
    }

    @Override
    public HashMap<String, String> getModeParam() {
	HashMap<String, String> result = new HashMap<String, String>();
	String value = stdParams.getMode();
	result.put(PayOneConstants.KEY_MODE, value);
	return result;
    }

    @Override
    public HashMap<String, String> getPortalIdParam() {
	HashMap<String, String> result = new HashMap<String, String>();
	String value = stdParams.getPortalId();
	result.put(PayOneConstants.KEY_PORTAL_ID, value);
	return result;
    }

    @Override
    public HashMap<String, String> getResponseTypeParam() {
	HashMap<String, String> result = new HashMap<String, String>();
	String value = PayOneConstants.RESPONSE_TYPE_JSON;
	result.put(PayOneConstants.KEY_RESPONSE_TYPE, value);
	return result;
    }

    public HashMap<String, String> getHashParams() {
	HashMap<String, String> result = new HashMap<String, String>();
	result.putAll(getEncodingParam());
	result.putAll(getMerchantIdParam());
	result.putAll(getModeParam());
	result.putAll(getPortalIdParam());
	result.putAll(getResponseTypeParam());
	return result;
    }

    public String getHashParam(HashMap<String, String> hashParams) {
	String result = null;

	StringBuffer concatenatedValues = new StringBuffer();

	// add all params alphabetically sorted
	final Map<String, String> sortedMap = new TreeMap<String, String>(hashParams);
	final Collection<String> sortedValues = sortedMap.values();
	for (String value : sortedValues) {
	    concatenatedValues.append(value);
	}

	// add secret key
	concatenatedValues.append(stdParams.getKey());

	// hash params
	String toBeHashed = concatenatedValues.toString();

	LOGGER.debug("params to be hashed: '{}'", toBeHashed);

	String encoding = getEncodingParam().get(PayOneConstants.KEY_ENCODING);
	byte[] bytesOfMessage;
	try {
	    bytesOfMessage = toBeHashed.getBytes(encoding);
	} catch (UnsupportedEncodingException e) {
	    throw new IllegalArgumentException("unsupported encoding!", e);
	}

	try {
	    result = HashUtil.md5(bytesOfMessage);
	} catch (NoSuchAlgorithmException e) {
	    throw new IllegalArgumentException("unsupported algorithm!", e);
	}

	LOGGER.debug("hashed param string: '{}'", result);

	return result;
    }
}
