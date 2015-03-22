package com.pairoo.frontend.webapp.wicket.pages.authenticated;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;

import com.datazuul.framework.webapp.wicket.components.form.ShinyForm;
import com.pairoo.domain.User;
import org.apache.wicket.model.CompoundPropertyModel;

/**
 * @author Ralf Eichinger
 */
public class DeleteProfilePage extends AuthenticatedWebPage {
    private static final long serialVersionUID = 1L;

    public DeleteProfilePage(final IModel<User> model) {
	super(new CompoundPropertyModel<User>(model));
    }

    @Override
    protected void onBeforeRender() {
	super.onBeforeRender();
	logEnter(DeleteProfilePage.class);
    }

    @Override
    protected void onInitialize() {
	super.onInitialize();

	// form
	final ShinyForm form = createForm("form");
	add(form);

	// save
	form.add(saveButton("save"));
    }

    private Button saveButton(String id) {
	return new Button(id);
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
