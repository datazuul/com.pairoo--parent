package com.pairoo.frontend.webapp.wicket.pages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.Application;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.bootstrap.Bootstrap;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.GenericWebPage;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.FormComponentLabel;
import org.apache.wicket.markup.html.form.SimpleFormComponentLabel;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.ClientProperties;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datazuul.framework.domain.Language;
import com.datazuul.framework.util.EnumUtils;
import com.datazuul.framework.webapp.wicket.components.locale.LanguageSwitcherPanel;
import com.datazuul.framework.webapp.wicket.components.locale.SortableLocale;
import com.datazuul.framework.webapp.wicket.model.LoadableDetachableDomainObjectModel;
import com.pairoo.business.api.ApplicationService;
import com.pairoo.business.api.UserAccountService;
import com.pairoo.business.api.UserService;
import com.pairoo.domain.User;
import com.pairoo.domain.UserAccount;
import com.pairoo.frontend.webapp.wicket.WicketWebApplication;
import com.pairoo.frontend.webapp.wicket.WicketWebSession;
import com.pairoo.frontend.webapp.wicket.components.ContextImageConstants;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.MyHomePage;
import com.pairoo.frontend.webapp.wicket.panels.FooterNaviPanel;
import com.pairoo.frontend.webapp.wicket.panels.MainNaviPanel;
import com.pairoo.frontend.webapp.wicket.panels.SupportPanel;

/**
 * Base page class.
 * <p>
 * The markup of this files provides some common html as well as includes a
 * reference to the css file that all other pages inherit through wicket's
 * markup inheritance feature.
 * 
 * @author Ralf Eichinger
 */
public class BasePage extends GenericWebPage {

    private static final long serialVersionUID = 2L;
    static final Logger LOGGER = LoggerFactory.getLogger(BasePage.class);
    @SpringBean(name = ApplicationService.BEAN_ID)
    protected ApplicationService applicationService;
    @SpringBean(name = UserAccountService.BEAN_ID)
    private UserAccountService userAccountService;
    @SpringBean(name = UserService.BEAN_ID)
    public UserService userService;
    private FeedbackPanel feedbackPanel;
    protected Component emailSupport;
    protected IModel<User> userModel;
    private WebMarkupContainer htmlTag;

    public BasePage() {
	super((IModel<?>) null);
    }

    public BasePage(final PageParameters params) {
	super(params);
    }

    public BasePage(final IModel<?> model) {
	super(model);
    }

    public BasePage(final IModel<?> model, final IModel<User> userModel) {
	super(model);
	this.userModel = userModel;
    }

    @Override
    protected void onDetach() {
	if (userModel != null) {
	    userModel.detach();
	}
	super.onDetach();
    }

    /**
     * Add Javascript Libraries to head (DOJO, EXT_CORE, JQUERY, JQUERY_UI,
     * MOOTOOLS_CORE, MOOTOOLS_MORE, PROTOTYPE, SCRIPTACULOUS, SWFOBJECT, YUI).
     */
    @Override
    public void renderHead(IHeaderResponse response) {
	super.renderHead(response);

	Bootstrap.renderHeadResponsive(response);
    }

    @Override
    protected void onInitialize() {
	super.onInitialize();

	// add(new HtmlTag("html"));
	// super.add(browserSpecificHtmlTag("html"));

	// version
	add(metaTagForWebappVersion("version"));
	// add(new OptimizedMobileViewportMetaTag("viewport"));
	// add(new ChromeFrameMetaTag("chrome-frame"));
	// add(new MetaTag("description", Model.of("description"),
	// Model.of("pairoo.com")));
	// add(new MetaTag("author", Model.of("author"),
	// Model.of("Pairoo GmbH")));

	// language dropdown
	add(currentLocaleLabel("currentLocale"));
	add(languageLinkList("languageDropDown"));

	// A container that renders the content that was bucketed into a certain
	// bucket
	// by FilteringHeaderResponse. Note that this container renders only its
	// body by default.
	// add(new HeaderResponseContainer("footer-container",
	// "footer-container"));

	// add(new com.google.excanvas.ExCanvasHeaderContributor());

	// language switcher panel
	// add(new SwitchLanguagePanel("switchLanguagePanel",
	// Arrays.asList(Locale
	// .getAvailableLocales())));
	// WicketWebApplication.LOCALES));

	// home link
	add(linkToHomePage("lnkHomePage"));

	// feedback
	add(createFeedbackComponent());

	// // main navigation
	// final MainNaviPanel mainNaviPanel = createMainNavigationComponent();
	// add(mainNaviPanel);
	// mainNaviPanel.visitChildren(MenuGroupLink.class, new
	// MenuGroupLinkVisitor());

	// // language switcher
	// add(createLanguageSwitcherComponent());
	//
	// footer navigation
	add(footerNavigation("footerNavi"));
	//
	// // support panel
	// // TODO support panel replaced with static content until phone
	// support
	// // is there:
	// Component supportPanel = createSupportPanel("supportPanel");
	// supportPanel.setVisible(false);
	// add(supportPanel);
	//
	// emailSupport = createEmailSupportComponent("emailSupport");
	// add(emailSupport);
    }

    private Component metaTagForWebappVersion(String id) {
	WebComponent c = new WebComponent(id);
	c.add(new AttributeModifier("content", ((WicketWebApplication) Application.get()).getVersion()));
	return c;
    }

    private Component createEmailSupportComponent(String id) {
	IModel<String> pathModel = new Model<String>() {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public String getObject() {
		String contextRelativePath = ContextImageConstants.TEXT_IMAGES_PATH + "/" + getLocale().getLanguage()
			+ "/" + ContextImageConstants.EMAIL_SUPPORT;
		return contextRelativePath;
	    }
	};
	Component c = new ContextImage(id, pathModel);
	return c;
    }

    private Component createSupportPanel(String id) {
	SupportPanel supportPanel = new SupportPanel(id);
	return supportPanel;
    }

    private FooterNaviPanel footerNavigation(String id) {
	FooterNaviPanel footerNaviPanel = new FooterNaviPanel(id);
	footerNaviPanel.setRenderBodyOnly(true);
	return footerNaviPanel;
    }

    private LanguageSwitcherPanel createLanguageSwitcherComponent() {
	final List<Language> availableTranslationLanguages = applicationService.getAvailableTranslationLanguages();
	final LanguageSwitcherPanel switchLanguagePanel = new LanguageSwitcherPanel("switchLanguagePanel",
		availableTranslationLanguages) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public void setLocale(final Locale locale) {
		final WicketWebSession session = (WicketWebSession) getSession();
		// set locale/language for current session
		session.setLocale(locale);
		// store as preferred language
		final Language language = applicationService.getLanguage(locale);
		final User user = session.getUser();
		if (user != null) {
		    final UserAccount userAccount = user.getUserAccount();
		    userAccount.setPreferredLanguage(language);
		    userAccountService.save(userAccount);
		}
	    }
	};
	return switchLanguagePanel;
    }

    private MainNaviPanel createMainNavigationComponent() {
	User user = getSessionUser();
	IModel<User> model = null;
	if (user != null) {
	    model = new LoadableDetachableDomainObjectModel<Long>(user, userService);
	}
	return new MainNaviPanel("mainNavi", model);
    }

    private FeedbackPanel createFeedbackComponent() {
	this.feedbackPanel = new FeedbackPanel("status");
	// {
	// @Override
	// protected String getCSSClass(final FeedbackMessage message) {
	// String cssClass = "alert";
	// if (message.isLevel(FeedbackMessage.ERROR)) {
	// cssClass += " alert-error";
	// } else if (message.isLevel(FeedbackMessage.WARNING)) {
	// // "alert" itself is warning
	// } else if (message.isLevel(FeedbackMessage.INFO)) {
	// cssClass += " alert-success";
	// }
	// return cssClass;
	// }
	// };
	this.feedbackPanel.setOutputMarkupId(true);
	return feedbackPanel;
    }

    private Link linkToHomePage(String id) {
	Link lnkHomePage = null;
	if (!isAuthenticated()) {
	    lnkHomePage = new BookmarkablePageLink<Void>(id, getApplication().getHomePage());
	} else {
	    User user = getSessionUser();
	    lnkHomePage = linkToMyHomePage(id, new LoadableDetachableDomainObjectModel<Long>(user, userService));
	}
	return lnkHomePage;
    }

    /**
     * Hint: never use this.getClass() when calling this method. It will not
     * work.
     * 
     * @param pageClass
     *            class of page
     */
    protected void logEnter(final Class<? extends Page> pageClass) {
	logEnter(pageClass.getSimpleName());

    }

    private void logEnter(String className) {
	if (className != null && !className.isEmpty()) {
	    WicketWebSession session = (WicketWebSession) getSession();
	    String username = "(not logged in)";
	    if (session.isAuthenticated()) {
		username = session.getUser().getUserAccount().getUsername();
	    }
	    LOGGER.info("'{}'/'{}' ENTERING page '{}'", new Object[] { session.getId(), username, className });
	} else {
	    LOGGER.info("ENTERING unknown page");
	}
    }

    public FeedbackPanel getFeedbackPanel() {
	return this.feedbackPanel;
    }

    protected User getSessionUser() {
	final WicketWebSession session = (WicketWebSession) getSession();
	return session.getUser();
    }

    public boolean isAuthenticated() {
	return getSessionUser() != null;
    }

    protected FormComponentLabel createFormComponentLabel(final String id, final FormComponent<?> formComponent) {
	return new SimpleFormComponentLabel(id, formComponent);
    }

    private Link<User> linkToMyHomePage(String id, IModel<User> model) {
	Link<User> link = new Link<User>(id, model) {
	    private static final long serialVersionUID = 1L;

	    public void onClick() {
		IModel<User> model = (IModel<User>) getDefaultModel();
		setResponsePage(new MyHomePage(model));
	    }
	};
	return link;
    }

    private Component browserSpecificHtmlTag(String id) {
	htmlTag = new WebMarkupContainer(id);

	WicketWebSession session = (WicketWebSession) getSession();
	ClientProperties clientProperties = session.getClientInfo().getProperties();

	if (clientProperties != null) {
	    int browserVersionMajor = clientProperties.getBrowserVersionMajor();
	    String classAttribute = "";
	    if (clientProperties.isBrowserInternetExplorer()) {
		if (browserVersionMajor < 7) {
		    classAttribute = "lt-ie9 lt-ie8 lt-ie7";
		} else if (browserVersionMajor == 7) {
		    // TODO check if it is not an mobile IE
		    classAttribute = "lt-ie9 lt-ie8";
		} else if (browserVersionMajor == 8) {
		    // TODO check if it is not an mobile IE
		    classAttribute = "lt-ie9";
		}
	    }

	    /*
	     * <!--[if lt IE 7]><html class="no-js lt-ie9 lt-ie8 lt-ie7"
	     * lang="en"> <![endif]--> <!--[if (IE 7)&!(IEMobile)]><html
	     * class="no-js lt-ie9 lt-ie8" lang="en"><![endif]--> <!--[if (IE
	     * 8)&!(IEMobile)]><html class="no-js lt-ie9" lang="en"><![endif]-->
	     * <!--[if gt IE 8]><!--> <html class="no-js" lang="en">
	     * <!--<![endif]-->
	     */

	    htmlTag.add(new AttributeAppender("class", classAttribute));
	}
	return htmlTag;
    }

    private Component languageLinkList(String id) {
	final List<Language> languages = applicationService.getAvailableTranslationLanguages();
	final List<Locale> sortedLocales = sortLocales(languages);

	final ListView<Locale> lv = new ListView<Locale>(id, sortedLocales) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    protected void populateItem(final ListItem<Locale> item) {
		final Locale itemLocale = item.getModelObject();
		final String display = itemLocale.getDisplayName(itemLocale);

		Link<Locale> changeLanguageLink = new Link<Locale>("changeLanguageLink", item.getModel()) {
		    private static final long serialVersionUID = 1L;

		    @Override
		    public void onClick() {
			Locale locale = (Locale) getDefaultModelObject();
			WicketWebSession session = ((WicketWebSession) getSession());
			Language chosenLanguage = applicationService.getLanguage(locale);
			session.setSessionLanguage(chosenLanguage);
			User sessionUser = session.getUser();
			if (sessionUser != null) {
			    UserAccount sessionUserAccount = sessionUser.getUserAccount();
			    if (sessionUserAccount != null) {
				sessionUserAccount.setPreferredLanguage(chosenLanguage);
				userAccountService.save(sessionUserAccount);
			    }
			}
			LOGGER.info("Locale changed to: {}", locale);
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

    private Label currentLocaleLabel(String id) {
	Label l = new Label(id, new Model() {
	    @Override
	    public Serializable getObject() {
		return getLocale().getDisplayLanguage(getLocale());
	    }
	});
	return l;
    }
}
