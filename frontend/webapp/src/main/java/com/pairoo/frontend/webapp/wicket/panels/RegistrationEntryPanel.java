package com.pairoo.frontend.webapp.wicket.panels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.FormComponentLabel;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

import com.datazuul.framework.domain.geo.Country;
import com.datazuul.framework.util.EnumUtils;
import com.pairoo.business.api.CountryService;
import com.pairoo.business.api.PersonProfileService;
import com.pairoo.business.api.UserAccountService;
import com.pairoo.domain.UserAccount;
import com.pairoo.domain.enums.PartnerType;
import com.pairoo.frontend.webapp.wicket.pages.PrivacyStatementPage;
import com.pairoo.frontend.webapp.wicket.pages.TermsOfUsePage;

/**
 * @author Ralf Eichinger
 */
public class RegistrationEntryPanel extends BasePanel {

    private static final long serialVersionUID = 1L;
    @SpringBean(name = CountryService.BEAN_ID)
    private CountryService countryService;
    @SpringBean(name = PersonProfileService.BEAN_ID)
    private PersonProfileService personProfileService;
    @SpringBean(name = UserAccountService.BEAN_ID)
    private UserAccountService userAccountService;
    // private DropDownChoice<GeoLocation> searchProfileGeoLocationSelection;
    // private DropDownChoice<Subdivision> searchProfileSubdivisionSelection;
    private boolean acceptedTermsAndConditions = false;

    public RegistrationEntryPanel(final String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        // ---------------- userprofile: partnertype
        final FormComponent<PartnerType> userProfilePartnerTypeSelection = createPartnerTypeFormComponent("userProfile.partnerType");
        add(userProfilePartnerTypeSelection);
        add(labelForFormComponent("userProfilePartnerTypeSelectionLabel", userProfilePartnerTypeSelection));

        // ---------------- useraccount: username/pseudonym
        final FormComponent<String> usernameField = createUsernameFormComponent("userAccount.username");
        add(usernameField);
        add(labelForFormComponent("usernameFieldLabel", usernameField));
//        add(new HintImage("usernameHint", new ResourceModel("hint.username")));

        // ---------------- useraccount: password
        final FormComponent<String> passwordField = createPasswordFormComponent("userAccount.password");
        add(passwordField);
        add(labelForFormComponent("passwordFieldLabel", passwordField));
//        add(new HintImage("passwordHint", new ResourceModel("hint.password")));

        // ---------------- user: email
        final FormComponent<String> emailField = createEmailFormComponent("email");
        add(emailField);
        add(labelForFormComponent("emailFieldLabel", emailField));
//        add(new HintImage("emailFieldHint", new ResourceModel("hint.email")));

        // ---------------- search profile: partnerType
        final FormComponent<PartnerType> searchProfilePartnerTypeSelection = createSearchProfilePartnerTypeFormComponent("searchProfile.partnerType");
        add(searchProfilePartnerTypeSelection);
        add(labelForFormComponent("searchProfilePartnerTypeSelectionLabel", searchProfilePartnerTypeSelection));

        // ---------------- search profile: min age
        final FormComponent<Integer> searchProfileMinAgeSelection = createMinAgeFormComponent("searchProfile.minAge");
        add(searchProfileMinAgeSelection);
        add(labelForFormComponent("searchProfileMinAgeSelectionLabel", searchProfileMinAgeSelection));

        // ---------------- search profile: max age
        final FormComponent<Integer> searchProfileMaxAgeSelection = createMaxAgeFormComponent("searchProfile.maxAge");
        add(searchProfileMaxAgeSelection);
        add(labelForFormComponent("searchProfileMaxAgeSelectionLabel", searchProfileMaxAgeSelection));

        // ---------------- user profile: country
        final FormComponent<Country> userProfileCountrySelection = createCountryFormComponent("userProfile.geoLocation.country");
        add(userProfileCountrySelection);
        add(labelForFormComponent("userProfileCountrySelectionLabel", userProfileCountrySelection));

        // ---------------- user profile: zipcode
        final FormComponent<String> userProfileZipcodeField = createZipcodeFormComponent("userProfile.geoLocation.zipcode");
        add(userProfileZipcodeField);
        add(labelForFormComponent("userProfileZipcodeFieldLabel", userProfileZipcodeField));

        // ---------------- check terms and conditions
        final FormComponent<Boolean> acceptedTerms = createAcceptedTermsFormComponent("acceptCheckbox");
        add(acceptedTerms);

        // ... label: IMPORTANT! NO SimpleFormComponentLabel !!!
        final FormComponentLabel termsAndConditionsCheckLabel = new FormComponentLabel("termsAndConditionsCheckLabel",
                acceptedTerms);
        add(termsAndConditionsCheckLabel);

        // AjaxFormValidatingBehavior.addToAllFormComponents(form,"onblur");

        final Link<Void> termsLink = createTermsLink("termsLink");
        termsAndConditionsCheckLabel.add(termsLink);

        final Link<Void> conditionsLink = createConditionsLink("conditionsLink");
        termsAndConditionsCheckLabel.add(conditionsLink);

        // search profile: subdivision
        // searchProfileSubdivisionSelection =
        // createSearchProfileSubdivisionSelection(
        // searchProfile, userModel);
        // add(searchProfileSubdivisionSelection);

        // search profile: city
        // searchProfileCitySelection = createSearchProfileCitySelection(
        // searchProfile, userModel);
        // add(searchProfileCitySelection);

        // AJAX behaviour
        // searchProfileCountrySelection
        // .add(new AjaxFormComponentUpdatingBehavior("onchange") {
        // @Override
        // protected void onUpdate(AjaxRequestTarget target) {
        // target.add(searchProfileSubdivisionSelection);
        // }
        // });
        // searchProfileSubdivisionSelection
        // .add(new AjaxFormComponentUpdatingBehavior("onchange") {
        // @Override
        // protected void onUpdate(AjaxRequestTarget target) {
        // target.add(searchProfileCitySelection);
        // }
        // });
    }

    private Link<Void> createConditionsLink(String id) {
        final Link<Void> conditionsLink = new Link<Void>(id, null) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                final PrivacyStatementPage page = new PrivacyStatementPage() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    protected void onGoBack() {
                        setResponsePage(RegistrationEntryPanel.this.getPage());
                    }
                };
                setResponsePage(page);
            }
        };
        final Label conditionsText = new Label("conditionsText", new ResourceModel("privacy_statement"));
        conditionsText.setRenderBodyOnly(true);
        conditionsLink.add(conditionsText);
        return conditionsLink;
    }

    private Link<Void> createTermsLink(String id) {
        final Link<Void> termsLink = new Link<Void>(id, null) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                final TermsOfUsePage page = new TermsOfUsePage() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    protected void onGoBack() {
                        setResponsePage(RegistrationEntryPanel.this.getPage());
                    }
                };
                setResponsePage(page);
            }
        };
        final Label termsText = new Label("termsText", new ResourceModel("terms_of_use"));
        termsText.setRenderBodyOnly(true);
        termsLink.add(termsText);
        return termsLink;
    }

    private FormComponent<Boolean> createAcceptedTermsFormComponent(String id) {
        // ... field
        final CheckBox acceptedTerms = new CheckBox(id, new PropertyModel<Boolean>(this, "acceptedTermsAndConditions"));
        acceptedTerms.setLabel(new ResourceModel("termsAndConditionsValidationCheckLabel"));
        // ... validation
        acceptedTerms.setRequired(true);
        acceptedTerms.add(new IValidator<Boolean>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void validate(final IValidatable<Boolean> component) {
                final Boolean accepted = component.getValue();
                // validation
                if (accepted.booleanValue() == false) {
                    acceptedTerms.error(getString("error.terms_not_accepted"));
                }
            }
        });
        return acceptedTerms;
    }

    private FormComponent<String> createZipcodeFormComponent(String id) {
        // ... field
        final RequiredTextField<String> userProfileZipcodeField = new RequiredTextField<String>(
                id);
        userProfileZipcodeField.setLabel(new ResourceModel("zipcode_area"));
        userProfileZipcodeField.add(new AttributeModifier("maxLength", "20"));
        userProfileZipcodeField.add(new AttributeModifier("size", "5"));
        // ... validation
        userProfileZipcodeField.add(StringValidator.maximumLength(20));
        userProfileZipcodeField.setRequired(true);

        return userProfileZipcodeField;
    }

    private FormComponent<Country> createCountryFormComponent(String id) {
        // ... values
        final IModel<List<Country>> countryOptionsModel = new AbstractReadOnlyModel<List<Country>>() {
            private static final long serialVersionUID = 1L;

            @Override
            public List<Country> getObject() {
                List<Country> selectableCountries = countryService.getSelectableCountries();
                selectableCountries.remove(Country.ANY);
                // sort in current locale
                selectableCountries = sortCountries(getLocale(), selectableCountries);
                return selectableCountries;
            }
        };
        // ... field
        final DropDownChoice<Country> userProfileCountrySelection = new DropDownChoice<Country>(
                id, countryOptionsModel);
        final IChoiceRenderer<Country> choiceRenderer = new IChoiceRenderer<Country>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Object getDisplayValue(final Country country) {
                if (country == null) {
                    return "";
                }
                return ((IModel<String>) new ResourceModel(EnumUtils.getEnumKey(country))).getObject();
            }

            @Override
            public String getIdValue(final Country object, final int index) {
                final List<? extends Country> choices = userProfileCountrySelection.getChoices();
                if (choices == null || index == -1) {
                    return "";
                }
                return String.valueOf(choices.get(index));
            }
        };
        userProfileCountrySelection.setChoiceRenderer(choiceRenderer);
        userProfileCountrySelection.setOutputMarkupId(true);
        userProfileCountrySelection.setEscapeModelStrings(false);
        userProfileCountrySelection.setLabel(new ResourceModel("living_in"));
        // ... validation
        userProfileCountrySelection.setRequired(true);

        return userProfileCountrySelection;
    }

    private FormComponent<Integer> createMaxAgeFormComponent(String id) {
        // ... values
        final ArrayList<Integer> maxAgeValues = new ArrayList<Integer>();
        for (int age = personProfileService.getMinAge(); age <= personProfileService.getMaxAge(); age++) {
            maxAgeValues.add(age);
        }
        // ... field
        final DropDownChoice<Integer> searchProfileMaxAgeSelection = new DropDownChoice<Integer>(
                id, maxAgeValues);
        searchProfileMaxAgeSelection.setLabel(new ResourceModel("searchProfileMaxAgeSelectionLabel"));
        searchProfileMaxAgeSelection.setEscapeModelStrings(false);
        // ... validation
        searchProfileMaxAgeSelection.setRequired(true);
        return searchProfileMaxAgeSelection;
    }

    private FormComponent<Integer> createMinAgeFormComponent(String id) {
        // ... values
        final ArrayList<Integer> minAgeValues = new ArrayList<Integer>();
        for (int age = personProfileService.getMinAge(); age <= personProfileService.getMaxAge(); age++) {
            minAgeValues.add(age);
        }
        // ... field
        final DropDownChoice<Integer> searchProfileMinAgeSelection = new DropDownChoice<Integer>(
                id, minAgeValues);
        searchProfileMinAgeSelection.setLabel(new ResourceModel("searchProfileMinAgeSelectionLabel"));
        searchProfileMinAgeSelection.setEscapeModelStrings(false);
        // ... validation
        searchProfileMinAgeSelection.setRequired(true);
        return searchProfileMinAgeSelection;
    }

    private FormComponent<PartnerType> createSearchProfilePartnerTypeFormComponent(String id) {
        // ... values
        final PartnerType[] partnerTypeValuesSearch = PartnerType.values();
        final List<PartnerType> partnerTypesSearch = new ArrayList<PartnerType>(Arrays.asList(partnerTypeValuesSearch));
        // partnerTypesSearch.remove(PartnerType.DONT_SAY);

        // ... field
        final DropDownChoice<PartnerType> searchProfilePartnerTypeSelection = new DropDownChoice<PartnerType>(
                id, partnerTypesSearch, new EnumChoiceRenderer<PartnerType>(this) {
            private static final long serialVersionUID = 1L;

            @Override
            protected String resourceKey(final PartnerType object) {
                final String result = "searchFor_" + super.resourceKey(object);
                return result;
            }
        });
        searchProfilePartnerTypeSelection.setLabel(new ResourceModel("searchProfilePartnerTypeSelectionLabel"));
        searchProfilePartnerTypeSelection.setEscapeModelStrings(false);
        // ... validation
        searchProfilePartnerTypeSelection.setRequired(true);
        return searchProfilePartnerTypeSelection;
    }

    private FormComponent<String> createEmailFormComponent(String id) {
        // ... field
        final RequiredTextField<String> emailField = new RequiredTextField<String>(id);
        emailField.setLabel(new ResourceModel("emailFieldLabel"));
        // ... validation
        emailField.add(new AttributeModifier("maxlength", "255"));
        emailField.add(StringValidator.lengthBetween(6, 255));
        emailField.add(EmailAddressValidator.getInstance());
        // business decision: emails does not have to be unique / used only once
        // emailField.add(new IValidator<String>() {
        // @Override
        // public void validate(final IValidatable<String> component) {
        // final String email = component.getValue();
        // // validation
        // final User user = userService.getByEmail(email);
        // if (user != null) {
        // emailField.error(getString("error.email_not_unique"));
        // }
        // }
        // });
        return emailField;
    }

    private FormComponent<String> createPasswordFormComponent(String id) {
        // ... field
        final PasswordTextField passwordField = new PasswordTextField(id);
        passwordField.setLabel(new ResourceModel("passwordFieldLabel"));
        passwordField.setResetPassword(false);
        // ... validation
        passwordField.add(new AttributeModifier("maxlength", "12"));
        passwordField.add(StringValidator.lengthBetween(6, 12));
        return passwordField;
    }

    private FormComponent<String> createUsernameFormComponent(String id) {
        // ... field
        final RequiredTextField<String> usernameField = new RequiredTextField<String>(id);
        usernameField.setLabel(new ResourceModel("usernameFieldLabel"));
        // ... validation
        usernameField.add(new AttributeModifier("maxLength", "255"));
        usernameField.add(StringValidator.lengthBetween(3, 255));
        usernameField.add(new IValidator<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void validate(final IValidatable<String> component) {
                final String username = component.getValue();
                // validation
                final UserAccount userAccount = userAccountService.getByUsername(username);
                if (userAccount != null) {
                    usernameField.error(getString("error.username_not_unique"));
                }
            }
        });
        return usernameField;
    }

    private FormComponent<PartnerType> createPartnerTypeFormComponent(String id) {
        // ... values
        final PartnerType[] partnerTypeValues = PartnerType.values();
        final List<PartnerType> partnerTypes = new ArrayList<PartnerType>(Arrays.asList(partnerTypeValues));
        partnerTypes.remove(PartnerType.ANY);
        // ... field
        final DropDownChoice<PartnerType> fc = new DropDownChoice<PartnerType>(id, partnerTypes,
                new EnumChoiceRenderer<PartnerType>(this));
        fc.setLabel(new ResourceModel("userProfilePartnerTypeSelectionLabel"));
        fc.setEscapeModelStrings(false);
        // ... validation
        fc.setRequired(true);
        return fc;
    }
    // private DropDownChoice<GeoLocation>
    // createSearchProfileGeoLocationSelection(final SearchProfile
    // searchProfile,
    // final CompoundPropertyModel userModel) {
    // final IModel<List<GeoLocation>> geoLocationOptionsModel = new
    // AbstractReadOnlyModel<List<GeoLocation>>() {
    // private static final long serialVersionUID = 1L;
    //
    // @Override
    // public List<GeoLocation> getObject() {
    // final Subdivision selectedSubdivision =
    // searchProfile.getGeoArea().getSubdivision();
    // if (selectedSubdivision == null) {
    // return Collections.emptyList();
    // }
    // return geoLocationService.getBySubdivision(selectedSubdivision);
    // }
    // };
    //
    // // TODO: sort alphabetically
    //
    // searchProfileGeoLocationSelection = new
    // DropDownChoice<GeoLocation>("searchProfileGeoLocationSelection",
    // userModel.bind("searchProfile.geoArea.geoLocation"),
    // geoLocationOptionsModel,
    // new IChoiceRenderer<GeoLocation>() {
    // private static final long serialVersionUID = 1L;
    //
    // @Override
    // public Object getDisplayValue(final GeoLocation geoLocation) {
    // if (geoLocation == null) {
    // return "";
    // }
    // return geoLocation.getName();
    // }
    //
    // @Override
    // public String getIdValue(final GeoLocation object, final int index) {
    // final List<? extends GeoLocation> choices =
    // searchProfileGeoLocationSelection.getChoices();
    // if (choices == null || index == -1) {
    // return "";
    // }
    // return String.valueOf(choices.get(index));
    // }
    // });
    // searchProfileGeoLocationSelection.setOutputMarkupId(true);
    // searchProfileGeoLocationSelection.setEscapeModelStrings(false);
    // return searchProfileGeoLocationSelection;
    // }
    // private DropDownChoice<Subdivision>
    // createSearchProfileSubdivisionSelection(final SearchProfile
    // searchProfile,
    // final CompoundPropertyModel userModel) {
    // final IModel<List<Subdivision>> subdivisionOptionsModel = new
    // AbstractReadOnlyModel<List<Subdivision>>() {
    // private static final long serialVersionUID = 1L;
    //
    // @Override
    // public List<Subdivision> getObject() {
    // final Country selectedCountry = searchProfile.getGeoArea().getCountry();
    // if (selectedCountry == null || selectedCountry == Country.ANY) {
    // return Collections.emptyList();
    // }
    // return subdivisionService.getByCountry(selectedCountry);
    // }
    // };
    //
    // // TODO: sort alphabetically
    //
    // searchProfileSubdivisionSelection = new
    // DropDownChoice<Subdivision>("searchProfileSubdivisionSelection",
    // userModel.bind("searchProfile.geoArea.subdivision"),
    // subdivisionOptionsModel,
    // new IChoiceRenderer<Subdivision>() {
    // private static final long serialVersionUID = 1L;
    //
    // @Override
    // public Object getDisplayValue(final Subdivision subdivision) {
    // if (subdivision == null) {
    // return "";
    // }
    // return subdivision.getName();
    // }
    //
    // @Override
    // public String getIdValue(final Subdivision object, final int index) {
    // final List<? extends Subdivision> choices =
    // searchProfileSubdivisionSelection.getChoices();
    // if (choices == null || index == -1) {
    // return "";
    // }
    // return "" + choices.get(index).getId();
    // }
    // });
    // searchProfileSubdivisionSelection.setOutputMarkupId(true);
    // searchProfileSubdivisionSelection.setEscapeModelStrings(false);
    // return searchProfileSubdivisionSelection;
    // }
}
