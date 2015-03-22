package com.pairoo.backend.db.hibernate.payment.payone;

import com.datazuul.framework.persistence.dao.hibernate.ExtendedGenericDaoImpl;
import com.pairoo.backend.dao.payment.payone.PayOneTransactionDao;
import com.pairoo.domain.payment.payone.PayOneTransaction;

/**
 * @author Ralf Eichinger
 */
public class PayOneTransactionDaoImpl extends ExtendedGenericDaoImpl<PayOneTransaction, Long> implements PayOneTransactionDao {
}
