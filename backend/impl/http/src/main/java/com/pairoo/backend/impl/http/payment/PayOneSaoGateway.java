package com.pairoo.backend.impl.http.payment;

import java.util.Map;

import com.pairoo.domain.payment.payone.request.CaptureParameters;
import com.pairoo.domain.payment.payone.request.PreAuthorizationParametersELV;
import com.pairoo.domain.payment.payone.request.PreAuthorizationParametersPseudoCreditCard;
import com.pairoo.domain.payment.payone.request.PreAuthorizationParametersWallet;
import com.pairoo.domain.payment.payone.request.StandardParameters;
import com.pairoo.domain.payment.payone.response.CaptureResponse;
import com.pairoo.domain.payment.payone.response.PreAuthorizationResponse;

public interface PayOneSaoGateway {
    public static final String BEAN_ID = "payOneSaoGateway";

    public PreAuthorizationResponse requestPreAuthorizationELV(StandardParameters standardParams,
	    PreAuthorizationParametersELV preAuthorizationParams);

    public PreAuthorizationResponse requestPreAuthorizationPseudoCreditCard(StandardParameters standardParams,
	    PreAuthorizationParametersPseudoCreditCard preAuthorizationParams);

    public PreAuthorizationResponse requestPreAuthorizationWallet(StandardParameters standardParams,
	    PreAuthorizationParametersWallet preAuthorizationParams);

    public CaptureResponse requestCapture(StandardParameters standardParams, CaptureParameters captureParams);

    public Map<String, String> requestAuthorization(Map<String, Object> payload);
}
