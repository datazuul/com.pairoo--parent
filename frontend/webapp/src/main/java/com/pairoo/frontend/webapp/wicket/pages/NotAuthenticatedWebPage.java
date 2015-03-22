package com.pairoo.frontend.webapp.wicket.pages;

import com.datazuul.framework.webapp.wicket.model.LoadableDetachableDomainObjectModel;
import com.pairoo.domain.User;
import com.pairoo.frontend.webapp.wicket.WicketWebSession;
import com.pairoo.frontend.webapp.wicket.panels.MyNaviPanel;
import java.util.Locale;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * @author ralf
 */
public class NotAuthenticatedWebPage extends BasePage {
    protected MyNaviPanel myNaviPanel = null;
    
    public NotAuthenticatedWebPage() {
        super();
    }
    
    public NotAuthenticatedWebPage(final IModel<?> model) {
        super(model);
    }
    
    public NotAuthenticatedWebPage(final PageParameters params) {
        super(params);
        
        
	// RewriteEngine on
	// RewriteCond %{HTTP_HOST} !^pairoo.com
	// RewriteCond %{HTTP_HOST} ^pairoo\.(.*) [NC]
	// RewriteRule ^/(.*)$ https://www.pairoo.com/$1?tld=%1 [QSA,L,R]

	// all TLDs other than .com will have the language parameter "tld"
	final String topLevelDomain = params.get("tld").toString();
	if (topLevelDomain != null) {
	    final Locale locale = applicationService.getLocaleForTLD(topLevelDomain);
	    final WicketWebSession session = (WicketWebSession) getSession();
	    // set locale/language for current session
	    session.setLocale(locale);
	}
        
        final String preferredLanguage = params.get("pl").toString();
	if (preferredLanguage != null) {
	    final Locale locale = applicationService.getLocaleForLanguage(preferredLanguage);
	    final WicketWebSession session = (WicketWebSession) getSession();
	    // set locale/language for current session
	    session.setLocale(locale);
	}
    }
    
    @Override
    protected void onInitialize() {
        super.onInitialize();
        
        // personal navigation
        myNaviPanel = createPersonalNavigationComponent("myNaviPanel");
        add(myNaviPanel);
    }
    
    private MyNaviPanel createPersonalNavigationComponent(String id) {
        User user = getSessionUser();
        IModel<User> model = null;
        if (user != null) {
            model = new LoadableDetachableDomainObjectModel<Long>(user, userService);
        }
        return new MyNaviPanel(id, model);
    }
}
