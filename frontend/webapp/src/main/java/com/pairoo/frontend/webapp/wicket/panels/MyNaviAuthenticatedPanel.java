package com.pairoo.frontend.webapp.wicket.panels;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import com.pairoo.domain.User;
import com.pairoo.frontend.webapp.wicket.pages.LogoutPage;
import java.util.Locale;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * @author Ralf Eichinger
 */
public class MyNaviAuthenticatedPanel extends Panel {

    private static final long serialVersionUID = 1L;

    public MyNaviAuthenticatedPanel(final String id, IModel<User> model) {
        super(id, new CompoundPropertyModel<User>(model));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(usernameLabel("userAccount.username"));
        add(logoutLink("logout"));
    }

    private Link<Void> logoutLink(String id) {
        return new Link<Void>(id) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                getSession().invalidate();
                Locale locale = getLocale();
                if (locale != null) {
                    PageParameters params = new PageParameters();
                    params.add("pl", locale.getLanguage());
                    setResponsePage(LogoutPage.class, params);
                } else {
                    setResponsePage(LogoutPage.class);
                }
            }
        };
    }

    private Label usernameLabel(String id) {
        return new Label(id);
    }

}
