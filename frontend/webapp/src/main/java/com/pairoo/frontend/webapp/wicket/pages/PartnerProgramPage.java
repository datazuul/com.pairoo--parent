package com.pairoo.frontend.webapp.wicket.pages;

/**
 * @author Ralf Eichinger
 */
public abstract class PartnerProgramPage extends BaseInterceptionPage {
    private static final long serialVersionUID = 1L;

    public PartnerProgramPage() {
	super();
    }

    @Override
    protected void onBeforeRender() {
	super.onBeforeRender();
	logEnter(PartnerProgramPage.class);
    }
}
