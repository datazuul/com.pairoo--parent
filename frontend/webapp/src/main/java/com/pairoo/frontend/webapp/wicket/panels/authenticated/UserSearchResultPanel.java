package com.pairoo.frontend.webapp.wicket.panels.authenticated;

import com.datazuul.framework.webapp.wicket.model.LoadableDetachableDomainObjectModel;
import com.pairoo.frontend.webapp.wicket.panels.*;

import org.apache.wicket.Component;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.EnumLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import com.pairoo.business.api.GeoLocationService;
import com.pairoo.business.api.PersonProfileService;
import com.pairoo.business.api.UserAccountService;
import com.pairoo.business.api.UserService;
import com.pairoo.domain.User;
import com.pairoo.domain.enums.FamilyStatusType;
import com.pairoo.domain.enums.ImageEntryType;
import com.pairoo.domain.enums.NumberOfKidsType;
import com.pairoo.domain.enums.PartnerType;
import com.pairoo.domain.enums.PartnershipType;
import com.pairoo.domain.search.UserSearchResult;
import com.pairoo.frontend.webapp.wicket.components.ContextImageConstants;
import com.pairoo.frontend.webapp.wicket.components.HeightLabel;
import com.pairoo.frontend.webapp.wicket.components.OnlineLabel;
import com.pairoo.frontend.webapp.wicket.components.ProfileImage;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.ProfileDetailsPage;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;

/**
 * @author Ralf Eichinger
 */
public abstract class UserSearchResultPanel extends BasePanel {

    private static final long serialVersionUID = 1L;
    @SpringBean(name = GeoLocationService.BEAN_ID)
    private GeoLocationService geoLocationService;
    @SpringBean(name = PersonProfileService.BEAN_ID)
    private PersonProfileService personProfileService;
    @SpringBean(name = UserAccountService.BEAN_ID)
    private UserAccountService userAccountService;
    @SpringBean(name = UserService.BEAN_ID)
    private UserService userService;
    private final Long idResultUser;

    public UserSearchResultPanel(final String id, final IModel<UserSearchResult> model, final Long idResultUser) {
        super(id, new CompoundPropertyModel<>(model));
        this.idResultUser = idResultUser;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final UserSearchResult userSearchResult = (UserSearchResult) getDefaultModelObject();

        // premium container
        add(premiumContainer("premiumContainer", userSearchResult));
        add(profileImage("profileImage", userSearchResult));

        final Link<Void> detailAction = detailAction("detailAction", userSearchResult);
        add(detailAction);
        detailAction.add(new Label("username"));

        // just distracts from first facts
        // // motto
        // add(new Label("userProfile.motto"));

	final Label age = new Label("age");
	boolean missingBirthdate = (userSearchResult.getAge() <= 0);
	if (missingBirthdate) {
	    age.setDefaultModel(Model.of("&nbsp;--&nbsp;"));
	    age.setEscapeModelStrings(false);
	    age.add(new AttributeAppender("class", "missingBirthdate"));
	}
        add(age);
        add(heightLabel("lblHeight", userSearchResult));

        // weight
        // no longer available on business decision
        // add(createWeightLabel(userProfile));

        add(new EnumLabel("geoLocation.country"));
        add(new Label("geoLocation.zipcode"));
        add(new Label("geoLocation.name"));
        add(new Label("distance"));

        // should match...
        // // country
        // add(new Label("country", new
        // ResourceModel(EnumUtils.getEnumKey(user.getUserProfile().getGeoLocation().getCountry())));
        //
        // // city
        // add(new Label("userProfile.geoLocation.city.name"));

        add(lastLoginLabel("lastLogin"));
        add(onlineStatusLabel("onlineLabel", userSearchResult));
        add(familyStatusLabel("familyStatusType"));
        add(numberOfKidsTypeLabel("numberOfKidsType"));

        // should match...
        // // partner type
        // add(new Label("partnerType", new ResourceModel("searchFor_"
        // + EnumUtils.getEnumKey(user.getSearchProfile().getPartnerType()))));

        add(partnershipTypeLabel("searchesPartnershipType"));

        // profession
        // add(new Label("userProfile.profession"));

        add(smokeTypeLabel("smokeType"));
        add(ethnicityLabel("ethnicity"));
    }

    private Link<Void> detailAction(String id, final UserSearchResult userSearchResult) {
        final Link<Void> detailAction = new Link<Void>(id) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                final ProfileDetailsPage nextPage = new ProfileDetailsPage(
                        new LoadableDetachableDomainObjectModel<Long>(User.class, idResultUser, userService),
                        new LoadableDetachableDomainObjectModel<Long>((User) getPage().getDefaultModelObject(), userService)) {
                    private static final long serialVersionUID = 1L;

                    @Override
                    protected void onGoBack() {
                        setResponsePage(getResponsePage());
                    }
                };
                setResponsePage(nextPage);
            }
        };
        return detailAction;
    }

    protected abstract WebPage getResponsePage();

    protected Component premiumContainer(final String id, UserSearchResult result) {
        WebMarkupContainer c = new WebMarkupContainer(id);
        c.setVisible(result.isPremium());
        return c;
    }

    private Component smokeTypeLabel(String id) {
        return new EnumLabel<>(id);
    }

    private HeightLabel heightLabel(String id, final UserSearchResult userSearchResult) {
        int height = -1;
        try {
            Integer heightObj = userSearchResult.getHeight();
            if (heightObj != null) {
                height = heightObj.intValue();
            }
        } catch (Exception e) {
        }
        return new HeightLabel(id, getUsersCountry().getUnitLength(), height);
    }

    private Component ethnicityLabel(String id) {
        return new EnumLabel<>(id);
    }

    private EnumLabel<PartnershipType> partnershipTypeLabel(String id) {
        return new EnumLabel<>(id);
    }

    private EnumLabel<NumberOfKidsType> numberOfKidsTypeLabel(String id) {
        return new EnumLabel<>(id);
    }

    protected OnlineLabel onlineStatusLabel(String id, UserSearchResult result) {
        return new OnlineLabel(id, result.isOnline());
    }

    protected EnumLabel<FamilyStatusType> familyStatusLabel(String id) {
        return new EnumLabel<>(id);
    }

    private DateLabel lastLoginLabel(String id) {
        final DateLabel lastLoginDate = DateLabel.forDatePattern(id, "dd-MMM-yy HH:mm"); // a");
        return lastLoginDate;
    }

    protected Component profileImage(String id, UserSearchResult result) {
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
