package com.pairoo.frontend.webapp.wicket.pages.authenticated;

import com.datazuul.framework.webapp.wicket.markup.html.link.MenuGroupMember;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import com.pairoo.domain.User;
import com.pairoo.frontend.webapp.wicket.pages.groups.MenuGroup;
import com.pairoo.frontend.webapp.wicket.panels.MessagesInboxPanel;
import com.pairoo.frontend.webapp.wicket.panels.MessagesOutboxPanel;

/**
 * @author Ralf Eichinger
 */
public class MessagesPage extends AuthenticatedWebPage implements MenuGroupMember {
    private static final long serialVersionUID = 1L;

    public MessagesPage(IModel<User> model) {
	super(model);
    }

    @Override
    protected void onBeforeRender() {
	super.onBeforeRender();
	logEnter(MessagesPage.class);
    }

    @Override
    protected void onInitialize() {
	super.onInitialize();

	final int countNewMessages = 10; // TODO counter

	// create a list of ITab objects used to feed the tabbed panel
	final List<ITab> tabs = new ArrayList<ITab>();
	// title + " " + countNewMessages
	tabs.add(new AbstractTab(new ResourceModel("inbox")) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public Panel getPanel(final String panelId) {
		return new MessagesInboxPanel(panelId, (IModel<User>) getPage().getDefaultModel());
	    }
	});

	tabs.add(new AbstractTab(new ResourceModel("outbox")) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public Panel getPanel(final String panelId) {
		return new MessagesOutboxPanel(panelId, (IModel<User>) getPage().getDefaultModel());
	    }
	});

	// add the new tabbed panel, attribute modifier only used to switch
	// between different css variations
	add(new TabbedPanel("tabs", tabs));
    }

    @Override
    public Object getMenuGroup() {
        return MenuGroup.MESSAGES;
    }
}
