package com.pairoo.domain.payment.payone.request;

import org.jscience.economics.money.Currency;

/**
 * See PayOne documentation "FinanceGate_Server_API_v_2_5_DE.pdf".
 * 
 * Parameter              Pflicht Format  Kommentar
 * txid                   +       N..12   ID des Zahlungsvorgangs (PAYONE)
 * sequencenumber         o       N..2    Sequenznummer für diese Transaktion
 *                                        innerhalb des Zahlungsvorgangs (1..n)
 *                                        z.B. PreAuthorization: 0, 1. Capture: 1, 2.
 *                                        Capture: 2
 *                                        Pflicht bei Multipartial-Capture (Ab dem 2.
 *                                        Capture)
 * amount                 +       A..7    Buchungsbetrag (in kleinster
 *                                        Währungseinheit! z.B. Cent)
 *                                        Der Betrag muss kleiner oder gleich dem
 *                                        Betrag der entsprechenden Buchung
 *                                        sein.
 * currency               +       Vorgabe Währung (ISO-4217)
 *  
 * @author Ralf Eichinger
 */
public class CaptureParameters {
    private long payoneTransactionId; // txid
    private int sequenceNumber;
    private int amount;
    private Currency currency;
    
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
     * @return the sequenceNumber
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }
    /**
     * @param sequenceNumber the sequenceNumber to set
     */
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
    /**
     * @return the amount
     */
    public int getAmount() {
        return amount;
    }
    /**
     * @param amount the amount to set
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }
    /**
     * @return the currency
     */
    public Currency getCurrency() {
        return currency;
    }
    /**
     * @param currency the currency to set
     */
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
    
}
