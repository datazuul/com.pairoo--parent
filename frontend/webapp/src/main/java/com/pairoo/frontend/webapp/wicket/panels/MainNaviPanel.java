package com.pairoo.frontend.webapp.wicket.panels;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.pairoo.domain.User;
import com.pairoo.frontend.webapp.wicket.WicketWebSession;

/**
 * @author Ralf Eichinger
 */
public class MainNaviPanel extends BasePanel {
    private static final long serialVersionUID = 1L;

    private Panel mainNavi;

    public MainNaviPanel(final String id, IModel<User> model) {
	super(id, model);
    }

    @Override
    protected void onInitialize() {
	super.onInitialize();
	
	WicketWebSession session = (WicketWebSession) getSession();
	if (!session.isAuthenticated()) {
	    // Login panel
	    mainNavi = new MainNaviNotAuthenticatedPanel("mainNavi");
	    mainNavi.setRenderBodyOnly(true);
	    add(mainNavi);
	} else {
	    mainNavi = new MainNaviAuthenticatedPanel("mainNavi", (IModel<User>) getDefaultModel());
	    mainNavi.setRenderBodyOnly(true);
	    add(mainNavi);
	}
    }
}
