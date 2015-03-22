package com.pairoo.frontend.webapp.wicket.pages;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.datazuul.framework.domain.Language;
import com.datazuul.framework.domain.geo.Country;
import com.datazuul.framework.domain.geo.GeoLocation;
import com.datazuul.framework.webapp.wicket.components.form.ShinyForm;
import com.datazuul.framework.webapp.wicket.model.LoadableDetachableDomainObjectModel;
import com.pairoo.business.api.GeoLocationService;
import com.pairoo.business.api.LandingPageActionService;
import com.pairoo.business.api.payment.VoucherPaymentService;
import com.pairoo.business.exceptions.RegistrationException;
import com.pairoo.domain.LandingPageAction;
import com.pairoo.domain.User;
import com.pairoo.domain.UserAccount;
import com.pairoo.domain.enums.LandingPageActionType;
import com.pairoo.domain.marketing.Promotion;
import com.pairoo.domain.payment.VoucherPaymentDuplicateException;
import com.pairoo.frontend.webapp.wicket.WicketWebSession;
import com.pairoo.frontend.webapp.wicket.components.PasswordFormComponent;
import com.pairoo.frontend.webapp.wicket.components.UsernameFormComponent;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.VoucherConfirmationPage;
import com.pairoo.frontend.webapp.wicket.panels.RegistrationEntryPanel;

/**
 * @author Ralf Eichinger
 */
public class VoucherLoginPage extends NotAuthenticatedWebPage {

    private static final long serialVersionUID = 1L;
    @SpringBean(name = GeoLocationService.BEAN_ID)
    private GeoLocationService geoLocationService;
    @SpringBean(name = LandingPageActionService.BEAN_ID)
    private LandingPageActionService landingPageActionService;
    @SpringBean(name = VoucherPaymentService.BEAN_ID)
    private VoucherPaymentService voucherPaymentService;

    public VoucherLoginPage(IModel<Promotion> model) {
	super(model);
    }

    @Override
    protected void onBeforeRender() {
	super.onBeforeRender();
	logEnter(VoucherLoginPage.class);
    }

    @Override
    protected void onInitialize() {
	super.onInitialize();

	myNaviPanel.setVisible(false);

	// login form
	final Form<User> loginForm = createLoginForm("loginForm", Model.of(new User()));
	add(loginForm);

	// useraccount: username
	final FormComponent<String> username = new UsernameFormComponent("userAccount.username");
	loginForm.add(username);
	loginForm.add(createFormComponentLabel("lblUsername", username));

	// useraccount: password
	final FormComponent<String> password = new PasswordFormComponent("userAccount.password");
	loginForm.add(password);
	loginForm.add(createFormComponentLabel("lblPassword", password));

	// registration form
	User registrationUser = userService.getRegistrationDefaultUser(getLocale());
	final ShinyForm registrationForm = createRegistrationForm("registrationForm", Model.of(registrationUser));
	add(registrationForm);
	registrationForm.add(createRegistrationEntryComponent("registrationEntryPanel"));
    }

    private Form<User> createLoginForm(String id, IModel<User> model) {
	// form
	final Form<User> form = new Form<User>(id, new CompoundPropertyModel<>(model)) {
	    private static final long serialVersionUID = 1L;

	    /**
	     * @see org.apache.wicket.markup.html.form.Form#onSubmit()
	     */
	    @Override
	    public final void onSubmit() {
		User user = (User) getDefaultModelObject();
		UserAccount userAccount = user.getUserAccount();
		WicketWebSession session = (WicketWebSession) getSession();
		User sessionUser = session.login(userAccount.getUsername(), userAccount.getPassword());
		if (sessionUser != null) {
		    // user on session has been refreshed during login!
		    userAccount = sessionUser.getUserAccount();

		    Promotion promotion = (Promotion) VoucherLoginPage.this.getDefaultModelObject();
		    // check if valid useraccount or for all, encash voucher
		    UserAccount userAccountPromotion = promotion.getUserAccount();
		    if (userAccountPromotion != null && !userAccountPromotion.getId().equals(userAccount.getId())) {
			final String errmsg = getString("error.promotion.user_invalid");
			error(errmsg);
		    } else {
			try {
			    voucherPaymentService.encashVoucher(userAccount, promotion);
			} catch (VoucherPaymentDuplicateException e) {
			    final String errmsg = getString("error.voucher_already_encashed");
			    error(errmsg);
			    return;
			} catch (Exception e) {
			    final String errmsg = getString("error.voucher_already_encashed");
			    error(errmsg);
			    return;
			}
			setResponsePage(new VoucherConfirmationPage(new LoadableDetachableDomainObjectModel<Long>(
				sessionUser, userService), false));
		    }
		} else {
		    // Try the component based localizer first. If not found try
		    // the application localizer. Else use the default
		    final String errmsg = getString("error.login");
		    // , this, "Unable to sign you in");

		    error(errmsg);
		}
	    }
	};
	return form;
    }

    private ShinyForm createRegistrationForm(String id, IModel<User> model) {
	final ShinyForm form = new ShinyForm(id, new CompoundPropertyModel<>(model)) {
	    private static final long serialVersionUID = 1L;

	    // @Override
	    // protected void beforeUpdateFormComponentModels() {
	    // super.beforeUpdateFormComponentModels();
	    // // Initialize membership model chain
	    // final User user = (User) getDefaultModelObject();
	    // UserAccount userAccount = user.getUserAccount();
	    // Membership membership = new Membership();
	    // userAccount.setCurrentMembership(membership);
	    // }
	    @Override
	    protected void onValidateModelObjects() {
		super.onValidateModelObjects();
		final User user = (User) getDefaultModelObject();

		final String zipcode = user.getUserProfile().getGeoLocation().getZipcode();
		final Country country = user.getUserProfile().getGeoLocation().getCountry();

		// lookup geolocation for this zipcode and country
		final GeoLocation geoLocation = geoLocationService.getByCountryAndZipcode(country, zipcode);
		if (geoLocation != null) {
		    // unique geolocation found, shift data in GeoArea object
		    user.getUserProfile().setGeoLocation(geoLocation);
		} else {
		    LOGGER.warn("no city found for zipcode" + zipcode + " in " + country);
		    Component regPanel = getPage().get("registrationForm:registrationEntryPanel");
		    regPanel.get("userProfile.geoLocation.zipcode").error(getString("error.no_geolocation_found_for_zipcode"));
		}
	    }

	    @Override
	    protected void onSubmit() {
		Promotion promotion = (Promotion) VoucherLoginPage.this.getDefaultModelObject();
		// check if specific useraccount or for all
		UserAccount userAccountPromotion = promotion.getUserAccount();
		if (userAccountPromotion != null) {
		    // then it is a promotion for a special (already registered)
		    // user account
		    // so it can not be a promotion for this (just registering)
		    // user account
		    final String errmsg = getString("error.promotion.user_invalid");
		    error(errmsg);
		    return;
		}

		// update user model
		final User user = (User) getDefaultModelObject();

		// store user (containing search profile and user profile)
		// in session for later registration
		final WicketWebSession session = (WicketWebSession) getSession();
		session.setUser(user);

		// generate activation link with token being sent in email
		// during registration process
		final LandingPageAction landingPageAction = new LandingPageAction(user.getUserAccount(),
			LandingPageActionType.ACTIVATE_ACCOUNT);
		// set preferred language
		Language sessionLanguage = ((WicketWebSession) getSession()).getSessionLanguage();
		user.getUserAccount().setPreferredLanguage(sessionLanguage);
		final String activationLink = LandingPage.createLandingPageActionUrl(getPage(), landingPageAction, sessionLanguage.getLocale());

		// preset country in search profile
		if (user.getUserProfile().getGeoLocation() != null) {
		    final Country country = user.getUserProfile().getGeoLocation().getCountry();
		    user.getSearchProfile().getGeoArea().setCountry(country);
		}

		// register and save user
		try {
		    // userService.register(user, preferredLocale,
		    // RequestUtils.toAbsolutePath(activationLink));
		    userService.registerAndEncashVoucher(user, sessionLanguage.getLocale(), activationLink, promotion);
		} catch (final RegistrationException e) {
		    // e.printStackTrace();
		    if (RegistrationException.Code.SEND_CONFIRMATION_EMAIL.equals(e.getCode())) {
			error(getString("RegistrationException.Code.SEND_CONFIRMATION_EMAIL"));
		    } else {
			error(getString("error.RegistrationException"));
		    }
		    // set a new default model object (with no ID from DB...)
		    final User cleanUser = userService.getRegistrationDefaultUser(getLocale());
		    cleanUser.setEmail(user.getEmail());
		    cleanUser.getSearchProfile().setPartnerType(user.getSearchProfile().getPartnerType());
		    cleanUser.getSearchProfile().setMinAge(user.getSearchProfile().getMinAge());
		    cleanUser.getSearchProfile().setMaxAge(user.getSearchProfile().getMaxAge());
		    cleanUser.getUserProfile().setPartnerType(user.getUserProfile().getPartnerType());
		    cleanUser.getUserProfile().getGeoLocation()
			    .setCountry(user.getUserProfile().getGeoLocation().getCountry());
		    cleanUser.getUserProfile().getGeoLocation()
			    .setZipcode(user.getUserProfile().getGeoLocation().getZipcode());
		    cleanUser.getUserAccount().setUsername(user.getUserAccount().getUsername());
		    cleanUser.getUserAccount().setPassword("");

		    // payment channels...

		    setDefaultModel(new CompoundPropertyModel<>(cleanUser));
		    return;
		} catch (VoucherPaymentDuplicateException e) {
		    final String errmsg = getString("error.voucher_already_encashed");
		    error(errmsg);
		    return;
		}

		// save landing page action
		// FIXME DataIntegrityViolationException: Could not execute JDBC
		// batch update; SQL [insert into LANDINGPAGEACTION (TOKEN,
		// ACTIONTYPE, USERACCOUNT_ID, ID) values (?, ?, ?, ?)];
		// constraint [null]; nested exception is
		// org.hibernate.exception.ConstraintViolationException: Could
		// not execute JDBC batch update
		// at
		// org.springframework.orm.hibernate3.SessionFactoryUtils.convertHibernateAccessException(SessionFactoryUtils.java:641)
		// ERROR: duplicate key value violates unique constraint
		// "landingpageaction_pkey"
		landingPageActionService.save(landingPageAction);

		// setResponsePage(new RegistrationPage(user));
		setResponsePage(new VoucherConfirmationPage(new LoadableDetachableDomainObjectModel<Long>(user,
			userService), true));
	    }
	};
	// ---------------- form
	// form.add(new JSR303FormValidator()); // does not work completely...
	// add(new GenericUniformBehavior("input, textarea, select, button"));
	return form;
    }

    private RegistrationEntryPanel createRegistrationEntryComponent(String id) {
	return new VoucherRegistrationEntryPanel(id);
    }

    private class VoucherRegistrationEntryPanel extends RegistrationEntryPanel {

	public VoucherRegistrationEntryPanel(String id) {
	    super(id);
	}
    }
}
