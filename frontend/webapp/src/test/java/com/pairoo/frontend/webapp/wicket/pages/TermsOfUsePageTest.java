package com.pairoo.frontend.webapp.wicket.pages;

import java.util.Locale;

import org.apache.wicket.Page;
import org.junit.Before;
import org.junit.Test;

import com.pairoo.frontend.webapp.wicket.AbstractWicketTest;
import com.pairoo.frontend.webapp.wicket.WicketWebSession;

public class TermsOfUsePageTest extends AbstractWicketTest {

    @Before
    @Override
    public void before() throws Exception {
        super.before();
    }

    @Test
    public void testRendering() {
        Page page = new TermsOfUsePage() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onGoBack() {
                // TODO Auto-generated method stub
            }
        };
        // mock session
        final WicketWebSession session = ((WicketWebSession) getTester().getSession());
        session.setLocale(Locale.ENGLISH);
        getTester().startPage(page);
        getTester().assertRenderedPage(TermsOfUsePage.class);
        getTester().dumpPage();

        session.setLocale(Locale.GERMAN);
        getTester().startPage(page);
        getTester().assertRenderedPage(TermsOfUsePage.class);
        getTester().dumpPage();
    }
}
