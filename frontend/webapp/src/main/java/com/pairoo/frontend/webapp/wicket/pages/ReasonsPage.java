package com.pairoo.frontend.webapp.wicket.pages;

/**
 * @author Ralf Eichinger
 */
public abstract class ReasonsPage extends BaseInterceptionPage {
    private static final long serialVersionUID = 1L;

    public ReasonsPage() {
	super();
    }

    @Override
    protected void onBeforeRender() {
	super.onBeforeRender();
	logEnter(ReasonsPage.class);
    }
}
