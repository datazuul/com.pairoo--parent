package com.pairoo.backend.impl.http.payment;

import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.integration.transformer.AbstractPayloadTransformer;

import com.datazuul.framework.domain.Language;
import com.datazuul.framework.domain.geo.Country;
import com.datazuul.framework.security.HashUtil;
import com.datazuul.framework.util.EnumUtils;
import com.pairoo.backend.LocalizedStrings;
import com.pairoo.domain.enums.SalutationType;
import com.pairoo.domain.payment.enums.ClearingType;
import com.pairoo.domain.payment.enums.WalletType;
import com.pairoo.domain.payment.payone.PayOneConstants;
import com.pairoo.domain.payment.payone.request.AuthorizationParameters;
import com.pairoo.domain.payment.payone.request.AuthorizationParametersELV;
import com.pairoo.domain.payment.payone.request.AuthorizationParametersPseudoCreditCard;
import com.pairoo.domain.payment.payone.request.AuthorizationParametersWallet;
import com.pairoo.domain.payment.payone.request.StandardParameters;

/**
 * Transforming Payment Objects to HTTP POST request params:
 * HTTP POST is a format for encoding key-value pairs with possibly duplicate keys.
 * Each key-value pair is separated by an '&' character, and each key is separated
 * from its value by an '=' character.
 * Keys and values are both escaped by replacing spaces with the '+' character and
 * then using URL encoding on all other non-alphanumeric characters.
 * 
 * For example, the key-value pairs
 * Name: Jonathan Doe
 * Age: 23
 * Formula: a + b == 13%!
 * 
 * are encoded as
 * Name=Jonathan+Doe&Age=23&Formula=a+%2B+b+%3D%3D+13%25%21
 * 
 * This implementation returns the key/value-pairs as Map, containing unencoded values.
 * 
 * @author Ralf Eichinger
 */
public class ObjectsToHttpPostBodyTransformer extends AbstractPayloadTransformer<Object, Map<String, String>> {
    @Override
    protected Map<String, String> transformPayload(Object payload) throws Exception {
	Map<String, String> result = new HashMap<String, String>();

	Map<String, Object> payloadObjects = (Map<String, Object>) payload;
	Set<String> keySet = payloadObjects.keySet();
	for (String key : keySet) {
	    if (StandardParameters.MAP_KEY.equals(key)) {
		StandardParameters params = (StandardParameters) payloadObjects.get(key);
		put(result, PayOneConstants.KEY_ENCODING, params.getEncoding());

		String secretKey = params.getKey();
		try {
		    secretKey = HashUtil.md5(secretKey);
		} catch (NoSuchAlgorithmException e) {
		    throw new IllegalArgumentException("unsupported algorithm!", e);
		}
		put(result, PayOneConstants.KEY_SECRET_KEY, secretKey);

		put(result, PayOneConstants.KEY_MERCHANT_ID, params.getMerchantAccountId());
		put(result, PayOneConstants.KEY_MODE, params.getMode());
		put(result, PayOneConstants.KEY_PORTAL_ID, params.getPortalId());
		put(result, PayOneConstants.KEY_REQUEST, params.getRequest());
	    } else if (AuthorizationParametersPseudoCreditCard.MAP_KEY.equals(key)
		    || AuthorizationParametersELV.MAP_KEY.equals(key)
		    || AuthorizationParametersWallet.MAP_KEY.equals(key)) {
		AuthorizationParameters authorizationParams = (AuthorizationParameters) payloadObjects.get(key);

		// common authorization params
		addCommonAuthorizationParameters(result, authorizationParams);

		// person data
		addPersonData(result, authorizationParams);

		if (AuthorizationParametersPseudoCreditCard.MAP_KEY.equals(key)) {
		    AuthorizationParametersPseudoCreditCard params = (AuthorizationParametersPseudoCreditCard) payloadObjects
			    .get(key);
		    put(result, PayOneConstants.KEY_CARD_PSEUDO_PAN, params.getPseudoCardPan());
		} else if (AuthorizationParametersELV.MAP_KEY.equals(key)) {
		    AuthorizationParametersELV params = (AuthorizationParametersELV) payloadObjects.get(key);
		    put(result, PayOneConstants.KEY_ELV_BANK_ACCOUNT, params.getBankAccount());
		    put(result, PayOneConstants.KEY_ELV_BANK_ACCOUNT_HOLDER, params.getBankAccountHolder());
		    put(result, PayOneConstants.KEY_ELV_BANK_CODE, String.valueOf(params.getBankCode()));
		    put(result, PayOneConstants.KEY_ELV_BANK_COUNTRY, params.getBankCountry().name().toUpperCase());
		} else if (AuthorizationParametersWallet.MAP_KEY.equals(key)) {
		    AuthorizationParametersWallet params = (AuthorizationParametersWallet) payloadObjects.get(key);
		    WalletType walletType = params.getWalletType();
		    if (WalletType.PAYPAL_EXPRESS.equals(walletType)) {
			put(result, PayOneConstants.KEY_WALLET_TYPE, PayOneConstants.WALLET_TYPE_PAYPAL_EXPRESS);
		    } else {
			throw new NotImplementedException();
		    }
		} else {
		    throw new NotImplementedException();
		}
	    }
	}

	return result;
    }

    private void addPersonData(Map<String, String> result, AuthorizationParameters authorizationParams) {
	put(result, PayOneConstants.KEY_PERSON_ADDRESSADDITION, authorizationParams.getAddressAddition());
	if (authorizationParams.getBirthday() != null) {
	    SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
	    String formattedDate = df.format(authorizationParams.getBirthday());
	    put(result, PayOneConstants.KEY_PERSON_BIRTHDAY, formattedDate);
	}
	put(result, PayOneConstants.KEY_PERSON_CITY, authorizationParams.getCity());
	put(result, PayOneConstants.KEY_PERSON_COMPANY, authorizationParams.getCompany());
	if (authorizationParams.getCountry() != null) {
	    Country country = authorizationParams.getCountry();
	    put(result, PayOneConstants.KEY_PERSON_COUNTRY, country.getCountryCode());
	}
	if (authorizationParams.getPayOneDebitorId() != -1) {
	    put(result, PayOneConstants.KEY_DEBITOR_ID, String.valueOf(authorizationParams.getPayOneDebitorId()));
	}
	put(result, PayOneConstants.KEY_PERSON_EMAIL, authorizationParams.getEmail());
	put(result, PayOneConstants.KEY_PERSON_FIRSTNAME, authorizationParams.getFirstname());
	put(result, PayOneConstants.KEY_PERSON_IP_ADDRESS, authorizationParams.getIp());
	put(result, PayOneConstants.KEY_PERSON_LANGUAGE, authorizationParams.getLanguage());
	put(result, PayOneConstants.KEY_PERSON_LASTNAME, authorizationParams.getLastname());
	put(result, PayOneConstants.KEY_PERSON_TELEPHONENUMBER, authorizationParams.getPhoneNumber());

	if (authorizationParams.getSalutation() != null) {
	    SalutationType salutationType = authorizationParams.getSalutation();
	    String enumKey = EnumUtils.getEnumKey(salutationType);
	    String languageCode = authorizationParams.getLanguage();
	    if (languageCode == null) {
		languageCode = Language.ENGLISH.getLocale().getLanguage();
	    }
	    String saluation = LocalizedStrings.get(enumKey, Language.getLanguage(languageCode).getLocale());
	    put(result, PayOneConstants.KEY_PERSON_SALUTATION, saluation);
	}
	// not filled, yet (just do it if needed, until now it is optional...)
	//		if (params.getState() != null) {
	//		    String state = params.getState().getCode();
	//		    // ISO-3166 Subdivisions; for US and CA only
	//		    put(result, PayOneConstants.KEY_PERSON_STATE, state);
	//		}
	put(result, PayOneConstants.KEY_PERSON_STREET, authorizationParams.getStreet());
	put(result, PayOneConstants.KEY_PERSON_TITLE, authorizationParams.getTitle());
	put(result, PayOneConstants.KEY_PERSON_VAT_ID, authorizationParams.getVatId());
	put(result, PayOneConstants.KEY_PERSON_ZIPCODE, authorizationParams.getZipcode());

	// transaction data
	put(result, PayOneConstants.KEY_MERCHANT_CUSTOMER_ID, authorizationParams.getMerchantCustomerId());
    }

    private void addCommonAuthorizationParameters(Map<String, String> result,
	    AuthorizationParameters authorizationParams) {
	if (authorizationParams.getSubAccountId() != -1) {
	    put(result, PayOneConstants.KEY_SUBACCOUNT_ID, String.valueOf(authorizationParams.getSubAccountId()));
	}
	ClearingType clearingType = authorizationParams.getClearingType();
	if (ClearingType.CREDIT_CARD.equals(clearingType)) {
	    put(result, PayOneConstants.KEY_CLEARING_TYPE, PayOneConstants.CLEARING_TYPE_CREDITCARD);
	} else if (ClearingType.ELEKTRONISCHE_LASTSCHRIFT.equals(clearingType)) {
	    put(result, PayOneConstants.KEY_CLEARING_TYPE, PayOneConstants.CLEARING_TYPE_ELEKTRONISCHE_LASTSCHRIFT);
	} else if (ClearingType.INVOICE.equals(clearingType)) {
	    put(result, PayOneConstants.KEY_CLEARING_TYPE, PayOneConstants.CLEARING_TYPE_INVOICE);
	} else if (ClearingType.ONLINE_TRANSFER.equals(clearingType)) {
	    put(result, PayOneConstants.KEY_CLEARING_TYPE, PayOneConstants.CLEARING_TYPE_ONLINE_TRANSFER);
	} else if (ClearingType.PAY_ON_DELIVERY.equals(clearingType)) {
	    put(result, PayOneConstants.KEY_CLEARING_TYPE, PayOneConstants.CLEARING_TYPE_PAY_ON_DELIVERY);
	} else if (ClearingType.WALLET.equals(clearingType)) {
	    put(result, PayOneConstants.KEY_CLEARING_TYPE, PayOneConstants.CLEARING_TYPE_WALLET);
	}
	put(result, PayOneConstants.KEY_MERCHANT_TRANSACTION_ID, authorizationParams.getMerchantTransactionReference());
	put(result, PayOneConstants.KEY_AMOUNT, String.valueOf(authorizationParams.getAmount()));
	if (authorizationParams.getCurrency() != null) {
	    put(result, PayOneConstants.KEY_CURRENCY, authorizationParams.getCurrency().getCode());
	}
	put(result, PayOneConstants.KEY_NARRATIVE_TEXT, authorizationParams.getNarrativeText());
	put(result, PayOneConstants.KEY_PARAM, authorizationParams.getParam());
    }

    private void put(Map<String, String> result, String key, String value) {
	if (value != null) {
	    result.put(key, value);
	}
    }
}
