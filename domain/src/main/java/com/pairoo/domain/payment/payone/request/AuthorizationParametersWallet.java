package com.pairoo.domain.payment.payone.request;

import com.pairoo.domain.payment.enums.ClearingType;
import com.pairoo.domain.payment.enums.WalletType;

/**
 * See PayOne documentation "FinanceGate_Server_API_v_2_5_DE.pdf".
 * 
 * Parameter (e-Wallet)
 * Parameter              Pflicht Format  Kommentar
 * wallettype             +       Vorgabe Wallet Anbieter
 *                                        PPE: PayPal Express
 * successurl             o       AN..255 URL "Zahlung erfolgreich"
 *                                        (nur wenn nicht bereits im PMI hinterlegt)
 * errorurl               o       AN..255 URL "Zahlung fehlerhaft"
 *                                        (nur wenn nicht bereits im PMI hinterlegt)
 * backurl                o       AN..255 URL "Zurück" oder "Abbrechen"
 *                                        (nur wenn nicht bereits im PMI hinterlegt)
 * 
 * As this parameters are the same than the ones in PreAuthorizationParametersWallet,
 * no additional fields have to be added.
 * 
 * @author Ralf Eichinger
 *
 */
public class AuthorizationParametersWallet extends AuthorizationParameters {
    public static final String MAP_KEY = "AuthorizationParametersWallet";
    private WalletType walletType;
    private String backUrl;
    private String errorUrl;
    private String successUrl;

    public AuthorizationParametersWallet(AuthorizationParameters params) {
	super(params);
	init();
    }

    public AuthorizationParametersWallet() {
	super();
	init();
    }

    private void init() {
	setClearingType(ClearingType.WALLET);
    }

    /**
     * @return the walletType
     */
    public WalletType getWalletType() {
	return walletType;
    }

    /**
     * @param walletType the walletType to set
     */
    public void setWalletType(WalletType walletType) {
	this.walletType = walletType;
    }

    /**
     * @return the backUrl
     */
    public String getBackUrl() {
	return backUrl;
    }

    /**
     * @param backUrl the backUrl to set
     */
    public void setBackUrl(String backUrl) {
	this.backUrl = backUrl;
    }

    /**
     * @return the errorUrl
     */
    public String getErrorUrl() {
	return errorUrl;
    }

    /**
     * @param errorUrl the errorUrl to set
     */
    public void setErrorUrl(String errorUrl) {
	this.errorUrl = errorUrl;
    }

    /**
     * @return the successUrl
     */
    public String getSuccessUrl() {
	return successUrl;
    }

    /**
     * @param successUrl the successUrl to set
     */
    public void setSuccessUrl(String successUrl) {
	this.successUrl = successUrl;
    }
}
