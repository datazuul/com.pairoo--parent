/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pairoo.frontend.webapp.wicket.pages.authenticated;

import com.pairoo.business.api.CountryService;
import com.pairoo.business.api.PersonProfileService;
import com.pairoo.business.services.impl.CountryServiceImpl;
import com.pairoo.business.services.impl.PersonProfileServiceImpl;
import com.pairoo.domain.MockUser;
import com.pairoo.domain.User;
import com.pairoo.frontend.webapp.wicket.AbstractWicketTest;
import com.pairoo.frontend.webapp.wicket.WicketWebSession;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author ralf
 */
public class MyProfilePageTest extends AbstractWicketTest {

    final User mockUser = new MockUser();

    @Override
    @Before
    public void before() throws Exception {
        super.before();

        getAppContext().putBean(CountryService.BEAN_ID, new CountryServiceImpl());
        getAppContext().putBean(PersonProfileService.BEAN_ID, new PersonProfileServiceImpl());

        WicketWebSession session = (WicketWebSession) getTester().getSession();
        session.setUser(mockUser);
    }

    @Test
    public void testRendering() {
        final WicketTester tester = getTester();
        tester.startPage(new MyProfilePage(Model.of(mockUser)));
        tester.assertRenderedPage(MyProfilePage.class);
        // tester.dumpPage();
    }
}
