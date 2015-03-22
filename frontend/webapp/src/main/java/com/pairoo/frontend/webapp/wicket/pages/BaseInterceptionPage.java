package com.pairoo.frontend.webapp.wicket.pages;

import org.apache.wicket.markup.html.link.Link;

/**
 * @author Ralf Eichinger
 */
public abstract class BaseInterceptionPage extends NotAuthenticatedWebPage {
    private static final long serialVersionUID = 1L;

    public BaseInterceptionPage() {
	super();
    }

    @Override
    protected void onInitialize() {
	super.onInitialize();

	// link to previous page
	add(backLink("lnkBack"));
    }

    private Link<Void> backLink(String id) {
	final Link<Void> lnkBack = new Link<Void>(id) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public void onClick() {
		onGoBack();
	    }
	};
	return lnkBack;
    }

    protected abstract void onGoBack();
}
