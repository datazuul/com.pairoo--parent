package com.pairoo.frontend.webapp.wicket.pages;

/**
 * @author Ralf Eichinger
 */
public abstract class GuidePage extends BaseInterceptionPage {

    private static final long serialVersionUID = 1L;

    public GuidePage() {
        super();
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        logEnter(GuidePage.class);
    }
}
