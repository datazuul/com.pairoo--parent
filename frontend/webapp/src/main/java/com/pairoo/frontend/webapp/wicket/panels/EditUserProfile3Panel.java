package com.pairoo.frontend.webapp.wicket.panels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import com.pairoo.domain.User;
import com.pairoo.domain.enums.Importance;

public class EditUserProfile3Panel extends BasePanel {
    private static final long serialVersionUID = 1L;

    public EditUserProfile3Panel(final String id, final IModel<User> model) {
	super(id, new CompoundPropertyModel<User>(model));
    }

    @Override
    protected void onInitialize() {
	super.onInitialize();

	// ---------------- userprofile: showing emotions
	final FormComponent<Importance> showingEmotionsSelection = createImportanceFormComponent("userProfile.personalValues.showingEmotions");
	add(showingEmotionsSelection);
	add(labelForFormComponent("showingEmotionsSelectionLabel", showingEmotionsSelection));

	// ---------------- userprofile: long relationship
	final FormComponent<Importance> longRelationshipSelection = createImportanceFormComponent("userProfile.personalValues.longRelationship");
	add(longRelationshipSelection);
	add(labelForFormComponent("longRelationshipSelectionLabel", longRelationshipSelection));

	// ---------------- userprofile: romance
	final FormComponent<Importance> romanceSelection = createImportanceFormComponent("userProfile.personalValues.romance");
	add(romanceSelection);
	add(labelForFormComponent("romanceSelectionLabel", romanceSelection));

	// ---------------- userprofile: short relationship
	final FormComponent<Importance> shortRelationshipSelection = createImportanceFormComponent("userProfile.personalValues.shortRelationship");
	add(shortRelationshipSelection);
	add(labelForFormComponent("shortRelationshipSelectionLabel", shortRelationshipSelection));

	// ---------------- userprofile: tenderness
	final FormComponent<Importance> tendernessSelection = createImportanceFormComponent("userProfile.personalValues.tenderness");
	add(tendernessSelection);
	add(labelForFormComponent("tendernessSelectionLabel", tendernessSelection));

	// ---------------- userprofile: freedom
	final FormComponent<Importance> freedomSelection = createImportanceFormComponent("userProfile.personalValues.freedom");
	add(freedomSelection);
	add(labelForFormComponent("freedomSelectionLabel", freedomSelection));

	// ---------------- userprofile: sexuality
	final FormComponent<Importance> sexualitySelection = createImportanceFormComponent("userProfile.personalValues.sexuality");
	add(sexualitySelection);
	add(labelForFormComponent("sexualitySelectionLabel", sexualitySelection));

	// ---------------- userprofile: different partners
	// final FormComponent<Importance> differentPartnersSelection =
	// createImportanceFormComponent("userProfile.personalValues.differentPartners");
	// add(differentPartnersSelection);
	// add(createFormComponentLabel("differentPartnersSelectionLabel",
	// differentPartnersSelection));

	// ---------------- userprofile: faithfulness
	final FormComponent<Importance> faithfulnessSelection = createImportanceFormComponent("userProfile.personalValues.faithfulness");
	add(faithfulnessSelection);
	add(labelForFormComponent("faithfulnessSelectionLabel", faithfulnessSelection));

	// ---------------- userprofile: respect
	final FormComponent<Importance> respectSelection = createImportanceFormComponent("userProfile.personalValues.respect");
	add(respectSelection);
	add(labelForFormComponent("respectSelectionLabel", respectSelection));

	// ---------------- userprofile: honesty
	final FormComponent<Importance> honestySelection = createImportanceFormComponent("userProfile.personalValues.honesty");
	add(honestySelection);
	add(labelForFormComponent("honestySelectionLabel", honestySelection));

	// ---------------- userprofile: security
	final FormComponent<Importance> securitySelection = createImportanceFormComponent("userProfile.personalValues.security");
	add(securitySelection);
	add(labelForFormComponent("securitySelectionLabel", securitySelection));

	// ---------------- userprofile: confidence
	final FormComponent<Importance> confidenceSelection = createImportanceFormComponent("userProfile.personalValues.confidence");
	add(confidenceSelection);
	add(labelForFormComponent("confidenceSelectionLabel", confidenceSelection));

	// ---------------- userprofile: charm
	final FormComponent<Importance> charmSelection = createImportanceFormComponent("userProfile.personalValues.charm");
	add(charmSelection);
	add(labelForFormComponent("charmSelectionLabel", charmSelection));

    }

    private FormComponent<Importance> createImportanceFormComponent(final String id) {
	return createImportanceFormComponent(id, id);
    }

    private FormComponent<Importance> createImportanceFormComponent(final String id, final String labelResourceKey) {
	// ... values
	final Importance[] importanceValues = Importance.values();
	final List<Importance> importanceList = new ArrayList<Importance>(Arrays.asList(importanceValues));
	// ... field
	final FormComponent<Importance> importanceSelection = new DropDownChoice<Importance>(id, importanceList,
		new EnumChoiceRenderer<Importance>(this));
	importanceSelection.setLabel(new ResourceModel(labelResourceKey));
	importanceSelection.setEscapeModelStrings(false);

	// ... validation
	importanceSelection.setRequired(true);
	return importanceSelection;
    }
}
