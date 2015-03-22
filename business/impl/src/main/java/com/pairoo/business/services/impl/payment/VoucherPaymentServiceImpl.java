package com.pairoo.business.services.impl.payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datazuul.framework.business.services.persistence.AbstractDomainObjectServiceImpl;
import com.pairoo.backend.dao.payment.PaymentChannelDao;
import com.pairoo.backend.dao.payment.VoucherPaymentDao;
import com.pairoo.business.api.MembershipService;
import com.pairoo.business.api.marketing.PromotionService;
import com.pairoo.business.api.payment.TransactionService;
import com.pairoo.business.api.payment.VoucherPaymentService;
import com.pairoo.domain.Membership;
import com.pairoo.domain.UserAccount;
import com.pairoo.domain.enums.MembershipStatus;
import com.pairoo.domain.marketing.Promotion;
import com.pairoo.domain.marketing.enums.PromotionType;
import com.pairoo.domain.payment.Transaction;
import com.pairoo.domain.payment.VoucherPayment;
import com.pairoo.domain.payment.VoucherPaymentDuplicateException;

public class VoucherPaymentServiceImpl extends AbstractDomainObjectServiceImpl<Long, VoucherPayment> implements
	VoucherPaymentService {

    static final Logger LOGGER = LoggerFactory.getLogger(VoucherPaymentServiceImpl.class);
    private final MembershipService membershipService;
    private final PromotionService promotionService;
    private final TransactionService transactionService;
    private final PaymentChannelDao paymentChannelDao;

    /**
     * Constructor needed for test handing over a mock dao.
     *
     * @param dao the data access interface
     */
    public VoucherPaymentServiceImpl(final PaymentChannelDao paymentChannelDao,
	    final VoucherPaymentDao voucherPaymentDao, final MembershipService membershipService,
	    final PromotionService promotionService, final TransactionService transactionService) {
	super(voucherPaymentDao);
	this.paymentChannelDao = paymentChannelDao;
	this.membershipService = membershipService;
	this.promotionService = promotionService;
	this.transactionService = transactionService;
    }

    @Override
    public void encashVoucher(UserAccount userAccount, Promotion promotion) throws VoucherPaymentDuplicateException {
	if (!PromotionType.VOUCHER.equals(promotion.getPromotionType())) {
	    throw new AssertionError("only promotions of type voucher can be encashed!");
	}
	LOGGER.info("encashing voucher with code = {} for user account = {}", new Object[]{promotion.getCode(),
	    userAccount.getUsername()});

	VoucherPayment voucherPayment = new VoucherPayment();
	voucherPayment.setPromotionCode(promotion.getCode());
	voucherPayment.setUserAccount(userAccount);
	voucherPayment.setEndDate(promotion.getValidTo());
	voucherPayment.setStartDate(promotion.getValidFrom());
	if (!dao.save(voucherPayment)) {
	    throw new VoucherPaymentDuplicateException();
	}
	Transaction paymentTransaction = transactionService.performTransaction(userAccount, voucherPayment);

	Membership membership = new Membership();
	membership.setPaymentTransaction(paymentTransaction);
	membership.setProduct(promotion.getProduct());
	membership.setPromotion(promotion);
	membershipService.addMembership(userAccount, membership, MembershipStatus.EXTENDED);

	// FIXME 28.10.: always getting NonUniqueObjectException: a different object
	// with the same identifier value was already associated with the
	// session:
	// [com.pairoo.domain.marketing.Promotion#1]
	// maybe because of starting different aop-transactions in different
	// services, both accessing the same Promotion instance?

	// if (!promotion.isUsed()) {
	// promotion.setUsed(true);
	// promotionService.save(promotion);
	// }

	LOGGER.info("user account roles now = {}", new Object[]{userAccount.getRoles().toString()});
    }
}
