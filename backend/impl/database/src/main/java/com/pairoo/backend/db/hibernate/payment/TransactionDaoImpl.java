package com.pairoo.backend.db.hibernate.payment;

import com.datazuul.framework.persistence.dao.hibernate.ExtendedGenericDaoImpl;
import com.pairoo.backend.dao.payment.TransactionDao;
import com.pairoo.domain.payment.Transaction;

/**
 * @author Ralf Eichinger
 */
public class TransactionDaoImpl extends ExtendedGenericDaoImpl<Transaction, Long> implements TransactionDao {
}
