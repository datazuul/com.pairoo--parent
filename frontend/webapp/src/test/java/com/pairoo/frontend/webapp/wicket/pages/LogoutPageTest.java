/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pairoo.frontend.webapp.wicket.pages;

import com.pairoo.frontend.webapp.wicket.AbstractWicketTest;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author ralf
 */
public class LogoutPageTest extends AbstractWicketTest {

    @Before
    public void setUp() throws Exception {
        super.before();
    }

    @Test
    public void testRendering() {
        final WicketTester tester = getTester();
        tester.startPage(LogoutPage.class);
        tester.assertRenderedPage(LogoutPage.class);
    }
}
