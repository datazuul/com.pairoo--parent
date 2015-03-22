package com.pairoo.domain.payment.payone.response;

/**
 * See PayOne documentation "FinanceGate_Server_API_v_2_5_DE.pdf".
 * 
 * Parameter              Pflicht Format  Kommentar
 * status                 +       Vorgabe APPROVED / ERROR
 * 
 * Parameter (APPROVED)
 * txid                   +       N..12   ID des Zahlungsvorgangs (PAYONE)
 * settleaccount          o       Vorgabe Gibt Ihnen Auskunft darüber, ob ein
 *                                        Saldenausgleich durchgeführt wurde.
 *                                        Werte: yes, no (s.o.)
 * 
 * Parameter (ERROR)
 * errorcode              +       N..6    Fehlernummer
 * errormessage           +       AN..255 Fehlernachricht
 * 
 * @author ralf
 */
public class CaptureResponse {
    private long payoneTransactionId; // txid
    private boolean settleAccount;
    private int errorCode;
    private String errorMessage;

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
     * @return the settleAccount
     */
    public boolean isSettleAccount() {
	return settleAccount;
    }

    /**
     * @param settleAccount the settleAccount to set
     */
    public void setSettleAccount(boolean settleAccount) {
	this.settleAccount = settleAccount;
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
}
