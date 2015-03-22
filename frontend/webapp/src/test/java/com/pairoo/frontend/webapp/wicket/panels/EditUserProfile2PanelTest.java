package com.pairoo.frontend.webapp.wicket.panels;

import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.DummyPanelPage;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;

import com.pairoo.domain.User;
import com.pairoo.frontend.webapp.wicket.AbstractWicketTest;

public class EditUserProfile2PanelTest extends AbstractWicketTest {
    @Test
    public void testPanel() {
	WicketTester tester = getTester();

	User user = new User();
	EditUserProfile2Panel panel = new EditUserProfile2Panel(DummyPanelPage.TEST_PANEL_ID, new Model<User>(user));
	tester.startComponentInPage(panel, null);

	tester.dumpPage();
    }
}
