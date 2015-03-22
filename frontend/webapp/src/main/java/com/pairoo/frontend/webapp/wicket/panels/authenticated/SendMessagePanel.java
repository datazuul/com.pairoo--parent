package com.pairoo.frontend.webapp.wicket.panels.authenticated;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
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
import com.pairoo.business.api.UserService;
import com.pairoo.domain.Message;
import com.pairoo.domain.User;
import com.pairoo.frontend.webapp.wicket.panels.BasePanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ralf Eichinger
 */
public abstract class SendMessagePanel extends BasePanel {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(SendMessagePanel.class);

    @SpringBean(name = MessageService.BEAN_ID)
    private MessageService messageService;
    
    @SpringBean(name = UserService.BEAN_ID)
    private UserService userService;

    public SendMessagePanel(final String id, final IModel<Message> model) {
        super(id, model);
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        logEnter(SendMessagePanel.class);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final ShinyForm form = createForm("form", new CompoundPropertyModel<>(
                (IModel<Message>) getDefaultModel()));
        add(form);

        // from
        final Label lblFrom = new Label("sender.userAccount.username");
        form.add(lblFrom);

        // to
        final Label lblTo = new Label("receiver.userAccount.username");
        form.add(lblTo);

        // ---------------- message: subject
        final FormComponent<String> subject = createMessageSubjectFormComponent("subject");
        form.add(subject);
        form.add(labelForFormComponent("subjectFieldLabel", subject));

        // ---------------- message: text
        final FormComponent<String> text = createMessageTextFormComponent("text");
        form.add(text);
        // ... label
        // none

        form.add(cancelButton("btnCancel"));
    }

    private Button cancelButton(String id) {
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
        final FormComponent<String> text = new TextArea<>(id);
        text.setLabel(new ResourceModel("message"));
        // ... validation
        text.add(StringValidator.maximumLength(4000));
        text.add(new AttributeModifier("maxlength", "4000"));
        // text.add(new AnnotatedConstraintBehavior());
        // removed, because causes confusion when no character can be typed in
        // anymore
        return text;
    }

    private FormComponent<String> createMessageSubjectFormComponent(String id) {
        // ... field
        final TextField<String> subject = new TextField<>(id);
        subject.setLabel(new ResourceModel("subject"));
        // ... behavior
        subject.add(new DefaultFocusBehavior());
        // ... validation
        subject.add(StringValidator.maximumLength(255));
        subject.add(new AttributeModifier("maxlength", "255"));
        return subject;
    }

    private ShinyForm createForm(String id, final IModel<Message> model) {
        final ShinyForm form = new ShinyForm(id, model) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                final Message message = (Message) getDefaultModelObject();

                try {
                    messageService.send(message);
                    getSession().info(getString("message_sent"));
                } catch (Exception e) {
                    User sender = message.getSender();
                    User receiver = message.getReceiver();
                    LOGGER.info("error during message sending. sender is user with id/email {}, receiver is user with id/email {}",
                            sender.getId() + "/" + sender.getEmail(), receiver.getId() + "/" + receiver.getEmail());
                    LOGGER.error("exeception during sending message", e);
                    
                    // retry after saving users
                    userService.save(sender);
                    userService.save(receiver);
                    
                    LOGGER.info("retry sending message...");
                    messageService.send(message);
                    getSession().info(getString("message_sent"));
                }
                onGoBack();
            }
        };
        return form;
    }

    protected abstract void onGoBack();
}
