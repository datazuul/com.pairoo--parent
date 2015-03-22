package com.pairoo.business.services.impl.payment.payone;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.measure.unit.Unit;

import org.apache.commons.lang.NotImplementedException;
import org.jscience.economics.money.Currency;
import org.jscience.economics.money.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pairoo.backend.sao.payment.PayOneSao;
import com.pairoo.business.LocalizedStrings;
import com.pairoo.business.api.NotificationService;
import com.pairoo.business.api.ProductService;
import com.pairoo.business.api.payment.payone.AuthorizationService;
import com.pairoo.business.api.payment.payone.PayOneTransactionService;
import com.pairoo.domain.Membership;
import com.pairoo.domain.User;
import com.pairoo.domain.payment.BankAccount;
import com.pairoo.domain.payment.CreditCardAccount;
import com.pairoo.domain.payment.PaymentChannel;
import com.pairoo.domain.payment.WalletAccount;
import com.pairoo.domain.payment.enums.PaymentChannelType;
import com.pairoo.domain.payment.enums.StatusType;
import com.pairoo.domain.payment.payone.PayOneConstants;
import com.pairoo.domain.payment.payone.PayOneTransaction;
import com.pairoo.domain.payment.payone.request.AuthorizationParameters;
import com.pairoo.domain.payment.payone.request.AuthorizationParametersELV;
import com.pairoo.domain.payment.payone.request.AuthorizationParametersPseudoCreditCard;
import com.pairoo.domain.payment.payone.request.AuthorizationParametersWallet;
import com.pairoo.domain.payment.payone.request.StandardParameters;
import com.pairoo.domain.payment.payone.response.AuthorizationResponse;

public class AuthorizationServiceImpl extends PayOneServiceImpl implements AuthorizationService {

    static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationServiceImpl.class);

    private final NotificationService notificationService;
    private PayOneSao payOneSao;
    private PayOneTransactionService payOneTransactionService;
    private ProductService productService;
    private int subAccountId;

    public AuthorizationServiceImpl(final PayOneSao payOneSao, final PayOneTransactionService payOneTransactionService,
            final ProductService productService, final String merchantId, final String portalId,
            final int subAccountId, final String secretKey, final String mode, final NotificationService notificationService) {
        super(merchantId, portalId, secretKey, mode, PayOneConstants.REQUEST_AUTHORIZATION);
        this.subAccountId = subAccountId;
        this.notificationService = notificationService;
        this.payOneSao = payOneSao;
        this.payOneTransactionService = payOneTransactionService;
        this.productService = productService;
    }

    @Override
    public HashMap<String, String> getHashParam() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HashMap<String, String> getRequestParam() {
        // TODO Auto-generated method stub
        return null;
    }

    private AuthorizationResponse authorize(StandardParameters stdParams, AuthorizationParametersELV params) {
        return payOneSao.requestAuthorizationELV(stdParams, params);
    }

    private AuthorizationResponse authorize(StandardParameters stdParams, AuthorizationParametersPseudoCreditCard params) {
        return payOneSao.requestAuthorizationPseudoCreditCard(stdParams, params);
    }

    private AuthorizationResponse authorize(StandardParameters stdParams, AuthorizationParametersWallet params) {
        return payOneSao.requestAuthorizationWallet(stdParams, params);
    }

    @Override
    public PayOneTransaction authorize(User user, Membership membership) {
        AuthorizationResponse authorizationResponse = null;
        PayOneTransaction transaction = new PayOneTransaction();

        // standard parameters (the same for all (authorization) requests)
        AuthorizationParameters params = new AuthorizationParameters(); // TODO not nice... change hierarchy just to see Authorization...

        // amount in specific currency
        Unit<Money> unitMoney = user.getUserProfile().getGeoLocation().getCountry().getUnitMoney();
        final int totalAmountInCent = productService.getTotalAmountInCent(membership.getProduct(), unitMoney);
        params.setAmount(totalAmountInCent);

        // currency
        params.setCurrency((Currency) unitMoney);
        // language
        Locale locale = user.getUserAccount().getPreferredLanguage().getLocale();
        params.setLanguage(locale.getLanguage());
        // customer id at merchant
        params.setMerchantCustomerId("user_" + user.getId());
        // transaction reference at merchant (must be globally unique! -> ERROR 911)
        SimpleDateFormat sdf = new SimpleDateFormat("MMdd-hhmm");
        String merchantTransactionReference = user.getUserAccount().getId() + "_" + sdf.format(new Date());
        if (merchantTransactionReference.length() > 20) {
            throw new IllegalArgumentException("merchant transaction reference id too long! "
                    + merchantTransactionReference);
        }
        params.setMerchantTransactionReference(merchantTransactionReference);
        // narrative text
        params.setNarrativeText(LocalizedStrings.get(LocalizedStrings.TRANSACTION_NARRATIVE_TEXT, locale));
        // payone subaccount id
        params.setSubAccountId(subAccountId);

	// person data
        // country
        params.setCountry(user.getAddress().getCountry());
        // firstname
        params.setFirstname(user.getFirstname());
        // lastname
        params.setLastname(user.getLastname());

        // send authorization request to payone
        PaymentChannel paymentChannel = membership.getPaymentTransaction().getPaymentChannel();
        PaymentChannelType paymentChannelType = paymentChannel.getPaymentChannelType();
        if (PaymentChannelType.VISA.equals(paymentChannelType)
                || PaymentChannelType.AMERICAN_EXPRESS.equals(paymentChannelType)
                || PaymentChannelType.MASTERCARD.equals(paymentChannelType)) {
            CreditCardAccount creditCardAccount = (CreditCardAccount) paymentChannel;

            AuthorizationParametersPseudoCreditCard params2 = new AuthorizationParametersPseudoCreditCard(params);
            String pseudoCardPan = creditCardAccount.getPseudopan();
            params2.setPseudoCardPan(pseudoCardPan);

            authorizationResponse = authorize(stdParams, params2);

            transaction.setClearingType(params2.getClearingType());
        } else if (PaymentChannelType.ELEKTRONISCHE_LASTSCHRIFT.equals(paymentChannelType)) {
            BankAccount bankAccount = (BankAccount) paymentChannel;

            AuthorizationParametersELV params2 = new AuthorizationParametersELV(params);
            params2.setBankAccount(bankAccount.getBankAccount());
            params2.setBankAccountHolder(bankAccount.getBankAccountHolder());
            params2.setBankCode(bankAccount.getBankCode());
            params2.setBankCountry(bankAccount.getBankCountry());

            authorizationResponse = authorize(stdParams, params2);

            transaction.setClearingType(params2.getClearingType());
        } else if (PaymentChannelType.PAYPAL.equals(paymentChannelType)) {
            WalletAccount walletAccount = (WalletAccount) paymentChannel;

            AuthorizationParametersWallet params2 = new AuthorizationParametersWallet(params);
            params2.setWalletType(walletAccount.getWalletType());

            authorizationResponse = authorize(stdParams, params2);

            transaction.setClearingType(params2.getClearingType());
        } else {
            throw new NotImplementedException();
        }

        // save transaction
        transaction = saveTransaction(transaction, user, params, authorizationResponse, totalAmountInCent,
                paymentChannel);

        if (StatusType.APPROVED.equals(transaction.getStatus())) {
            // 4. inform Pairoo about new member
            notificationService.notifyAboutNewPayment(user, transaction);
        }
        return transaction;
    }

    private PayOneTransaction saveTransaction(PayOneTransaction transaction, User user, AuthorizationParameters params,
            AuthorizationResponse authorizationResponse, final int amount, PaymentChannel paymentChannel) {
        transaction.setAmount(amount);
        transaction.setCurrencyCode(params.getCurrency().getCode());
        transaction.setNarrativeText(params.getNarrativeText());
        transaction.setPaymentChannel(paymentChannel);
        transaction.setStatus(authorizationResponse.getStatus());
        transaction.setUserAccount(user.getUserAccount());

        transaction.setCustomerMessage(authorizationResponse.getCustomerMessage());
        transaction.setErrorCode(authorizationResponse.getErrorCode());
        transaction.setErrorMessage(authorizationResponse.getErrorMessage());
        transaction.setMerchantTransactionReference(params.getMerchantTransactionReference());
        //	transaction.setParam(null);
        transaction.setPayoneDebitorId(authorizationResponse.getPayoneDebitorId());
        transaction.setPayoneId(authorizationResponse.getPayoneTransactionId());
        transaction.setRedirectUrl(authorizationResponse.getRedirectUrl());
        transaction.setSubAccountId(params.getSubAccountId());

        LOGGER.info(
                "transaction [payone id={}/debitor={}, username '{}']: amount({} {}), clearing type({}), payment channel ({}), status({}, {}, {})",
                new Object[]{transaction.getPayoneId(), transaction.getPayoneDebitorId(),
                    transaction.getUserAccount().getUsername(), transaction.getAmount(),
                    transaction.getCurrencyCode(), transaction.getClearingType().name(),
                    transaction.getPaymentChannel().getPaymentChannelType().name(), transaction.getStatus().name(),
                    transaction.getErrorCode(), transaction.getErrorMessage()});

        payOneTransactionService.save(transaction);

        return transaction;
    }
}
