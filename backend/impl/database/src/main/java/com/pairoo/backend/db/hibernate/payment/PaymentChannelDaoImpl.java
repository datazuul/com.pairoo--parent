package com.pairoo.backend.db.hibernate.payment;

import com.datazuul.framework.persistence.dao.hibernate.ExtendedGenericDaoImpl;
import com.pairoo.backend.dao.payment.PaymentChannelDao;
import com.pairoo.domain.payment.PaymentChannel;

/**
 * @author Ralf Eichinger
 */
public class PaymentChannelDaoImpl extends ExtendedGenericDaoImpl<PaymentChannel, Long> implements PaymentChannelDao {
}
