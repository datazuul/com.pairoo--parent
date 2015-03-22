package com.pairoo.frontend.webapp.wicket.panels.authenticated;

import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.DummyPanelPage;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;

import com.pairoo.domain.Message;
import com.pairoo.frontend.webapp.wicket.AbstractWicketTest;

public class AnswerMessagePanelTest extends AbstractWicketTest {
    @Test
    public void testPanel() {
	WicketTester tester = getTester();

	Message message = new Message();
	AnswerMessagePanel panel = new AnswerMessagePanel(DummyPanelPage.TEST_PANEL_ID, new Model<Message>(message)) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    protected void onGoBack() {
		// TODO Auto-generated method stub
	    }
	};
	tester.startComponentInPage(panel, null);

	tester.dumpPage();
    }
}
