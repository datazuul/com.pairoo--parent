package com.pairoo.business.services.impl.payment;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datazuul.framework.business.services.persistence.AbstractDomainObjectServiceImpl;
import com.pairoo.backend.dao.payment.TransactionDao;
import com.pairoo.business.api.payment.TransactionService;
import com.pairoo.domain.UserAccount;
import com.pairoo.domain.payment.Transaction;
import com.pairoo.domain.payment.VoucherPayment;
import com.pairoo.domain.payment.enums.StatusType;

/**
 * @author Ralf Eichinger
 */
public class TransactionServiceImpl extends
	AbstractDomainObjectServiceImpl<Long, Transaction> implements
	TransactionService {
    static final Logger LOGGER = LoggerFactory
	    .getLogger(TransactionServiceImpl.class);

    /**
     * Constructor needed for test handing over a dao.
     * 
     * @param dao
     *            the data access interface
     */
    public TransactionServiceImpl(final TransactionDao dao) {
	super(dao);
    }

    @Override
    public Transaction performTransaction(final UserAccount userAccount,
	    final VoucherPayment voucherPayment) {
	Transaction result = new Transaction();
	result.setAmount(0);
	result.setClearingType(null);
	result.setCurrencyCode(null);
	result.setNarrativeText("voucher " + voucherPayment.getPromotionCode());
	result.setPaymentChannel(voucherPayment);
	result.setStatus(StatusType.APPROVED);
	result.setTimeStamp(new Date());
	result.setUserAccount(userAccount);

	if (dao.save(result)) {
	    return result;
	} else {
	    return null;
	}
    }
}
