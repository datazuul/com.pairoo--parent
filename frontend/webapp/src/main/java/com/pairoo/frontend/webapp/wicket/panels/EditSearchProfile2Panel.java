package com.pairoo.frontend.webapp.wicket.panels;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import com.datazuul.framework.webapp.wicket.components.form.EnumCheckGroupPanel;
import com.pairoo.domain.User;
import com.pairoo.domain.enums.BodyType;
import com.pairoo.domain.enums.EyeColor;
import com.pairoo.domain.enums.HairColor;
import com.pairoo.domain.enums.PartnerType;
import com.pairoo.frontend.webapp.wicket.components.HeightFormComponent;

/**
 * Form for editing SearchProfile domain object.
 * 
 * @author Ralf Eichinger
 */
public class EditSearchProfile2Panel extends BasePanel {
    private static final long serialVersionUID = 1L;

    public EditSearchProfile2Panel(final String id, final IModel<User> model) {
	super(id, new CompoundPropertyModel<User>(model));
    }

    @Override
    protected void onInitialize() {
	super.onInitialize();

	add(checkGroupPanelForBodyType("searchProfile.bodyTypes"));

	// ---------------- search profile: min height
	final FormComponent<Integer> searchProfileMinHeightSelection = createSearchProfileMinHeightFormComponent();
	add(searchProfileMinHeightSelection);
	add(labelForFormComponent("searchProfileMinHeightSelectionLabel", searchProfileMinHeightSelection));

	// ---------------- search profile: max height
	final FormComponent<Integer> searchProfileMaxHeightSelection = createSearchProfileMaxHeightFormComponent();
	add(searchProfileMaxHeightSelection);
	add(labelForFormComponent("searchProfileMaxHeightSelectionLabel", searchProfileMaxHeightSelection));

	// ---------------- search profile: eye color
	// Removed on business request JTrac PAIROO-251
	// add(createSearchProfileEyeColorsFormComponent());

	// ---------------- search profile: hair color
	add(createSearchProfileHairColorsFormComponent());
    }

    private FormComponent<HairColor> createSearchProfileHairColorsFormComponent() {
	// ... model
	final CompoundPropertyModel<User> model = (CompoundPropertyModel<User>) getDefaultModel();
	// ... values
	final HairColor[] hairColorValues = HairColor.values();
	final ArrayList<HairColor> hairColors = new ArrayList<HairColor>(Arrays.asList(hairColorValues));
	hairColors.remove(HairColor.DONT_SAY);
	// TODO possible without unpacking User object?
	final User user = model.getObject();
	if (user.getSearchProfile().getPartnerType() == PartnerType.FEMALE) {
	    hairColors.remove(HairColor.BALD);
	}
	// ... component
	final EnumCheckGroupPanel<HairColor> fc = new EnumCheckGroupPanel<HairColor>("searchProfile.hairColors",
		model.bind("searchProfile.hairColors"), hairColors, HairColor.ANY);
	fc.setLabel(new ResourceModel("searchProfileHairColorSelectionLabel"));
	// ... validation
	fc.setRequired(true);
	return fc;
    }

    private FormComponent<EyeColor> createSearchProfileEyeColorsFormComponent() {
	// ... model
	final CompoundPropertyModel<User> model = (CompoundPropertyModel<User>) getDefaultModel();
	// ... values
	final EyeColor[] eyeColorValues = EyeColor.values();
	final ArrayList<EyeColor> eyeColors = new ArrayList<EyeColor>(Arrays.asList(eyeColorValues));
	eyeColors.remove(EyeColor.DONT_SAY);
	// ... component
	final EnumCheckGroupPanel<EyeColor> searchProfileEyeColorSelection = new EnumCheckGroupPanel<EyeColor>(
		"searchProfile.eyeColors", model.bind("searchProfile.eyeColors"), eyeColors, EyeColor.ANY);
	searchProfileEyeColorSelection.setLabel(new ResourceModel("searchProfileEyeColorSelectionLabel"));
	// ... validation
	searchProfileEyeColorSelection.setRequired(true);
	return searchProfileEyeColorSelection;
    }

    private FormComponent<Integer> createSearchProfileMaxHeightFormComponent() {
	// ... field
	final FormComponent<Integer> searchProfileMaxHeightSelection = new HeightFormComponent(
		"searchProfile.maxHeightCm");
	searchProfileMaxHeightSelection.setLabel(new ResourceModel("searchProfileMaxHeightSelectionLabel"));
	searchProfileMaxHeightSelection.setEscapeModelStrings(false);
	// ... validation
	searchProfileMaxHeightSelection.setRequired(true);
	return searchProfileMaxHeightSelection;
    }

    private FormComponent<Integer> createSearchProfileMinHeightFormComponent() {
	// ... field
	final FormComponent<Integer> searchProfileMinHeightSelection = new HeightFormComponent(
		"searchProfile.minHeightCm");
	searchProfileMinHeightSelection.setLabel(new ResourceModel("searchProfileMinHeightSelectionLabel"));
	searchProfileMinHeightSelection.setEscapeModelStrings(false);
	// ... validation
	searchProfileMinHeightSelection.setRequired(true);
	return searchProfileMinHeightSelection;
    }

    private FormComponent<BodyType> checkGroupPanelForBodyType(String id) {
	// ... model
	final CompoundPropertyModel<User> model = (CompoundPropertyModel<User>) getDefaultModel();
	// ... values
	final BodyType[] bodyTypeValues = BodyType.values();
	final ArrayList<BodyType> bodyTypes = new ArrayList<BodyType>(Arrays.asList(bodyTypeValues));
	bodyTypes.remove(BodyType.DONT_SAY);
	// ... component
	final EnumCheckGroupPanel<BodyType> fc = new EnumCheckGroupPanel<BodyType>(id, model.bind(id), bodyTypes,
		BodyType.ANY);
	fc.setLabel(new ResourceModel("searchProfileBodyTypeSelectionLabel"));
	// ... validation
	fc.setRequired(true);
	return fc;
    }
}
