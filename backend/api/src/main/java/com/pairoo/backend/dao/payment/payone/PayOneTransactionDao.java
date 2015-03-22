package com.pairoo.backend.dao.payment.payone;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.pairoo.domain.payment.payone.PayOneTransaction;

/**
 * TODO: really needed? may be TransactionDao is enough... HBM extends...
 * @author Ralf Eichinger
 */
public interface PayOneTransactionDao extends GenericDAO<PayOneTransaction, Long> {

}
