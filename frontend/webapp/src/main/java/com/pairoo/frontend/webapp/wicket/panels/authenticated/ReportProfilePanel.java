package com.pairoo.frontend.webapp.wicket.panels.authenticated;

import com.datazuul.framework.webapp.wicket.components.form.ShinyForm;
import com.pairoo.business.api.UserService;
import com.pairoo.domain.User;
import com.pairoo.frontend.webapp.wicket.panels.BasePanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ralf Eichinger
 */
public class ReportProfilePanel extends BasePanel {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportProfilePanel.class);
    @SpringBean(name = UserService.BEAN_ID)
    private UserService userService;
    private String message;

    public ReportProfilePanel(final String id, final IModel<User> profileUserModel, final IModel<User> reporterUserModel) {
        super(id, profileUserModel, reporterUserModel);
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        logEnter(ReportProfilePanel.class);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final ShinyForm form = createForm(new CompoundPropertyModel<User>((IModel<User>) getDefaultModel()));
        add(form);

        // text with username
        form.add(createDialogText());

        // textarea
        form.add(createTextarea());

        // buttons
        final Button btnCancel = createCancelButton();
        form.add(btnCancel);

        final Button btnReport = createReportButton();
        form.add(btnReport);

    }

    private FormComponent<String> createTextarea() {
        final FormComponent<String> textArea = new TextArea<String>("textarea", new PropertyModel<String>(this,
                "message"));
        return textArea;
    }

    private Label createDialogText() {
        final Label label = new Label("dialog.text", new StringResourceModel("dialog.text", getDefaultModel()));
        label.setEscapeModelStrings(false);
        return label;
    }

    private Button createReportButton() {
        final Button btn = new AjaxButton("btnReport") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(final AjaxRequestTarget target, final Form<?> form) {
                final String text = message;
                final User reporter = userModel.getObject();
                final User profileUser = (User) getDefaultModelObject();
                try {
                    userService.reportProfile(reporter, profileUser, text, getLocale());
                } catch (final Exception ex) {
                    error(getString("error.technical"));
                }
                getSession().info(getString("profile_reported"));
                ModalWindow.closeCurrent(target);
            }

            @Override
            protected void onError(final AjaxRequestTarget target, final Form<?> form) {
                // TODO Auto-generated method stub
            }
        };
        btn.setDefaultFormProcessing(true);
        return btn;
    }

    private Button createCancelButton() {
        final Button btnCancel = new AjaxButton("btnCancel") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(final AjaxRequestTarget target, final Form<?> form) {
                ModalWindow.closeCurrent(target);
            }

            @Override
            protected void onError(final AjaxRequestTarget target, final Form<?> form) {
                // TODO Auto-generated method stub
            }
        };
        btnCancel.setDefaultFormProcessing(false);
        return btnCancel;
    }

    private ShinyForm createForm(final IModel<User> model) {
        final ShinyForm form = new ShinyForm("form", new CompoundPropertyModel<User>(model));
        return form;
    }
}
