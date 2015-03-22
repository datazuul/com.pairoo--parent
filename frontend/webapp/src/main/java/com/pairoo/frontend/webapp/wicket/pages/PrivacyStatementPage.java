package com.pairoo.frontend.webapp.wicket.pages;

/**
 * @author Ralf Eichinger
 */
public abstract class PrivacyStatementPage extends BaseInterceptionPage {

    private static final long serialVersionUID = 1L;

    public PrivacyStatementPage() {
        super();
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        logEnter(PrivacyStatementPage.class);
    }
}
