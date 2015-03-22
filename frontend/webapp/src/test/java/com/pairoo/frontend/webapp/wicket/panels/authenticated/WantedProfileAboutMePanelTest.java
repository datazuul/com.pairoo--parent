package com.pairoo.frontend.webapp.wicket.panels.authenticated;

import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.DummyPanelPage;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import com.datazuul.framework.domain.geo.GeoLocation;
import com.pairoo.business.api.PersonProfileService;
import com.pairoo.business.services.impl.PersonProfileServiceImpl;
import com.pairoo.domain.User;
import com.pairoo.frontend.webapp.wicket.AbstractWicketTest;

public class WantedProfileAboutMePanelTest extends AbstractWicketTest {
    User mockUser = new User();
    
    @Before
    public void before() throws Exception {
	super.before();
	getAppContext().putBean(PersonProfileService.BEAN_ID, new PersonProfileServiceImpl());
	
	GeoLocation geoLocation = new GeoLocation();
	geoLocation.setName("Munich");
	mockUser.getUserProfile().setGeoLocation(geoLocation);
    }

    @Test
    public void testPanel() {
	WicketTester tester = getTester();

	WantedProfileAboutMePanel panel = new WantedProfileAboutMePanel(DummyPanelPage.TEST_PANEL_ID, new Model<User>(
		mockUser));
	tester.startComponentInPage(panel, null);

	tester.dumpPage();
    }
}
