package com.pairoo.frontend.webapp.wicket.pages;

import org.junit.Before;
import org.junit.Test;

import com.pairoo.frontend.webapp.wicket.AbstractWicketTest;
import com.pairoo.frontend.webapp.wicket.panels.MyNaviNotAuthenticatedPanel;

public class LoginPageTest extends AbstractWicketTest {

    @Before
    @Override
    public void before() throws Exception {
        super.before();
    }

    @Test
    public void testRendering() {
        getTester().startPage(LoginPage.class);
        getTester().assertRenderedPage(LoginPage.class);
        getTester().assertComponent("loginPanel", MyNaviNotAuthenticatedPanel.class);
    }
}
