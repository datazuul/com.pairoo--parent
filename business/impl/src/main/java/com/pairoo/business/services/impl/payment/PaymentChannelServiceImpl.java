package com.pairoo.business.services.impl.payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datazuul.framework.business.services.persistence.AbstractDomainObjectServiceImpl;
import com.pairoo.backend.dao.payment.PaymentChannelDao;
import com.pairoo.business.api.payment.PaymentChannelService;
import com.pairoo.domain.User;
import com.pairoo.domain.payment.BankAccount;
import com.pairoo.domain.payment.CreditCardAccount;
import com.pairoo.domain.payment.PaymentChannel;
import com.pairoo.domain.payment.VoucherPayment;
import com.pairoo.domain.payment.WalletAccount;
import com.pairoo.domain.payment.enums.PaymentChannelType;

public class PaymentChannelServiceImpl extends AbstractDomainObjectServiceImpl<Long, PaymentChannel> implements
	PaymentChannelService {
    static final Logger LOGGER = LoggerFactory.getLogger(PaymentChannelServiceImpl.class);

    /**
     * Constructor needed for test handing over a dao.
     * 
     * @param dao
     *            the data access interface
     */
    public PaymentChannelServiceImpl(final PaymentChannelDao dao) {
	super(dao);
    }

    @Override
    public PaymentChannel getDefaultPaymentChannel() {
	CreditCardAccount defaultPaymentChannel = new CreditCardAccount();
	defaultPaymentChannel.setPaymentChannelType(PaymentChannelType.VISA);
	return defaultPaymentChannel;
    }

    /*
     * FIXME: lookup previous payment channel of given user to get those data preset
     * 
     * (non-Javadoc)
     * @see com.pairoo.business.api.payment.PaymentChannelService#getDefaultPaymentChannel(com.pairoo.domain.User, com.pairoo.domain.payment.enums.PaymentChannelType)
     */
    @Override
    public PaymentChannel getDefaultPaymentChannel(User user, PaymentChannelType paymentChannelType) {
	PaymentChannel result = null;
	switch (paymentChannelType) {
	case AMERICAN_EXPRESS:
	    result = new CreditCardAccount();
	    break;
	case ELEKTRONISCHE_LASTSCHRIFT:
	    result = new BankAccount();
	    break;
	case MASTERCARD:
	    result = new CreditCardAccount();
	    break;
	case PAYPAL:
	    result = new WalletAccount();
	    break;
	case VISA:
	    result = new CreditCardAccount();
	    break;
	case VOUCHER:
	    result = new VoucherPayment();
	    break;
	default:
	    result = getDefaultPaymentChannel();
	    break;
	}
	result.setPaymentChannelType(paymentChannelType);
	return result;
    }

}
