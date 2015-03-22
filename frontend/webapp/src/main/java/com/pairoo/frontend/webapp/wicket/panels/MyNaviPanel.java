package com.pairoo.frontend.webapp.wicket.panels;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.pairoo.domain.User;
import com.pairoo.frontend.webapp.wicket.WicketWebSession;

/**
 * @author Ralf Eichinger
 */
public class MyNaviPanel extends BasePanel {
    private static final long serialVersionUID = 1L;

    private Panel myNavi;

    public MyNaviPanel(final String id, IModel<User> model) {
	super(id, model);
    }

    @Override
    protected void onInitialize() {
	super.onInitialize();

	IModel<User> model = (IModel<User>) getDefaultModel();
	if (!((WicketWebSession) getSession()).isAuthenticated()) {
	    // Login panel
	    myNavi = new MyNaviNotAuthenticatedPanel("myNavi", model, false);
	    myNavi.setRenderBodyOnly(true);
	    add(myNavi);
	} else {
	    myNavi = new MyNaviAuthenticatedPanel("myNavi", model);
	    myNavi.setRenderBodyOnly(true);
	    add(myNavi);
	}

    }
}
