package com.pairoo.domain.payment.payone.request;

/**
 * See PayOne documentation "FinanceGate_Server_API_v_2_5_DE.pdf".
 * 
 * Parameter Pflicht Format  Kommentar
 * mid       +       N..6    ID des Merchant-Accounts
 * portalid  +       A..7    ID des Zahlungsportals
 * key               AN..32  Key des Zahlungsportals als MD5 Wert
 *           +
 * mode      +       Vorgabe test: Testmodus
 *                           live: Livemodus
 * request   +       Vorgabe Anfragen:
 *                           - preauthorization
 *                           - authorization
 *                           - capture
 *                           - refund
 *                           ...
 * encoding  -       Vorgabe ISO-8859-1 (default)
 *                           UTF-8
 *
 * @author Ralf Eichinger
 */
public class StandardParameters {
    public final static String MAP_KEY = "StandardParameters";
    
    private String merchantAccountId; // mid
    private String portalId;
    private String key;
    private String mode;
    private String request;
    private String encoding;

    public StandardParameters(final String merchantAccountId, final String portalId, final String key,
	    final String mode, final String request, final String encoding) {
	this.encoding = encoding;
	this.key = key;
	this.merchantAccountId = merchantAccountId;
	this.mode = mode;
	this.portalId = portalId;
	this.request = request;
    }

    /**
     * @return the merchantAccountId
     */
    public String getMerchantAccountId() {
	return merchantAccountId;
    }

    /**
     * @param merchantAccountId the merchantAccountId to set
     */
    public void setMerchantAccountId(String merchantAccountId) {
	this.merchantAccountId = merchantAccountId;
    }

    /**
     * @return the portalId
     */
    public String getPortalId() {
	return portalId;
    }

    /**
     * @param portalId the portalId to set
     */
    public void setPortalId(String portalId) {
	this.portalId = portalId;
    }

    /**
     * @return the key
     */
    public String getKey() {
	return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
	this.key = key;
    }

    /**
     * @return the mode
     */
    public String getMode() {
	return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(String mode) {
	this.mode = mode;
    }

    /**
     * @return the request
     */
    public String getRequest() {
	return request;
    }

    /**
     * @param request the request to set
     */
    public void setRequest(String request) {
	this.request = request;
    }

    /**
     * @return the encoding
     */
    public String getEncoding() {
	return encoding;
    }

    /**
     * @param encoding the encoding to set
     */
    public void setEncoding(String encoding) {
	this.encoding = encoding;
    }

}
