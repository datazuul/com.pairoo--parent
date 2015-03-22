package com.pairoo.business.api.payment;

import com.datazuul.framework.business.services.DomainObjectService;
import com.pairoo.domain.User;
import com.pairoo.domain.payment.PaymentChannel;
import com.pairoo.domain.payment.enums.PaymentChannelType;

public interface PaymentChannelService extends DomainObjectService<Long, PaymentChannel> {
    public static final String BEAN_ID = "paymentChannelService";

    /**
     * @return the default payment channel
     */
    public PaymentChannel getDefaultPaymentChannel();

    /**
     * Tries to lookup a previous used paymentchannel of an user of a special type.
     * @param user owner of the channel
     * 
     * @return an empty payment channel of a special type (or a filled one with previous used data)
     */
    public PaymentChannel getDefaultPaymentChannel(User user, PaymentChannelType paymentChannelType);
}
