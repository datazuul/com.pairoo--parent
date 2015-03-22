package com.pairoo.domain.payment.enums;

/**
 * INVOICE == Rechnung,
 * ONLINE_TRANSFER == Online-Überweisung (EC)
 * PAY_ON_DELIVERY == Nachnahme,
 * PRE_PAYMENT == Vorkasse,
 * 
 * @author Ralf Eichinger
 */
public enum ClearingType {
    ELEKTRONISCHE_LASTSCHRIFT, CREDIT_CARD, PRE_PAYMENT, INVOICE, PAY_ON_DELIVERY, ONLINE_TRANSFER, WALLET;
}
