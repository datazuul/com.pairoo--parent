package com.pairoo.frontend.webapp.wicket.panels.authenticated;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.datazuul.framework.util.EnumUtils;
import com.datazuul.framework.webapp.wicket.components.form.ShinyForm;
import com.pairoo.business.api.MessageService;
import com.pairoo.domain.Message;
import com.pairoo.domain.enums.WinkType;
import com.pairoo.frontend.webapp.wicket.panels.BasePanel;

/**
 * @author Ralf Eichinger
 */
public abstract class SendWinkPanel extends BasePanel {

    private static final long serialVersionUID = 1L;
    @SpringBean(name = MessageService.BEAN_ID)
    private MessageService messageService;

    public SendWinkPanel(final String id, final IModel<Message> model) {
        super(id, model);
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        logEnter(SendWinkPanel.class);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final ShinyForm form = createForm("form", new CompoundPropertyModel<Message>(
                (IModel<Message>) getDefaultModel()));
        add(form);

        // from
        final Label lblFrom = new Label("sender.userAccount.username");
        form.add(lblFrom);

        // to
        final Label lblTo = new Label("receiver.userAccount.username");
        form.add(lblTo);

        // ---------------- wink type
        form.add(winkTypeFormComponent("text"));

        // ... label
        // final SimpleFormComponentLabel userProfileSmokeTypeSelectionLabel =
        // new SimpleFormComponentLabel(
        // "userProfileSmokeTypeSelectionLabel",
        // userProfileSmokeTypeSelection);
        // add(userProfileSmokeTypeSelectionLabel);

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

    private RadioChoice<WinkType> winkTypeFormComponent(String id) {
        // ... values
        final WinkType[] winkTypeValues = WinkType.values();
        final List<WinkType> winkTypes = new ArrayList<WinkType>(Arrays.asList(winkTypeValues));
        // ... field
        final RadioChoice<WinkType> radioChoice = new RadioChoice<WinkType>(id, winkTypes,
                new EnumChoiceRenderer<WinkType>(SendWinkPanel.this));
        // userProfileSmokeTypeSelection.setLabel(new ResourceModel(
        // "userProfileSmokeTypeSelectionLabel"));
        radioChoice.setEscapeModelStrings(false); // dont convert to html
        radioChoice.setPrefix("<label class=\"radio\">");
        radioChoice.setSuffix("</label>");
        // ... validation
        radioChoice.setRequired(true);
        return radioChoice;
    }

    private ShinyForm createForm(String id, final IModel<Message> model) {
        final ShinyForm form = new ShinyForm(id, model) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                final Message message = (Message) getDefaultModelObject();
                message.setSubject(getString(EnumUtils.getEnumKey(message.getMessageType())));
                messageService.send(message);

                getSession().info(getString("message_sent"));
                onGoBack();
            }
        };
        return form;
    }

    protected abstract void onGoBack();
}
