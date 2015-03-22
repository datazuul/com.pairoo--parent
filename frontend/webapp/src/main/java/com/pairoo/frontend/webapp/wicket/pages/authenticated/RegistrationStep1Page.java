package com.pairoo.frontend.webapp.wicket.pages.authenticated;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;

import com.datazuul.framework.webapp.wicket.components.form.ShinyForm;
import com.datazuul.framework.webapp.wicket.model.LoadableDetachableDomainObjectModel;
import com.pairoo.domain.SearchProfile;
import com.pairoo.domain.User;
import com.pairoo.domain.enums.MaxDistance;
import com.pairoo.frontend.webapp.wicket.panels.EditSearchProfile1Panel;

/**
 * <h1>Default values</h1>
 *
 * <h2>Search nearby:</h2>
 *
 * <ul> <li>searchprofile geolocation == userprofile geolocation</li>
 * <li>searchprofile max distance == 20KM</li> </ul>
 *
 * @author Ralf Eichinger
 */
public class RegistrationStep1Page extends AuthenticatedWebPage {

    private static final long serialVersionUID = 1L;

    public RegistrationStep1Page(final IModel<User> model) {
        super(model);
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        logEnter(RegistrationStep1Page.class);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        IModel<User> model = (IModel<User>) getDefaultModel();

        // set default values for fields in this step
        final User user = model.getObject();
        final SearchProfile searchProfile = user.getSearchProfile();
        searchProfile.getGeoArea().setGeoLocation(user.getUserProfile().getGeoLocation());
        searchProfile.getGeoArea().setMaxDistance(MaxDistance.UNITS_20);

        final ShinyForm form = form("form", model);
        add(form);
        form.add(editSearchProfile1Panel("editSearchProfile1Panel", model));
        form.add(saveButton("saveButton"));

    }

    private Button saveButton(String id) {
        final Button btnSave = new Button(id);
        return btnSave;
    }

    private EditSearchProfile1Panel editSearchProfile1Panel(String id, IModel<User> model) {
        final EditSearchProfile1Panel panel = new EditSearchProfile1Panel(id, model);
        panel.setRenderBodyOnly(true);
        return panel;
    }

    private ShinyForm form(String id, IModel<User> model) {
        final ShinyForm form = new ShinyForm("form", model) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                final User user = (User) getDefaultModelObject();
                userService.save(user);
                setResponsePage(new RegistrationStep2Page(new LoadableDetachableDomainObjectModel<Long>(user, userService)));
            }
        };
        return form;
    }
}
