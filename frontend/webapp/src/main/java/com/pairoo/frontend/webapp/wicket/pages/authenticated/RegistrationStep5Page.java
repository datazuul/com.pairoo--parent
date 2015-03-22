package com.pairoo.frontend.webapp.wicket.pages.authenticated;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;

import com.datazuul.framework.webapp.wicket.components.form.ShinyForm;
import com.datazuul.framework.webapp.wicket.model.LoadableDetachableDomainObjectModel;
import com.pairoo.domain.User;
import com.pairoo.frontend.webapp.wicket.panels.EditUserProfile2Panel;

/**
 * @author Ralf Eichinger
 */
public class RegistrationStep5Page extends AuthenticatedWebPage {

    private static final long serialVersionUID = 1L;

    public RegistrationStep5Page(final IModel<User> model) {
        super(model);
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        logEnter(RegistrationStep5Page.class);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        IModel<User> model = (IModel<User>) getDefaultModel();

        final ShinyForm form = new ShinyForm("form", model) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                final User user = (User) getDefaultModelObject();
                userService.save(user);
                setResponsePage(new RegistrationStep6Page(new LoadableDetachableDomainObjectModel<Long>(user, userService)));
            }
        };
        add(form);

        final EditUserProfile2Panel panel = new EditUserProfile2Panel("editUserProfile2Panel", model);
        panel.setRenderBodyOnly(true);
        form.add(panel);

        form.add(new Button("saveButton"));
    }
}
