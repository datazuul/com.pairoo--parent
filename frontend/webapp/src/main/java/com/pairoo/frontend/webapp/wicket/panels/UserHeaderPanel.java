package com.pairoo.frontend.webapp.wicket.panels;

import javax.measure.quantity.Length;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import org.apache.wicket.Component;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.EnumLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.jscience.physics.amount.Amount;

import com.datazuul.framework.domain.geo.GeoLocation;
import com.pairoo.business.api.GeoLocationService;
import com.pairoo.business.api.UserAccountService;
import com.pairoo.domain.PersonProfile;
import com.pairoo.domain.User;
import com.pairoo.domain.UserAccount;
import com.pairoo.domain.UserProfile;
import com.pairoo.domain.enums.Ethnicity;
import com.pairoo.domain.enums.FamilyStatusType;
import com.pairoo.domain.enums.ImageEntryType;
import com.pairoo.domain.enums.NumberOfKidsType;
import com.pairoo.domain.enums.PartnerType;
import com.pairoo.domain.enums.PartnershipType;
import com.pairoo.domain.enums.SmokeType;
import com.pairoo.frontend.webapp.wicket.WicketWebSession;
import com.pairoo.frontend.webapp.wicket.components.ContextImageConstants;
import com.pairoo.frontend.webapp.wicket.components.HeightLabel;
import com.pairoo.frontend.webapp.wicket.components.OnlineLabel;
import com.pairoo.frontend.webapp.wicket.components.ProfileImage;
import com.pairoo.frontend.webapp.wicket.components.WeightLabel;

/**
 * @author Ralf Eichinger
 */
public class UserHeaderPanel extends BasePanel {

    private static final long serialVersionUID = 1L;
    @SpringBean(name = GeoLocationService.BEAN_ID)
    private GeoLocationService geoLocationService;
    @SpringBean(name = UserAccountService.BEAN_ID)
    private UserAccountService userAccountService;

    public UserHeaderPanel(final String id, final IModel<User> model) {
        super(id, new CompoundPropertyModel<User>(model));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final User user = (User) getDefaultModelObject();
        final UserAccount userAccount = user.getUserAccount();
        final UserProfile userProfile = user.getUserProfile();

        // premium container
        add(premiumContainer("premiumContainer", userAccount));
        add(profileImage("profileImage", userProfile));
        add(new Label("userAccount.username"));

        // just distracts from first facts
        // // motto
        // add(new Label("userProfile.motto"));

        add(ageLabel("age"));
        add(heightLabel("lblHeight", userProfile));

        // weight
        // no longer available on business decision
        // add(createWeightLabel(userProfile));

        add(new EnumLabel("userProfile.geoLocation.country", userProfile.getGeoLocation().getCountry()));
        add(new Label("userProfile.geoLocation.zipcode"));
        add(new Label("userProfile.geoLocation.name"));
        add(distanceLabel("lblDistance"));

        // should match...
        // // country
        // add(new Label("country", new
        // ResourceModel(EnumUtils.getEnumKey(user.getUserProfile().getGeoLocation().getCountry())));
        //
        // // city
        // add(new Label("userProfile.geoLocation.city.name"));

        add(lastLoginLabel("userAccount.lastLogin"));
        add(onlineStatusLabel("onlineLabel", userAccount));
        add(familyStatusLabel("familyStatus", user));
        add(numberOfKidsTypeLabel("numberOfKids", user));

        // should match...
        // // partner type
        // add(new Label("partnerType", new ResourceModel("searchFor_"
        // + EnumUtils.getEnumKey(user.getSearchProfile().getPartnerType()))));

        add(partnershipTypeLabel("partnershipType", user));

        // profession
        // add(new Label("userProfile.profession"));

        add(smokeTypeLabel("userProfile.lifeStyle.smokeType"));
        add(ethnicityLabel("lblEthnicity", user));
    }

    private Component premiumContainer(final String id, final UserAccount userAccount) {
        WebMarkupContainer c = new WebMarkupContainer(id);
        c.setVisible(userAccountService.isPremiumMember(userAccount));
        return c;
    }

    private Component smokeTypeLabel(String id) {
        // final SmokeType smokeType =
        // user.getUserProfile().getLifeStyle().getSmokeType();
        return new EnumLabel<SmokeType>(id);
    }

    private WeightLabel createWeightLabel(final UserProfile userProfile) {
        int weight = -1;
        try {
            weight = userProfile.getAppearance().getWeight().intValue();
        } catch (Exception e) {
        }
        return new WeightLabel("lblWeight", getUsersCountry().getUnitMass(), weight);
    }

    private HeightLabel heightLabel(String id, final UserProfile userProfile) {
        int height = -1;
        try {
            height = userProfile.getAppearance().getHeight().intValue();
        } catch (Exception e) {
        }
        return new HeightLabel(id, getUsersCountry().getUnitLength(), height);
    }

    private Component ethnicityLabel(String id, final User user) {
        final Ethnicity ethnicity = user.getUserProfile().getAppearance().getEthnicity();
        return new EnumLabel<Ethnicity>(id, ethnicity);
    }

    private Component distanceLabel(String id) {
        final User sessionUser = ((WicketWebSession) getSession()).getUser();
        final GeoLocation geoLocation1 = sessionUser.getUserProfile().getGeoLocation();
        final User user = (User) getDefaultModelObject();
        final GeoLocation geoLocation2 = user.getUserProfile().getGeoLocation();
        final double distanceInMeter = geoLocationService.distance(geoLocation1.getLatitude(),
                geoLocation1.getLongitude(), geoLocation2.getLatitude(), geoLocation2.getLongitude());

        final long distanceKm = Math.round(new Double(distanceInMeter / 1000));
        Amount<Length> distanceInKm = Amount.valueOf(distanceKm, SI.KILOMETER);
        Unit<Length> preferredUnit = getUsersCountry().getUnitDistance();
        long distanceInPreferredUnit = Math.round(distanceInKm.doubleValue(preferredUnit));

        return new Label(id, String.valueOf(distanceInPreferredUnit) + " " + preferredUnit.toString());
    }

    private EnumLabel<PartnershipType> partnershipTypeLabel(String id, final User user) {
        return new EnumLabel<PartnershipType>(id, user.getSearchProfile().getPartnershipType());
    }

    private EnumLabel<NumberOfKidsType> numberOfKidsTypeLabel(String id, final User user) {
        return new EnumLabel<NumberOfKidsType>(id, user.getUserProfile().getNumberOfKidsType());
    }

    private OnlineLabel onlineStatusLabel(String id, final UserAccount userAccount) {
        return new OnlineLabel(id, new Model<UserAccount>(userAccount));
    }

    private EnumLabel<FamilyStatusType> familyStatusLabel(String id, final User user) {
        return new EnumLabel<FamilyStatusType>(id, user.getUserProfile().getFamilyStatus());
    }

    private DateLabel lastLoginLabel(String id) {
        final DateLabel lastLoginDate = DateLabel.forDatePattern(id, "dd-MMM-yy HH:mm"); // a");
        return lastLoginDate;
    }

    private Label ageLabel(String id) {
        final User user = (User) getDefaultModelObject();
        final int age = PersonProfile.getAge(user.getUserProfile().getBirthdate());
        String ageStr = "--";
        if (age != -1) {
            ageStr = String.valueOf(age);
        }
        return new Label(id, ageStr);
    }

    private Component profileImage(String id, final UserProfile userProfile) {
        final ProfileImage profileImage = new ProfileImage(id, new Model<UserProfile>(userProfile),
                ImageEntryType.MIDDLE);
        Component image = profileImage;
        if (!profileImage.exists() || !profileImage.isVisible()) {
            if (PartnerType.FEMALE.equals(userProfile.getPartnerType())) {
                image = new ContextImage(id, ContextImageConstants.GENDER_FEMALE);
            } else if (PartnerType.MALE.equals(userProfile.getPartnerType())) {
                image = new ContextImage(id, ContextImageConstants.GENDER_MALE);
            } else {
                image = new ContextImage(id, ContextImageConstants.GENDER_UNKNOWN);
            }
        }
        return image;
    }
}
