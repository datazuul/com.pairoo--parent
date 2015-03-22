package com.pairoo.frontend.webapp.wicket.pages.authenticated;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;

import com.datazuul.framework.webapp.wicket.components.form.ShinyForm;
import com.datazuul.framework.webapp.wicket.model.LoadableDetachableDomainObjectModel;
import com.pairoo.domain.User;
import com.pairoo.frontend.webapp.wicket.panels.EditUserProfile1Panel;

/**
 * @author Ralf Eichinger
 */
public class RegistrationStep4Page extends AuthenticatedWebPage {

    private static final long serialVersionUID = 1L;

    public RegistrationStep4Page(final IModel<User> model) {
        super(model);
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        logEnter(RegistrationStep4Page.class);
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
                setResponsePage(new RegistrationStep5Page(new LoadableDetachableDomainObjectModel<Long>(user, userService)));
            }
        };
        add(form);

        final EditUserProfile1Panel panel = new EditUserProfile1Panel("editUserProfile1Panel", model, false);
        panel.setRenderBodyOnly(true);
        form.add(panel);

        form.add(new Button("saveButton"));
    }
}
