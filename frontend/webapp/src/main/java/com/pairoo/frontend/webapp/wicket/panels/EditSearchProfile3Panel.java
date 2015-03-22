package com.pairoo.frontend.webapp.wicket.panels;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import com.datazuul.framework.webapp.wicket.components.form.EnumCheckGroupPanel;
import com.pairoo.domain.User;
import com.pairoo.domain.enums.AppearanceStyle;
import com.pairoo.domain.enums.EducationType;
import com.pairoo.domain.enums.HobbyType;
import com.pairoo.domain.enums.IncomeType;
import com.pairoo.domain.enums.OccupationType;
import com.pairoo.domain.enums.SmokeType;
import com.pairoo.domain.enums.SportType;

/**
 * Form for editing SearchProfile domain object.
 * 
 * @author Ralf Eichinger
 */
public class EditSearchProfile3Panel extends BasePanel {
    private static final long serialVersionUID = 1L;

    public EditSearchProfile3Panel(final String id, final IModel<User> model) {
	super(id, new CompoundPropertyModel<User>(model));
    }

    @Override
    protected void onInitialize() {
	super.onInitialize();

	// ---------------- search profile: smoke type
	add(createSearchProfileSmokeTypeFormComponent());

	// ---------------- search profile: education type
	add(createSearchProfileEducationTypesFormComponent());

	// ---------------- search profile: yearly income
	final FormComponent<IncomeType> searchProfileIncomeTypeSelection = createSearchProfileIncomeTypeFormComponent();
	add(searchProfileIncomeTypeSelection);
	add(labelForFormComponent("searchProfileIncomeTypeSelectionLabel", searchProfileIncomeTypeSelection));

	// ---------------- search profile: occupation type
	final FormComponent<OccupationType> searchProfileOccupationTypeSelection = createSearchProfileOccupationTypeFormComponent();
	add(searchProfileOccupationTypeSelection);
	add(labelForFormComponent("searchProfileOccupationTypeSelectionLabel", searchProfileOccupationTypeSelection));

	// ---------------- search profile: interests / hobby types
	add(createSearchProfileHobbyTypesFormComponent());

	// ---------------- search profile: sport types
	add(createSearchProfileSportTypesFormComponent());

	// ---------------- search profile: Appearance Style (fashion)
	add(createSearchProfileAppearanceStyleFormComponent());
    }

    private FormComponent<AppearanceStyle> createSearchProfileAppearanceStyleFormComponent() {
	// ... model
	final CompoundPropertyModel<User> model = (CompoundPropertyModel<User>) getDefaultModel();
	// ... values
	final AppearanceStyle[] appearanceStyleValues = AppearanceStyle.values();
	final ArrayList<AppearanceStyle> appearanceStyles = new ArrayList<AppearanceStyle>(
		Arrays.asList(appearanceStyleValues));
	appearanceStyles.remove(AppearanceStyle.DONT_SAY);
	// ... component
	final EnumCheckGroupPanel<AppearanceStyle> fc = new EnumCheckGroupPanel<AppearanceStyle>(
		"searchProfile.appearanceStyles", model.bind("searchProfile.appearanceStyles"), appearanceStyles,
		AppearanceStyle.ANY);
	fc.setLabel(new ResourceModel("searchProfileAppearanceStyleSelectionLabel"));
	fc.setEscapeModelStrings(false);
	// ... validation
	fc.setRequired(true);
	return fc;
    }

    private FormComponent<HobbyType> createSearchProfileSportTypesFormComponent() {
	// ... model
	final CompoundPropertyModel<User> model = (CompoundPropertyModel<User>) getDefaultModel();
	// ... values
	final SportType[] sportTypeValues = SportType.values();
	final ArrayList<SportType> sportTypes = new ArrayList<SportType>(Arrays.asList(sportTypeValues));
	sportTypes.remove(SportType.DONT_SAY);
	// ... component
	final EnumCheckGroupPanel<SportType> fc = new EnumCheckGroupPanel<SportType>("searchProfile.sportTypes",
		model.bind("searchProfile.sportTypes"), sportTypes, SportType.ANY);
	fc.setLabel(new ResourceModel("searchProfileSportTypeSelectionLabel"));
	fc.setEscapeModelStrings(false);
	// ... validation
	// searchProfileSportTypesSelection.setRequired(true);
	return fc;
    }

    private FormComponent<HobbyType> createSearchProfileHobbyTypesFormComponent() {
	// ... model
	final CompoundPropertyModel<User> model = (CompoundPropertyModel<User>) getDefaultModel();
	// ... values
	final HobbyType[] hobbyTypeValues = HobbyType.values();
	final ArrayList<HobbyType> hobbyTypes = new ArrayList<HobbyType>(Arrays.asList(hobbyTypeValues));
	hobbyTypes.remove(HobbyType.DONT_SAY);
	// ... component
	final EnumCheckGroupPanel<HobbyType> fc = new EnumCheckGroupPanel<HobbyType>("searchProfile.hobbyTypes",
		model.bind("searchProfile.hobbyTypes"), hobbyTypes, HobbyType.ANY);
	fc.setLabel(new ResourceModel("searchProfileHobbyTypeSelectionLabel"));
	fc.setEscapeModelStrings(false);
	// ... validation
	// searchProfileHobbyTypesSelection.setRequired(true);
	return fc;
    }

    private FormComponent<OccupationType> createSearchProfileOccupationTypeFormComponent() {
	// ... values
	final OccupationType[] occupationTypeValues = OccupationType.values();
	final ArrayList<OccupationType> occupationTypes = new ArrayList<OccupationType>(
		Arrays.asList(occupationTypeValues));
	occupationTypes.remove(OccupationType.DONT_SAY);

	// ... field
	final DropDownChoice<OccupationType> searchProfileOccupationTypeSelection = new DropDownChoice<OccupationType>(
		"searchProfile.occupationType", occupationTypes, new EnumChoiceRenderer<OccupationType>(this));
	searchProfileOccupationTypeSelection.setLabel(new ResourceModel("searchProfileOccupationTypeSelectionLabel"));
	searchProfileOccupationTypeSelection.setEscapeModelStrings(false);

	// ... validation
	searchProfileOccupationTypeSelection.setRequired(true);
	return searchProfileOccupationTypeSelection;
    }

    private FormComponent<IncomeType> createSearchProfileIncomeTypeFormComponent() {
	// ... values
	final IncomeType[] incomeTypeValues = IncomeType.values();
	final ArrayList<IncomeType> incomeTypes = new ArrayList<IncomeType>(Arrays.asList(incomeTypeValues));
	incomeTypes.remove(IncomeType.DONT_SAY);

	// ... field
	final DropDownChoice<IncomeType> searchProfileIncomeTypeSelection = new DropDownChoice<IncomeType>(
		"searchProfile.incomeType", incomeTypes, new EnumChoiceRenderer<IncomeType>(this));
	searchProfileIncomeTypeSelection.setLabel(new ResourceModel("searchProfileIncomeTypeSelectionLabel"));
	searchProfileIncomeTypeSelection.setEscapeModelStrings(false);
	// dont convert to html

	// ... validation
	searchProfileIncomeTypeSelection.setRequired(true);
	return searchProfileIncomeTypeSelection;
    }

    private FormComponent<EducationType> createSearchProfileEducationTypesFormComponent() {
	// ... model
	final CompoundPropertyModel<User> model = (CompoundPropertyModel<User>) getDefaultModel();
	// ... values
	final EducationType[] educationTypeValues = EducationType.values();
	final ArrayList<EducationType> educationTypes = new ArrayList<EducationType>(Arrays.asList(educationTypeValues));
	educationTypes.remove(EducationType.DONT_SAY);
	// ... component
	final EnumCheckGroupPanel<EducationType> fc = new EnumCheckGroupPanel<EducationType>(
		"searchProfile.educationTypes", model.bind("searchProfile.educationTypes"), educationTypes,
		EducationType.ANY);
	fc.setLabel(new ResourceModel("searchProfileEducationTypeSelectionLabel"));
	fc.setEscapeModelStrings(false);
	// ... validation
	fc.setRequired(true);
	return fc;
    }

    private FormComponent<SmokeType> createSearchProfileSmokeTypeFormComponent() {
	// ... values
	final SmokeType[] smokeTypeValues = SmokeType.values();
	final ArrayList<SmokeType> smokeTypes = new ArrayList<SmokeType>(Arrays.asList(smokeTypeValues));
	smokeTypes.remove(SmokeType.DONT_SAY);

	// ... field
	final DropDownChoice<SmokeType> searchProfileSmokeTypeSelection = new DropDownChoice<SmokeType>(
		"searchProfile.smokeType", smokeTypes, new EnumChoiceRenderer<SmokeType>(this));
	searchProfileSmokeTypeSelection.setLabel(new ResourceModel("searchProfileSmokeTypeSelectionLabel"));
	searchProfileSmokeTypeSelection.setEscapeModelStrings(false);

	// ... validation
	searchProfileSmokeTypeSelection.setRequired(true);
	return searchProfileSmokeTypeSelection;
    }
}
