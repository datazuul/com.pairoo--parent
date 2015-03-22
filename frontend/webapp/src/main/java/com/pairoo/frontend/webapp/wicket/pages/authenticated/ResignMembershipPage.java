package com.pairoo.frontend.webapp.wicket.pages.authenticated;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;

import com.datazuul.framework.webapp.wicket.components.form.ShinyForm;
import com.pairoo.domain.User;

/**
 * @author Ralf Eichinger
 */
public class ResignMembershipPage extends AuthenticatedWebPage {
    private static final long serialVersionUID = 1L;

    public ResignMembershipPage(final IModel<User> model) {
	super(model);
    }

    @Override
    protected void onBeforeRender() {
	super.onBeforeRender();
	logEnter(ResignMembershipPage.class);
    }

    @Override
    protected void onInitialize() {
	super.onInitialize();

	final ShinyForm form = createForm("form");
	add(form);

	form.add(new Button("save"));
    }

    private ShinyForm createForm(String id) {
	final ShinyForm form = new ShinyForm(id, getDefaultModel()) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    protected void onSubmit() {
		// final User user = (User) getDefaultModelObject();
		// userService.save(user);
		//
		// // TODO send to payment provider
		//
		// info(getString("saved"));
		// final WicketWebSession session = (WicketWebSession)
		// getSession();
		// session.setUser(user);
		// setResponsePage(new MyProfilePage());
		error("not implemented, yet");
	    }
	};
	return form;
    }
}
