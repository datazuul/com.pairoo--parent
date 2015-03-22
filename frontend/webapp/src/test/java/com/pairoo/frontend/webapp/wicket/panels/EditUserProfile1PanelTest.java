package com.pairoo.frontend.webapp.wicket.panels;

import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.DummyPanelPage;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import com.pairoo.business.api.CountryService;
import com.pairoo.business.api.PersonProfileService;
import com.pairoo.business.services.impl.CountryServiceImpl;
import com.pairoo.business.services.impl.PersonProfileServiceImpl;
import com.pairoo.domain.User;
import com.pairoo.frontend.webapp.wicket.AbstractWicketTest;

public class EditUserProfile1PanelTest extends AbstractWicketTest {
    @Before
    public void before() throws Exception {
	super.before();
	getAppContext().putBean(CountryService.BEAN_ID, new CountryServiceImpl());
	getAppContext().putBean(PersonProfileService.BEAN_ID, new PersonProfileServiceImpl());
    }

    @Test
    public void testPanel() {
	WicketTester tester = getTester();

	User user = new User();
	EditUserProfile1Panel panel = new EditUserProfile1Panel(DummyPanelPage.TEST_PANEL_ID, new Model<User>(user),
		true);
	tester.startComponentInPage(panel, null);

	tester.dumpPage();
    }
}
