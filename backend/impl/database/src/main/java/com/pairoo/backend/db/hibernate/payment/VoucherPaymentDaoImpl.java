package com.pairoo.backend.db.hibernate.payment;

import com.datazuul.framework.persistence.dao.hibernate.ExtendedGenericDaoImpl;
import com.pairoo.backend.dao.payment.VoucherPaymentDao;
import com.pairoo.domain.payment.VoucherPayment;

/**
 * @author Ralf Eichinger
 */
public class VoucherPaymentDaoImpl extends ExtendedGenericDaoImpl<VoucherPayment, Long> implements VoucherPaymentDao {

    @Override
    public boolean save(VoucherPayment entity) {
        try {
            super.save(entity);
        } catch (Exception e) {
            // vouchers/paymentchannels with same startdate can't be used by same user...
            // (TODO not really describing feature "voucher only once usable by user")
            // ERROR:  duplicate key value violates unique constraint "paymentchannels_useraccount_id_startdate_key"
            // DETAIL:  Key (useraccount_id, startdate)=(1, 2012-09-20 00:00:00) already exists.
            return false;
        }
        return true;
    }
}
