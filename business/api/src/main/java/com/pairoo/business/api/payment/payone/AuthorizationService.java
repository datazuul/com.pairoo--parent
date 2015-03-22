package com.pairoo.business.api.payment.payone;

import com.pairoo.domain.Membership;
import com.pairoo.domain.User;
import com.pairoo.domain.payment.payone.PayOneTransaction;

/**
 * @author Ralf Eichinger
 */
public interface AuthorizationService extends PayOneService {
    public static final String BEAN_ID = "authorizationService";

    public PayOneTransaction authorize(User user, Membership membership);
}
