package com.pairoo.frontend.webapp.wicket.pages.authenticated;

import com.datazuul.framework.domain.geo.Country;
import com.datazuul.framework.domain.geo.GeoLocation;
import com.pairoo.business.api.CountryService;
import com.pairoo.business.api.PersonProfileService;
import com.pairoo.business.services.impl.CountryServiceImpl;
import com.pairoo.business.services.impl.PersonProfileServiceImpl;
import com.pairoo.domain.User;
import com.pairoo.domain.UserProfile;
import com.pairoo.domain.enums.PartnerType;
import com.pairoo.frontend.webapp.wicket.AbstractWicketTest;
import com.pairoo.frontend.webapp.wicket.WicketWebSession;
import javax.servlet.ServletException;
import org.apache.wicket.Page;
import org.apache.wicket.model.Model;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Ralf Eichinger
 */
public class RegistrationStep4PageTest extends AbstractWicketTest {

    final User mockUser = new User();

    @Before
    @Override
    public void before() throws Exception {
        super.before();

        GeoLocation geoLocation = new GeoLocation();
        geoLocation.setCountry(Country.GERMANY);

        UserProfile userProfile = new UserProfile();
        userProfile.setGeoLocation(geoLocation);
        userProfile.setPartnerType(PartnerType.FEMALE);
        mockUser.setUserProfile(userProfile);

        getAppContext().putBean(CountryService.BEAN_ID, new CountryServiceImpl());
        getAppContext().putBean(PersonProfileService.BEAN_ID, new PersonProfileServiceImpl());

        WicketWebSession session = (WicketWebSession) getTester().getSession();
        session.setUser(mockUser);
    }

    @Test
    public void testRendering() throws ServletException {
        final Page page = new RegistrationStep4Page(new Model<User>(mockUser));
        getTester().startPage(page);
        getTester().assertRenderedPage(RegistrationStep4Page.class);
    }
}
