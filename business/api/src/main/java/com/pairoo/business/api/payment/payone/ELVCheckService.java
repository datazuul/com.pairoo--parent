package com.pairoo.business.api.payment.payone;

import java.util.HashMap;

/**
 * See FinanceGate_Client_API_v_0_9_DE.pdf, page 41ff.
 * 
 * Parameter      Pflicht      Format         Kommentar
 * aid            +            N..6           ID des Sub-Accounts
 * checktype      -            Vorgabe        0 = reguläre Prüfung (Vorgabe)
 *                                            1 = Prüfung gegen die POS-Sperrliste
 * bankaccount    +            N..11          Kontonummer
 * bankcode       o            N..8           Bankleitzahl
 *                                            (nur in DE & AT)
 * bankcountry    +            Vorgabe        Kontotyp/ Land
 *                                            DE: Deutschland
 * language       -            Vorgabe        Sprachkennzeichen (ISO 639)
 * 
 * Legende
 * N..x           Numerischer Wert (maximal x Zeichen)
 * AN..x          Alphanumerischer Wert (maximal x Zeichen)
 * 
 * @author Ralf Eichinger
 */
public interface ELVCheckService extends PayOneService {
    public static final String BEAN_ID = "elvCheckService";

    /**
     * Parameter "aid": see class comment
     * @return payone subaccount id
     */
    public HashMap<String, String> getSubAccountIdParam();

    /**
     * Parameter "checktype": see class comment
     * @param paymentChannelType
     * @return credit card type
     */
    public HashMap<String, String> getCheckTypeParam();
}
