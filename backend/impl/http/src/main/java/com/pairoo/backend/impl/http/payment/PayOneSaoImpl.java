package com.pairoo.backend.impl.http.payment;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pairoo.backend.sao.payment.PayOneSao;
import com.pairoo.domain.payment.enums.StatusType;
import com.pairoo.domain.payment.payone.PayOneConstants;
import com.pairoo.domain.payment.payone.request.AuthorizationParametersELV;
import com.pairoo.domain.payment.payone.request.AuthorizationParametersPseudoCreditCard;
import com.pairoo.domain.payment.payone.request.AuthorizationParametersWallet;
import com.pairoo.domain.payment.payone.request.CaptureParameters;
import com.pairoo.domain.payment.payone.request.PreAuthorizationParametersELV;
import com.pairoo.domain.payment.payone.request.PreAuthorizationParametersPseudoCreditCard;
import com.pairoo.domain.payment.payone.request.PreAuthorizationParametersWallet;
import com.pairoo.domain.payment.payone.request.StandardParameters;
import com.pairoo.domain.payment.payone.response.AuthorizationResponse;
import com.pairoo.domain.payment.payone.response.CaptureResponse;
import com.pairoo.domain.payment.payone.response.PreAuthorizationResponse;

public class PayOneSaoImpl implements PayOneSao {
    static final Logger LOGGER = LoggerFactory.getLogger(PayOneSaoImpl.class);
    private final PayOneSaoGateway gateway;

    public PayOneSaoImpl(PayOneSaoGateway gateway) {
	this.gateway = gateway;
    }

    @Override
    public PreAuthorizationResponse requestPreAuthorizationELV(StandardParameters standardParams,
	    PreAuthorizationParametersELV preAuthorizationParams) {
	// FIXME see below
	return gateway.requestPreAuthorizationELV(standardParams, preAuthorizationParams);
    }

    @Override
    public PreAuthorizationResponse requestPreAuthorizationPseudoCreditCard(StandardParameters standardParams,
	    PreAuthorizationParametersPseudoCreditCard preAuthorizationParams) {
	// FIXME see below
	return gateway.requestPreAuthorizationPseudoCreditCard(standardParams, preAuthorizationParams);
    }

    @Override
    public PreAuthorizationResponse requestPreAuthorizationWallet(StandardParameters standardParams,
	    PreAuthorizationParametersWallet preAuthorizationParams) {
	// FIXME see below
	return gateway.requestPreAuthorizationWallet(standardParams, preAuthorizationParams);
    }

    @Override
    public CaptureResponse requestCapture(StandardParameters standardParams, CaptureParameters captureParams) {
	// FIXME see above
	return gateway.requestCapture(standardParams, captureParams);
    }

    @Override
    public AuthorizationResponse requestAuthorizationELV(StandardParameters standardParams,
	    AuthorizationParametersELV authorizationParams) {
	Map<String, Object> payload = new HashMap<>();
	payload.put(StandardParameters.MAP_KEY, standardParams);
	payload.put(AuthorizationParametersELV.MAP_KEY, authorizationParams);

	Map<String, String> responseMap = gateway.requestAuthorization(payload);

	AuthorizationResponse response = mapResponseToAuthorizationResponse(responseMap);
	return response;
    }

    @Override
    public AuthorizationResponse requestAuthorizationPseudoCreditCard(StandardParameters standardParams,
	    AuthorizationParametersPseudoCreditCard authorizationParams) {
	Map<String, Object> payload = new HashMap<>();
	payload.put(StandardParameters.MAP_KEY, standardParams);
	payload.put(AuthorizationParametersPseudoCreditCard.MAP_KEY, authorizationParams);

	Map<String, String> responseMap = gateway.requestAuthorization(payload);

	AuthorizationResponse response = mapResponseToAuthorizationResponse(responseMap);
	return response;
    }

    @Override
    public AuthorizationResponse requestAuthorizationWallet(StandardParameters standardParams,
	    AuthorizationParametersWallet authorizationParams) {
	Map<String, Object> payload = new HashMap<>();
	payload.put(StandardParameters.MAP_KEY, standardParams);
	payload.put(AuthorizationParametersWallet.MAP_KEY, authorizationParams);

	Map<String, String> responseMap = gateway.requestAuthorization(payload);

	AuthorizationResponse response = mapResponseToAuthorizationResponse(responseMap);
	return response;
    }

    private AuthorizationResponse mapResponseToAuthorizationResponse(Map<String, String> responseMap) {
	AuthorizationResponse response = new AuthorizationResponse();
	response.setCustomerMessage(responseMap.get(PayOneConstants.RESPONSE_KEY_CUSTOMER_MESSAGE));

	String errorcode = responseMap.get(PayOneConstants.RESPONSE_KEY_ERROR_CODE);
	if (errorcode != null) {
	    response.setErrorCode(Integer.valueOf(errorcode));
	}
	response.setErrorMessage(responseMap.get(PayOneConstants.RESPONSE_KEY_ERROR_MESSAGE));

	String payoneDebitorId = responseMap.get(PayOneConstants.RESPONSE_KEY_DEBITOR_ID);
	if (payoneDebitorId != null) {
	    response.setPayoneDebitorId(Integer.valueOf(payoneDebitorId));
	}

	String payoneTransactionId = responseMap.get(PayOneConstants.RESPONSE_KEY_TRANSACTION_ID);
	if (payoneTransactionId != null) {
	    response.setPayoneTransactionId(Long.valueOf(payoneTransactionId));
	}

	response.setRedirectUrl(responseMap.get(PayOneConstants.RESPONSE_KEY_REDIRECT_URL));
	String status = responseMap.get(PayOneConstants.RESPONSE_KEY_STATUS);
	if (PayOneConstants.RESPONSE_STATUS_APPROVED.equals(status)) {
	    response.setStatus(StatusType.APPROVED);
	} else if (PayOneConstants.RESPONSE_STATUS_ERROR.equals(status)) {
	    response.setStatus(StatusType.ERROR);
	} else if (PayOneConstants.RESPONSE_STATUS_REDIRECT.equals(status)) {
	    response.setStatus(StatusType.REDIRECT);
	}
	return response;
    }
}
