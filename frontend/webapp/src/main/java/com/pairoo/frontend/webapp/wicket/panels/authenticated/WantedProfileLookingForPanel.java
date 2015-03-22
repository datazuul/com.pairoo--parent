package com.pairoo.frontend.webapp.wicket.panels.authenticated;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import com.datazuul.framework.domain.Language;
import com.datazuul.framework.domain.geo.Country;
import com.datazuul.framework.util.EnumUtils;
import com.pairoo.domain.SearchProfile;
import com.pairoo.domain.User;
import com.pairoo.domain.enums.AppearanceStyle;
import com.pairoo.domain.enums.BodyType;
import com.pairoo.domain.enums.EducationType;
import com.pairoo.domain.enums.Ethnicity;
import com.pairoo.domain.enums.FamilyStatusType;
import com.pairoo.domain.enums.HairColor;
import com.pairoo.domain.enums.IncomeType;
import com.pairoo.domain.enums.NumberOfKidsType;
import com.pairoo.domain.enums.OccupationType;
import com.pairoo.domain.enums.PartnerType;
import com.pairoo.domain.enums.PartnershipType;
import com.pairoo.domain.enums.SmokeType;
import com.pairoo.domain.enums.WantMoreChildrenType;
import com.pairoo.frontend.webapp.wicket.components.HeightLabel;
import com.pairoo.frontend.webapp.wicket.panels.BasePanel;

/**
 * @author Ralf Eichinger
 */
public class WantedProfileLookingForPanel extends BasePanel {
    private static final long serialVersionUID = 1L;

    public WantedProfileLookingForPanel(final String id, final IModel<User> model) {
	super(id, new CompoundPropertyModel<User>(model));
    }

    @Override
    protected void onBeforeRender() {
	super.onBeforeRender();
	logEnter(WantedProfileLookingForPanel.class);
    }

    @Override
    protected void onInitialize() {
	super.onInitialize();

	final SearchProfile searchProfile = ((User) getDefaultModelObject()).getSearchProfile();

	add(partnerTypeLabel("lblPartnerType", searchProfile));
	add(partnershipTypeLabel("lblPartnershipType", searchProfile));
	add(new Label("searchProfile.minAge"));
	add(new Label("searchProfile.maxAge"));
	add(cityLabel("lblCity", searchProfile));
	add(countryLabel("lblCountry", searchProfile));

	// FIXME religion missing?

	add(familyStatusTypesLabel("familyStatusTypesList", searchProfile));
	add(numberOfKidsTypeLabel("lblNumberOfKidsType", searchProfile));
	add(wantMoreChildrenTypeLabel("lblWantMoreChildrenType", searchProfile));
	add(smokeTypeLabel("lblSmokeType", searchProfile));
	add(motherLanguageLabel("lblMotherTongue", searchProfile));
	add(languagesLabel("languagesList", searchProfile));
	add(ethnicitiesLabel("ethnicitiesList", searchProfile));
	add(bodyTypesLabel("bodyTypesList", searchProfile));
	add(heightLabel("lblHeightFrom", searchProfile.getMinHeightCm()));
	add(heightLabel("lblHeightTo", searchProfile.getMaxHeightCm()));

	// eye color
	// Removed on business request JTrac PAIROO-251
	// add(createEyeColorComponent(searchProfile));

	add(hairColorsLabel("hairColorsList", searchProfile));
	add(appearanceStylesLabel("appearanceStylesList", searchProfile));
	add(educationTypesLabel("educationTypesList", searchProfile));
	add(occupationTypeLabel("lblOccupationType", searchProfile));
	add(incomeTypeLabel("lblYearlyIncome", searchProfile));
    }

    private HeightLabel heightLabel(final String id, final Integer heightObject) {
	int height = -1;
	try {
	    height = heightObject.intValue();
	} catch (final Exception e) {

	}
	return new HeightLabel(id, getUsersCountry().getUnitLength(), height);
    }

    private Component incomeTypeLabel(String id, final SearchProfile searchProfile) {
	final IncomeType incomeType = searchProfile.getIncomeType();
	return new Label(id, new ResourceModel(EnumUtils.getEnumKey(incomeType)));
    }

    private Component occupationTypeLabel(String id, final SearchProfile searchProfile) {
	// FIXME differs from mockup: only one value no multiple choice possible
	final OccupationType occupationType = searchProfile.getOccupationType();
	return new Label(id, new ResourceModel(EnumUtils.getEnumKey(occupationType)));
    }

    private Component educationTypesLabel(String id, final SearchProfile searchProfile) {
	final List<EducationType> educationTypesList = searchProfile.getEducationTypes();
	return new ListView<EducationType>(id, educationTypesList) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    protected void populateItem(final ListItem<EducationType> item) {
		final EducationType itemModel = item.getModelObject();
		item.add(new Label("lblItemModel", new ResourceModel(EnumUtils.getEnumKey(itemModel))));

		final Label separator = new Label("separator", ", ");
		item.add(separator);
		if (item.getIndex() == getList().size() - 1) {
		    separator.setVisible(false);
		}
	    }
	};
    }

    private Component appearanceStylesLabel(String id, final SearchProfile searchProfile) {
	final List<AppearanceStyle> appearancStyleList = searchProfile.getAppearanceStyles();
	return new ListView<AppearanceStyle>(id, appearancStyleList) {
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

    private Component hairColorsLabel(String id, final SearchProfile searchProfile) {
	final List<HairColor> hairColorsList = searchProfile.getHairColors();
	return new ListView<HairColor>(id, hairColorsList) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    protected void populateItem(final ListItem<HairColor> item) {
		final HairColor itemModel = item.getModelObject();
		item.add(new Label("lblItemModel", new ResourceModel(EnumUtils.getEnumKey(itemModel))));

		final Label separator = new Label("separator", ", ");
		item.add(separator);
		if (item.getIndex() == getList().size() - 1) {
		    separator.setVisible(false);
		}
	    }
	};
    }

    private Component bodyTypesLabel(String id, final SearchProfile searchProfile) {
	final List<BodyType> bodyTypesList = searchProfile.getBodyTypes();
	return new ListView<BodyType>(id, bodyTypesList) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    protected void populateItem(final ListItem<BodyType> item) {
		final BodyType itemModel = item.getModelObject();
		item.add(new Label("lblItemModel", new ResourceModel(EnumUtils.getEnumKey(itemModel))));

		final Label separator = new Label("separator", ", ");
		item.add(separator);
		if (item.getIndex() == getList().size() - 1) {
		    separator.setVisible(false);
		}
	    }
	};
    }

    private Component ethnicitiesLabel(String id, final SearchProfile searchProfile) {
	final List<Ethnicity> ethnicitiesList = searchProfile.getEthnicities();
	return new ListView<Ethnicity>(id, ethnicitiesList) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    protected void populateItem(final ListItem<Ethnicity> item) {
		final Ethnicity itemModel = item.getModelObject();
		item.add(new Label("lblItemModel", new ResourceModel(EnumUtils.getEnumKey(itemModel))));

		final Label separator = new Label("separator", ", ");
		item.add(separator);
		if (item.getIndex() == getList().size() - 1) {
		    separator.setVisible(false);
		}
	    }
	};
    }

    private Component languagesLabel(String id, final SearchProfile searchProfile) {
	final List<Language> languagesList = searchProfile.getLanguages();
	return new ListView<Language>(id, languagesList) {
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

    private Component motherLanguageLabel(String id, final SearchProfile searchProfile) {
	final Language motherLanguage = searchProfile.getMotherLanguage();
	return new Label(id, new ResourceModel(EnumUtils.getEnumKey(motherLanguage)));
    }

    private Component smokeTypeLabel(String id, final SearchProfile searchProfile) {
	final SmokeType smokeType = searchProfile.getSmokeType();
	return new Label(id, new ResourceModel(EnumUtils.getEnumKey(smokeType)));
    }

    private Component wantMoreChildrenTypeLabel(String id, final SearchProfile searchProfile) {
	final WantMoreChildrenType wantMoreChildrenType = searchProfile.getWantMoreChildrenType();
	return new Label(id, new ResourceModel(EnumUtils.getEnumKey(wantMoreChildrenType)));
    }

    private Component numberOfKidsTypeLabel(String id, final SearchProfile searchProfile) {
	final NumberOfKidsType numberOfKidsType = searchProfile.getNumberOfKidsType();
	return new Label(id, new ResourceModel(EnumUtils.getEnumKey(numberOfKidsType)));
    }

    private Component familyStatusTypesLabel(String id, final SearchProfile searchProfile) {
	final List<FamilyStatusType> familyStatusTypesList = searchProfile.getFamilyStatusTypes();
	return new ListView<FamilyStatusType>(id, familyStatusTypesList) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    protected void populateItem(final ListItem<FamilyStatusType> item) {
		final FamilyStatusType itemModel = item.getModelObject();
		item.add(new Label("lblItemModel", new ResourceModel(EnumUtils.getEnumKey(itemModel))));

		final Label separator = new Label("separator", ", ");
		item.add(separator);
		if (item.getIndex() == getList().size() - 1) {
		    separator.setVisible(false);
		}
	    }
	};
    }

    private Component cityLabel(String id, final SearchProfile searchProfile) {
	String name = "--";
	try {
	    name = searchProfile.getGeoArea().getGeoLocation().getName();
	} catch (final NullPointerException npe) {

	}
	return new Label(id, name);
    }

    private Component countryLabel(String id, final SearchProfile searchProfile) {
	String key = "null";
	try {
	    final Country country = searchProfile.getGeoArea().getGeoLocation().getCountry();
	    key = EnumUtils.getEnumKey(country);
	} catch (final NullPointerException npe) {

	}
	return new Label(id, new ResourceModel(key));
    }

    private Component partnershipTypeLabel(String id, final SearchProfile searchProfile) {
	final PartnershipType partnershipType = searchProfile.getPartnershipType();
	return new Label(id, new ResourceModel(EnumUtils.getEnumKey(partnershipType)));
    }

    private Component partnerTypeLabel(String id, final SearchProfile searchProfile) {
	final PartnerType partnerType = searchProfile.getPartnerType();
	return new Label(id, new ResourceModel(EnumUtils.getEnumKey(partnerType)));
    }
}
