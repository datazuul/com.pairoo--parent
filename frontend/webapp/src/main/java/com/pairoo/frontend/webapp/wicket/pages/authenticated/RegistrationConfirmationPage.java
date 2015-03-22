package com.pairoo.frontend.webapp.wicket.pages.authenticated;

import com.datazuul.framework.webapp.wicket.model.LoadableDetachableDomainObjectModel;
import com.pairoo.domain.User;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;

/**
 * @author Ralf Eichinger
 */
public class RegistrationConfirmationPage extends AuthenticatedWebPage {

    private static final long serialVersionUID = 1L;

    public RegistrationConfirmationPage(final IModel<User> model) {
        super(model);
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        logEnter(RegistrationConfirmationPage.class);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        IModel<User> model = (IModel<User>) getDefaultModel();

        add(linkToMembershipPage("lnkMembershipPage", model));
        add(linkToSuggestionsPage("lnkSuggestionsPage", model));
    }

    private Link<User> linkToSuggestionsPage(String id, IModel<User> model) {
        Link<User> link = new Link<User>(id, model) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                User user = (User) getDefaultModelObject();
                setResponsePage(new SuggestionsPage(new LoadableDetachableDomainObjectModel<Long>(user, userService)));
            }
        };
        return link;
    }

    private Link<User> linkToMembershipPage(String id, IModel<User> model) {
        return new Link<User>(id, model) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                User user = (User) getDefaultModelObject();
                setResponsePage(new MembershipPage(new LoadableDetachableDomainObjectModel<Long>(user, userService)));
            }
        };
    }
}
