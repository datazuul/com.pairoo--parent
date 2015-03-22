package com.pairoo.frontend.webapp.wicket.pages.authenticated;

import com.datazuul.framework.domain.geo.GeoLocation;
import com.pairoo.business.api.CountryService;
import com.pairoo.business.api.UserService;
import com.pairoo.business.services.impl.CountryServiceImpl;
import com.pairoo.domain.SearchProfile;
import com.pairoo.domain.User;
import com.pairoo.frontend.webapp.wicket.AbstractWicketTest;
import com.pairoo.frontend.webapp.wicket.WicketWebSession;
import javax.servlet.ServletException;
import org.apache.wicket.Page;
import org.apache.wicket.model.Model;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 * @author Ralf Eichinger
 */
public class RegistrationStep1PageTest extends AbstractWicketTest {

    final User mockUser = new User();

    @Before
    @Override
    public void before() throws Exception {
        super.before();

        // 1. setup dependencies and mock objects
        mockUser.setSearchProfile(new SearchProfile());
        mockUser.getSearchProfile().getGeoArea().setGeoLocation(new GeoLocation());

        final UserService userService = mock(UserService.class);
        when(userService.save(mockUser)).thenReturn(true);

        // 2. setup mock injection environment
        getAppContext().putBean(UserService.BEAN_ID, userService);
        // non mock (standalone) services
        getAppContext().putBean(CountryService.BEAN_ID, new CountryServiceImpl());


        // mock session
        final WicketWebSession session = ((WicketWebSession) getTester().getSession());
        session.setUser(mockUser);
    }

    @Test
    public void testRendering() throws ServletException {
        final Page page = new RegistrationStep1Page(new Model<User>(mockUser));
        getTester().startPage(page);
        getTester().assertRenderedPage(RegistrationStep1Page.class);
        // app.assertComponent("confirmForm", Form.class);
        // app.assertComponent("confirmForm:confirm", Button.class);
        // app.setParameterForNextRequest("confirmForm:confirm", "pressed");
        // app.submitForm("confirmForm");
        // app.assertRenderedPage(DummyHomePage.class);

        getTester().dumpPage();
    }
}
