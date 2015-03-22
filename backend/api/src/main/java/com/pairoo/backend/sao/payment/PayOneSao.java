package com.pairoo.backend.sao.payment;

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

public interface PayOneSao {
    public static final String BEAN_ID = "payOneSao";

    public PreAuthorizationResponse requestPreAuthorizationELV(StandardParameters standardParams,
	    PreAuthorizationParametersELV preAuthorizationParams);

    public PreAuthorizationResponse requestPreAuthorizationPseudoCreditCard(StandardParameters standardParams,
	    PreAuthorizationParametersPseudoCreditCard preAuthorizationParams);

    public PreAuthorizationResponse requestPreAuthorizationWallet(StandardParameters standardParams,
	    PreAuthorizationParametersWallet preAuthorizationParams);

    public AuthorizationResponse requestAuthorizationELV(StandardParameters standardParams,
	    AuthorizationParametersELV authorizationParams);

    public AuthorizationResponse requestAuthorizationPseudoCreditCard(StandardParameters standardParams,
	    AuthorizationParametersPseudoCreditCard authorizationParams);

    public AuthorizationResponse requestAuthorizationWallet(StandardParameters standardParams,
	    AuthorizationParametersWallet authorizationParams);

    public CaptureResponse requestCapture(StandardParameters standardParams, CaptureParameters captureParams);
}
