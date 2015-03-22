package com.pairoo.frontend.webapp.wicket.panels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.validation.validator.StringValidator;

import com.datazuul.framework.webapp.wicket.components.form.EnumCheckGroupPanel;
import com.pairoo.domain.User;
import com.pairoo.domain.enums.AppearanceStyle;
import com.pairoo.domain.enums.HobbyType;
import com.pairoo.domain.enums.KitchenType;
import com.pairoo.domain.enums.SmokeType;
import com.pairoo.domain.enums.SportType;

public class EditUserProfile2Panel extends BasePanel {

    private static final long serialVersionUID = 1L;

    public EditUserProfile2Panel(final String id, final IModel<User> model) {
        super(id, new CompoundPropertyModel<User>(model));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        // ---------------- userprofile: smoketype
        final FormComponent<SmokeType> userProfileSmokeTypeSelection = createUserProfileLifeStyleSmokeTypeFormComponent();
        add(userProfileSmokeTypeSelection);
        add(labelForFormComponent("userProfileSmokeTypeSelectionLabel", userProfileSmokeTypeSelection));

        // ---------------- userprofile: motto
        final FormComponent<String> userProfileMottoField = createUserProfileMottoFormComponent();
        add(userProfileMottoField);
        add(labelForFormComponent("userProfileMottoFieldLabel", userProfileMottoField));

        // ---------------- userprofile: appearance style
        final FormComponent<AppearanceStyle> userProfileAppearanceStylesSelection = createUserProfileAppearanceAppearanceStyleFormComponent();
        add(userProfileAppearanceStylesSelection);
        // add(createFormComponentLabel("userProfileAppearanceStylesSelectionLabel",
        // userProfileAppearanceStylesSelection));

        // ---------------- userprofile: interests
        final FormComponent<HobbyType> userProfileHobbyTypesSelection = createUserProfileLifeStyleHobbyTypesFormComponent();
        add(userProfileHobbyTypesSelection);
        // add(createFormComponentLabel("userProfileHobbyTypesSelectionLabel",
        // userProfileHobbyTypesSelection));

        // ---------------- userprofile: sport types
        final FormComponent<SportType> userProfileSportTypesSelection = createUserProfileLifeStyleSportTypesFormComponent();
        add(userProfileSportTypesSelection);
        // add(createFormComponentLabel("userProfileSportTypesSelectionLabel",
        // userProfileSportTypesSelection));

        // ---------------- userprofile: kitchen types
        final FormComponent<KitchenType> userProfileKitchenTypesSelection = createUserProfileLifeStyleKitchenTypesFormComponent();
        add(userProfileKitchenTypesSelection);
        // add(createFormComponentLabel("userProfileKitchenTypesSelectionLabel",
        // userProfileKitchenTypesSelection));

    }

    private FormComponent<KitchenType> createUserProfileLifeStyleKitchenTypesFormComponent() {
        // ... model
        final CompoundPropertyModel<User> model = (CompoundPropertyModel<User>) getDefaultModel();
        // ... values
        final KitchenType[] kitchenTypeValues = KitchenType.values();
        final ArrayList<KitchenType> kitchenTypes = new ArrayList<KitchenType>(Arrays.asList(kitchenTypeValues));
        kitchenTypes.remove(KitchenType.ANY);
        // ... component
        final FormComponent<KitchenType> fc = new EnumCheckGroupPanel<KitchenType>(
                "userProfile.lifeStyle.kitchenTypes", model.bind("userProfile.lifeStyle.kitchenTypes"), kitchenTypes,
                KitchenType.DONT_SAY);
        fc.setLabel(new ResourceModel("userProfileKitchenTypesSelectionLabel"));
        fc.setEscapeModelStrings(false);
        // ... validation
        fc.setRequired(true);
        return fc;
    }

    private FormComponent<SportType> createUserProfileLifeStyleSportTypesFormComponent() {
        // ... model
        final CompoundPropertyModel<User> model = (CompoundPropertyModel<User>) getDefaultModel();
        // ... values
        final SportType[] sportTypeValues = SportType.values();
        final ArrayList<SportType> sportTypes = new ArrayList<SportType>(Arrays.asList(sportTypeValues));
        sportTypes.remove(SportType.ANY);
        // ... component
        final FormComponent<SportType> fc = new EnumCheckGroupPanel<SportType>("userProfile.lifeStyle.sportTypes",
                model.bind("userProfile.lifeStyle.sportTypes"), sportTypes, SportType.DONT_SAY);
        fc.setLabel(new ResourceModel("userProfileSportTypesSelectionLabel"));
        fc.setEscapeModelStrings(false);
        // ... validation
        fc.setRequired(true);
        return fc;
    }

    private FormComponent<HobbyType> createUserProfileLifeStyleHobbyTypesFormComponent() {
        // ... model
        final CompoundPropertyModel<User> model = (CompoundPropertyModel<User>) getDefaultModel();
        // ... values
        final HobbyType[] hobbyTypeValues = HobbyType.values();
        final ArrayList<HobbyType> hobbyTypes = new ArrayList<HobbyType>(Arrays.asList(hobbyTypeValues));
        hobbyTypes.remove(HobbyType.ANY);
        // ... component
        final FormComponent<HobbyType> fc = new EnumCheckGroupPanel<HobbyType>("userProfile.lifeStyle.hobbyTypes",
                model.bind("userProfile.lifeStyle.hobbyTypes"), hobbyTypes, HobbyType.DONT_SAY);
        fc.setLabel(new ResourceModel("userProfileHobbyTypesSelectionLabel"));
        fc.setEscapeModelStrings(false);
        // ... validation
        fc.setRequired(true);
        return fc;
    }

    private FormComponent<AppearanceStyle> createUserProfileAppearanceAppearanceStyleFormComponent() {
        // ... model
        final CompoundPropertyModel<User> model = (CompoundPropertyModel<User>) getDefaultModel();
        // ... values
        final AppearanceStyle[] appearanceStyleValues = AppearanceStyle.values();
        final ArrayList<AppearanceStyle> appearanceStyles = new ArrayList<AppearanceStyle>(
                Arrays.asList(appearanceStyleValues));
        appearanceStyles.remove(AppearanceStyle.ANY);
        // ... component
        final FormComponent<AppearanceStyle> fc = new EnumCheckGroupPanel<AppearanceStyle>(
                "userProfile.appearance.appearanceStyles", model.bind("userProfile.appearance.appearanceStyles"),
                appearanceStyles, AppearanceStyle.DONT_SAY);
        fc.setLabel(new ResourceModel("userProfileAppearanceStylesSelectionLabel"));
        fc.setEscapeModelStrings(false);
        // ... validation
        fc.setRequired(true);
        return fc;
    }

    private FormComponent<String> createUserProfileMottoFormComponent() {
        // ... field
        final FormComponent<String> userProfileMottoField = new TextField<String>("userProfile.motto");
        userProfileMottoField.setLabel(new ResourceModel("userProfileMottoFieldLabel"));
        userProfileMottoField.add(new AttributeModifier("maxLength", "30"));
        userProfileMottoField.add(new AttributeModifier("size", "50"));
        // ... validation
        userProfileMottoField.add(StringValidator.maximumLength(30));
        userProfileMottoField.setRequired(false);
        return userProfileMottoField;
    }

    private FormComponent<SmokeType> createUserProfileLifeStyleSmokeTypeFormComponent() {
        // ... values
        final SmokeType[] smokeTypeValues = SmokeType.values();
        final List<SmokeType> smokeTypes = new ArrayList<SmokeType>(Arrays.asList(smokeTypeValues));
        smokeTypes.remove(SmokeType.ANY);
        // ... field
        final FormComponent<SmokeType> userProfileSmokeTypeSelection = new DropDownChoice<SmokeType>(
                "userProfile.lifeStyle.smokeType", smokeTypes, new EnumChoiceRenderer<SmokeType>(this));
        userProfileSmokeTypeSelection.setLabel(new ResourceModel("userProfileSmokeTypeSelectionLabel"));
        userProfileSmokeTypeSelection.setEscapeModelStrings(false);

        // ... validation
        userProfileSmokeTypeSelection.setRequired(true);
        return userProfileSmokeTypeSelection;
    }
}
