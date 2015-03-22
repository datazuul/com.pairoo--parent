package com.pairoo.frontend.webapp.wicket.pages.errors;

import com.pairoo.frontend.webapp.wicket.pages.NotAuthenticatedWebPage;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

/**
 * @author Ralf Eichinger
 */
public class InternalErrorPage extends NotAuthenticatedWebPage {

    private static final long serialVersionUID = 1L;

    public InternalErrorPage() {
        super();
        logEnter(InternalErrorPage.class);

        // info message
        error(getString("error.technical"));

        add(createHomePageLink());
    }

    private Component createHomePageLink() {
        return new BookmarkablePageLink<Void>("lnkToHomePage", getApplication().getHomePage());
    }
}
