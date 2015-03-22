package com.pairoo.business.api;

import com.pairoo.domain.User;
import com.pairoo.domain.payment.payone.PayOneTransaction;

/**
 * Bundles notification sending to Pairoo about interesting system and business events.
 * @author ralf
 */
public interface NotificationService {
    public void notifyAboutNewMember(User user);

    public void notifyAboutNewPayment(User user, PayOneTransaction transaction);
}
