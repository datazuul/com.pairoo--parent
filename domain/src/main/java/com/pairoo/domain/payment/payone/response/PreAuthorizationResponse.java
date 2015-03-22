package com.pairoo.domain.payment.payone.response;

import com.pairoo.domain.payment.enums.StatusType;

/**
 * See PayOne documentation "FinanceGate_Server_API_v_2_5_DE.pdf".
 * 
 * Parameter              Pflicht Format  Kommentar
 * status                 +       Vorgabe APPROVED / REDIRECT / ERROR
 * 
 * Parameter (APPROVED)
 * txid                   +       N..12   ID des Zahlungsvorgangs (PAYONE)
 * userid                 +       N..8    ID des Debitors (PAYONE)
 * 
 * Parameter (REDIRECT) (3D-Secure/Online Überweisung/e-Wallet)
 * txid                   +       N..12   ID des Zahlungsvorgangs (PAYONE)
 * userid                 +       N..8    ID des Debitors (PAYONE)
 * redirecturl            +       AN..255 Redirect-URL
 * 
 * Parameter (ERROR)
 * errorcode              +       N..6    Fehlernummer
 * errormessage           +       AN..255 Fehlernachricht für den Händler
 * customermessage        -       AN..255 Fehlernachricht für den Endkunden
 *                                        (Sprachauswahl erfolgt über die
 *                                         Sprache des Endkunden „language“)
 *                                         
 * Parameter (Kreditkarte – sofern AVS beauftragt )
 * (AVS (Address Verification System): wird derzeit nur für American Express unterstützt)
 * protect_result_avs     -       A1      AVS-Rückgabewert, s. Kap. 5.3
 * 
 * @author Ralf Eichinger
 *
 */
public class PreAuthorizationResponse {
    private StatusType status;
    
    private long payoneTransactionId; // txid
    private int payoneDebitorId; //userid
    private String redirectUrl;
    private int errorCode;
    private String errorMessage;
    private String customerMessage;

    /**
     * @return the payoneTransactionId
     */
    public long getPayoneTransactionId() {
	return payoneTransactionId;
    }

    /**
     * @param payoneTransactionId the payoneTransactionId to set
     */
    public void setPayoneTransactionId(long payoneTransactionId) {
	this.payoneTransactionId = payoneTransactionId;
    }

    /**
     * @return the payoneDebitorId
     */
    public int getPayoneDebitorId() {
	return payoneDebitorId;
    }

    /**
     * @param payoneDebitorId the payoneDebitorId to set
     */
    public void setPayoneDebitorId(int payoneDebitorId) {
	this.payoneDebitorId = payoneDebitorId;
    }

    /**
     * @return the redirectUrl
     */
    public String getRedirectUrl() {
	return redirectUrl;
    }

    /**
     * @param redirectUrl the redirectUrl to set
     */
    public void setRedirectUrl(String redirectUrl) {
	this.redirectUrl = redirectUrl;
    }

    /**
     * @return the errorCode
     */
    public int getErrorCode() {
	return errorCode;
    }

    /**
     * @param errorCode the errorCode to set
     */
    public void setErrorCode(int errorCode) {
	this.errorCode = errorCode;
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
	return errorMessage;
    }

    /**
     * @param errorMessage the errorMessage to set
     */
    public void setErrorMessage(String errorMessage) {
	this.errorMessage = errorMessage;
    }

    /**
     * @return the customerMessage
     */
    public String getCustomerMessage() {
	return customerMessage;
    }

    /**
     * @param customerMessage the customerMessage to set
     */
    public void setCustomerMessage(String customerMessage) {
	this.customerMessage = customerMessage;
    }

    public StatusType getStatus() {
	return status;
    }

    public void setStatus(StatusType status) {
	this.status = status;
    }
}
