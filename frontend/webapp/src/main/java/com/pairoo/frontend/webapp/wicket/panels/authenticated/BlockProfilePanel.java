package com.pairoo.frontend.webapp.wicket.panels.authenticated;

import com.datazuul.framework.webapp.wicket.components.form.ShinyForm;
import com.pairoo.business.api.BlockedUserService;
import com.pairoo.domain.User;
import com.pairoo.frontend.webapp.wicket.panels.BasePanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ralf Eichinger
 */
public class BlockProfilePanel extends BasePanel {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(BlockProfilePanel.class);
    @SpringBean(name = BlockedUserService.BEAN_ID)
    private BlockedUserService blockedUserService;

    public BlockProfilePanel(final String id, final IModel<User> profileUserModel, final IModel<User> blockerUserModel) {
        super(id, profileUserModel, blockerUserModel);
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        logEnter(BlockProfilePanel.class);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final ShinyForm form = createForm("form", new CompoundPropertyModel<User>((IModel<User>) getDefaultModel()));
        add(form);

        // text with username
        form.add(dialogTextLabel("dialog.text"));

        // buttons
        form.add(cancelButton("btnCancel"));
        form.add(blockButton("btnBlock"));
    }

    private Label dialogTextLabel(String id) {
        final Label label = new Label(id, new StringResourceModel("dialog.text", getDefaultModel()));
        label.setEscapeModelStrings(false);
        return label;
    }

    private Button blockButton(String id) {
        final Button btn = new AjaxButton(id) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(final AjaxRequestTarget target, final Form<?> form) {
                final User user = userModel.getObject();
                final User blockedUser = (User) BlockProfilePanel.this.getDefaultModelObject();
                try {
                    blockedUserService.add(user, blockedUser);
                    getSession().info(getString("profile_blocked"));
                } catch (final Exception ex) {
                    LOGGER.error("blocking user failed", ex);
                    getSession().error(getString("error.technical"));
                }
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

    private Button cancelButton(String id) {
        final Button btnCancel = new AjaxButton(id) {
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

    private ShinyForm createForm(String id, final IModel<User> model) {
        final ShinyForm form = new ShinyForm(id, new CompoundPropertyModel<User>(model));
        return form;
    }
}
