package com.pairoo.domain.payment.payone.response;

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
 * As this parameters are the same than the ones in PreAuthorizationResponse,
 * no additional fields have to be added.
 * 
 * @author Ralf Eichinger
 */
public class AuthorizationResponse extends PreAuthorizationResponse {

}
