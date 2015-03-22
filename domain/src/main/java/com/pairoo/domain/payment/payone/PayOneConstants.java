package com.pairoo.domain.payment.payone;

/**
 * See PayOne documentations "FinanceGate_Client_API_v_0_9_DE.pdf"
 * and "FinanceGate_Server_API_v_2_5_DE.pdf".
 * 
 * @author Ralf Eichinger
 */
public class PayOneConstants {
    // key/parameter names
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_BANKACCOUNT_CHECK_TYPE = "checktype";
    public static final String KEY_CARD_EXPIRE_MONTH = "cardexpiremonth";
    public static final String KEY_CARD_EXPIRE_YEAR = "cardexpireyear";
    public static final String KEY_CARD_HOLDER = "cardholder";
    public static final String KEY_CARD_PAN = "cardpan";
    public static final String KEY_CARD_PSEUDO_PAN = "pseudocardpan";
    public static final String KEY_CARD_TYPE = "cardtype";
    public static final String KEY_CARD_VALIDATION_CODE_2 = "cardcvc2";
    public static final String KEY_CLEARING_TYPE = "clearingtype";
    public static final String KEY_CURRENCY = "currency";
    public static final String KEY_DEBITOR_ID = "userid";
    public static final String KEY_ELV_BANK_ACCOUNT = "bankaccount";
    public static final String KEY_ELV_BANK_ACCOUNT_HOLDER = "bankaccountholder";
    public static final String KEY_ELV_BANK_CODE = "bankcode";
    public static final String KEY_ELV_BANK_COUNTRY = "bankcountry";
    public static final String KEY_ENCODING = "encoding";
    public static final String KEY_ERROR_URL = "errorurl";
    public static final String KEY_HASH = "hash";
    public static final String KEY_LANGUAGE = "language";
    public static final String KEY_MODE = "mode";
    public static final String KEY_MERCHANT_CUSTOMER_ID = "customerid";
    public static final String KEY_MERCHANT_ID = "mid";
    public static final String KEY_MERCHANT_TRANSACTION_ID = "reference";
    public static final String KEY_NARRATIVE_TEXT = "narrative_text";
    public static final String KEY_PARAM = "param";
    public static final String KEY_PERSON_ADDRESSADDITION = "addressaddition";
    public static final String KEY_PERSON_BIRTHDAY = "birthday";
    public static final String KEY_PERSON_CITY = "city";
    public static final String KEY_PERSON_COMPANY = "company";
    public static final String KEY_PERSON_COUNTRY = "country";
    public static final String KEY_PERSON_EMAIL = "email";
    public static final String KEY_PERSON_FIRSTNAME = "firstname";
    public static final String KEY_PERSON_IP_ADDRESS = "ip";
    public static final String KEY_PERSON_LANGUAGE = "language";
    public static final String KEY_PERSON_LASTNAME = "lastname";
    public static final String KEY_PERSON_SALUTATION = "salutation";
    public static final String KEY_PERSON_STATE = "state";
    public static final String KEY_PERSON_STREET = "street";
    public static final String KEY_PERSON_TELEPHONENUMBER = "telephonenumber";
    public static final String KEY_PERSON_TITLE = "title";
    public static final String KEY_PERSON_VAT_ID = "vatid";
    public static final String KEY_PERSON_ZIPCODE = "zip";
    public static final String KEY_PORTAL_ID = "portalid";
    public static final String KEY_REQUEST = "request";
    public static final String KEY_RESPONSE_TYPE = "responsetype";
    public static final String KEY_SECRET_KEY = "key";
    public static final String KEY_STORE_CARDDATA = "storecarddata";
    public static final String KEY_SUBACCOUNT_ID = "aid";
    public static final String KEY_SUCCESS_URL = "successurl";
    public static final String KEY_WALLET_TYPE = "wallettype";

    // parameter values
    public static final String BANKACCOUNT_CHECK_TYPE_POS = "1";
    public static final String BANKACCOUNT_CHECK_TYPE_REGULAR = "0";
    public static final String CARD_TYPE_AMERICAN_EXPRESS = "A";
    public static final String CARD_TYPE_CARTE_BLEUE = "B";
    public static final String CARD_TYPE_DINERS = "D";
    public static final String CARD_TYPE_DISCOVER = "C";
    public static final String CARD_TYPE_JCB = "J";
    public static final String CARD_TYPE_MAESTRO_INTERNATIONAL = "O";
    public static final String CARD_TYPE_MAESTRO_UK = "U";
    public static final String CARD_TYPE_MASTERCARD = "M";
    public static final String CARD_TYPE_VISA = "V";
    public static final String CLEARING_TYPE_CREDITCARD = "cc";
    public static final String CLEARING_TYPE_ELEKTRONISCHE_LASTSCHRIFT = "elv";
    public static final String CLEARING_TYPE_INVOICE = "rec";
    public static final String CLEARING_TYPE_ONLINE_TRANSFER = "sb";
    public static final String CLEARING_TYPE_PAY_ON_DELIVERY = "cod";
    public static final String CLEARING_TYPE_WALLET = "wlt";
    public static final String ENCODING_ISO_8859_1 = "ISO-8859-1";
    public static final String ENCODING_UTF8 = "UTF-8";
    public static final String MODE_PRODUCTION = "live";
    public static final String MODE_TEST = "test";
    public static final String REQUEST_AUTHORIZATION = "authorization";
    public static final String REQUEST_BANK_ACCOUNT_CHECK = "bankaccountcheck";
    public static final String REQUEST_CAPTURE = "capture";
    public static final String REQUEST_CREDITCARD_CHECK = "creditcardcheck";
    public static final String REQUEST_PREAUTHORIZATION = "preauthorization";
    public static final String REQUEST_REFUND = "refund";
    public static final String RESPONSE_KEY_CUSTOMER_MESSAGE = "customermessage";
    public static final String RESPONSE_KEY_DEBITOR_ID = "userid";
    public static final String RESPONSE_KEY_ERROR_CODE = "errorcode";
    public static final String RESPONSE_KEY_ERROR_MESSAGE = "errormessage";
    public static final String RESPONSE_KEY_STATUS = "status";
    public static final String RESPONSE_KEY_REDIRECT_URL = "redirecturl";
    public static final String RESPONSE_KEY_TRANSACTION_ID = "txid";
    public static final String RESPONSE_STATUS_APPROVED = "APPROVED";
    public static final String RESPONSE_STATUS_ERROR = "ERROR";
    public static final String RESPONSE_STATUS_REDIRECT = "REDIRECT";
    public static final String RESPONSE_TYPE_JSON = "JSON";
    public static final String RESPONSE_TYPE_REDIRECT = "REDIRECT";
    public static final String STORE_CARDDATA_FALSE = "no";
    public static final String STORE_CARDDATA_TRUE = "yes";
    public static final String WALLET_TYPE_PAYPAL_EXPRESS = "PPE";
}
