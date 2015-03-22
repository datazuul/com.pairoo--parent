package com.pairoo.frontend.webapp.wicket.pages;

/**
 * @author Ralf Eichinger
 */
public abstract class TermsOfUsePage extends BaseInterceptionPage {

    private static final long serialVersionUID = 1L;

    public TermsOfUsePage() {
        super();
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        logEnter(TermsOfUsePage.class);
    }
}
