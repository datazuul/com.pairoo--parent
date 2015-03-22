package com.pairoo.frontend.webapp.admin.wicket.pages;

import com.pairoo.frontend.webapp.wicket.pages.BasePage;
import com.pairoo.frontend.webapp.wicket.pages.LoginPage;
import com.pairoo.frontend.webapp.wicket.panels.MyNaviNotAuthenticatedPanel;

/**
 * @author Ralf Eichinger
 */
public class LoginPage extends BasePage {
    private static final long serialVersionUID = 1L;

    public LoginPage() {
	super();

	myNaviPanel.setVisible(false);

	add(createLoginComponent());
    }

    private MyNaviNotAuthenticatedPanel createLoginComponent() {
	return new MyNaviNotAuthenticatedPanel("loginPanel", false);
    }

    @Override
    protected void onBeforeRender() {
	super.onBeforeRender();
	logEnter(LoginPage.class);
    }
}