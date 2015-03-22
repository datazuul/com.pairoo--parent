package com.pairoo.frontend.webapp.wicket.components;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.validation.validator.StringValidator;

public class PasswordFormComponent extends PasswordTextField {

    private static final long serialVersionUID = 1L;

    public PasswordFormComponent(String id) {
        super(id);

        setType(String.class);
        setLabel(new ResourceModel("label.password"));
        setResetPassword(false);

        // ... validation
        setRequired(true);
        add(new AttributeModifier("maxlength", "12"));
        add(StringValidator.lengthBetween(6, 12));
    }
}
