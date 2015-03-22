package com.pairoo.frontend.webapp.wicket.panels.authenticated;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.EnumLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import com.datazuul.framework.util.EnumUtils;
import com.pairoo.domain.User;
import com.pairoo.domain.UserProfile;
import com.pairoo.domain.enums.HobbyType;
import com.pairoo.domain.enums.Importance;
import com.pairoo.domain.enums.KitchenType;
import com.pairoo.domain.enums.SportType;
import com.pairoo.frontend.webapp.wicket.panels.BasePanel;

/**
 * @author Ralf Eichinger
 */
public class WantedProfileAboutMyLifePanel extends BasePanel {
    private static final long serialVersionUID = 1L;

    public WantedProfileAboutMyLifePanel(final String id, final IModel<User> model) {
	super(id, new CompoundPropertyModel<User>(model));
    }

    @Override
    protected void onBeforeRender() {
	super.onBeforeRender();
	logEnter(WantedProfileAboutMyLifePanel.class);
    }

    @Override
    protected void onInitialize() {
	super.onInitialize();

	final UserProfile userProfile = ((User) getDefaultModelObject()).getUserProfile();

	add(hobbyTypes("hobbyTypesList", userProfile));
	add(sportTypes("sportTypesList", userProfile));
	add(kitchenTypes("kitchenTypesList", userProfile));
	add(showingEmotionsLabel("lblShowingEmotions", userProfile));
	add(romanceLabel("lblRomance", userProfile));
	add(tendernessLabel("lblTenderness", userProfile));
	add(longRelationshipLabel("lblLongRelationship", userProfile));
	add(shortRelationshipLabel("lblShortRelationship", userProfile));
	add(freedomLabel("lblFreedom", userProfile));
	add(faithfulnessLabel("lblFaithfulness", userProfile));
	add(honestyLabel("lblHonesty", userProfile));
	add(confidenceLabel("lblConfidence", userProfile));
	add(respectLabel("lblRespect", userProfile));
	add(securityLabel("lblSecurity", userProfile));
	add(charmLabel("lblCharm", userProfile));
	add(sexualityLabel("lblSexuality", userProfile));
	add(createDifferentPartnersComponent("lblDifferentPartners", userProfile));
    }

    private Component createDifferentPartnersComponent(String id, final UserProfile userProfile) {
	final Importance importance = userProfile.getPersonalValues().getDifferentPartners();
	return createImportanceComponent(id, importance);
    }

    private Component sexualityLabel(String id, final UserProfile userProfile) {
	final Importance importance = userProfile.getPersonalValues().getSexuality();
	return createImportanceComponent(id, importance);
    }

    private Component charmLabel(String id, final UserProfile userProfile) {
	final Importance importance = userProfile.getPersonalValues().getCharm();
	return createImportanceComponent(id, importance);
    }

    private Component securityLabel(String id, final UserProfile userProfile) {
	final Importance importance = userProfile.getPersonalValues().getSecurity();
	return createImportanceComponent(id, importance);
    }

    private Component respectLabel(String id, final UserProfile userProfile) {
	final Importance importance = userProfile.getPersonalValues().getRespect();
	return createImportanceComponent(id, importance);
    }

    private Component confidenceLabel(String id, final UserProfile userProfile) {
	final Importance importance = userProfile.getPersonalValues().getConfidence();
	return createImportanceComponent(id, importance);
    }

    private Component honestyLabel(String id, final UserProfile userProfile) {
	final Importance importance = userProfile.getPersonalValues().getHonesty();
	return createImportanceComponent(id, importance);
    }

    private Component faithfulnessLabel(String id, final UserProfile userProfile) {
	final Importance importance = userProfile.getPersonalValues().getFaithfulness();
	return createImportanceComponent(id, importance);
    }

    private Component freedomLabel(String id, final UserProfile userProfile) {
	final Importance importance = userProfile.getPersonalValues().getFreedom();
	return createImportanceComponent(id, importance);
    }

    private Component shortRelationshipLabel(String id, final UserProfile userProfile) {
	final Importance importance = userProfile.getPersonalValues().getShortRelationship();
	return createImportanceComponent(id, importance);
    }

    private Component longRelationshipLabel(String id, final UserProfile userProfile) {
	final Importance importance = userProfile.getPersonalValues().getLongRelationship();
	return createImportanceComponent(id, importance);
    }

    private Component tendernessLabel(String id, final UserProfile userProfile) {
	final Importance importance = userProfile.getPersonalValues().getTenderness();
	return createImportanceComponent(id, importance);
    }

    private Component romanceLabel(String id, final UserProfile userProfile) {
	final Importance importance = userProfile.getPersonalValues().getRomance();
	return createImportanceComponent(id, importance);
    }

    private Component showingEmotionsLabel(String id, final UserProfile userProfile) {
	final Importance importance = userProfile.getPersonalValues().getShowingEmotions();
	return createImportanceComponent(id, importance);
    }

    private Component createImportanceComponent(final String id, final Importance importance) {
	return new EnumLabel<Importance>(id, importance);
    }

    private Component kitchenTypes(String id, final UserProfile userProfile) {
	final List<KitchenType> kitchenTypesList = userProfile.getLifeStyle().getKitchenTypes();
	return new ListView<KitchenType>(id, kitchenTypesList) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    protected void populateItem(final ListItem<KitchenType> item) {
		final KitchenType itemModel = item.getModelObject();
		item.add(new Label("lblItemModel", new ResourceModel(EnumUtils.getEnumKey(itemModel))));

		final Label separator = new Label("separator", ", ");
		item.add(separator);
		if (item.getIndex() == getList().size() - 1) {
		    separator.setVisible(false);
		}
	    }
	};
    }

    private Component sportTypes(String id, final UserProfile userProfile) {
	final List<SportType> sportTypesList = userProfile.getLifeStyle().getSportTypes();
	return new ListView<SportType>(id, sportTypesList) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    protected void populateItem(final ListItem<SportType> item) {
		final SportType itemModel = item.getModelObject();
		item.add(new Label("lblItemModel", new ResourceModel(EnumUtils.getEnumKey(itemModel))));

		final Label separator = new Label("separator", ", ");
		item.add(separator);
		if (item.getIndex() == getList().size() - 1) {
		    separator.setVisible(false);
		}
	    }
	};
    }

    private Component hobbyTypes(String id, final UserProfile userProfile) {
	final List<HobbyType> hobbyTypesList = userProfile.getLifeStyle().getHobbyTypes();
	return new ListView<HobbyType>(id, hobbyTypesList) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    protected void populateItem(final ListItem<HobbyType> item) {
		final HobbyType itemModel = item.getModelObject();
		item.add(new Label("lblItemModel", new ResourceModel(EnumUtils.getEnumKey(itemModel))));

		final Label separator = new Label("separator", ", ");
		item.add(separator);
		if (item.getIndex() == getList().size() - 1) {
		    separator.setVisible(false);
		}
	    }
	};
    }
}
