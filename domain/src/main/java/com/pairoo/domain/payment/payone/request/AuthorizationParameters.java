package com.pairoo.domain.payment.payone.request;

import java.util.Date;

import org.jscience.economics.money.Currency;

import com.datazuul.framework.domain.geo.Country;
import com.datazuul.framework.domain.geo.Subdivision;
import com.pairoo.domain.enums.SalutationType;
import com.pairoo.domain.payment.enums.ClearingType;

/**
 * See PayOne documentation "FinanceGate_Server_API_v_2_5_DE.pdf".
 * 
 * Parameter              Pflicht Format  Kommentar
 * aid                    +       N..6    ID des Sub-Accounts
 * clearingtype           +       Vorgabe elv: Lastschrift
 *                                        cc: Kreditkarte
 *                                        vor: Vorkasse
 *                                        rec: Rechnung
 *                                        cod: Nachnahme
 *                                        sb: Online-Überweisung
 *                                        wlt: e-Wallet
 * reference              +       AN..20  Händlerreferenznummer des
 *                                        Zahlungsvorgangs.
 *                                        (Erlaubte Zeichen: 0-9, a-z, A-Z, .,-,_,/)
 * amount                 +       N..7    Gesamtpreis (in kleinster Währungseinheit! z.B. Cent)
 * currency               +       Vorgabe Währung (ISO-4217)
 * param                  -       AN..255 individueller Parameter
 * narrative_text         -       AN..81  Dynamischer Textbestandteil des
 *                                        Buchungstextes von Lastschriften
 *                                        (3 Auszugszeilen à 27 Zeichen) und Kreditkarteneinzügen.
 *
 *
 * Parameter ( Personendaten )
 * customerid             -       AN..20  Kundennummer beim Händler
 *                                        (Erlaubte Zeichen: 0-9, a-z, A-Z, .,-,_,/)
 * userid                 -       N..8    ID des Debitors (PAYONE)
 * salutation             -       AN..10  Anrede, (z.B. „Herr“, „Frau“, „Firma“)
 * title                  -       AN..20  Titel (z.B. „Dr.“, „Prof.“)
 * firstname              +       AN..50  Vorname
 * lastname               +       AN..50  Nachname
 * company                -       AN..50  Firma
 * street                 -       AN..50  Strasse und Hausnummer
 * addressaddition        -       AN..50  Adresszusatz (z.B. „7. Stock“, „bei Maier“)
 * zip                    -       AN..10  PLZ
 * city                   -       AN..50  Stadt
 * country                +       Vorgabe Land (ISO-3166)
 * state                  -       Vorgabe Bundesstaat (ISO-3166 Subdivisions)
 *                                        (nur wenn country=US oder CA)
 * email                  -       AN..50  E-Mail Adresse
 * telephonenumber        -       AN..50  Telefonnummer
 * birthday               -       N8      Geburtstag (YYYYMMDD)
 * language               -       Vorgabe Sprachkennzeichen (ISO 639)
 * vatid                  -       AN..50  Umsatzsteuer-Ident-Nummer
 * ip                     -       AN..15  IP Adresse des Kunden (123.123.123.123)
 * 
 * @author Ralf Eichinger
 */
public class AuthorizationParameters {
    private int subAccountId; // aid
    private ClearingType clearingType;
    private String merchantTransactionReference; // reference
    private int amount;
    private Currency currency;
    private String param;
    private String narrativeText;

    // person data
    private String merchantCustomerId; // customerid
    private int payOneDebitorId = -1; // userid
    private SalutationType salutation;
    private String title;
    private String firstname;
    private String lastname;
    private String company;
    private String street;
    private String addressAddition;
    private String zipcode;
    private String city;
    private Country country;
    private Subdivision state;
    private String email;
    private String phoneNumber;
    private Date birthday;
    private String language;
    private String vatId;
    private String ip;

    public AuthorizationParameters() {
	super();
    }

    public AuthorizationParameters(AuthorizationParameters params) {
	super();

	setAddressAddition(params.getAddressAddition());
	setAmount(params.getAmount());
	setBirthday(params.getBirthday());
	setCity(params.getCity());
	setClearingType(params.getClearingType());
	setCompany(params.getCompany());
	setCountry(params.getCountry());
	setCurrency(params.getCurrency());
	setEmail(params.getEmail());
	setFirstname(params.getFirstname());
	setIp(params.getIp());
	setLanguage(params.getLanguage());
	setLastname(params.getLastname());
	setMerchantCustomerId(params.getMerchantCustomerId());
	setMerchantTransactionReference(params.getMerchantTransactionReference());
	setNarrativeText(params.getNarrativeText());
	setParam(params.getParam());
	setPayOneDebitorId(params.getPayOneDebitorId());
	setPhoneNumber(params.getPhoneNumber());
	setSalutation(params.getSalutation());
	setState(params.getState());
	setStreet(params.getStreet());
	setSubAccountId(params.getSubAccountId());
	setTitle(params.getTitle());
	setVatId(params.getVatId());
	setZipcode(params.getZipcode());
    }

    /**
     * @return the subAccountId
     */
    public int getSubAccountId() {
	return subAccountId;
    }

    /**
     * @param subAccountId the subAccountId to set
     */
    public void setSubAccountId(int subAccountId) {
	this.subAccountId = subAccountId;
    }

    /**
     * @return the clearingType
     */
    public ClearingType getClearingType() {
	return clearingType;
    }

    /**
     * @param clearingType the clearingType to set
     */
    public void setClearingType(ClearingType clearingType) {
	this.clearingType = clearingType;
    }

    /**
     * @return the merchantTransactionReference
     */
    public String getMerchantTransactionReference() {
	return merchantTransactionReference;
    }

    /**
     * @param merchantTransactionReference the merchantTransactionReference to set
     */
    public void setMerchantTransactionReference(String merchantTransactionReference) {
	this.merchantTransactionReference = merchantTransactionReference;
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

    /**
     * @return the param
     */
    public String getParam() {
	return param;
    }

    /**
     * @param param the param to set
     */
    public void setParam(String param) {
	this.param = param;
    }

    /**
     * @return the narrativeText
     */
    public String getNarrativeText() {
	return narrativeText;
    }

    /**
     * @param narrativeText the narrativeText to set
     */
    public void setNarrativeText(String narrativeText) {
	this.narrativeText = narrativeText;
    }

    /**
     * @return the merchantCustomerId
     */
    public String getMerchantCustomerId() {
	return merchantCustomerId;
    }

    /**
     * @param merchantCustomerId the merchantCustomerId to set
     */
    public void setMerchantCustomerId(String merchantCustomerId) {
	this.merchantCustomerId = merchantCustomerId;
    }

    /**
     * @return the payOneDebitorId
     */
    public int getPayOneDebitorId() {
	return payOneDebitorId;
    }

    /**
     * @param payOneDebitorId the payOneDebitorId to set
     */
    public void setPayOneDebitorId(int payOneDebitorId) {
	this.payOneDebitorId = payOneDebitorId;
    }

    /**
     * @return the salutation
     */
    public SalutationType getSalutation() {
	return salutation;
    }

    /**
     * @param salutation the salutation to set
     */
    public void setSalutation(SalutationType salutation) {
	this.salutation = salutation;
    }

    /**
     * @return the title
     */
    public String getTitle() {
	return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
	this.title = title;
    }

    /**
     * @return the firstname
     */
    public String getFirstname() {
	return firstname;
    }

    /**
     * @param firstname the firstname to set
     */
    public void setFirstname(String firstname) {
	this.firstname = firstname;
    }

    /**
     * @return the lastname
     */
    public String getLastname() {
	return lastname;
    }

    /**
     * @param lastname the lastname to set
     */
    public void setLastname(String lastname) {
	this.lastname = lastname;
    }

    /**
     * @return the company
     */
    public String getCompany() {
	return company;
    }

    /**
     * @param company the company to set
     */
    public void setCompany(String company) {
	this.company = company;
    }

    /**
     * @return the street
     */
    public String getStreet() {
	return street;
    }

    /**
     * @param street the street to set
     */
    public void setStreet(String street) {
	this.street = street;
    }

    /**
     * @return the addressAddition
     */
    public String getAddressAddition() {
	return addressAddition;
    }

    /**
     * @param addressAddition the addressAddition to set
     */
    public void setAddressAddition(String addressAddition) {
	this.addressAddition = addressAddition;
    }

    /**
     * @return the zipcode
     */
    public String getZipcode() {
	return zipcode;
    }

    /**
     * @param zipcode the zipcode to set
     */
    public void setZipcode(String zipcode) {
	this.zipcode = zipcode;
    }

    /**
     * @return the city
     */
    public String getCity() {
	return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
	this.city = city;
    }

    /**
     * @return the country
     */
    public Country getCountry() {
	return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(Country country) {
	this.country = country;
    }

    /**
     * @return the state
     */
    public Subdivision getState() {
	return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(Subdivision state) {
	this.state = state;
    }

    /**
     * @return the email
     */
    public String getEmail() {
	return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
	this.email = email;
    }

    /**
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
	return phoneNumber;
    }

    /**
     * @param phoneNumber the phoneNumber to set
     */
    public void setPhoneNumber(String phoneNumber) {
	this.phoneNumber = phoneNumber;
    }

    /**
     * @return the birthday
     */
    public Date getBirthday() {
	return birthday;
    }

    /**
     * @param birthday the birthday to set
     */
    public void setBirthday(Date birthday) {
	this.birthday = birthday;
    }

    /**
     * @return the language
     */
    public String getLanguage() {
	return language;
    }

    /**
     * @param language the language to set
     */
    public void setLanguage(String language) {
	this.language = language;
    }

    /**
     * @return the vatId
     */
    public String getVatId() {
	return vatId;
    }

    /**
     * @param vatId the vatId to set
     */
    public void setVatId(String vatId) {
	this.vatId = vatId;
    }

    /**
     * @return the ip
     */
    public String getIp() {
	return ip;
    }

    /**
     * @param ip the ip to set
     */
    public void setIp(String ip) {
	this.ip = ip;
    }

}
