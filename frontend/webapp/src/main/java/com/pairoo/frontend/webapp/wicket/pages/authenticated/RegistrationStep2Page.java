package com.pairoo.frontend.webapp.wicket.pages.authenticated;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.datazuul.framework.webapp.wicket.components.form.ShinyForm;
import com.datazuul.framework.webapp.wicket.model.LoadableDetachableDomainObjectModel;
import com.pairoo.business.api.PersonProfileService;
import com.pairoo.domain.User;
import com.pairoo.frontend.webapp.wicket.panels.EditSearchProfile2Panel;

/**
 * @author Ralf Eichinger
 */
public class RegistrationStep2Page extends AuthenticatedWebPage {

    private static final long serialVersionUID = 1L;
    @SpringBean(name = PersonProfileService.BEAN_ID)
    private PersonProfileService personProfileService;

    public RegistrationStep2Page(final IModel<User> model) {
        super(model);
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        logEnter(RegistrationStep2Page.class);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        IModel<User> model = (IModel<User>) getDefaultModel();

        final User user = model.getObject();
        user.getSearchProfile().setMinHeightCm((int) personProfileService.getMinHeight().getExactValue());
        user.getSearchProfile().setMaxHeightCm((int) personProfileService.getMaxHeight().getExactValue());

        final ShinyForm form = form("form", model);
        add(form);
        form.add(editSearchProfile2Panel("editSearchProfile2Panel", model));
        form.add(new Button("saveButton"));

    }

    private EditSearchProfile2Panel editSearchProfile2Panel(String id, IModel<User> model) {
        final EditSearchProfile2Panel panel = new EditSearchProfile2Panel(id, model);
        panel.setRenderBodyOnly(true);
        return panel;
    }

    private ShinyForm form(String id, IModel<User> model) {
        final ShinyForm form = new ShinyForm(id, model) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                final User user = (User) getDefaultModelObject();
                userService.save(user);
                setResponsePage(new RegistrationStep3Page(new LoadableDetachableDomainObjectModel<Long>(user, userService)));
            }
        };
        return form;
    }
}
