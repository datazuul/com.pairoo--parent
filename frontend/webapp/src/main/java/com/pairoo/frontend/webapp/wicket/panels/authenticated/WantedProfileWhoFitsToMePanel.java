package com.pairoo.frontend.webapp.wicket.panels.authenticated;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import com.datazuul.framework.util.EnumUtils;
import com.pairoo.domain.SearchProfile;
import com.pairoo.domain.User;
import com.pairoo.domain.UserProfile;
import com.pairoo.domain.enums.HobbyType;
import com.pairoo.domain.enums.Importance;
import com.pairoo.domain.enums.SportType;
import com.pairoo.frontend.webapp.wicket.panels.BasePanel;

/**
 * @author Ralf Eichinger
 */
public class WantedProfileWhoFitsToMePanel extends BasePanel {
    private static final long serialVersionUID = 1L;

    public WantedProfileWhoFitsToMePanel(final String id, final IModel<User> model) {
	super(id, new CompoundPropertyModel<User>(model));
    }

    @Override
    protected void onBeforeRender() {
	super.onBeforeRender();
	logEnter(WantedProfileWhoFitsToMePanel.class);
    }

    @Override
    protected void onInitialize() {
	super.onInitialize();

	User user = (User) getDefaultModelObject();
	final SearchProfile searchProfile = user.getSearchProfile();
	final UserProfile userProfile = user.getUserProfile();

	// interests
	add(createHobbyTypesComponent(searchProfile));

	// sport types
	add(createSportTypesComponent(searchProfile));

	// FIXME personal values is a copy from "about my life"... are you sure?
	// showing emotions
	add(createShowingEmotionsComponent(userProfile));

	// romance
	add(createRomanceComponent(userProfile));

	// tenderness
	add(createTendernessComponent(userProfile));

	// long relationship
	add(createLongRelationshipComponent(userProfile));

	// short relationship
	add(createShortRelationshipComponent(userProfile));

	// freedom
	add(createFreedomComponent(userProfile));

	// faithfulness
	add(createFaithfulnessComponent(userProfile));

	// honesty
	add(createHonestyComponent(userProfile));

	// confidence
	add(createConfidenceComponent(userProfile));

	// respect
	add(createRespectComponent(userProfile));

	// security
	add(createSecurityComponent(userProfile));

	// charm
	add(createCharmComponent(userProfile));

	// sexuality
	add(createSexualityComponent(userProfile));

	// different partners
	add(createDifferentPartnersComponent(userProfile));
    }

    private Component createDifferentPartnersComponent(final UserProfile userProfile) {
	final Importance importance = userProfile.getPersonalValues().getDifferentPartners();
	return createImportanceComponent("lblDifferentPartners", importance);
    }

    private Component createSexualityComponent(final UserProfile userProfile) {
	final Importance importance = userProfile.getPersonalValues().getSexuality();
	return createImportanceComponent("lblSexuality", importance);
    }

    private Component createCharmComponent(final UserProfile userProfile) {
	final Importance importance = userProfile.getPersonalValues().getCharm();
	return createImportanceComponent("lblCharm", importance);
    }

    private Component createSecurityComponent(final UserProfile userProfile) {
	final Importance importance = userProfile.getPersonalValues().getSecurity();
	return createImportanceComponent("lblSecurity", importance);
    }

    private Component createRespectComponent(final UserProfile userProfile) {
	final Importance importance = userProfile.getPersonalValues().getRespect();
	return createImportanceComponent("lblRespect", importance);
    }

    private Component createConfidenceComponent(final UserProfile userProfile) {
	final Importance importance = userProfile.getPersonalValues().getConfidence();
	return createImportanceComponent("lblConfidence", importance);
    }

    private Component createHonestyComponent(final UserProfile userProfile) {
	final Importance importance = userProfile.getPersonalValues().getHonesty();
	return createImportanceComponent("lblHonesty", importance);
    }

    private Component createFaithfulnessComponent(final UserProfile userProfile) {
	final Importance importance = userProfile.getPersonalValues().getFaithfulness();
	return createImportanceComponent("lblFaithfulness", importance);
    }

    private Component createFreedomComponent(final UserProfile userProfile) {
	final Importance importance = userProfile.getPersonalValues().getFreedom();
	return createImportanceComponent("lblFreedom", importance);
    }

    private Component createShortRelationshipComponent(final UserProfile userProfile) {
	final Importance importance = userProfile.getPersonalValues().getShortRelationship();
	return createImportanceComponent("lblShortRelationship", importance);
    }

    private Component createLongRelationshipComponent(final UserProfile userProfile) {
	final Importance importance = userProfile.getPersonalValues().getLongRelationship();
	return createImportanceComponent("lblLongRelationship", importance);
    }

    private Component createTendernessComponent(final UserProfile userProfile) {
	final Importance importance = userProfile.getPersonalValues().getTenderness();
	return createImportanceComponent("lblTenderness", importance);
    }

    private Component createRomanceComponent(final UserProfile userProfile) {
	final Importance importance = userProfile.getPersonalValues().getRomance();
	return createImportanceComponent("lblRomance", importance);
    }

    private Component createShowingEmotionsComponent(final UserProfile userProfile) {
	final Importance importance = userProfile.getPersonalValues().getShowingEmotions();
	return createImportanceComponent("lblShowingEmotions", importance);
    }

    private Component createImportanceComponent(final String id, final Importance importance) {
	return new Label(id, new ResourceModel(EnumUtils.getEnumKey(importance)));
    }

    private Component createSportTypesComponent(final SearchProfile searchProfile) {
	final List<SportType> sportsList = searchProfile.getSportTypes();
	return new ListView<SportType>("sportTypesList", sportsList) {
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

    private Component createHobbyTypesComponent(final SearchProfile searchProfile) {
	final List<HobbyType> hobbiesList = searchProfile.getHobbyTypes();
	return new ListView<HobbyType>("hobbiesList", hobbiesList) {
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
