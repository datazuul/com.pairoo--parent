package com.pairoo.frontend.webapp.wicket.pages.errors;

import com.pairoo.frontend.webapp.wicket.pages.NotAuthenticatedWebPage;
import org.apache.wicket.RestartResponseException;

/**
 * @author Ralf Eichinger
 */
public class PageExpiredErrorPage extends NotAuthenticatedWebPage {

    private static final long serialVersionUID = 1L;

    public PageExpiredErrorPage() {
        super();
        logEnter(PageExpiredErrorPage.class);

        getSession().error(getString("error.session_timeout"));
        throw new RestartResponseException(getApplication().getHomePage());
    }
}
