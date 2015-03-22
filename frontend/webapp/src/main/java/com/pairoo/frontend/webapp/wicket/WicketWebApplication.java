package com.pairoo.frontend.webapp.wicket;

import java.util.HashMap;

import javax.persistence.EntityNotFoundException;

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
import org.apache.wicket.protocol.http.IRequestLogger;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.component.IRequestableComponent;
import org.apache.wicket.request.cycle.AbstractRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.file.Folder;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.datazuul.framework.webapp.wicket.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import com.datazuul.framework.webapp.wicket.model.LoadableDetachableDomainObjectModel;
import com.datazuul.framework.webapp.wicket.util.resource.locator.MavenDevResourceStreamLocator;
import com.pairoo.business.api.ApplicationService;
//import com.jquery.JQueryResourceReference;
//import com.jquery.JQueryResourceReference.Version;
import com.pairoo.business.api.UserService;
import com.pairoo.domain.enums.Role;
import com.pairoo.frontend.webapp.wicket.authorization.UserRolesAuthorizer;
import com.pairoo.frontend.webapp.wicket.components.ImageResourceReference;
import com.pairoo.frontend.webapp.wicket.pages.HomePage;
import com.pairoo.frontend.webapp.wicket.pages.LandingPage;
import com.pairoo.frontend.webapp.wicket.pages.LoginPage;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.AuthenticatedWebPage;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.MembershipPage;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.ProfileDetailsPage;
import com.pairoo.frontend.webapp.wicket.pages.errors.EntityNotFoundErrorPage;
import com.pairoo.frontend.webapp.wicket.pages.errors.InternalErrorPage;
import com.pairoo.frontend.webapp.wicket.pages.errors.PageExpiredErrorPage;
import com.pairoo.frontend.webapp.wicket.panels.authenticated.SendMessagePanel;
import java.util.Set;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * The central WebApplication class for Pairoo. The web application needs
 * environment variables to work properly: - "env" = "DEMO" or "PROD" -
 * "systemEnv.pairoo.payoneMode" = "test" or "live"
 *
 * @author Ralf Eichinger
 */
public class WicketWebApplication extends WebApplication implements ApplicationContextAware {

    static final Logger LOGGER = LoggerFactory.getLogger(WicketWebApplication.class);
    private final HashMap<String, Session> sessionMap = new HashMap<>();
    private SpringComponentInjector springComponentInjector;
    private Folder uploadFolder = null;
    private String version = "unknown";
    @SpringBean(name = UserService.BEAN_ID)
    private UserService userService;

    public static final String ENV_DEMO = "DEMO";
    public static final String ENV_PRODUCTION = "PROD";
    
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

        initVersion();
        initWicketSettings();
        initExceptionHandling();
        initApplicationPages();
        initI18n();
        initSpringBeanInjection();
        initDevelopmentResourceLocator();
        initAuthorizationStrategy();
        initUpload();
        initSharedResources();
        initRequestLogger();
        // initBrowserInfo();
        // initSessionUnboundListener();
    }

    private void initExceptionHandling() {
        getRequestCycleListeners().add(new AbstractRequestCycleListener() {
            @Override
            public IRequestHandler onException(RequestCycle cycle, Exception ex) {
                if (ex instanceof EntityNotFoundException) {
                    throw new RestartResponseException(new EntityNotFoundErrorPage((EntityNotFoundException) ex));
                } else {
                    return super.onException(cycle, ex);
                }
            }
        });
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
//        Locale.setDefault(Locale.ENGLISH);
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
                        IRequestParameters requestParameters = RequestCycle.get().getRequest().getRequestParameters();
                        if (requestParameters != null) {
                            Set<String> parameterNames = requestParameters.getParameterNames();
                            PageParameters params = new PageParameters();
                            for (String parameterName : parameterNames) {
                                params.add(parameterName, requestParameters.getParameterValue(parameterName));
                            }
                            throw new RestartResponseAtInterceptPageException(LoginPage.class, params);
                        }
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
                    LOGGER.info("user {} not authorized to access {}", new Object[]{session.getUsername(),
                        componentClass});
                    throw new RestartResponseException(new MembershipPage(new LoadableDetachableDomainObjectModel<Long>(
                            session.getUser(), userService)));
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
                            new Object[]{session.getUsername(), action.getName(), component.getId()});
                    throw new RestartResponseException(new MembershipPage(new LoadableDetachableDomainObjectModel<Long>(
                            session.getUser(), userService)));
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
        MetaDataRoleAuthorizationStrategy.authorize(SendMessagePanel.class, Role.PREMIUM.getCode());
    }

    /**
     * Use a custom resource locator for development mode. (gets resources from
     * "src" directory and not from "target"
     */
    private void initDevelopmentResourceLocator() {
        if (RuntimeConfigurationType.DEVELOPMENT == getConfigurationType()) {
            getResourceSettings().setResourceStreamLocator(new MavenDevResourceStreamLocator());
            getResourceSettings().setResourcePollFrequency(Duration.ONE_SECOND);
        }
    }

    private void initSharedResources() {
        mountResource("/img/${id}/${size}", new ImageResourceReference());
        // http://localhost:28080/portal/landingPage?token=Liebe2013
        mountPage("/landingPage", LandingPage.class);
        // http://localhost:28080/portal/landingPage/Liebe2013
        // or http://localhost:28080/portal/landingPage/90f35e01-b0ec-4beb-ad8d-6ab0addcc404
        mountPage("/landingPage/${token}", LandingPage.class);
        // http://localhost:28080/portal/landingPage/90f35e01-b0ec-4beb-ad8d-6ab0addcc404/115
        mountPage("/landingPage/${token}/${param}", LandingPage.class);
        mountPage("/p/${visitorUserId}/${profileUserId}", ProfileDetailsPage.class);
//        mountPage("/p/${visitorUserId}/${profileUserId}/${tld}", ProfileDetailsPage.class);
    }

    /**
     * Enable bean injection via the \
     *
     * @SpringBean annotation in Wicket component classes
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
        final WicketWebSession session = new WicketWebSession(request);
        session.init();
        // session.setStyle("style1");
        // session.setStyle("style2");
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

    public String getEnvironment() {
        return System.getProperty("env");
    }
    
    private void initBrowserInfo() {
        getRequestCycleSettings().setGatherExtendedBrowserInfo(true);
    }
}
