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
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.datazuul.framework.domain.geo.GeoLocation;
import com.datazuul.framework.webapp.wicket.components.form.EnumCheckGroupPanel;
import com.datazuul.framework.webapp.wicket.components.form.ShinyForm;
import com.datazuul.framework.webapp.wicket.model.LoadableDetachableDomainObjectModel;
import com.pairoo.business.api.GeoLocationService;
import com.pairoo.business.api.PersonProfileService;
import com.pairoo.business.api.UserService;
import com.pairoo.domain.SearchProfile;
import com.pairoo.domain.User;
import com.pairoo.domain.enums.Ethnicity;
import com.pairoo.domain.enums.PartnerType;
import com.pairoo.domain.enums.ProfilePictureType;
import com.pairoo.domain.enums.SmokeType;
import com.pairoo.domain.geo.GeoArea;
import com.pairoo.frontend.webapp.wicket.WicketWebSession;
import com.pairoo.frontend.webapp.wicket.components.GeoAreaFormComponentPanel;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.SearchResultsPage;

/**
 * @author Ralf Eichinger
 */
public class BasicSearchPanel extends BasePanel {

    private static final long serialVersionUID = 1L;
    @SpringBean(name = GeoLocationService.BEAN_ID)
    private GeoLocationService geoLocationService;
    @SpringBean(name = UserService.BEAN_ID)
    private UserService userService;
    @SpringBean(name = PersonProfileService.BEAN_ID)
    private PersonProfileService personProfileService;

    public BasicSearchPanel(final String id, final IModel<User> model) {
        super(id, new CompoundPropertyModel<User>(model));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final SearchProfile searchProfile = getSearchProfile((IModel<User>) getDefaultModel());

        // form
        final ShinyForm form = createForm(new CompoundPropertyModel<SearchProfile>(searchProfile));
        add(form);

        // ---------------- searchprofile: partnertype
        final FormComponent<PartnerType> searchProfilePartnerTypeSelection = createPartnerTypeFormComponent();
        form.add(searchProfilePartnerTypeSelection);
        form.add(labelForFormComponent("searchProfile.partnerType.label", searchProfilePartnerTypeSelection));

        // ---------------- search profile: min age
        final FormComponent<Integer> searchProfileMinAgeSelection = createMinAgeFormComponent();
        form.add(searchProfileMinAgeSelection);
        form.add(labelForFormComponent("searchProfile.minAge.label", searchProfileMinAgeSelection));

        // ---------------- search profile: max age
        final FormComponent<Integer> searchProfileMaxAgeSelection = createMaxAgeSelection();
        form.add(searchProfileMaxAgeSelection);
        form.add(labelForFormComponent("searchProfile.maxAge.label", searchProfileMaxAgeSelection));

        // ---------------- search profile: geo area
        final FormComponentPanel<GeoArea> searchProfileGeoArea = new GeoAreaFormComponentPanel("geoArea",
                new PropertyModel<GeoArea>(searchProfile, "geoArea"));
        searchProfileGeoArea.setLabel(new ResourceModel("searchProfile.geoArea.label"));
        form.add(searchProfileGeoArea);
        form.add(labelForFormComponent("searchProfile.geoArea.label", searchProfileGeoArea));

        // ---------------- search profile: smoke type
        final FormComponent<SmokeType> searchProfileSmokeTypeFormComponent = createSearchProfileSmokeTypeFormComponent();
        form.add(searchProfileSmokeTypeFormComponent);
        form.add(labelForFormComponent("searchProfile.smokeType.label", searchProfileSmokeTypeFormComponent));

        // ---------------- search profile: smoke type
        final FormComponent<ProfilePictureType> searchProfileProfilePictureTypeFormComponent = createSearchProfileProfilePictureTypeFormComponent();
        form.add(searchProfileProfilePictureTypeFormComponent);
        form.add(labelForFormComponent("searchProfile.profilePictureType.label",
                searchProfileProfilePictureTypeFormComponent));

        // ---------------- search profile: ethnicity
        final FormComponent<Ethnicity> searchProfileEthnicitySelection = createEthnicityFormComponent((CompoundPropertyModel<SearchProfile>) form
                .getDefaultModel());
        form.add(searchProfileEthnicitySelection);
        form.add(labelForFormComponent("searchProfile.ethnicities.label", searchProfileEthnicitySelection));
    }

    private SearchProfile getSearchProfile(final IModel<User> model) {
        final WicketWebSession session = (WicketWebSession) getSession();
        SearchProfile searchProfile = session.getSearchProfile();
        if (searchProfile == null) {
            final User user = model.getObject();
            searchProfile = new SearchProfile();
            // partnertype
            searchProfile.setPartnerType(user.getSearchProfile().getPartnerType());
            // min age
            searchProfile.setMinAge(user.getSearchProfile().getMinAge());
            // max age
            searchProfile.setMaxAge(user.getSearchProfile().getMaxAge());

            // geo area
            final GeoArea geoAreaSuggestions = user.getSearchProfile().getGeoArea();
            final GeoArea geoArea = new GeoArea();

            // country
            geoArea.setCountry(geoAreaSuggestions.getCountry());

            // max distance
            geoArea.setMaxDistance(geoAreaSuggestions.getMaxDistance());

            // zipcode starts with
            geoArea.setZipcodeStartsWith(geoAreaSuggestions.getZipcodeStartsWith());

            // geo location
            final GeoLocation geoLocationSuggestions = geoAreaSuggestions.getGeoLocation();
            GeoLocation geoLocation = null;
            if (geoLocationSuggestions != null) {
                geoLocation = new GeoLocation();
                geoLocation.setCountry(geoLocationSuggestions.getCountry());
                geoLocation.setZipcode(geoLocationSuggestions.getZipcode());
            }
            geoArea.setGeoLocation(geoLocation);

            // country not set, yet? (is used as target for dropdown...)
            if (geoArea.getCountry() == null && geoLocation != null) {
                geoArea.setCountry(geoLocation.getCountry());
            }
            if (geoArea.getCountry() == null) {
                geoArea.setCountry(user.getUserProfile().getGeoLocation().getCountry());
            }
            searchProfile.setGeoArea(geoArea);

            // smoke type
            searchProfile.setSmokeType(user.getSearchProfile().getSmokeType());

            // profile picture type
            searchProfile.setProfilePictureType(user.getSearchProfile().getProfilePictureType());

            // ethnicity
            final List<Ethnicity> ethnicities = new ArrayList<Ethnicity>(user.getSearchProfile().getEthnicities());
            searchProfile.setEthnicities(ethnicities);

            session.setSearchProfile(searchProfile);
        }
        return searchProfile;
    }

    private FormComponent<Ethnicity> createEthnicityFormComponent(final CompoundPropertyModel<SearchProfile> model) {
        // ... values
        final Ethnicity[] ethnicityValues = Ethnicity.values();
        final ArrayList<Ethnicity> ethnicities = new ArrayList<Ethnicity>(Arrays.asList(ethnicityValues));
        ethnicities.remove(Ethnicity.DONT_SAY);
        // ... component
        final FormComponent<Ethnicity> searchProfileEthnicitySelection = new EnumCheckGroupPanel<Ethnicity>(
                "ethnicities", model.bind("ethnicities"), ethnicities, Ethnicity.ANY);
        searchProfileEthnicitySelection.setLabel(new ResourceModel("searchProfile.ethnicities.label"));
        // ... validation
        // searchProfileEthnicitySelection.setRequired(true);
        return searchProfileEthnicitySelection;
    }

    private FormComponent<ProfilePictureType> createSearchProfileProfilePictureTypeFormComponent() {
        // ... values
        final ProfilePictureType[] enumValues = ProfilePictureType.values();
        final ArrayList<ProfilePictureType> enumTypes = new ArrayList<ProfilePictureType>(Arrays.asList(enumValues));
        enumTypes.remove(ProfilePictureType.WITHOUT_PICTURES); // TODO search without pictures returns as many results as pictures found
        // ... field
        final DropDownChoice<ProfilePictureType> formComponent = new DropDownChoice<ProfilePictureType>(
                "profilePictureType", enumTypes, new EnumChoiceRenderer<ProfilePictureType>(this));
        formComponent.setLabel(new ResourceModel("profilePictureType"));
        formComponent.setEscapeModelStrings(false);

        // ... validation
        formComponent.setRequired(true);
        return formComponent;
    }

    private FormComponent<SmokeType> createSearchProfileSmokeTypeFormComponent() {
        // ... values
        final SmokeType[] smokeTypeValues = SmokeType.values();
        final ArrayList<SmokeType> smokeTypes = new ArrayList<SmokeType>(Arrays.asList(smokeTypeValues));
        smokeTypes.remove(SmokeType.DONT_SAY);

        // ... field
        final DropDownChoice<SmokeType> searchProfileSmokeTypeSelection = new DropDownChoice<SmokeType>("smokeType",
                smokeTypes, new EnumChoiceRenderer<SmokeType>(this));
        searchProfileSmokeTypeSelection.setLabel(new ResourceModel("smoke"));
        searchProfileSmokeTypeSelection.setEscapeModelStrings(false);

        // ... validation
        searchProfileSmokeTypeSelection.setRequired(true);
        return searchProfileSmokeTypeSelection;
    }

    private FormComponent<Integer> createMaxAgeSelection() {
        // ... values
        final ArrayList<Integer> maxAgeValues = new ArrayList<Integer>();
        for (int age = personProfileService.getMinAge(); age <= personProfileService.getMaxAge(); age++) {
            maxAgeValues.add(age);
        }
        // ... field
        final DropDownChoice<Integer> searchProfileMaxAgeSelection = new DropDownChoice<Integer>("maxAge", maxAgeValues);
        searchProfileMaxAgeSelection.setLabel(new ResourceModel("searchProfile.maxAge.label"));
        searchProfileMaxAgeSelection.setEscapeModelStrings(false);
        // ... validation
        searchProfileMaxAgeSelection.setRequired(true);
        return searchProfileMaxAgeSelection;
    }

    private FormComponent<Integer> createMinAgeFormComponent() {
        // ... values
        final ArrayList<Integer> minAgeValues = new ArrayList<Integer>();
        for (int age = personProfileService.getMinAge(); age <= personProfileService.getMaxAge(); age++) {
            minAgeValues.add(age);
        }
        // ... field
        final DropDownChoice<Integer> searchProfileMinAgeSelection = new DropDownChoice<Integer>("minAge", minAgeValues);
        searchProfileMinAgeSelection.setLabel(new ResourceModel("searchProfile.minAge.label"));
        searchProfileMinAgeSelection.setEscapeModelStrings(false);
        // ... validation
        searchProfileMinAgeSelection.setRequired(true);
        return searchProfileMinAgeSelection;
    }

    private FormComponent<PartnerType> createPartnerTypeFormComponent() {
        // ... values
        final PartnerType[] partnerTypeValues = PartnerType.values();
        final List<PartnerType> partnerTypes = new ArrayList<PartnerType>(Arrays.asList(partnerTypeValues));
        // partnerTypes.remove(PartnerType.DONT_SAY);
        // ... field
        final DropDownChoice<PartnerType> searchProfilePartnerTypeSelection = new DropDownChoice<PartnerType>(
                "partnerType", partnerTypes, new EnumChoiceRenderer<PartnerType>(this));
        searchProfilePartnerTypeSelection.setLabel(new ResourceModel("searchProfile.partnerType.label"));
        searchProfilePartnerTypeSelection.setEscapeModelStrings(false);
        // ... validation
        searchProfilePartnerTypeSelection.setRequired(true);
        return searchProfilePartnerTypeSelection;
    }

    private ShinyForm createForm(final CompoundPropertyModel<SearchProfile> compoundPropertyModel) {
        final ShinyForm form = new ShinyForm("form", compoundPropertyModel) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                User user = (User) BasicSearchPanel.this.getDefaultModelObject();
                final SearchProfile searchProfile = (SearchProfile) getDefaultModelObject();
                final WicketWebSession session = (WicketWebSession) getSession();
                session.setSearchProfile(searchProfile);

                // show search results page
                final SearchResultsPage nextPage = new SearchResultsPage((IModel<User>) new LoadableDetachableDomainObjectModel<Long>(
                        user, userService), searchProfile);
                setResponsePage(nextPage);
            }
        };
        return form;
    }
}
