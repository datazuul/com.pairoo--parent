package com.pairoo.frontend.webapp.wicket;

import java.util.Locale;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;

import com.datazuul.framework.domain.Language;
import com.pairoo.business.services.impl.ApplicationServiceImpl;
import com.pairoo.frontend.webapp.wicket.pages.HomePage;

public class WicketWebApplicationMock extends WicketWebApplication {

    @Override
    public void init() {
    }

    @Override
    public Class<? extends Page> getHomePage() {
	return HomePage.class;
    }

    @Override
    public Session newSession(final Request request, final Response response) {
	final WicketWebSession session = new WicketWebSession(request);
	session.setApplicationService(new ApplicationServiceImpl());
	session.setSessionLanguage(Language.ENGLISH);
	session.setLocale(Locale.ENGLISH);
	// session.setStyle("style2");
	return session;
    }
}
