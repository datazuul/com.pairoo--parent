package com.pairoo.frontend.webapp.wicket.panels;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.value.ValueMap;

import com.datazuul.framework.webapp.wicket.components.form.ShinyForm;
import com.pairoo.business.api.UserAccountService;
import com.pairoo.domain.UserAccount;
import com.pairoo.frontend.webapp.wicket.WicketWebSession;
//import com.visural.wicket.behavior.inputhint.InputHintBehavior;

/**
 * @deprecated do not use unless you update validation etc., no longer used
 * @author Ralf Eichinger
 */
@Deprecated
public class MyNaviNotAuthenticatedPanelDropDown extends Panel {
	private static final long serialVersionUID = 1L;

	@SpringBean(name = UserAccountService.BEAN_ID)
	private UserAccountService userAccountService;

	WebMarkupContainer loginContainer;

	/**
	 * @param id
	 *            See Component constructor
	 * @see org.apache.wicket.Component#Component(String)
	 */
	public MyNaviNotAuthenticatedPanelDropDown(final String id) {
		super(id);

		// Create feedback panel and add to page
		// final FeedbackPanel feedback = new FeedbackPanel("feedback");
		// add(feedback);
		// use overall feedback panel!

		// toggle login
		add(new AjaxFallbackLink<Void>("toggleLogin") {
			private static final long serialVersionUID = 1L;
			boolean loginVisible = false;

			@Override
			public void onClick(final AjaxRequestTarget target) {
				loginVisible = !loginVisible;

				if (loginVisible) {
					loginContainer.add(new AttributeModifier("style", new Model<String>("display:block")));
				} else {
					loginContainer.add(new AttributeModifier("style", new Model<String>("display:none")));
				}
				target.add(loginContainer);
			}
		});

		// Add sign-in form to page, passing feedback panel as
		// validation error handler
		loginContainer = new WebMarkupContainer("loginContainer");
		loginContainer.add(new AttributeModifier("style", true, new Model<String>("display:none")));
		loginContainer.setOutputMarkupId(true);

		final LoginForm form = new LoginForm("loginForm");
		loginContainer.add(form);
		add(loginContainer);
	}

	/** Field for password. */
	private PasswordTextField password;

	/** Field for user name. */
	private TextField<String> username;

	/**
	 * Sign in form.
	 */
	public final class LoginForm extends ShinyForm {
		private static final long serialVersionUID = 1L;

		/** El-cheapo model for form. */
		private final ValueMap properties = new ValueMap();

		/**
		 * Constructor.
		 * 
		 * @param id
		 *            id of the form component
		 */
		public LoginForm(final String id) {
			super(id);

			// Attach textfield components that edit properties map
			username = new TextField<String>("username", new PropertyModel<String>(properties, "username"));
			username.setType(String.class);
//			username.add(new InputHintBehavior(this, "Pseudonym", "color: #aaa;"));
			add(username);

			password = new PasswordTextField("password", new PropertyModel<String>(properties, "password"));
			password.setType(String.class);
			add(password);
		}

		/**
		 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
		 */
		@Override
		public final void onSubmit() {
			if (login(getUsername(), getPassword())) {
				// If login has been called because the user was not yet
				// logged in, than continue to the original destination,
				// otherwise to the Home page
				// if (!continueToOriginalDestination()) {
				// setResponsePage(getApplication().getHomePage());
				// }
				// setResponsePage(new MyHomePage());
			} else {
				// Try the component based localizer first. If not found try the
				// application localizer. Else use the default
				final String errmsg = getLocalizer().getString("loginError", this, "Unable to sign you in");

				error(errmsg);
			}
		}
	}

	/**
	 * Convenience method to access the password.
	 * 
	 * @return The password
	 */
	public String getPassword() {
		return password.getDefaultModelObjectAsString();
	}

	/**
	 * Convenience method to access the username.
	 * 
	 * @return The user name
	 */
	public String getUsername() {
		return username.getDefaultModelObjectAsString();
	}

	/**
	 * Sign in user if possible.
	 * 
	 * @param username
	 *            The username
	 * @param password
	 *            The password
	 * @return True if signin was successful
	 */
	public boolean login(final String username, final String password) {
		final UserAccount userAccount = userAccountService.getByUsername(username);
		if (userAccount != null && userAccount.getPassword().equals(password)) {
			((WicketWebSession) getSession()).setUser(userAccount.getUser());
			return true;
		}
		// just for testing:
		// ((WicketWebSession) getSession()).setUserAccount(new UserAccount());
		// return true;

		return false;
	}

}
