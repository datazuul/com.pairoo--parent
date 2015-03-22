package com.pairoo.frontend.webapp.wicket.pages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.datazuul.framework.domain.Language;
import com.datazuul.framework.domain.geo.Country;
import com.datazuul.framework.domain.geo.GeoLocation;
import com.datazuul.framework.util.EnumUtils;
import com.datazuul.framework.webapp.wicket.components.form.ShinyForm;
import com.datazuul.framework.webapp.wicket.components.locale.SortableLocale;
import com.datazuul.framework.webapp.wicket.model.LoadableDetachableDomainObjectModel;
import com.pairoo.business.api.GeoLocationService;
import com.pairoo.business.api.LandingPageActionService;
import com.pairoo.business.exceptions.RegistrationException;
import com.pairoo.domain.LandingPageAction;
import com.pairoo.domain.User;
import com.pairoo.domain.enums.LandingPageActionType;
import com.pairoo.frontend.webapp.wicket.WicketWebSession;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.MyHomePage;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.RegistrationStep1Page;
import com.pairoo.frontend.webapp.wicket.panels.MyNaviPanel;
import com.pairoo.frontend.webapp.wicket.panels.RegistrationEntryPanel;
import com.pairoo.frontend.webapp.wicket.panels.StoryPanel;

/**
 * @author Ralf Eichinger
 */
public class HomePage extends NotAuthenticatedWebPage {

    private static final long serialVersionUID = 2L;
    @SpringBean(name = GeoLocationService.BEAN_ID)
    private GeoLocationService geoLocationService;
    @SpringBean(name = LandingPageActionService.BEAN_ID)
    private LandingPageActionService landingPageActionService;
//    protected MyNaviPanel myNaviPanel = null;

    public HomePage(final PageParameters params) {
	super(params);
    }

    public HomePage() {
	super();
    }

    @Override
    protected void onBeforeRender() {
	super.onBeforeRender();
	logEnter(HomePage.class);
    }

    @Override
    protected void onInitialize() {
	super.onInitialize();

	final WicketWebSession session = (WicketWebSession) getSession();
	if (session.isAuthenticated()) {
	    throw new RestartResponseException(new MyHomePage(new LoadableDetachableDomainObjectModel<Long>(
		    session.getUser(), userService)));
	}

	// personal navigation
//	myNaviPanel = createPersonalNavigationComponent("myNaviPanel");
//	add(myNaviPanel);

	// registration
	final ShinyForm form = createForm("form",
		new CompoundPropertyModel<>(userService.getRegistrationDefaultUser(getLocale())));
	add(form);
	form.add(registrationEntryPanel("registrationEntryPanel"));

	// outside form
	add(linkToReasonsPage("lnkReasons"));
	add(storyPanel("storyPanel"));
	add(linkToQualityPromisePage("lnkQuality"));
    }

//    private MyNaviPanel createPersonalNavigationComponent(String id) {
//	User user = getSessionUser();
//	IModel<User> model = null;
//	if (user != null) {
//	    model = new LoadableDetachableDomainObjectModel<Long>(user, userService);
//	}
//	return new MyNaviPanel(id, model);
//    }

    private Component storyPanel(final String id) {
	final Component c = new StoryPanel(id);
	return c;
    }

    private Link<Void> linkToQualityPromisePage(String id) {
	final Link<Void> c = new Link<Void>(id) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public void onClick() {
		setResponsePage(new QualityPromisePage() {
		    private static final long serialVersionUID = 1L;

		    @Override
		    protected void onGoBack() {
			setResponsePage(HomePage.this);
		    }
		});
	    }
	};
	c.setVisible(false);
	return c;
    }

    private Link<Void> linkToReasonsPage(String id) {
	final Link<Void> c = new Link<Void>(id) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public void onClick() {
		setResponsePage(new ReasonsPage() {
		    private static final long serialVersionUID = 1L;

		    @Override
		    protected void onGoBack() {
			setResponsePage(HomePage.this);
		    }
		});
	    }
	};
	c.setVisible(false);
	return c;
    }

    private RegistrationEntryPanel registrationEntryPanel(String id) {
	return new RegistrationEntryPanel(id);
    }

    private ShinyForm createForm(String id, IModel<User> model) {
	final ShinyForm form = new ShinyForm(id, model) {
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
		    get("registrationEntryPanel:userProfile.geoLocation.zipcode").error(
			    getString("error.no_geolocation_found_for_zipcode"));
		}
	    }

	    @Override
	    protected void onSubmit() {
		// update user model
		User user = (User) getDefaultModelObject();

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
		    user = userService.register(user, sessionLanguage.getLocale(), activationLink);
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
		    setDefaultModel(new CompoundPropertyModel<User>(cleanUser));
		    return;
		}

		// save landing page action
		landingPageActionService.save(landingPageAction);

		// store user (containing search profile and user profile)
		// in session for later registration
		final WicketWebSession session = (WicketWebSession) getSession();
		session.setUser(user);

		// setResponsePage(new RegistrationPage(user));
		setResponsePage(new RegistrationStep1Page(new LoadableDetachableDomainObjectModel<Long>(user,
			userService)));
	    }
	};
	// ---------------- form
	// form.add(new JSR303FormValidator()); // does not work completely...
	// add(new GenericUniformBehavior("input, textarea, select, button"));
	return form;
    }

    private Component languageLinkList(String id) {
	final List<Language> languages = applicationService.getAvailableTranslationLanguages();
	final List<Locale> sortedLocales = sortLocales(languages);

	final ListView<Locale> lv = new ListView<Locale>("lv", sortedLocales) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    protected void populateItem(final ListItem<Locale> item) {
		final Locale itemLocale = item.getModelObject();
		final String display = itemLocale.getDisplayName(getLocale());

		Link changeLanguageLink = new Link("changeLanguageLink", item.getModel()) {
		    @Override
		    public void onClick() {
			Locale locale = (Locale) getDefaultModelObject();
			getSession().setLocale(locale);
			LOGGER.info("Locale changed to: {}", getLocale());
		    }
		};
		item.add(changeLanguageLink);

		Label changeLanguageText = new Label("changeLanguageText", display);
		changeLanguageText.setRenderBodyOnly(true);
		changeLanguageLink.add(changeLanguageText);
	    }
	};
	lv.setOutputMarkupId(true);

	return lv;
    }

    private List<Locale> sortLocales(final List<Language> languages) {
	final ArrayList<SortableLocale> sortableChoicesList = new ArrayList<SortableLocale>();
	for (Language language : languages) {
	    String label = getLocalizer().getString(EnumUtils.getEnumKey(language), this, null, language.getLocale(),
		    null, null);

	    final SortableLocale sLocale = new SortableLocale(language.getLocale(), label);
	    sortableChoicesList.add(sLocale);
	}
	Collections.sort(sortableChoicesList);

	final List<Locale> sortedChoicesList = new ArrayList<Locale>();
	for (final SortableLocale sLocale : sortableChoicesList) {
	    final Locale locale = sLocale.getLocale();
	    sortedChoicesList.add(0, locale);
	}
	return sortedChoicesList;
    }
}
