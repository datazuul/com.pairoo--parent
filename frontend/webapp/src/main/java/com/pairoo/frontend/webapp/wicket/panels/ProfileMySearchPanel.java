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
public class ProfileMySearchPanel extends BasePanel {

    private static final long serialVersionUID = 1L;
    @SpringBean(name = UserService.BEAN_ID)
    private UserService userService;

    public ProfileMySearchPanel(final String id, final IModel<User> model) {
        super(id, model);
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        logEnter(ProfileMySearchPanel.class);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final ShinyForm form = new ShinyForm("form", getDefaultModel()) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                final User user = (User) getDefaultModelObject();
                // FIXME DataIntegrityViolationException: Could not execute JDBC
                // batch update; SQL [insert into GEOAREA (CONTINENT, COUNTRY,
                // GEOLOCATION_ID, MAXDISTANCE, SUBDIVISION_ID, ZIPCODE_START,
                // ID) values (?, ?, ?, ?, ?, ?, ?)]; constraint [null]; nested
                // exception is
                // org.hibernate.exception.ConstraintViolationException: Could
                // not execute JDBC batch update
                // insert into GEOAREA (CONTINENT, COUNTRY, GEOLOCATION_ID,
                // MAXDISTANCE, SUBDIVISION_ID, ZIPCODE_START, ID) values (NULL,
                // 'GERMANY', '75449', 'ANY', NULL, '80331', '17')
                userService.save(user);
                info(getString("saved"));
                // final WicketWebSession session = (WicketWebSession)
                // getSession();
                // session.setUser(user);
            }
        };
        add(form);

        final EditSearchProfile1Panel panel1 = new EditSearchProfile1Panel("editSearchProfile1Panel",
                (IModel<User>) getDefaultModel());
        panel1.setRenderBodyOnly(true);
        form.add(panel1);

        final EditSearchProfile2Panel panel2 = new EditSearchProfile2Panel("editSearchProfile2Panel",
                (IModel<User>) getDefaultModel());
        panel2.setRenderBodyOnly(true);
        form.add(panel2);

        form.add(new Button("saveButton"));
    }
}
