package com.pairoo.backend.dao.payment;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.pairoo.domain.payment.Transaction;

/**
 * @author Ralf Eichinger
 */
public interface TransactionDao extends GenericDAO<Transaction, Long> {

}
