package com.pairoo.business.api.payment.payone;

import com.pairoo.domain.payment.payone.request.PreAuthorizationParametersELV;
import com.pairoo.domain.payment.payone.request.PreAuthorizationParametersPseudoCreditCard;
import com.pairoo.domain.payment.payone.request.PreAuthorizationParametersWallet;
import com.pairoo.domain.payment.payone.request.StandardParameters;
import com.pairoo.domain.payment.payone.response.PreAuthorizationResponse;

/**
 * @author Ralf Eichinger
 */
public interface PreAuthorizationService extends PayOneService {
    public static final String BEAN_ID = "preAuthorizationService";

    public PreAuthorizationResponse preAuthorize(StandardParameters stdParams, PreAuthorizationParametersELV params);

    public PreAuthorizationResponse preAuthorize(StandardParameters stdParams,
	    PreAuthorizationParametersPseudoCreditCard params);

    public PreAuthorizationResponse preAuthorize(StandardParameters stdParams, PreAuthorizationParametersWallet params);
}
