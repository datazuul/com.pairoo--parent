package com.pairoo.frontend.webapp.wicket.pages;

import com.pairoo.domain.User;
import com.pairoo.frontend.webapp.wicket.panels.MyNaviNotAuthenticatedPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * @author Ralf Eichinger
 */
public class LoginPage extends NotAuthenticatedWebPage {

    private static final long serialVersionUID = 1L;

    public LoginPage() {
        super();
    }

    public LoginPage(PageParameters params) {
        super(params);
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        logEnter(LoginPage.class);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        myNaviPanel.setVisible(false);

        add(loginPanel("loginPanel"));
    }

    private MyNaviNotAuthenticatedPanel loginPanel(String id) {
        return new LoginPanel(id, null, false);
    }

    class LoginPanel extends MyNaviNotAuthenticatedPanel {

        public LoginPanel(String id, IModel<User> model, boolean includeRememberMe) {
            super(id, model, includeRememberMe);
        }
    }
}
