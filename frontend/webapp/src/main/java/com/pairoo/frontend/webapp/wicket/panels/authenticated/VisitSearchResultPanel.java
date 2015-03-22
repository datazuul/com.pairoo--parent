package com.pairoo.frontend.webapp.wicket.panels.authenticated;

import com.pairoo.domain.enums.FamilyStatusType;
import com.pairoo.domain.enums.ImageEntryType;
import com.pairoo.domain.enums.PartnerType;
import com.pairoo.domain.search.UserSearchResult;
import com.pairoo.frontend.webapp.wicket.components.ContextImageConstants;
import com.pairoo.frontend.webapp.wicket.components.OnlineLabel;
import com.pairoo.frontend.webapp.wicket.components.ProfileImage;
import com.pairoo.frontend.webapp.wicket.panels.BasePanel;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.EnumLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

/**
 *
 * @author ralf
 */
public class VisitSearchResultPanel extends BasePanel {

    public VisitSearchResultPanel(final String id, final IModel<UserSearchResult> model) {
        super(id, new CompoundPropertyModel<>(model));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        final UserSearchResult userSearchResult = (UserSearchResult) getDefaultModelObject();

        add(premiumContainer("premiumContainer", userSearchResult));
        add(profileImage("profileImage", userSearchResult));
        add(onlineStatusLabel("onlineLabel", userSearchResult));
        add(new Label("username"));
        add(new Label("age"));
        add(familyStatusLabel("familyStatusType"));
        add(new Label("geoLocation.zipcode"));
    }

    private Component premiumContainer(final String id, UserSearchResult result) {
        WebMarkupContainer c = new WebMarkupContainer(id);
        c.setVisible(result.isPremium());
        return c;
    }

    private OnlineLabel onlineStatusLabel(String id, UserSearchResult result) {
        return new OnlineLabel(id, result.isOnline());
    }

    private EnumLabel<FamilyStatusType> familyStatusLabel(String id) {
        return new EnumLabel<>(id);
    }

    private Component profileImage(String id, UserSearchResult result) {
        final ProfileImage profileImage = new ProfileImage(id, result.getProfileImageEntry(),
                ImageEntryType.MIDDLE);
        Component image = profileImage;
        if (!profileImage.exists() || !profileImage.isVisible()) {
            if (PartnerType.FEMALE.equals(result.getPartnerType())) {
                image = new ContextImage(id, ContextImageConstants.GENDER_FEMALE);
            } else if (PartnerType.MALE.equals(result.getPartnerType())) {
                image = new ContextImage(id, ContextImageConstants.GENDER_MALE);
            } else {
                image = new ContextImage(id, ContextImageConstants.GENDER_UNKNOWN);
            }
        }
        return image;
    }
}
