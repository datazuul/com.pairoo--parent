package com.pairoo.frontend.webapp.wicket.pages.authenticated;

import java.util.ArrayList;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;

import com.datazuul.framework.webapp.wicket.components.form.ShinyForm;
import com.datazuul.framework.webapp.wicket.model.LoadableDetachableDomainObjectModel;
import com.pairoo.domain.SearchProfile;
import com.pairoo.domain.User;
import com.pairoo.domain.enums.AppearanceStyle;
import com.pairoo.domain.enums.EducationType;
import com.pairoo.domain.enums.HobbyType;
import com.pairoo.frontend.webapp.wicket.panels.EditSearchProfile3Panel;

/**
 * @author Ralf Eichinger
 */
public class RegistrationStep3Page extends AuthenticatedWebPage {

    private static final long serialVersionUID = 1L;

    public RegistrationStep3Page(final IModel<User> model) {
        super(model);
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        logEnter(RegistrationStep3Page.class);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        IModel<User> model = (IModel<User>) getDefaultModel();
        final User user = model.getObject();

        final SearchProfile searchProfile = user.getSearchProfile();

        final ArrayList<AppearanceStyle> appearanceStyles = new ArrayList<AppearanceStyle>();
        appearanceStyles.add(AppearanceStyle.ANY);
        searchProfile.setAppearanceStyles(appearanceStyles);

        final ArrayList<EducationType> educationTypes = new ArrayList<EducationType>();
        educationTypes.add(EducationType.ANY);
        searchProfile.setEducationTypes(educationTypes);

        final ArrayList<HobbyType> hobbies = new ArrayList<HobbyType>();
        hobbies.add(HobbyType.ANY);
        searchProfile.setHobbyTypes(hobbies);

        final ShinyForm form = new ShinyForm("form", model) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                final User user = (User) getDefaultModelObject();
                userService.save(user);
                setResponsePage(new RegistrationStep4Page(new LoadableDetachableDomainObjectModel<Long>(user, userService)));
            }
        };
        add(form);

        final EditSearchProfile3Panel panel = new EditSearchProfile3Panel("editSearchProfile3Panel", model);
        panel.setRenderBodyOnly(true);
        form.add(panel);

        form.add(new Button("saveButton"));
    }
}
