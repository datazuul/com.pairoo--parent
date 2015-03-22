package com.pairoo.frontend.webapp.wicket.panels;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datazuul.framework.webapp.wicket.model.LoadableDetachableDomainObjectModel;
import com.pairoo.business.api.UserService;
import com.pairoo.domain.User;
import com.pairoo.frontend.webapp.wicket.WicketWebSession;
import com.pairoo.frontend.webapp.wicket.components.PasswordFormComponent;
import com.pairoo.frontend.webapp.wicket.components.UsernameFormComponent;
import com.pairoo.frontend.webapp.wicket.pages.PasswordForgottenPage;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.MyHomePage;

/**
 * @author Ralf Eichinger
 */
public class MyNaviNotAuthenticatedPanel extends BasePanel {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyNaviNotAuthenticatedPanel.class);
    private static final long serialVersionUID = 1L;
    @SpringBean(name = UserService.BEAN_ID)
    private UserService userService;
    private boolean includeRememberMe;

    /**
     * @param id See Component constructor
     * @param model model
     * @param includeRememberMe True if form should include a remember-me checkbox
     * @see org.apache.wicket.Component#Component(String)
     */
    public MyNaviNotAuthenticatedPanel(final String id, IModel<User> model, final boolean includeRememberMe) {
        super(id, model);
        this.includeRememberMe = includeRememberMe;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        // form
        final Form<User> form = createForm("form", Model.of(new User()));
        add(form);

        // useraccount: username
        final FormComponent<String> username = createUsernameFormComponent("userAccount.username", form);
        form.add(username);
//        form.add(labelForFormComponent("lblUsername", username));

        // useraccount: password
        final FormComponent<String> password = new PasswordFormComponent("userAccount.password");
        form.add(password);
//        form.add(labelForFormComponent("lblPassword", password));

        // remember me
//        form.add(createRememberMeFormComponent(includeRememberMe));

        // password forgotten link
        form.add(createPasswordForgottenLink("lnkPasswordForgotten"));
    }

    private Link<Void> createPasswordForgottenLink(String id) {
        final BookmarkablePageLink<Void> lnkPasswordForgotten = new BookmarkablePageLink<Void>(id,
                PasswordForgottenPage.class);
        return lnkPasswordForgotten;
    }

    private WebMarkupContainer createRememberMeFormComponent(final boolean includeRememberMe) {
        // MarkupContainer row for remember me checkbox
        final WebMarkupContainer rememberMeContainer = new WebMarkupContainer("rememberMeContainer");
        // Add rememberMe checkbox
        rememberMeContainer.add(new CheckBox("rememberMe", new PropertyModel<Boolean>(MyNaviNotAuthenticatedPanel.this,
                "rememberMe")));
        // Make form values persistent
        // FIXME we removed that feature and introduced
        // IAuthenticationStrategy. A default implementation has been
        // provided and the examples have been updated. As usual it gets
        // registered with ISecuritySettings.
        // setPersistent(rememberMe);
        // Show remember me checkbox?
        rememberMeContainer.setVisible(includeRememberMe);
        return rememberMeContainer;
    }

    private Form<User> createForm(String id, IModel<User> model) {
        final Form<User> form = new Form<User>(id, new CompoundPropertyModel<User>(model)) {
            private static final long serialVersionUID = 1L;

            /**
             * @see org.apache.wicket.markup.html.form.Form#onSubmit()
             */
            @Override
            public final void onSubmit() {
                User userData = (User) getDefaultModelObject();
                WicketWebSession session = (WicketWebSession) getSession();
                User sessionUser = session.login(userData.getUserAccount().getUsername(), userData.getUserAccount().getPassword());
                if (sessionUser != null) {
                    // If login has been called because the user was not yet
                    // logged in, than continue to the original destination,
                    // otherwise to the user's Home page
                    continueToOriginalDestination();
                    // otherwise:
                    IModel<User> model = new LoadableDetachableDomainObjectModel<Long>(sessionUser, userService);
                    setResponsePage(new MyHomePage(model));
                } else {
                    // Try the component based localizer first. If not found try
                    // the
                    // application localizer. Else use the default
                    final String errmsg = getString("error.login"); // , this,
                    // "Unable to sign you in");

                    error(errmsg);
                }
            }
        };
        return form;
    }

    private FormComponent<String> createUsernameFormComponent(String id, final Form<User> form) {
        FormComponent<String> fc = new UsernameFormComponent(id);
        // ... behaviour
        fc.add(new AttributeModifier("placeholder", new StringResourceModel("usernamePlaceholderText",
                MyNaviNotAuthenticatedPanel.this, null)));
        return fc;
    }
}
