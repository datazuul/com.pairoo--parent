package com.pairoo.frontend.webapp.wicket.pages;

import java.util.Locale;

import org.apache.wicket.Page;
import org.junit.Before;
import org.junit.Test;

import com.pairoo.frontend.webapp.wicket.AbstractWicketTest;
import com.pairoo.frontend.webapp.wicket.WicketWebSession;

public class GuidePageTest extends AbstractWicketTest {

    @Before
    @Override
    public void before() throws Exception {
        super.before();
    }

    @Test
    public void testRendering() {
        Page page = new GuidePage() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onGoBack() {
            }
        };
        // mock session
        final WicketWebSession session = ((WicketWebSession) getTester().getSession());
        session.setLocale(Locale.ENGLISH);
        getTester().startPage(page);
        getTester().assertRenderedPage(GuidePage.class);

        session.setLocale(Locale.GERMAN);
        getTester().startPage(page);
        getTester().assertRenderedPage(GuidePage.class);
    }
}
