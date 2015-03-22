package com.pairoo.frontend.webapp.wicket.panels.authenticated;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.EnumLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import com.datazuul.framework.domain.Language;
import com.datazuul.framework.util.EnumUtils;
import com.pairoo.domain.PersonProfile;
import com.pairoo.domain.User;
import com.pairoo.domain.UserProfile;
import com.pairoo.domain.enums.AppearanceStyle;
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
import com.pairoo.domain.enums.SmokeType;
import com.pairoo.domain.enums.Starsign;
import com.pairoo.domain.enums.WantMoreChildrenType;
import com.pairoo.frontend.webapp.wicket.components.HeightLabel;
import com.pairoo.frontend.webapp.wicket.components.WeightLabel;
import com.pairoo.frontend.webapp.wicket.panels.BasePanel;

/**
 * @author Ralf Eichinger
 */
public class WantedProfileAboutMePanel extends BasePanel {

    private static final long serialVersionUID = 1L;

    public WantedProfileAboutMePanel(final String id, final IModel<User> model) {
        super(id, new CompoundPropertyModel<User>(model));
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        logEnter(WantedProfileAboutMePanel.class);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final UserProfile userProfile = ((User) getDefaultModelObject()).getUserProfile();

        add(labelForPartnerType("userProfile.partnerType"));
        add(labelForAge("lblAge", userProfile));
        add(labelForStarsign("lblStarsign", userProfile));
        add(labelForCity("userProfile.geoLocation.name"));
        add(labelForCountry("userProfile.geoLocation.country"));
        add(labelForReligion("userProfile.religion"));

        // family status
        add(createFamilyStatusComponent(userProfile));

        // children
        add(createNumberOfKidsTypeComponent(userProfile));

        // wants more children
        add(createWantMoreChildrenTypeComponent(userProfile));

        // smoke type
        add(createSmokeTypeComponent(userProfile));

        // mother tongue
        add(createMotherLanguageComponent(userProfile));

        // foreign languages
        add(createLanguagesComponent(userProfile));

        // motto
        add(createMottoComponent(userProfile));

        // ethnicity
        add(createEthnicityComponent(userProfile));

        // bodytype
        add(createBodyTypeComponent(userProfile));

        // height
        add(createHeightLabel(userProfile));

        // weight
        // removed on business demand: JTrac PAIROO-269
        // add(createWeightLabel(userProfile));

        // eye color
        add(createEyeColorComponent(userProfile));

        // hair color
        add(createHairColorComponent(userProfile));

        // appearance styles
        add(createAppearanceStylesComponent(userProfile));

        // education
        add(createEducationComponent(userProfile));

        // occupation
        add(createOccupationTypeComponent(userProfile));

        // yearly income
        add(createIncomeTypeComponent(userProfile));

    }

    private Label labelForCity(String id) {
        return new Label(id);
    }

    private Component labelForPartnerType(final String id) {
        return new EnumLabel<PartnerType>(id);
    }

    private WeightLabel createWeightLabel(final UserProfile userProfile) {
        int weight = -1;
        try {
            weight = userProfile.getAppearance().getWeight().intValue();
        } catch (final Exception e) {
        }
        return new WeightLabel("lblWeight", getUsersCountry().getUnitMass(), weight);
    }

    private HeightLabel createHeightLabel(final UserProfile userProfile) {
        int height = -1;
        try {
            height = userProfile.getAppearance().getHeight().intValue();
        } catch (final Exception e) {
        }
        return new HeightLabel("lblHeight", getUsersCountry().getUnitLength(), height);
    }

    private Component createIncomeTypeComponent(final UserProfile userProfile) {
        final IncomeType incomeType = userProfile.getIncomeType();
        return new Label("lblYearlyIncome", new ResourceModel(EnumUtils.getEnumKey(incomeType)));
    }

    private Component createOccupationTypeComponent(final UserProfile userProfile) {
        final OccupationType occupationType = userProfile.getOccupationType();
        return new Label("lblOccupationType", new ResourceModel(EnumUtils.getEnumKey(occupationType)));
    }

    private Component createEducationComponent(final UserProfile userProfile) {
        final EducationType education = userProfile.getEducation();
        return new Label("lblEducation", new ResourceModel(EnumUtils.getEnumKey(education)));
    }

    private Component createAppearanceStylesComponent(final UserProfile userProfile) {
        final List<AppearanceStyle> appearancStyleList = userProfile.getAppearance().getAppearanceStyles();
        return new ListView<AppearanceStyle>("appearanceStylesList", appearancStyleList) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<AppearanceStyle> item) {
                final AppearanceStyle itemModel = item.getModelObject();
                item.add(new Label("lblItemModel", new ResourceModel(EnumUtils.getEnumKey(itemModel))));

                final Label separator = new Label("separator", ", ");
                item.add(separator);
                if (item.getIndex() == getList().size() - 1) {
                    separator.setVisible(false);
                }
            }
        };
    }

    private Component createHairColorComponent(final UserProfile userProfile) {
        final HairColor hairColor = userProfile.getAppearance().getHairColor();
        return new Label("lblHairColor", new ResourceModel(EnumUtils.getEnumKey(hairColor)));
    }

    private Component createEyeColorComponent(final UserProfile userProfile) {
        final EyeColor eyeColor = userProfile.getAppearance().getEyeColor();
        return new Label("lblEyeColor", new ResourceModel(EnumUtils.getEnumKey(eyeColor)));
    }

    private Component createBodyTypeComponent(final UserProfile userProfile) {
        final BodyType bodyType = userProfile.getAppearance().getBodyType();
        return new Label("lblBodyType", new ResourceModel(EnumUtils.getEnumKey(bodyType)));
    }

    private Component createEthnicityComponent(final UserProfile userProfile) {
        final Ethnicity ethnicity = userProfile.getAppearance().getEthnicity();
        return new Label("lblEthnicity", new ResourceModel(EnumUtils.getEnumKey(ethnicity)));
    }

    private Component createMottoComponent(final UserProfile userProfile) {
        final String motto = userProfile.getMotto();
        final Label lblMotto = new Label("lblMotto", motto);
        lblMotto.setEscapeModelStrings(true);
        return lblMotto;
    }

    private Component createLanguagesComponent(final UserProfile userProfile) {
        final List<Language> languagesList = userProfile.getLanguages();
        final List<Language> sortedLanguages = sortLanguages(new ArrayList<Language>(languagesList));

        return new ListView<Language>("languagesList", sortedLanguages) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<Language> item) {
                final Language itemModel = item.getModelObject();
                item.add(new Label("lblItemModel", new ResourceModel(EnumUtils.getEnumKey(itemModel))));

                final Label separator = new Label("separator", ", ");
                item.add(separator);
                if (item.getIndex() == getList().size() - 1) {
                    separator.setVisible(false);
                }
            }
        };
    }

    private Component createMotherLanguageComponent(final UserProfile userProfile) {
        final Language motherLanguage = userProfile.getMotherLanguage();
        return new Label("lblMotherTongue", new ResourceModel(EnumUtils.getEnumKey(motherLanguage)));
    }

    private Component createSmokeTypeComponent(final UserProfile userProfile) {
        final SmokeType smokeType = userProfile.getLifeStyle().getSmokeType();
        return new Label("lblSmokeType", new ResourceModel(EnumUtils.getEnumKey(smokeType)));
    }

    private Component createWantMoreChildrenTypeComponent(final UserProfile userProfile) {
        final WantMoreChildrenType wantMoreChildrenType = userProfile.getWantMoreChildrenType();
        return new Label("lblWantMoreChildrenType", new ResourceModel(EnumUtils.getEnumKey(wantMoreChildrenType)));
    }

    private Component createNumberOfKidsTypeComponent(final UserProfile userProfile) {
        final NumberOfKidsType numberOfKidsType = userProfile.getNumberOfKidsType();
        return new Label("lblNumberOfKidsType", new ResourceModel(EnumUtils.getEnumKey(numberOfKidsType)));
    }

    private Component createFamilyStatusComponent(final UserProfile userProfile) {
        final FamilyStatusType familyStatus = userProfile.getFamilyStatus();
        return new Label("lblFamilyStatus", new ResourceModel(EnumUtils.getEnumKey(familyStatus)));
    }

    private Component labelForReligion(final String id) {
        return new EnumLabel<PartnerType>(id);
    }

    private Component labelForCountry(final String id) {
        return new EnumLabel<PartnerType>(id);
    }

    private Component labelForStarsign(final String id, final UserProfile userProfile) {
        final Starsign starsign = PersonProfile.getStarsign(userProfile.getBirthdate());
        return new Label(id, new ResourceModel(EnumUtils.getEnumKey(starsign)));
    }

    private Component labelForAge(final String id, final UserProfile userProfile) {
        final Date birthdate = userProfile.getBirthdate();
        final int age = PersonProfile.getAge(birthdate);
        String ageStr = "--";
        if (age != -1) {
            ageStr = String.valueOf(age);
        }
        return new Label(id, ageStr);
    }
}
