package com.pairoo.frontend.webapp.wicket.pages.authenticated;

import com.datazuul.framework.domain.Language;
import com.datazuul.framework.webapp.wicket.components.form.ShinyForm;
import com.pairoo.domain.User;
import com.pairoo.frontend.webapp.wicket.WicketWebSession;
import java.util.Locale;
import javax.validation.ValidationException;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChangePasswordPage extends AuthenticatedWebPage {

    private static final long serialVersionUID = 1L;
    static final Logger LOGGER = LoggerFactory.getLogger(ChangePasswordPage.class);
    private String oldPassword;
    private String newPassword;
    private String confirmNewPassword;

    public ChangePasswordPage(final IModel<User> model) {
        super(new CompoundPropertyModel<User>(model));
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        logEnter(ChangePasswordPage.class);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        // ---------------- form
        final ShinyForm form = createForm("form", null);
        add(form);

        // ---------------- old password
        final FormComponent<String> oldPasswordField = createOldPasswordFormComponent("oldPassword", new PropertyModel<String>(this, "oldPassword"));
        form.add(oldPasswordField);
        form.add(createFormComponentLabel("oldPasswordLabel", oldPasswordField));

        // ---------------- new password
        final FormComponent<String> newPasswordField = createNewPasswordFormComponent("newPassword", new PropertyModel<String>(this, "newPassword"));
        form.add(newPasswordField);
        form.add(createFormComponentLabel("newPasswordLabel", newPasswordField));

        // ---------------- confirm new password
        final FormComponent<String> confirmNewPasswordField = createConfirmNewPasswordFormComponent("confirmNewPassword", new PropertyModel<String>(this, "confirmNewPassword"));
        form.add(confirmNewPasswordField);
        form.add(createFormComponentLabel("confirmNewPasswordLabel", confirmNewPasswordField));

        form.add(new EqualPasswordInputValidator(newPasswordField, confirmNewPasswordField));
    }

    private ShinyForm createForm(String id, IModel<?> model) {
        final ShinyForm form = new ShinyForm(id, model) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                final User user = (User) ChangePasswordPage.this.getDefaultModelObject();

                Locale locale = null;
                final Language preferredLanguage = user.getUserAccount().getPreferredLanguage();
                if (preferredLanguage != null) {
                    locale = preferredLanguage.getLocale();
                } else {
                    locale = getSession().getLocale();
                }
                try {
                    userService.changePassword(user, locale, oldPassword, newPassword, confirmNewPassword);
                    // TODO how to implement specific validation exceptions (a
                    // lot of exceptions?)?
                } catch (final ValidationException e) {
                    LOGGER.info("changing password failed", e);
                    error(getString("validation_failed"));
                    return;
                }
                // TODO maybe it is possible to live without explicitly setting
                // user to session?
                // new password has been reset because user on session gets
                // stored on logout...
                final WicketWebSession session = (WicketWebSession) getSession();
                session.setUser(user);
                // info(getString("password_sent", userModel));
                // decided to not send...
                info(getString("password_changed"));
            }
        };
        return form;
    }

    private FormComponent<String> createConfirmNewPasswordFormComponent(String id, IModel<String> model) {
        // ... field
        final PasswordTextField fc = new PasswordTextField(id, model);
        fc.setLabel(new ResourceModel("confirm_new_password"));
        fc.setResetPassword(true);
        // ... validation
        fc.add(new AttributeModifier("maxlength", "12"));
        fc.add(StringValidator.lengthBetween(6, 12));
        return fc;
    }

    private FormComponent<String> createNewPasswordFormComponent(String id, IModel<String> model) {
        // ... field
        final PasswordTextField fc = new PasswordTextField(id, model);
        fc.setLabel(new ResourceModel("new_password"));
        fc.setResetPassword(true);
        // ... validation
        fc.add(new AttributeModifier("maxlength", "12"));
        fc.add(StringValidator.lengthBetween(6, 12));
        return fc;
    }

    private FormComponent<String> createOldPasswordFormComponent(String id, IModel<String> model) {
        // ... field
        final PasswordTextField fc = new PasswordTextField(id, model);
        fc.setLabel(new ResourceModel("old_password"));
        fc.setResetPassword(true);
        // ... validation
        fc.add(new AttributeModifier("maxlength", "12"));
        fc.add(StringValidator.lengthBetween(6, 12));
        return fc;
    }
}
