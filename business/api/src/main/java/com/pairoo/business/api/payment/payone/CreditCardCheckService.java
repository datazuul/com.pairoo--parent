package com.pairoo.business.api.payment.payone;

import java.util.HashMap;

import com.pairoo.domain.payment.enums.PaymentChannelType;

/**
 * See FinanceGate_Client_API_v_0_9_DE.pdf, page 39ff.
 * 
 * Parameter      Pflicht      Format         Kommentar
 * aid            +            N..6           ID des Sub-Accounts
 * cardpan        +            N..19          Kartenummer
 * cardtype       +            Vorgabe        Kartentyp
 *                                            V: Visa
 *                                            M: Mastercard
 *                                            A: Amex
 *                                            D: Diners
 *                                            J: JCB
 *                                            O: Maestro International
 *                                            U: Maestro UK
 *                                            C: Discover
 *                                            B: Carte Bleue
 * cardexpiredate +            AN4            Verfallsdatum YYMM
 * cardcvc2       +            N..4           Kartenprüfnummer
 * storecarddata  -            Vorgabe        no: Kartendaten werden nicht gespeichert
 *                                            yes: Kartendaten werden gespeicher, es wird
 *                                                 eine Pseudokartennummer zurückgegeben
 * language       -            Vorgabe        Sprachkennzeichen (ISO 639)
 * 
 * Legende
 * N..x           Numerischer Wert (maximal x Zeichen)
 * AN..x          Alphanumerischer Wert (maximal x Zeichen)
 * 
 * no implementation needed yet for: 
 * 
 * @author Ralf Eichinger
 */
public interface CreditCardCheckService extends PayOneService {
    public static final String BEAN_ID = "creditCardCheckService";

    /**
     * Parameter "aid": see class comment
     * @return payone subaccount id
     */
    public HashMap<String, String> getSubAccountIdParam();

    /**
     * Parameter "cardtype": see class comment
     * @param paymentChannelType
     * @return credit card type
     */
    public HashMap<String, String> getCardTypeParam(PaymentChannelType paymentChannelType);

    /**
     * Parameter "storecarddata": see class comment
     * @return "yes" or "no"
     */
    public HashMap<String, String> getStoreCardDataParam();
}
