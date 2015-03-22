package com.pairoo.business.services.impl;

import com.pairoo.backend.sao.EmailSao;
import com.pairoo.business.LocalizedStrings;
import com.pairoo.business.api.MessageService;
import com.pairoo.business.api.NotificationService;
import static com.pairoo.business.services.impl.UserServiceImpl.LOGGER;
import com.pairoo.domain.Message;
import com.pairoo.domain.PersonProfile;
import com.pairoo.domain.User;
import com.pairoo.domain.UserProfile;
import com.pairoo.domain.payment.payone.PayOneTransaction;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @see NotificationService
 * @author ralf
 */
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private EmailSao emailSao;
    private final MessageService messageService;

    public NotificationServiceImpl(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void notifyAboutNewMember(User user) {
        Locale locale = Locale.GERMAN;
        final User sender = new User();
        sender.setEmail(LocalizedStrings.get(LocalizedStrings.EMAIL_ADDRESS_PAIROO, locale));

        User receiver = getNotificationReceiver(locale);

        Map<String, Object> model = new HashMap<>();
        model.put("user", user);
        final UserProfile userProfile = user.getUserProfile();
        Date birthdate = userProfile.getBirthdate();
        if (birthdate != null) {
            model.put("age", PersonProfile.getAge(birthdate));
        } else {
            model.put("age", "--");
        }
        model.put("partnerType", userProfile.getPartnerType().name());

        final Message msg = messageService.createMessage(receiver, sender, locale,
                "Neues Mitglied " + user.getUserAccount().getUsername(), "com/pairoo/business/templates/notification-registration-txt", model);

        try {
            emailSao.sendMail(msg);
            LOGGER.info("Notification email about new registration sent to {}", receiver.getEmail());
        } catch (final Exception e) {
            LOGGER.warn("Sending of notification email about new registration to {} FAILED!", receiver.getEmail());
        }
    }

    private User getNotificationReceiver(Locale locale) {
        final User receiver = new User();
        if ("DEMO".equals(System.getProperty("env"))) {
            receiver.setEmail("ralf.eichinger@pairoo.de");
        } else {
            receiver.setEmail(LocalizedStrings.get(LocalizedStrings.EMAIL_ADDRESS_SUPPORT, locale));
        }
        return receiver;
    }

    @Override
    public void notifyAboutNewPayment(User user, PayOneTransaction transaction) {
        Locale locale = Locale.GERMAN;
        final User sender = new User();
        sender.setEmail(LocalizedStrings.get(LocalizedStrings.EMAIL_ADDRESS_PAIROO, locale));

        User receiver = getNotificationReceiver(locale);

        Map<String, Object> model = new HashMap<>();
        model.put("user", user);

        String paymentChannelType = "?";
        try {
            paymentChannelType = transaction.getPaymentChannel().getPaymentChannelType().name();
        } catch (Exception e) {

        }
        model.put("paymentChannelType", paymentChannelType);

        String amount = "?";
         try {
            amount = "" + transaction.getAmount() + " " + transaction.getCurrencyCode();
        } catch (Exception e) {

        }
        model.put("amount", amount);
        
        final Message msg = messageService.createMessage(receiver, sender, locale,
                "Bezahlungs-Transaktion von User " + user.getUserAccount().getUsername(), "com/pairoo/business/templates/notification-payment-txt", model);

        try {
            emailSao.sendMail(msg);
            LOGGER.info("Notification email about new payment sent to {}", receiver.getEmail());
        } catch (final Exception e) {
            LOGGER.warn("Sending of notification email about new payment to {} FAILED!", receiver.getEmail());
        }
    }

}
