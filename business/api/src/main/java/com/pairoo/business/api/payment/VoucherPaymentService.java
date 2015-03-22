package com.pairoo.business.api.payment;

import com.datazuul.framework.business.services.DomainObjectService;
import com.pairoo.domain.UserAccount;
import com.pairoo.domain.marketing.Promotion;
import com.pairoo.domain.payment.VoucherPayment;
import com.pairoo.domain.payment.VoucherPaymentDuplicateException;

/**
 * @author Ralf Eichinger
 */
public interface VoucherPaymentService extends
	DomainObjectService<Long, VoucherPayment> {
    public static final String BEAN_ID = "voucherPaymentService";

    /**
     * TODO make private and add logic of pages to encashVoucher...
     * 
     * Get the voucher identified uniquely by the given code.
     * 
     * @param code
     *            code of the voucher
     * @return matching voucher
     */
//    public VoucherPayment getVoucherByCode(String code);

    /**
     * Encash a voucher by given useraccount.
     * 
     * @param userAccount
     *            encasher
     * @param promotion
     *            promotion of type voucher to encash
     * @throws VoucherPaymentDuplicateException
     */
    public void encashVoucher(UserAccount userAccount, Promotion promotion) throws VoucherPaymentDuplicateException;
}
