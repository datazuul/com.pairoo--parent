package com.pairoo.business.api.payment.payone;

import java.util.HashMap;

/**
 * See FinanceGate_Client_API_v_0_9_DE.pdf, page 16ff.
 * 
 * Parameter    Pflicht Format  Kommentar
 * mid          +       N..6    ID des Merchants
 * portalid     +       A..7    ID des Zahlungsportals
 * mode         +       Vorgabe test: Testmodus
 *                              live: Livemodus
 * request      +       Vorgabe Anfragen:
 *                              - creditcardcheck
 *                              - bankaccountcheck
 *                              ...
 * responsetype +       Vorgabe - JSON
 *                              - REDIRECT
 * hash                 AN..32  Hashwert (s. Kapitel 3.1.3)
 *              +
 * successurl   o       AN..255 URL „Zahlung erfolgreich“
 *                              (nur wenn responsetype=REDIRECT oder
 *                              entsprechender Request es erfordert)
 * errorurl     o       AN..255 URL „Zahlung fehlerhaft“
 *                              (nur wenn responsetype=REDIRECT oder
 *                              entsprechender Request es erfordert)
 * encoding     -       Vorgabe ISO-8859-1 (default), UTF-8
 * 
 * Legende
 * N..x           Numerischer Wert (maximal x Zeichen)
 * AN..x          Alphanumerischer Wert (maximal x Zeichen)
 * 
 * no implementation needed yet for: successurl, errorurl
 * 
 * @author Ralf Eichinger
 */
public interface PayOneService {
    public static final String BEAN_ID = "payOneService";

    /**
     * Parameter "encoding": optional, Vorgabe: "ISO-8859-1" (default), "UTF-8"
     * @return encoding
     */
    public HashMap<String, String> getEncodingParam();

    /**
     * Parameter "hash": required, AN..32, Hashwert (s. Kapitel 3.1.3), e.g. 19062005567ca72601cc9d031f9a94b1
     * 
     * Der Hash-Wert dient als Schutz vor Veränderung der Aufrufparameter durch den Kunden z.B. eine
     * Veränderung des Preises.
     * Gebildet wird der Hash-Wert aus den Aufrufparametern und einem geheimen Schlüssel mit Hilfe des
     * Hashverfahrens MD5. Alle Parameterwerte, die geschützt werden müssen, werden in alphabetischer
     * Reihenfolge aneinandergehängt. Anschließend wird zusätzlich der Schlüssel an diese Zeichenkette
     * angehängt und der MD5 Wert berechnet.
     * Sortiert werden die Parameter nach den Parameternamen. Die Reihenfolge der Parameter in der
     * Aufruf URL spielen dabei keine Rolle. Die Parameter, die geschützt werden müssen, entnehmen Sie
     * bitte der unten aufgeführten Tabelle.
     * Den Schlüssel (Key) können Sie im PMI (PAYONE Merchant Interface) festlegen. Geben Sie diesen
     * Schlüssel in keinem Fall an Dritte weiter.
     * Folgende Parameter-Werte müssen Sie im HASH Wert aufnehmen:
     * 
     * access_aboperiod, access_aboprice, access_canceltime, access_expiretime, access_period, access_price,
     * access_starttime, access_vat, accesscode, accessname, addresschecktype,
     * aid, amount, backurl, checktype, clearingtype, consumerscoretype, currency, customerid, de[x], due_time, eci,
     * encoding, errorurl, exiturl, id[x], invoice_deliverymode,
     * invoiceappendix, invoiceid, mid, mode, narrative_text, no[x], param, portalid, pr[x], productid, 
     * request, reference, responsetype, settleperiod, settletime, storecarddata, successurl, ti[x], userid,
     * va[x], vaccountname, vreference
     *                   
     * @return hash
     */
    public HashMap<String, String> getHashParam();

    /**
     * Parameter "mid": required, N..6, ID des Merchants, e.g. 19360
     * @return merchant id
     */
    public HashMap<String, String> getMerchantIdParam();

    /**
     * Parameter "mode": required, Vorgabe: "test" = Testmodus, "live" = Livemodus
     * @return mode
     */
    public HashMap<String, String> getModeParam();

    /**
     * Parameter "portalid": required, A..7, ID des Zahlungsportals, e.g. 2000000
     * @return portal id
     */
    public HashMap<String, String> getPortalIdParam();

    /**
     * Parameter "request": required, Vorgabe Anfragen: "creditcardcheck", "bankaccountcheck", ...
     * @return request
     */
    public HashMap<String, String> getRequestParam();

    /**
     * Parameter "responsetype": required, Vorgabe: "JSON", "REDIRECT"
     * @return response type
     */
    public HashMap<String, String> getResponseTypeParam();
}
