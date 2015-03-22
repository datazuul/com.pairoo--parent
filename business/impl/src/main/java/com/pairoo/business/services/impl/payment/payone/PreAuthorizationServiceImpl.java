package com.pairoo.business.services.impl.payment.payone;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pairoo.backend.sao.payment.PayOneSao;
import com.pairoo.business.api.payment.payone.PreAuthorizationService;
import com.pairoo.domain.payment.payone.PayOneConstants;
import com.pairoo.domain.payment.payone.request.PreAuthorizationParametersELV;
import com.pairoo.domain.payment.payone.request.PreAuthorizationParametersPseudoCreditCard;
import com.pairoo.domain.payment.payone.request.PreAuthorizationParametersWallet;
import com.pairoo.domain.payment.payone.request.StandardParameters;
import com.pairoo.domain.payment.payone.response.PreAuthorizationResponse;

public class PreAuthorizationServiceImpl extends PayOneServiceImpl implements PreAuthorizationService {
    static final Logger LOGGER = LoggerFactory.getLogger(PreAuthorizationServiceImpl.class);
    private PayOneSao payOneSao;
    private String subAccountId;

    public PreAuthorizationServiceImpl(PayOneSao payOneSao, final String merchantId, final String portalId,
	    final String subAccountId, final String secretKey, final String mode) {
	super(merchantId, portalId, secretKey, mode, PayOneConstants.REQUEST_AUTHORIZATION);
	this.subAccountId = subAccountId;
	this.payOneSao = payOneSao;
    }

    @Override
    public HashMap<String, String> getHashParam() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public HashMap<String, String> getRequestParam() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public PreAuthorizationResponse preAuthorize(StandardParameters stdParams, PreAuthorizationParametersELV params) {
	return payOneSao.requestPreAuthorizationELV(stdParams, params);
    }

    @Override
    public PreAuthorizationResponse preAuthorize(StandardParameters stdParams,
	    PreAuthorizationParametersPseudoCreditCard params) {
	return payOneSao.requestPreAuthorizationPseudoCreditCard(stdParams, params);
    }

    @Override
    public PreAuthorizationResponse preAuthorize(StandardParameters stdParams, PreAuthorizationParametersWallet params) {
	return payOneSao.requestPreAuthorizationWallet(stdParams, params);
    }

}
