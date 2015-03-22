package com.pairoo.frontend.webapp.wicket.pages;

import com.datazuul.framework.domain.Language;
import com.datazuul.framework.webapp.wicket.components.form.ShinyForm;
import com.pairoo.business.api.UserAccountService;
import com.pairoo.domain.User;
import com.pairoo.domain.UserAccount;
import java.util.Locale;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

public class PasswordForgottenPage extends NotAuthenticatedWebPage {

    private static final long serialVersionUID = 1L;
    @SpringBean(name = UserAccountService.BEAN_ID)
    private UserAccountService userAccountService;

    public PasswordForgottenPage() {
        super();
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        logEnter(PasswordForgottenPage.class);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        // ---------------- form
        final ShinyForm form = createForm("form");
        add(form);

        // ---------------- useraccount: username/pseudonym
        final FormComponent<String> usernameField = createUsernameFormComponent("userAccount.username");
        form.add(usernameField);
        form.add(createFormComponentLabel("usernameFieldLabel", usernameField));

        // ---------------- user: email
        final FormComponent<String> emailField = createEmailFormComponent("email");
        form.add(emailField);
        form.add(createFormComponentLabel("emailFieldLabel", emailField));
    }

    private FormComponent<String> createEmailFormComponent(String id) {
        final RequiredTextField<String> emailField = new RequiredTextField<String>(id);
        emailField.setLabel(new ResourceModel("emailAddress"));
        // ... validation
        emailField.add(new AttributeModifier("maxlength", "255"));
        emailField.add(StringValidator.lengthBetween(6, 255));
        emailField.add(EmailAddressValidator.getInstance());
        return emailField;
    }

    private FormComponent<String> createUsernameFormComponent(String id) {
        final RequiredTextField<String> usernameField = new RequiredTextField<String>(id);
        usernameField.setLabel(new ResourceModel("username"));
        // ... validation
        usernameField.add(new AttributeModifier("maxLength", "255"));
        usernameField.add(StringValidator.lengthBetween(3, 255));
        return usernameField;
    }

    private ShinyForm createForm(String id) {
        final CompoundPropertyModel<User> defaultModelObject = new CompoundPropertyModel<User>(new User());
        return new ShinyForm(id, defaultModelObject) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                User inputUser = (User) getDefaultModelObject();

                // Credential check:
                // don't be specific in returning error message to avoid checks
                // by hackers
                // if emails or pseudonyms exist!

                // check if pseudonym exists
                final UserAccount storedUserAccount = userAccountService.getByUsername(inputUser.getUserAccount()
                        .getUsername());
                if (storedUserAccount == null) {
                    error(getString("error.invalid_credentials"));
                    return;
                }

                // check if email is same
                User storedUser = storedUserAccount.getUser();
                if (storedUser == null || storedUser.getEmail() == null || !storedUser.getEmail().equalsIgnoreCase(inputUser.getEmail())) {
                    error(getString("error.invalid_credentials"));
                    return;
                }

                Locale locale;
                final Language preferredLanguage = storedUserAccount.getPreferredLanguage();
                if (preferredLanguage != null) {
                    locale = preferredLanguage.getLocale();
                } else {
                    locale = getSession().getLocale();
                }
                userService.resetPassword(storedUser, locale);
                // no automatic login, user shall login manually...
                // final WicketWebSession session = (WicketWebSession)
                // getSession();
                // session.setUser(userByEmail);
                info(getString("password_sent", defaultModelObject));
            }
        ;
    }
;
}
}
