package com.pairoo.domain.payment;

import com.pairoo.domain.payment.enums.PaymentChannelType;

/**
 * @author Ralf Eichinger
 */
public class VoucherPayment extends PaymentChannel {
    private static final long serialVersionUID = 1L;

    private String promotionCode;

    public VoucherPayment() {
	setPaymentChannelType(PaymentChannelType.VOUCHER);
    }

    /**
     * @param promotionCode
     *            the promotionCode to set
     */
    public void setPromotionCode(String promotionCode) {
	this.promotionCode = promotionCode;
    }

    /**
     * @return the promotionCode
     */
    public String getPromotionCode() {
	return promotionCode;
    }
}
