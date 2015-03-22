package com.pairoo.frontend.webapp.wicket.panels.authenticated;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import com.datazuul.framework.webapp.wicket.behavior.DefaultFocusBehavior;
import com.datazuul.framework.webapp.wicket.components.form.ShinyForm;
import com.pairoo.business.api.MessageService;
import com.pairoo.domain.Message;
import com.pairoo.frontend.webapp.wicket.panels.BasePanel;

/**
 * @author Ralf Eichinger
 */
public abstract class AnswerMessagePanel extends BasePanel {

    private static final long serialVersionUID = 1L;
    @SpringBean(name = MessageService.BEAN_ID)
    private MessageService messageService;

    public AnswerMessagePanel(final String id, final IModel<Message> model) {
        super(id, model);
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        logEnter(AnswerMessagePanel.class);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final ShinyForm form = createForm("form", (IModel<Message>) getDefaultModel());
        add(form);

        // ---------------- message: subject
        final FormComponent<String> subject = createMessageSubjectFormComponent("subject");
        form.add(subject);
        form.add(labelForFormComponent("subjectFieldLabel", subject));

        // ---------------- message: text
        final FormComponent<String> text = createMessageTextFormComponent("text");
        form.add(text);
        // ... label
        // none

        final Button btnCancel = createCancelButton("btnCancel");
        form.add(btnCancel);
    }

    private Button createCancelButton(String id) {
        final Button btnCancel = new Button(id) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit() {
                onGoBack();
            }
        };
        btnCancel.setDefaultFormProcessing(false);
        return btnCancel;
    }

    private FormComponent<String> createMessageTextFormComponent(String id) {
        final FormComponent<String> text = new TextArea<String>(id);
        text.setLabel(new ResourceModel("message"));
        // ... behavior
        text.add(new DefaultFocusBehavior());
        // ... validation
        text.add(StringValidator.maximumLength(4000));
        text.add(new AttributeModifier("maxlength", "4000"));
        // text.add(new AnnotatedConstraintBehavior());
        // removed, because causes confusion when no character can be typed in
        // anymore without some hint
        return text;
    }

    private FormComponent<String> createMessageSubjectFormComponent(String id) {
        // ... field
        final TextField<String> subject = new TextField<String>(id);
        subject.setLabel(new ResourceModel("subject"));
        // ... validation
        subject.add(StringValidator.maximumLength(255));
        subject.add(new AttributeModifier("maxlength", "255"));
        return subject;
    }

    private ShinyForm createForm(String id, final IModel<Message> model) {
        final ShinyForm form = new ShinyForm(id, new CompoundPropertyModel<Message>(model)) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                final Message message = (Message) getDefaultModelObject();
                messageService.send(message);
                info(getString("message_sent"));
                onGoBack();
            }
        };
        return form;
    }

    protected abstract void onGoBack();
}
