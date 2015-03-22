/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pairoo.frontend.webapp.wicket.pages;

import com.pairoo.frontend.webapp.wicket.AbstractWicketTest;
import com.pairoo.frontend.webapp.wicket.WicketWebSession;
import java.util.Locale;
import org.apache.wicket.Page;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author ralf
 */
public class QualityPromisePageTest extends AbstractWicketTest {

    @Before
    public void setUp() throws Exception {
        super.before();
    }

    @Test
    public void testRendering() {
        Page page = new QualityPromisePage() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onGoBack() {
            }
        };
        // mock session
        final WicketWebSession session = ((WicketWebSession) getTester().getSession());
        session.setLocale(Locale.ENGLISH);
        getTester().startPage(page);
        getTester().assertRenderedPage(QualityPromisePage.class);

        session.setLocale(Locale.GERMAN);
        getTester().startPage(page);
        getTester().assertRenderedPage(QualityPromisePage.class);
    }
}
