package com.pairoo.frontend.webapp.wicket;

import java.util.Locale;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.IRequestLogger.ISessionLogInfo;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datazuul.framework.domain.Language;
import com.datazuul.framework.domain.geo.Country;
import com.datazuul.framework.webapp.wicket.model.LoadableDetachableDomainObjectModel;
import com.pairoo.business.api.ApplicationService;
import com.pairoo.business.api.UserAccountService;
import com.pairoo.business.api.UserService;
import com.pairoo.domain.SearchProfile;
import com.pairoo.domain.User;
import com.pairoo.domain.UserAccount;

/**
 * @author Ralf Eichinger
 */
public class WicketWebSession extends WebSession implements ISessionLogInfo {

    private static final long serialVersionUID = 1L;
    static final Logger LOGGER = LoggerFactory
	    .getLogger(WicketWebSession.class);
    private IModel<User> userModel;
    private SearchProfile searchProfile = null;
    private Language sessionLanguage = null;
    @SpringBean(name = ApplicationService.BEAN_ID)
    private ApplicationService applicationService;
    @SpringBean(name = UserAccountService.BEAN_ID)
    private UserAccountService userAccountService;
    @SpringBean(name = UserService.BEAN_ID)
    private UserService userService;

    /**
     * Constructor
     * 
     * @param request
     *            The current request object
     */
    public WicketWebSession(final Request request) {
	super(request);
    }

    public void init() {
	Injector.get().inject(this);
	initSessionLanguage();
    }

    private void initSessionLanguage() {
	Locale locale = getLocale();
	setSessionLanguage(applicationService.getLanguage(locale));

	User sessionUser = getUser();
	if (sessionUser != null) {
	    UserAccount sessionUserAccount = sessionUser.getUserAccount();
	    if (sessionUserAccount != null) {
		Language preferredLanguageOfUser = sessionUserAccount
			.getPreferredLanguage();
		setSessionLanguage(preferredLanguageOfUser);
	    }
	}
    }

    @Override
    public void detach() {
	// detach the models you're holding in your custom session
	// by calling all their detach methods.
	if (userModel != null) {
	    userModel.detach();
	}
	super.detach();
    }

    public User getUser() {
	if (userModel == null) {
	    return null;
	}
	return userModel.getObject();
    }

    public boolean isAuthenticated() {
	return getUser() != null;
    }

    public void onBeforeDestroy() {
	if (isAuthenticated()) {
	    User user = getUser();
	    LOGGER.info("destroying session of user {}", user.getUserAccount()
		    .getUsername());
	    userAccountService.logout(user.getUserAccount());
	    userModel.detach();
	    userModel = null;
	}
    }

    /**
     * @param user
     *            the user to set
     */
    public void setUser(final User user) {
	this.userModel = new LoadableDetachableDomainObjectModel<Long>(user,
		userService);
    }

    @Override
    public Object getSessionInfo() {
	return "username="
		+ (getUser() == null ? "n/a" : getUser().getUserAccount()
			.getUsername());
    }

    /**
     * @return the searchProfile
     */
    public SearchProfile getSearchProfile() {
	return searchProfile;
    }

    /**
     * @param searchProfile
     *            the searchProfile to set
     */
    public void setSearchProfile(final SearchProfile searchProfile) {
	this.searchProfile = searchProfile;
    }

    public Language getSessionLanguage() {
	return sessionLanguage;
    }

    public void setSessionLanguage(Language sessionLanguage) {
	this.sessionLanguage = sessionLanguage;
	super.setLocale(sessionLanguage.getLocale());
    }

    @Override
    public void setLocale(Locale locale) {
        super.setLocale(locale);
        this.sessionLanguage = applicationService.getLanguage(locale);
    }
    
    public Country getUsersCountry() {
	try {
	    return getUser().getUserProfile().getGeoLocation().getCountry();
	} catch (Exception e) {
	    return Country.GERMANY;
	}
    }

    public String getUsername() {
	String result = "unknown";
	try {
	    result = getUser().getUserAccount().getUsername();
	} catch (Exception e) {
	}
	return result;
    }

    /**
     * Sign in user if possible.
     * 
     * @param username
     *            The username
     * @param password
     *            The password
     * @return User if signin was successful otherwise null
     */
    public User login(final String username, final String password) {
	final UserAccount userAccount = userAccountService.login(username,
		password);
	if (userAccount == null) {
	    LOGGER.info("user '{}' FAILED to log in", username);
	    return null;
	}
	return login(userAccount);
    }

    public User login(UserAccount userAccount) {
	if (userAccount != null) {
	    LOGGER.info("user '{}' logged in", userAccount.getUsername());
	    final User user = userAccount.getUser();
	    setUser(user);

	    Language preferredLanguageOfUser = userAccount.getPreferredLanguage();
	    if (preferredLanguageOfUser != null) {
		setSessionLanguage(preferredLanguageOfUser);
	    }
	    return user;
	}
	return null;
    }

    public void reloadUser() {
	Long userAccountId = getUser().getUserAccount().getId();
	UserAccount userAccount = userAccountService.get(userAccountId);
	setUser(userAccount.getUser());
    }

    public void setUserAccountService(UserAccountService userAccountService) {
	this.userAccountService = userAccountService;
    }

    public void setApplicationService(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }
}
