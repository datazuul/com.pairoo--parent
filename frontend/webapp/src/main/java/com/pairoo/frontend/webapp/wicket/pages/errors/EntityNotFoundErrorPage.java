package com.pairoo.frontend.webapp.wicket.pages.errors;

import com.pairoo.frontend.webapp.wicket.pages.NotAuthenticatedWebPage;
import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityNotFoundErrorPage extends NotAuthenticatedWebPage {

    private static final long serialVersionUID = 1L;
    static final Logger LOGGER = LoggerFactory.getLogger(EntityNotFoundErrorPage.class);

    public EntityNotFoundErrorPage() {
        super();
        logEnter(EntityNotFoundErrorPage.class);

        // info message
        error(getString("error.data_changed"));
    }

    public EntityNotFoundErrorPage(EntityNotFoundException ex) {
        LOGGER.warn("Data changed during session by another user: " + ex.getMessage());
    }
}
