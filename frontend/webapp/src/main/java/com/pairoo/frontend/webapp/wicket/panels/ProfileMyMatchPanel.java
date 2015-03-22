package com.pairoo.frontend.webapp.wicket.panels;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.datazuul.framework.webapp.wicket.components.form.ShinyForm;
import com.pairoo.business.api.UserService;
import com.pairoo.domain.User;

/**
 * @author Ralf Eichinger
 */
public class ProfileMyMatchPanel extends BasePanel {

    private static final long serialVersionUID = 1L;
    @SpringBean(name = UserService.BEAN_ID)
    private UserService userService;

    public ProfileMyMatchPanel(final String id, final IModel<User> model) {
        super(id, model);
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        logEnter(ProfileMyMatchPanel.class);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final ShinyForm form = new ShinyForm("form", getDefaultModel()) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                final User user = (User) getDefaultModelObject();
                userService.save(user);
                info(getString("saved"));
                // final WicketWebSession session = (WicketWebSession)
                // getSession();
                // session.setUser(user);
            }
        };
        add(form);

        final EditSearchProfile3Panel panel = new EditSearchProfile3Panel("editSearchProfile3Panel",
                (IModel<User>) getDefaultModel());
        panel.setRenderBodyOnly(true);
        form.add(panel);

        form.add(new Button("saveButton"));
    }
}
