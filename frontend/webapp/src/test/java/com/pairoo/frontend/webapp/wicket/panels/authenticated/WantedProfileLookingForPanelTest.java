package com.pairoo.frontend.webapp.wicket.panels.authenticated;

import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.DummyPanelPage;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;

import com.pairoo.domain.User;
import com.pairoo.frontend.webapp.wicket.AbstractWicketTest;

public class WantedProfileLookingForPanelTest extends AbstractWicketTest {
    User mockUser = new User();

    @Test
    public void testPanel() {
	WicketTester tester = getTester();

	WantedProfileLookingForPanel panel = new WantedProfileLookingForPanel(DummyPanelPage.TEST_PANEL_ID,
		new Model<User>(mockUser));
	tester.startComponentInPage(panel, null);

	tester.dumpPage();
    }
}
