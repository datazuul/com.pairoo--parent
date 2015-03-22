/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pairoo.frontend.webapp.wicket.pages.authenticated;

import com.pairoo.business.api.CountryService;
import com.pairoo.business.api.FavoriteService;
import com.pairoo.business.api.MessageService;
import com.pairoo.business.api.PersonProfileService;
import com.pairoo.business.api.UserAccountService;
import com.pairoo.business.api.VisitService;
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
import static org.mockito.Mockito.*;

/**
 *
 * @author ralf
 */
public class MyHomePageTest extends AbstractWicketTest {

    final User mockUser = new MockUser();

    @Override
    @Before
    public void before() throws Exception {
        super.before();

        // 1. setup dependencies and mock objects
        final UserAccountService userAccountService = mock(UserAccountService.class);
        final MessageService messageService = mock(MessageService.class);
        final FavoriteService favoriteService = mock(FavoriteService.class);
        final VisitService visitService = mock(VisitService.class);

        // 2. setup mock injection environment
        getAppContext().putBean(CountryService.BEAN_ID, new CountryServiceImpl());
        getAppContext().putBean(FavoriteService.BEAN_ID, favoriteService);
        getAppContext().putBean(MessageService.BEAN_ID, messageService);
        getAppContext().putBean(PersonProfileService.BEAN_ID, new PersonProfileServiceImpl());
        getAppContext().putBean(UserAccountService.BEAN_ID, userAccountService);
        getAppContext().putBean(VisitService.BEAN_ID, visitService);

        WicketWebSession session = (WicketWebSession) getTester().getSession();
        session.setUser(mockUser);
    }

    @Test
    public void testRendering() {
        final WicketTester tester = getTester();
        tester.startPage(new MyHomePage(Model.of(mockUser)));
        tester.assertRenderedPage(MyHomePage.class);
        // tester.dumpPage();
    }
}
