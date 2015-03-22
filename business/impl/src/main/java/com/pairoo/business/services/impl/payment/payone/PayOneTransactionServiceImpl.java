package com.pairoo.business.services.impl.payment.payone;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datazuul.framework.business.services.persistence.AbstractDomainObjectServiceImpl;
import com.pairoo.backend.dao.payment.payone.PayOneTransactionDao;
import com.pairoo.business.api.payment.payone.PayOneTransactionService;
import com.pairoo.domain.payment.payone.PayOneTransaction;

/**
 * @author Ralf Eichinger
 */
public class PayOneTransactionServiceImpl extends AbstractDomainObjectServiceImpl<Long, PayOneTransaction> implements
	PayOneTransactionService {
    static final Logger LOGGER = LoggerFactory.getLogger(PayOneTransactionServiceImpl.class);

    /**
     * Constructor needed for test handing over a dao.
     * 
     * @param dao
     *            the data access interface
     */
    public PayOneTransactionServiceImpl(final PayOneTransactionDao dao) {
	super(dao);
    }
    
    @Override
    public boolean save(PayOneTransaction payOneTransaction) {
        payOneTransaction.setTimeStamp(new Date());
        return super.save(payOneTransaction);
    }
}
