package com.pairoo.frontend.webapp.admin.wicket;

import java.util.HashMap;
import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.Localizer;
import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.Session;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.authorization.strategies.CompoundAuthorizationStrategy;
import org.apache.wicket.authorization.strategies.page.SimplePageAuthorizationStrategy;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.protocol.http.IRequestLogger;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.component.IRequestableComponent;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.file.Folder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.datazuul.framework.domain.authorization.Roles;
import com.datazuul.framework.webapp.wicket.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import com.datazuul.framework.webapp.wicket.util.resource.locator.MavenDevResourceStreamLocator;
import com.jquery.JQueryResourceReference;
import com.jquery.JQueryResourceReference.Version;
import com.pairoo.frontend.webapp.wicket.authorization.UserRolesAuthorizer;
import com.pairoo.frontend.webapp.wicket.components.ImageResourceReference;
import com.pairoo.frontend.webapp.wicket.pages.HomePage;
import com.pairoo.frontend.webapp.wicket.pages.LandingPage;
import com.pairoo.frontend.webapp.wicket.pages.LoginPage;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.AuthenticatedWebPage;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.MembershipPage;
import com.pairoo.frontend.webapp.wicket.pages.errors.InternalErrorPage;
import com.pairoo.frontend.webapp.wicket.pages.errors.PageExpiredErrorPage;
import com.pairoo.frontend.webapp.wicket.panels.authenticated.SendMessagePanel;

/**
 * The central WebApplication class for Pairoo. The web application needs
 * environment variables to work properly: - "env" = "DEMO" or "PROD" -
 * "systemEnv.pairoo.payoneMode" = "test" or "live"
 * 
 * @author Ralf Eichinger
 */
public class WicketWebApplication extends WebApplication implements ApplicationContextAware {
    static final Logger LOGGER = LoggerFactory.getLogger(WicketWebApplication.class);

    private final HashMap<String, Session> sessionMap = new HashMap<String, Session>();
    private SpringComponentInjector springComponentInjector;

    private Folder uploadFolder = null;
    private String version = "unknown";

    public ApplicationContext getApplicationContext() {
	return WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<? extends Page> getHomePage() {
	return HomePage.class;
    }

    /**
     * @return the folder for uploads
     */
    public Folder getUploadFolder() {
	return uploadFolder;
    }

    @Override
    protected void init() {
	super.init();

	// init jqwicket
	// super.getComponentPreOnBeforeRenderListeners().add(
	// new JQComponentOnBeforeRenderListener(new
	// JQContributionConfig().withDefaultJQueryUi()));

	initVersion();
	initWicketSettings();
	initApplicationPages();
	initI18n();
	initSpringBeanInjection();
	initDevelopmentResourceLocator();
	initAuthorizationStrategy();
	initVisuralWicket();
	initUpload();
	initSharedResources();
	initRequestLogger();
	// initSessionUnboundListener();
    }

    private void initVersion() {
	version = (String) getServletContext().getAttribute("version");
    }

    private void initApplicationPages() {
	getApplicationSettings().setPageExpiredErrorPage(PageExpiredErrorPage.class);
	getApplicationSettings().setInternalErrorPage(InternalErrorPage.class);
    }

    // private void initSessionUnboundListener() {
    // getSessionStore().getUnboundListener().add(new UnboundListener() {
    // @Override
    // public void sessionUnbound(final String sessionId) {
    // final WicketWebSession session = (WicketWebSession)
    // sessionMap.get(sessionId);
    // if (session != null) {
    // session.onBeforeDestroy();
    // session.invalidate();
    // }
    // sessionMap.remove(sessionId);
    // }
    // });
    // }

    private void initWicketSettings() {
	getMarkupSettings().setStripWicketTags(true);
    }

    private void initI18n() {
	/*
	 * It reads like Wicket tries three things the find out about the
	 * encoding of a markup file: 1. Evaluates the <?xml ... ?> declaration
	 * in beginning of the markup file. (Missing for you?) 2. Uses the
	 * default encoding specified by
	 * Application#getMarkupSettings().setDefaultMarkupEncoding(String) 3.
	 * Uses the OS default.
	 */
	getMarkupSettings().setDefaultMarkupEncoding("UTF-8");
	getRequestCycleSettings().setResponseRequestEncoding("UTF-8");
	Locale.setDefault(Locale.ENGLISH);
    }

    private void initRequestLogger() {
	// deactivated as it does not provide what we want...
	getRequestLoggerSettings().setRequestLoggerEnabled(false);
	// change the number of stored requests (by default, it keeps track of
	// the last 2000 requests for programmatic access):
	getRequestLoggerSettings().setRequestsWindowSize(100);

	// add org.apache.wicket.protocol.http.RequestLogger=INFO to logging
	// configuration

	// implement ISessionLogInfo in application specific Session
	// implementation
    }

    private void initAuthorizationStrategy() {
	final CompoundAuthorizationStrategy compoundAuthorizationStrategy = new CompoundAuthorizationStrategy();

	final IAuthorizationStrategy authenticatedCheck = new SimplePageAuthorizationStrategy(
		AuthenticatedWebPage.class, LoginPage.class) {
	    @Override
	    protected boolean isAuthorized() {
		// Is user signed in?
		if (((WicketWebSession) Session.get()).isAuthenticated()) {
		    // okay to proceed
		    return true;
		}

		// Force sign in
		throw new RestartResponseAtInterceptPageException(LoginPage.class);
	    }
	};

	// IAuthorizationStrategy authorizedCheck = new
	// RoleAuthorizationStrategy(new UserRolesAuthorizer()) {};
	compoundAuthorizationStrategy.add(new MetaDataRoleAuthorizationStrategy(new UserRolesAuthorizer()) {
	    @Override
	    public <T extends IRequestableComponent> boolean isInstantiationAuthorized(final Class<T> componentClass) {
		final boolean authorized = super.isInstantiationAuthorized(componentClass);
		if (!authorized) {
		    final WicketWebSession session = (WicketWebSession) Session.get();
		    session.error(Localizer.get().getString("error.restricted_to_premium_members", null));
		    LOGGER.info("user {} not authorized to access {}", new Object[] { session.getUsername(),
			    componentClass });
		    throw new RestartResponseException(new MembershipPage());
		}
		return authorized;
	    }

	    @Override
	    public boolean isActionAuthorized(final Component component, final Action action) {
		final boolean authorized = super.isActionAuthorized(component, action);
		if (!authorized) {
		    final WicketWebSession session = (WicketWebSession) Session.get();
		    session.info(Localizer.get().getString("error.restricted_to_premium_members", null));
		    LOGGER.info("user {} not authorized for action {} of component id {}",
			    new Object[] { session.getUsername(), action.getName(), component.getId() });
		    throw new RestartResponseException(new MembershipPage());
		}
		return authorized;
	    }
	});
	compoundAuthorizationStrategy.add(authenticatedCheck);
	// compoundAuthorizationStrategy.add(authorizedCheck);

	getSecuritySettings().setAuthorizationStrategy(compoundAuthorizationStrategy);

	// role base authorization for pages/panels etc.:
	// MetaDataRoleAuthorizationStrategy.authorize(MessagesPage.class,
	// Roles.PREMIUM);
	MetaDataRoleAuthorizationStrategy.authorize(SendMessagePanel.class, Roles.PREMIUM);
    }

    /**
     * Use a custom resource locator for development mode. (gets resources from
     * "src" directory and not from "target"
     */
    private void initDevelopmentResourceLocator() {
	if (RuntimeConfigurationType.DEVELOPMENT == getConfigurationType()) {
	    getResourceSettings().setResourceStreamLocator(new MavenDevResourceStreamLocator());
	}
    }

    private void initSharedResources() {
	mountResource("/img/${id}/${size}", new ImageResourceReference());
	mountPage("/landingPage", LandingPage.class);
    }

    /**
     * Enable bean injection via the \@SpringBean annotation in Wicket component
     * classes
     */
    private void initSpringBeanInjection() {
	if (springComponentInjector == null) {
	    springComponentInjector = new SpringComponentInjector(this);
	    getComponentInstantiationListeners().add(springComponentInjector);
	}
    }

    private void initUpload() {
	getResourceSettings().setThrowExceptionOnMissingResource(false);

	uploadFolder = new Folder(System.getProperty("java.io.tmpdir"), "wicket-uploads");
	// Ensure folder exists
	uploadFolder.mkdirs();
	LOGGER.info("using upload folder {}", uploadFolder.getAbsolutePath());
	getApplicationSettings().setUploadProgressUpdatesEnabled(true);
    }

    private void initVisuralWicket() {
	getHeaderContributorListenerCollection().add(new IHeaderContributor() {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public void renderHead(final IHeaderResponse response) {
		response.renderJavaScriptReference(new JQueryResourceReference(Version.V1_3_2));
	    }
	});
    }

    @Override
    protected IRequestLogger newRequestLogger() {
	return super.newRequestLogger();
	// return new RequestLogger();
	// not used custom one as it also logs non-page requests and can not be
	// customized to log page classnames (sometimes renders something like
	// Homepage$xxx)
	// using logEnter() in BasePage explicitly instead
    }

    @Override
    public Session newSession(final Request request, final Response response) {
	// return new WicketWebSession(request).setStyle("style1");
	final WicketWebSession session = new WicketWebSession(request);
	session.init();
	session.setStyle("style2");
	final String sessionId = getSessionStore().getSessionId(request, true);
	sessionMap.put(sessionId, session);
	return session;
    }

    @Override
    public void sessionUnbound(final String sessionId) {
	final WicketWebSession session = (WicketWebSession) sessionMap.get(sessionId);
	if (session != null) {
	    session.onBeforeDestroy();
	    try {
		session.invalidate();
	    } catch (final WicketRuntimeException e) {
		/*
		 * ERROR
		 * 
		 * Problem scavenging sessions
		 * org.apache.wicket.WicketRuntimeException: There is no
		 * application attached to current thread Timer-0 at
		 * org.apache.wicket.Application.get(Application.java:230) at
		 * org.apache.wicket.Session.getApplication(Session.java:327) at
		 * org
		 * .apache.wicket.protocol.http.WebSession.invalidate(WebSession
		 * .java:166)
		 */
	    }
	    sessionMap.remove(sessionId);
	}
	super.sessionUnbound(sessionId);
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
	springComponentInjector = new SpringComponentInjector(this, applicationContext, true);
	getComponentInstantiationListeners().add(springComponentInjector);
    }

    public String getVersion() {
	return version;
    }
}
