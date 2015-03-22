package com.pairoo.frontend.webapp.wicket.components;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.validation.validator.StringValidator;

public class UsernameFormComponent extends TextField<String> {

    private static final long serialVersionUID = 1L;

    public UsernameFormComponent(String id) {
        super(id);

        // ... field
        setType(String.class);
        setLabel(new ResourceModel("label.username"));
        // ... validation
        add(new AttributeModifier("maxLength", "255"));
        add(StringValidator.lengthBetween(3, 255));
        setRequired(true);
    }
}
