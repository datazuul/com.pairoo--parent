package com.pairoo.business.api.payment.payone;

import com.datazuul.framework.business.services.DomainObjectService;
import com.pairoo.domain.payment.payone.PayOneTransaction;

public interface PayOneTransactionService extends DomainObjectService<Long, PayOneTransaction> {
    public static final String BEAN_ID = "payoneTransactionService";
}
