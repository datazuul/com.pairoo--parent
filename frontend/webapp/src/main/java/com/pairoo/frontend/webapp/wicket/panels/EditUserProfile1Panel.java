package com.pairoo.frontend.webapp.wicket.panels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import com.datazuul.framework.domain.Language;
import com.datazuul.framework.domain.geo.Country;
import com.datazuul.framework.domain.geo.GeoLocation;
import com.datazuul.framework.util.EnumUtils;
import com.datazuul.framework.webapp.wicket.components.datechooser.DateChooserPanel;
import com.datazuul.framework.webapp.wicket.components.form.EnumCheckGroupPanel;
import com.pairoo.business.api.CountryService;
import com.pairoo.business.api.GeoLocationService;
import com.pairoo.domain.User;
import com.pairoo.domain.enums.BodyType;
import com.pairoo.domain.enums.EducationType;
import com.pairoo.domain.enums.Ethnicity;
import com.pairoo.domain.enums.EyeColor;
import com.pairoo.domain.enums.FamilyStatusType;
import com.pairoo.domain.enums.HairColor;
import com.pairoo.domain.enums.IncomeType;
import com.pairoo.domain.enums.NumberOfKidsType;
import com.pairoo.domain.enums.OccupationType;
import com.pairoo.domain.enums.PartnerType;
import com.pairoo.domain.enums.Religion;
import com.pairoo.domain.enums.WantMoreChildrenType;
import com.pairoo.frontend.webapp.wicket.components.HeightFormComponent;
import com.pairoo.frontend.webapp.wicket.components.WeightFormComponent;

public class EditUserProfile1Panel extends BaseFormComponentPanel<User> {

    private static final long serialVersionUID = 1L;
    @SpringBean(name = CountryService.BEAN_ID)
    private CountryService countryService;
    @SpringBean(name = GeoLocationService.BEAN_ID)
    private GeoLocationService geoLocationService;
    private FormComponent<Country> userProfileCountrySelection;
    private FormComponent<String> userProfileZipcodeField;
    private final boolean showCredentials;

    public EditUserProfile1Panel(final String id, final IModel<User> model, final boolean showCredentials) {
        super(id, new CompoundPropertyModel<User>(model));

        this.showCredentials = showCredentials;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        // group of credential components
        final WebMarkupContainer credentials = new WebMarkupContainer("credentials", null);
        credentials.setVisible(showCredentials);
        add(credentials);

        // ---------------- useraccount: pseudonym
        final Label pseudonym = new Label("userAccount.username");
        credentials.add(pseudonym);

        // ---------------- user: email
        final Label email = new Label("email");
        credentials.add(email);

        // ---------------- userprofile: partnertype
        final FormComponent<PartnerType> userProfilePartnerTypeSelection = createUserProfilePartnerTypeFormComponent();
        add(userProfilePartnerTypeSelection);
        add(createFormComponentLabel("userProfilePartnerTypeSelectionLabel", userProfilePartnerTypeSelection));

        // ---------------- userprofile: birthdate
        final FormComponent<Date> userProfileBirthdateSelection = createUserProfileBirthdateFormComponent();
        add(userProfileBirthdateSelection);
        add(createFormComponentLabel("userProfileBirthdateSelectionLabel", userProfileBirthdateSelection));

        // ---------------- userprofile: geolocation country
        userProfileCountrySelection = createUserProfileGeoLocationCountryFormComponent();
        add(userProfileCountrySelection);
        add(createFormComponentLabel("userProfileCountrySelectionLabel", userProfileCountrySelection));

        // ---------------- userprofile: zipcode
        userProfileZipcodeField = createUserProfileGeoLocationZipcodeFormComponent();
        add(userProfileZipcodeField);
        add(createFormComponentLabel("userProfileZipcodeFieldLabel", userProfileZipcodeField));

        // ---------------- userprofile: religion
        final FormComponent<Religion> userProfileReligionSelection = createUserProfileReligionFormComponent();
        add(userProfileReligionSelection);
        add(createFormComponentLabel("userProfileReligionSelectionLabel", userProfileReligionSelection));

        // ---------------- userprofile: ethnicity
        final FormComponent<Ethnicity> userProfileEthnicitySelection = createUserProfileAppearanceEthnicityFormComponent();
        add(userProfileEthnicitySelection);
        add(createFormComponentLabel("userProfileEthnicitySelectionLabel", userProfileEthnicitySelection));

        // ---------------- userprofile: family status type
        final FormComponent<FamilyStatusType> userProfileFamilyStatusTypeSelection = createUserProfileFamilyStatusFormComponent();
        add(userProfileFamilyStatusTypeSelection);
        add(createFormComponentLabel("userProfileFamilyStatusTypeSelectionLabel", userProfileFamilyStatusTypeSelection));

        // ---------------- userprofile: Kids type
        final FormComponent<NumberOfKidsType> userProfileNumberOfKidsTypeSelection = createUserProfileNumberOfKidsFormComponent();
        add(userProfileNumberOfKidsTypeSelection);
        add(createFormComponentLabel("userProfileNumberOfKidsTypeSelectionLabel", userProfileNumberOfKidsTypeSelection));

        // ---------------- userprofile: want more children
        final FormComponent<WantMoreChildrenType> userProfileWantMoreChildrenTypeSelection = createUserProfileWantMoreChildrenFormComponent();
        add(userProfileWantMoreChildrenTypeSelection);
        add(createFormComponentLabel("userProfileWantMoreChildrenTypeSelectionLabel",
                userProfileWantMoreChildrenTypeSelection));

        // ---------------- userprofile: height
        final FormComponent<Integer> userProfileHeightSelection = createUserProfileAppearanceHeightFormComponent();
        add(userProfileHeightSelection);
        add(createFormComponentLabel("userProfileHeightSelectionLabel", userProfileHeightSelection));

        // ---------------- userprofile: weight
        // Removed on business demand JTrac: PAIROO-269
        // final FormComponent<Integer> userProfileWeightSelection =
        // createUserProfileAppearanceWeightFormComponent();
        // add(userProfileWeightSelection);
        // add(createFormComponentLabel("userProfileWeightSelectionLabel",
        // userProfileWeightSelection));

        // ---------------- userprofile: body type
        final FormComponent<BodyType> userProfileBodyTypeSelection = createUserProfileAppearanceBodyTypeFormComponent();
        add(userProfileBodyTypeSelection);
        add(createFormComponentLabel("userProfileBodyTypeSelectionLabel", userProfileBodyTypeSelection));

        // ---------------- userprofile: eye color
        final FormComponent<EyeColor> userProfileEyeColorSelection = createUserProfileAppearanceEyeColorFormComponent();
        add(userProfileEyeColorSelection);
        add(createFormComponentLabel("userProfileEyeColorSelectionLabel", userProfileEyeColorSelection));

        // ---------------- userprofile: hair color
        final FormComponent<HairColor> userProfileHairColorSelection = createUserProfileAppearanceHairColorFormComponent();
        add(userProfileHairColorSelection);
        add(createFormComponentLabel("userProfileHairColorSelectionLabel", userProfileHairColorSelection));

        // ---------------- userprofile: education type
        final FormComponent<EducationType> userProfileEducationTypeSelection = createUserProfileEducationFormComponent();
        add(userProfileEducationTypeSelection);
        add(createFormComponentLabel("userProfileEducationTypeSelectionLabel", userProfileEducationTypeSelection));

        // ---------------- userprofile: occupation type
        final FormComponent<OccupationType> userProfileOccupationTypeSelection = createUserProfileOccupationTypeFormComponent();
        add(userProfileOccupationTypeSelection);
        add(createFormComponentLabel("userProfileOccupationTypeSelectionLabel", userProfileOccupationTypeSelection));

        // ---------------- userprofile: yearly income
        final FormComponent<IncomeType> userProfileIncomeTypeSelection = createUserProfileIncomeTypeFormComponent();
        add(userProfileIncomeTypeSelection);
        add(createFormComponentLabel("userProfileIncomeTypeSelectionLabel", userProfileIncomeTypeSelection));

        // ---------------- userprofile: mother language
        final FormComponent<Language> userProfileMotherLanguageSelection = createUserProfileMotherLanguageFormComponent();
        add(userProfileMotherLanguageSelection);
        add(createFormComponentLabel("userProfileMotherLanguageSelectionLabel", userProfileMotherLanguageSelection));

        // ---------------- userprofile: languages
        final FormComponent<Language> userProfileLanguagesSelection = createUserProfileLanguagesFormComponent();
        add(userProfileLanguagesSelection);
        add(createFormComponentLabel("userProfileLanguagesSelectionLabel", userProfileLanguagesSelection));

    }

    private FormComponent<Language> createUserProfileLanguagesFormComponent() {
        // ... model
        final CompoundPropertyModel<User> model = (CompoundPropertyModel<User>) getDefaultModel();
        // ... values
        final Language[] languageValues = Language.values();
        final ArrayList<Language> languages = new ArrayList<Language>(Arrays.asList(languageValues));
        languages.remove(Language.ANY);
        final List<Language> sortedLanguages = sortLanguages(languages);

        // ... component
        // FIXME: [2012-08-27 11:20:27,624] ERROR [ajp-bio-18009-exec-8][]
        // org.apache.wicket.DefaultExceptionMapper.internalMap - Unexpected
        // error occurred
        // org.apache.wicket.WicketRuntimeException: CheckGroup
        // [8:tabs:panel:form:editUserProfile1Panel:userProfile.languages:group]
        // contains a null model object, must be an object of type
        // java.util.Collection
        final EnumCheckGroupPanel<Language> fc = new EnumCheckGroupPanel<Language>("userProfile.languages",
                model.bind("userProfile.languages"), sortedLanguages, Language.DONT_SAY);
        fc.setLabel(new ResourceModel("userProfileLanguagesSelectionLabel"));
        fc.setEscapeModelStrings(false);
        // ... validation
        fc.setRequired(true);
        return fc;
    }

    private FormComponent<Language> createUserProfileMotherLanguageFormComponent() {
        // ... values
        final Language[] languageValues = Language.values();
        final ArrayList<Language> languages = new ArrayList<Language>(Arrays.asList(languageValues));
        languages.remove(Language.ANY);
        final List<Language> sortedLanguages = sortLanguages(languages);

        // ... field
        final FormComponent<Language> userProfileMotherLanguageSelection = new DropDownChoice<Language>(
                "userProfile.motherLanguage", sortedLanguages, new EnumChoiceRenderer<Language>(this));
        userProfileMotherLanguageSelection.setLabel(new ResourceModel("motherTongue"));
        userProfileMotherLanguageSelection.setEscapeModelStrings(false);

        // ... validation
        userProfileMotherLanguageSelection.setRequired(true);
        return userProfileMotherLanguageSelection;
    }

    private FormComponent<IncomeType> createUserProfileIncomeTypeFormComponent() {
        // ... values
        final IncomeType[] incomeTypeValues = IncomeType.values();
        final ArrayList<IncomeType> incomeTypes = new ArrayList<IncomeType>(Arrays.asList(incomeTypeValues));
        incomeTypes.remove(IncomeType.ANY);

        // ... field
        final FormComponent<IncomeType> userProfileIncomeTypeSelection = new DropDownChoice<IncomeType>(
                "userProfile.incomeType", incomeTypes, new EnumChoiceRenderer<IncomeType>(this));
        userProfileIncomeTypeSelection.setLabel(new ResourceModel("userProfileIncomeTypeSelectionLabel"));
        userProfileIncomeTypeSelection.setEscapeModelStrings(false);

        // ... validation
        userProfileIncomeTypeSelection.setRequired(true);
        return userProfileIncomeTypeSelection;
    }

    private FormComponent<OccupationType> createUserProfileOccupationTypeFormComponent() {
        // ... values
        final OccupationType[] occupationTypeValues = OccupationType.values();
        final ArrayList<OccupationType> occupationTypes = new ArrayList<OccupationType>(
                Arrays.asList(occupationTypeValues));
        occupationTypes.remove(OccupationType.ANY);

        // ... field
        final FormComponent<OccupationType> userProfileOccupationTypeSelection = new DropDownChoice<OccupationType>(
                "userProfile.occupationType", occupationTypes, new EnumChoiceRenderer<OccupationType>(this));
        userProfileOccupationTypeSelection.setLabel(new ResourceModel("userProfileOccupationTypeSelectionLabel"));
        userProfileOccupationTypeSelection.setEscapeModelStrings(false);

        // ... validation
        userProfileOccupationTypeSelection.setRequired(true);
        return userProfileOccupationTypeSelection;
    }

    private FormComponent<EducationType> createUserProfileEducationFormComponent() {
        // ... values
        final EducationType[] educationTypeValues = EducationType.values();
        final ArrayList<EducationType> educationTypes = new ArrayList<EducationType>(Arrays.asList(educationTypeValues));

        // Selection Any removed
        educationTypes.remove(EducationType.ANY);

        // ... field
        final FormComponent<EducationType> userProfileEducationTypeSelection = new DropDownChoice<EducationType>(
                "userProfile.education", educationTypes, new EnumChoiceRenderer<EducationType>(this));
        userProfileEducationTypeSelection.setLabel(new ResourceModel("userProfileEducationTypeSelectionLabel"));
        userProfileEducationTypeSelection.setEscapeModelStrings(false);

        // ... validation
        userProfileEducationTypeSelection.setRequired(true);
        return userProfileEducationTypeSelection;
    }

    private FormComponent<HairColor> createUserProfileAppearanceHairColorFormComponent() {
        // ... values
        final HairColor[] hairColorValues = HairColor.values();
        final ArrayList<HairColor> hairColors = new ArrayList<HairColor>(Arrays.asList(hairColorValues));

        // Selection Any removed
        hairColors.remove(HairColor.ANY);

        // ... field
        final FormComponent<HairColor> userProfileHairColorSelection = new DropDownChoice<HairColor>(
                "userProfile.appearance.hairColor", hairColors, new EnumChoiceRenderer<HairColor>(this));
        userProfileHairColorSelection.setLabel(new ResourceModel("userProfileHairColorSelectionLabel"));
        userProfileHairColorSelection.setEscapeModelStrings(false);

        // ... validation
        userProfileHairColorSelection.setRequired(true);
        return userProfileHairColorSelection;
    }

    private FormComponent<EyeColor> createUserProfileAppearanceEyeColorFormComponent() {
        // ... values
        final EyeColor[] eyeColorValues = EyeColor.values();
        final ArrayList<EyeColor> eyeColors = new ArrayList<EyeColor>(Arrays.asList(eyeColorValues));

        // Selection Any removed
        eyeColors.remove(EyeColor.ANY);

        // ... field
        final FormComponent<EyeColor> userProfileEyeColorSelection = new DropDownChoice<EyeColor>(
                "userProfile.appearance.eyeColor", eyeColors, new EnumChoiceRenderer<EyeColor>(this));
        userProfileEyeColorSelection.setLabel(new ResourceModel("userProfileEyeColorSelectionLabel"));
        userProfileEyeColorSelection.setEscapeModelStrings(false);

        // ... validation
        userProfileEyeColorSelection.setRequired(true);
        return userProfileEyeColorSelection;
    }

    private FormComponent<BodyType> createUserProfileAppearanceBodyTypeFormComponent() {
        // ... values
        final BodyType[] bodyTypeValues = BodyType.values();
        final ArrayList<BodyType> bodyTypes = new ArrayList<BodyType>(Arrays.asList(bodyTypeValues));

        // Selection Any removed
        bodyTypes.remove(BodyType.ANY);

        // ... field
        final FormComponent<BodyType> userProfileBodyTypeSelection = new DropDownChoice<BodyType>(
                "userProfile.appearance.bodyType", bodyTypes, new EnumChoiceRenderer<BodyType>(this));
        userProfileBodyTypeSelection.setLabel(new ResourceModel("userProfileBodyTypeSelectionLabel"));
        userProfileBodyTypeSelection.setEscapeModelStrings(false);

        // ... validation
        userProfileBodyTypeSelection.setRequired(true);
        return userProfileBodyTypeSelection;
    }

    private FormComponent<Integer> createUserProfileAppearanceWeightFormComponent() {
        // ... field
        final FormComponent<Integer> userProfileWeightSelection = new WeightFormComponent(
                "userProfile.appearance.weight");
        userProfileWeightSelection.setLabel(new ResourceModel("userProfileWeightSelectionLabel"));
        userProfileWeightSelection.setEscapeModelStrings(false);

        // ... validation
        userProfileWeightSelection.setRequired(true);
        return userProfileWeightSelection;
    }

    private FormComponent<Integer> createUserProfileAppearanceHeightFormComponent() {
        // ... field
        final FormComponent<Integer> userProfileHeightSelection = new HeightFormComponent(
                "userProfile.appearance.height");
        userProfileHeightSelection.setLabel(new ResourceModel("userProfileHeightSelectionLabel"));
        userProfileHeightSelection.setEscapeModelStrings(false);

        // ... validation
        userProfileHeightSelection.setRequired(true);
        return userProfileHeightSelection;
    }

    private FormComponent<WantMoreChildrenType> createUserProfileWantMoreChildrenFormComponent() {
        // ... values
        final WantMoreChildrenType[] wantMoreChildrenTypeValues = WantMoreChildrenType.values();
        final ArrayList<WantMoreChildrenType> wantMoreChildrenTypes = new ArrayList<WantMoreChildrenType>(
                Arrays.asList(wantMoreChildrenTypeValues));
        wantMoreChildrenTypes.remove(WantMoreChildrenType.ANY);

        // ... field
        final DropDownChoice<WantMoreChildrenType> userProfileWantMoreChildrenTypeSelection = new DropDownChoice<WantMoreChildrenType>(
                "userProfile.wantMoreChildrenType", wantMoreChildrenTypes,
                new EnumChoiceRenderer<WantMoreChildrenType>(this));
        userProfileWantMoreChildrenTypeSelection.setLabel(new ResourceModel("want_more_children"));
        userProfileWantMoreChildrenTypeSelection.setEscapeModelStrings(false);

        // ... validation
        userProfileWantMoreChildrenTypeSelection.setRequired(true);
        return userProfileWantMoreChildrenTypeSelection;
    }

    private FormComponent<NumberOfKidsType> createUserProfileNumberOfKidsFormComponent() {
        // ... values
        final NumberOfKidsType[] numberOfKidsTypeValues = NumberOfKidsType.values();
        final ArrayList<NumberOfKidsType> numberOfKidsTypes = new ArrayList<NumberOfKidsType>(
                Arrays.asList(numberOfKidsTypeValues));

        // Selection Any removed
        numberOfKidsTypes.remove(NumberOfKidsType.ANY);

        // ... field
        final FormComponent<NumberOfKidsType> userProfileNumberOfKidsTypeSelection = new DropDownChoice<NumberOfKidsType>(
                "userProfile.numberOfKidsType", numberOfKidsTypes, new EnumChoiceRenderer<NumberOfKidsType>(this));
        userProfileNumberOfKidsTypeSelection.setLabel(new ResourceModel("userProfileNumberOfKidsTypeSelectionLabel"));
        userProfileNumberOfKidsTypeSelection.setEscapeModelStrings(false);

        // ... validation
        userProfileNumberOfKidsTypeSelection.setRequired(true);
        return userProfileNumberOfKidsTypeSelection;
    }

    private FormComponent<FamilyStatusType> createUserProfileFamilyStatusFormComponent() {
        // ... values
        final FamilyStatusType[] familyStatusTypeValues = FamilyStatusType.values();
        final ArrayList<FamilyStatusType> familyStatusTypes = new ArrayList<FamilyStatusType>(
                Arrays.asList(familyStatusTypeValues));

        // Selection Any removed
        familyStatusTypes.remove(FamilyStatusType.ANY);

        // ... field
        final DropDownChoice<FamilyStatusType> userProfileFamilyStatusTypeSelection = new DropDownChoice<FamilyStatusType>(
                "userProfile.familyStatus", familyStatusTypes, new EnumChoiceRenderer<FamilyStatusType>(this));
        userProfileFamilyStatusTypeSelection.setLabel(new ResourceModel("userProfileFamilyStatusTypeSelectionLabel"));
        userProfileFamilyStatusTypeSelection.setEscapeModelStrings(false);

        // ... validation
        userProfileFamilyStatusTypeSelection.setRequired(true);
        return userProfileFamilyStatusTypeSelection;
    }

    private FormComponent<Ethnicity> createUserProfileAppearanceEthnicityFormComponent() {
        // ... values
        final Ethnicity[] ethnicityValues = Ethnicity.values();
        final ArrayList<Ethnicity> ethnicities = new ArrayList<Ethnicity>(Arrays.asList(ethnicityValues));

        // Selection Any removed
        ethnicities.remove(Ethnicity.ANY);

        // ... field
        final DropDownChoice<Ethnicity> userProfileEthnicitySelection = new DropDownChoice<Ethnicity>(
                "userProfile.appearance.ethnicity", ethnicities, new EnumChoiceRenderer<Ethnicity>(this));
        userProfileEthnicitySelection.setLabel(new ResourceModel("userProfileEthnicitySelectionLabel"));
        userProfileEthnicitySelection.setEscapeModelStrings(false);

        // ... validation
        userProfileEthnicitySelection.setRequired(true);
        return userProfileEthnicitySelection;
    }

    private FormComponent<Religion> createUserProfileReligionFormComponent() {
        // ... values
        final Religion[] religionValues = Religion.values();
        final ArrayList<Religion> religions = new ArrayList<Religion>(Arrays.asList(religionValues));

        // Selection Any removed
        religions.remove(Religion.ANY);

        // ... field
        final DropDownChoice<Religion> userProfileReligionSelection = new DropDownChoice<Religion>(
                "userProfile.religion", religions, new EnumChoiceRenderer<Religion>(this));
        userProfileReligionSelection.setLabel(new ResourceModel("religion"));
        userProfileReligionSelection.setEscapeModelStrings(false);

        // ... validation
        userProfileReligionSelection.setRequired(true);
        return userProfileReligionSelection;
    }

    private FormComponent<String> createUserProfileGeoLocationZipcodeFormComponent() {
        // ... field
        final RequiredTextField<String> userProfileZipcodeField = new RequiredTextField<String>(
                "userProfile.geoLocation.zipcode");
        userProfileZipcodeField.setLabel(new ResourceModel("userProfileZipcodeSelectionLabel"));
        userProfileZipcodeField.add(new AttributeModifier("maxLength", "20"));
        // international zipcode format...
        userProfileZipcodeField.add(new AttributeModifier("size", "5"));
        // ... validation
        userProfileZipcodeField.add(StringValidator.maximumLength(20));
        // userProfileZipcodeField.setRequired(true);
        return userProfileZipcodeField;
    }

    private FormComponent<Country> createUserProfileGeoLocationCountryFormComponent() {
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
                "userProfile.geoLocation.country", countryOptionsModel);
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
        userProfileCountrySelection.setLabel(new ResourceModel("userProfileCountrySelectionLabel"));
        // ... validation
        userProfileCountrySelection.setRequired(true);

        return userProfileCountrySelection;
    }

    private FormComponent<Date> createUserProfileBirthdateFormComponent() {
        // ... values
        final Calendar now = Calendar.getInstance();

        // ... field
        final DateChooserPanel<Date> userProfileBirthdateSelection = new DateChooserPanel<Date>(
                "userProfile.birthdate", now.get(Calendar.YEAR) - 90, now.get(Calendar.YEAR) - 17, getSession()
                .getLocale());
        userProfileBirthdateSelection.setLabel(new ResourceModel("userProfileBirthdateSelectionLabel"));
        // ... validation
        userProfileBirthdateSelection.setRequired(true);
        return userProfileBirthdateSelection;
    }

    private FormComponent<PartnerType> createUserProfilePartnerTypeFormComponent() {
        // ... values
        final PartnerType[] partnerTypeValues = PartnerType.values();
        final List<PartnerType> partnerTypes = new ArrayList<PartnerType>(Arrays.asList(partnerTypeValues));
        partnerTypes.remove(PartnerType.ANY);

        // ... field
        final DropDownChoice<PartnerType> userProfilePartnerTypeSelection = new DropDownChoice<PartnerType>(
                "userProfile.partnerType", partnerTypes, new EnumChoiceRenderer<PartnerType>(this));
        userProfilePartnerTypeSelection.setLabel(new ResourceModel("userProfilePartnerTypeSelectionLabel"));
        userProfilePartnerTypeSelection.setEscapeModelStrings(false);

        // ... validation
        userProfilePartnerTypeSelection.setRequired(true);
        return userProfilePartnerTypeSelection;
    }

    @Override
    protected void convertInput() {
        super.convertInput();

        final Country country = userProfileCountrySelection.getConvertedInput();
        final String zipcode = userProfileZipcodeField.getConvertedInput();

        // lookup geolocation for this zipcode and country
        final GeoLocation geoLocation = geoLocationService.getByCountryAndZipcode(country, zipcode);
        final User user = (User) getDefaultModelObject();
        if (geoLocation != null) {
            // unique geolocation found, shift data in GeoArea object
            user.getUserProfile().setGeoLocation(geoLocation);
        } else {
            userProfileZipcodeField.error(getString("error.no_geolocation_found_for_zipcode"));
        }

        setConvertedInput(user);
    }
}
