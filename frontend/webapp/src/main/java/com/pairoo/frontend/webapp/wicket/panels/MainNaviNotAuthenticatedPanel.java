package com.pairoo.frontend.webapp.wicket.panels;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;

import com.pairoo.frontend.webapp.wicket.pages.HomePage;

/**
 * @author Ralf Eichinger
 */
public class MainNaviNotAuthenticatedPanel extends BasePanel {
    // private static final Logger LOGGER =
    // LoggerFactory.getLogger(MainNaviNotAuthenticatedPanel.class);
    private static final long serialVersionUID = 1L;

    public MainNaviNotAuthenticatedPanel(final String id) {
	super(id);
    }

    @Override
    protected void onInitialize() {
	super.onInitialize();

	add(linkToHomePage("lnkHomePage"));
    }

    private Link<Void> linkToHomePage(String id) {
	return new BookmarkablePageLink<Void>(id, HomePage.class);
    }
}
