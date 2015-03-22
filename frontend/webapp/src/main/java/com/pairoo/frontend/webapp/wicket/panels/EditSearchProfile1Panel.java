package com.pairoo.frontend.webapp.wicket.panels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;

import com.datazuul.framework.domain.Language;
import com.datazuul.framework.webapp.wicket.components.form.EnumCheckGroupPanel;
import com.pairoo.domain.User;
import com.pairoo.domain.enums.Ethnicity;
import com.pairoo.domain.enums.FamilyStatusType;
import com.pairoo.domain.enums.NumberOfKidsType;
import com.pairoo.domain.enums.PartnerType;
import com.pairoo.domain.enums.PartnershipType;
import com.pairoo.domain.enums.WantMoreChildrenType;
import com.pairoo.domain.geo.GeoArea;
import com.pairoo.frontend.webapp.wicket.components.GeoAreaFormComponentPanel;

/**
 * Form for editing SearchProfile domain object.
 * 
 * @author Ralf Eichinger
 */
public class EditSearchProfile1Panel extends BasePanel {
    private static final long serialVersionUID = 1L;

    public EditSearchProfile1Panel(final String id, final IModel<User> model) {
	super(id, new CompoundPropertyModel<User>(model));
    }

    @Override
    protected void onInitialize() {
	super.onInitialize();

	add(dropDownChoiceForPartnerType("searchProfile.partnerType"));

	final FormComponent<PartnershipType> partnershipTypeFC = dropDownChoiceForPartnershipType("searchProfile.partnershipType");
	add(partnershipTypeFC);
	add(labelForFormComponent("searchProfilePartnershipTypeSelectionLabel", partnershipTypeFC));

	final FormComponent<Integer> minAgeFC = dropDownChoiceForMinAge("searchProfile.minAge");
	add(minAgeFC);
	add(labelForFormComponent("searchProfileMinAgeLabel", minAgeFC));

	final FormComponent<Integer> maxAgeFC = dropDownChoiceForMaxAge("searchProfile.maxAge");
	add(maxAgeFC);
	add(labelForFormComponent("searchProfileMaxAgeLabel", maxAgeFC));

	final FormComponentPanel<GeoArea> searchProfileGeoArea = new GeoAreaFormComponentPanel("geoArea",
		new PropertyModel<GeoArea>(getDefaultModelObject(), "searchProfile.geoArea"));
	searchProfileGeoArea.setLabel(new ResourceModel("searchProfile.geoArea.label"));
	add(searchProfileGeoArea);

	add(checkGroupPanelForFamilyStatusTypes("searchProfile.familyStatusTypes"));

	final FormComponent<NumberOfKidsType> numberOfKidsFC = dropDownChoiceForNumberOfKids("searchProfile.numberOfKidsType");
	add(numberOfKidsFC);
	add(labelForFormComponent("searchProfileNumberOfKidsTypeSelectionLabel", numberOfKidsFC));

	final FormComponent<WantMoreChildrenType> searchProfileWantMoreChildrenTypeSelection = dropDownChoiceForWantMoreChildren("searchProfile.wantMoreChildrenType");
	add(searchProfileWantMoreChildrenTypeSelection);
	add(labelForFormComponent("searchProfileWantMoreChildrenTypeSelectionLabel",
		searchProfileWantMoreChildrenTypeSelection));

	final FormComponent<Language> motherLanguageFC = dropDownChoiceForMotherLanguage("searchProfile.motherLanguage");
	add(motherLanguageFC);
	add(labelForFormComponent("searchProfileMotherLanguageSelectionLabel", motherLanguageFC));

	final FormComponent<Language> languagesFC = checkGroupPanelForLanguages("searchProfile.languages");
	add(languagesFC);
	add(labelForFormComponent("searchProfileLanguagesSelectionLabel", languagesFC));

	final FormComponent<Ethnicity> ethnicitiesFC = checkGroupPanelForEthnicities("searchProfile.ethnicities");
	add(ethnicitiesFC);
    }

    private FormComponent<Ethnicity> checkGroupPanelForEthnicities(final String id) {
	// ... model
	final CompoundPropertyModel<User> model = (CompoundPropertyModel<User>) getDefaultModel();
	// ... values
	final Ethnicity[] ethnicityValues = Ethnicity.values();
	final ArrayList<Ethnicity> ethnicities = new ArrayList<Ethnicity>(Arrays.asList(ethnicityValues));
	ethnicities.remove(Ethnicity.DONT_SAY);
	// ... component
	final EnumCheckGroupPanel<Ethnicity> fc = new EnumCheckGroupPanel<Ethnicity>(id, model.bind(id), ethnicities,
		Ethnicity.ANY);
	fc.setLabel(new ResourceModel("searchProfileEthnicitySelectionLabel"));
	// ... validation
	fc.setRequired(true);
	return fc;
    }

    private FormComponent<Language> checkGroupPanelForLanguages(final String id) {
	// ... model
	final CompoundPropertyModel<User> model = (CompoundPropertyModel<User>) getDefaultModel();
	// ... values
	final Language[] languageValues = Language.values();
	final ArrayList<Language> languages = new ArrayList<Language>(Arrays.asList(languageValues));
	languages.remove(Language.DONT_SAY);
	final List<Language> sortedLanguages = sortLanguages(languages);

	// ... component
	final EnumCheckGroupPanel<Language> fc = new EnumCheckGroupPanel<Language>(id, model.bind(id), sortedLanguages,
		Language.ANY);
	fc.setLabel(new ResourceModel("searchProfileLanguagesSelectionLabel"));
	fc.setEscapeModelStrings(false);
	// ... validation
	fc.setRequired(true);
	return fc;
    }

    private FormComponent<Language> dropDownChoiceForMotherLanguage(final String id) {
	// ... values
	final Language[] languageValues = Language.values();
	final ArrayList<Language> languages = new ArrayList<Language>(Arrays.asList(languageValues));
	languages.remove(Language.DONT_SAY);
	final List<Language> sortedLanguages = sortLanguages(languages);

	// ... field
	final DropDownChoice<Language> fc = new DropDownChoice<Language>(id, sortedLanguages,
		new EnumChoiceRenderer<Language>(this));
	fc.setLabel(new ResourceModel("motherTongue"));
	fc.setEscapeModelStrings(false);
	// ... validation
	fc.setRequired(true);
	return fc;
    }

    private FormComponent<WantMoreChildrenType> dropDownChoiceForWantMoreChildren(final String id) {
	// ... values
	final WantMoreChildrenType[] wantMoreChildrenTypeValues = WantMoreChildrenType.values();
	final ArrayList<WantMoreChildrenType> wantMoreChildrenTypes = new ArrayList<WantMoreChildrenType>(
		Arrays.asList(wantMoreChildrenTypeValues));
	wantMoreChildrenTypes.remove(WantMoreChildrenType.DONT_SAY);
	// ... field
	final DropDownChoice<WantMoreChildrenType> fc = new DropDownChoice<WantMoreChildrenType>(id,
		wantMoreChildrenTypes, new EnumChoiceRenderer<WantMoreChildrenType>(this));
	fc.setLabel(new ResourceModel("wants_more_children"));
	fc.setEscapeModelStrings(false);
	// ... validation
	fc.setRequired(true);
	return fc;
    }

    private FormComponent<NumberOfKidsType> dropDownChoiceForNumberOfKids(final String id) {
	// ... values
	final NumberOfKidsType[] numberOfKidsTypeValues = NumberOfKidsType.values();
	final ArrayList<NumberOfKidsType> numberOfKidsTypes = new ArrayList<NumberOfKidsType>(
		Arrays.asList(numberOfKidsTypeValues));
	numberOfKidsTypes.remove(NumberOfKidsType.DONT_SAY);
	// ... field
	final DropDownChoice<NumberOfKidsType> fc = new DropDownChoice<NumberOfKidsType>(id, numberOfKidsTypes,
		new EnumChoiceRenderer<NumberOfKidsType>(this));
	fc.setLabel(new ResourceModel("number_of_kids"));
	fc.setEscapeModelStrings(false);
	// ... validation
	fc.setRequired(true);
	return fc;
    }

    private FormComponent<FamilyStatusType> checkGroupPanelForFamilyStatusTypes(final String id) {
	// ... model
	final CompoundPropertyModel<User> model = (CompoundPropertyModel<User>) getDefaultModel();
	// ... values
	final FamilyStatusType[] familyStatusTypeValues = FamilyStatusType.values();
	final ArrayList<FamilyStatusType> familyStatusTypes = new ArrayList<FamilyStatusType>(
		Arrays.asList(familyStatusTypeValues));
	familyStatusTypes.remove(FamilyStatusType.DONT_SAY);
	// ... component
	final EnumCheckGroupPanel<FamilyStatusType> fc = new EnumCheckGroupPanel<FamilyStatusType>(id, model.bind(id),
		familyStatusTypes, FamilyStatusType.ANY);
	fc.setLabel(new ResourceModel("searchProfileFamilyStatusTypeSelectionLabel"));
	// ... validation
	fc.setRequired(true);
	return fc;
    }

    private FormComponent<Integer> dropDownChoiceForMaxAge(final String id) {
	// ... values
	final ArrayList<Integer> maxAgeValues = new ArrayList<Integer>();
	for (int age = 18; age <= 90; age++) {
	    maxAgeValues.add(age);
	}
	// ... field
	final DropDownChoice<Integer> fc = new DropDownChoice<Integer>(id, maxAgeValues);
	fc.setLabel(new ResourceModel("to"));
	fc.setEscapeModelStrings(false);
	// ... validation
	fc.setRequired(true);
	return fc;
    }

    private FormComponent<Integer> dropDownChoiceForMinAge(final String id) {
	// ... values
	final ArrayList<Integer> minAgeValues = new ArrayList<Integer>();
	for (int age = 18; age <= 90; age++) {
	    minAgeValues.add(age);
	}
	// ... field
	final DropDownChoice<Integer> fc = new DropDownChoice<Integer>(id, minAgeValues);
	fc.setLabel(new ResourceModel("in_the_age_from"));
	fc.setEscapeModelStrings(false);
	// ... validation
	fc.setRequired(true);
	return fc;
    }

    private FormComponent<PartnershipType> dropDownChoiceForPartnershipType(final String id) {
	// ... values
	final PartnershipType[] partnershipTypeValues = PartnershipType.values();
	final ArrayList<PartnershipType> partnershipTypes = new ArrayList<PartnershipType>(
		Arrays.asList(partnershipTypeValues));
	// partnershipTypes.remove(PartnershipType.ANY);
	// ... field
	final DropDownChoice<PartnershipType> fc = new DropDownChoice<PartnershipType>(id, partnershipTypes,
		new EnumChoiceRenderer<PartnershipType>(this));
	fc.setLabel(new ResourceModel("searchProfilePartnershipTypeSelectionLabel"));
	fc.setEscapeModelStrings(false);
	// ... validation
	fc.setRequired(true);
	add(fc);
	return fc;
    }

    private FormComponent<PartnerType> dropDownChoiceForPartnerType(final String id) {
	// ... values
	final PartnerType[] partnerTypeValues = PartnerType.values();
	final ArrayList<PartnerType> partnerTypes = new ArrayList<PartnerType>(Arrays.asList(partnerTypeValues));
	// partnerTypes.remove(PartnerType.DONT_SAY);
	// ... field
	final DropDownChoice<PartnerType> fc = new DropDownChoice<PartnerType>(id, partnerTypes,
		new EnumChoiceRenderer<PartnerType>(this) {
		    private static final long serialVersionUID = 1L;

		    @Override
		    protected String resourceKey(final PartnerType object) {
			final String result = "searchFor_" + super.resourceKey(object);
			return result;
		    }
		});
	fc.setLabel(new ResourceModel("searchProfilePartnerTypeSelectionLabel"));
	fc.setEscapeModelStrings(false);
	// ... validation
	fc.setRequired(true);
	// ... label: headline
	return fc;
    }
}
