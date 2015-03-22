package com.pairoo.business.api.payment;

import com.datazuul.framework.business.services.DomainObjectService;
import com.pairoo.domain.UserAccount;
import com.pairoo.domain.payment.Transaction;
import com.pairoo.domain.payment.VoucherPayment;

public interface TransactionService extends DomainObjectService<Long, Transaction> {
    public static final String BEAN_ID = "transactionService";

    public Transaction performTransaction(final UserAccount userAccount, final VoucherPayment voucher);
}
