package com.pairoo.frontend.webapp.wicket.pages;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * @author Ralf Eichinger
 */
public class LogoutPage extends NotAuthenticatedWebPage {
    private static final long serialVersionUID = 1L;

    public LogoutPage(PageParameters params) {
	super(params);
    }
    
    public LogoutPage() {
	super();
    }

    @Override
    protected void onBeforeRender() {
	super.onBeforeRender();
	logEnter(LogoutPage.class);
    }

    @Override
    protected void onInitialize() {
	super.onInitialize();

	// info message
	info(getString("logout_successful"));

	add(linkToHomePage("lnkToHomePage"));
    }

    private Component linkToHomePage(String id) {
	return new BookmarkablePageLink<Void>(id, getApplication().getHomePage());
    }

}
