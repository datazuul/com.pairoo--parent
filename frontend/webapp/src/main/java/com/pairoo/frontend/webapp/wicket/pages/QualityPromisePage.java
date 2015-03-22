package com.pairoo.frontend.webapp.wicket.pages;

/**
 * @author Ralf Eichinger
 */
public abstract class QualityPromisePage extends BaseInterceptionPage {
    private static final long serialVersionUID = 1L;

    public QualityPromisePage() {
	super();
    }

    @Override
    protected void onBeforeRender() {
	super.onBeforeRender();
	logEnter(QualityPromisePage.class);
    }
}
