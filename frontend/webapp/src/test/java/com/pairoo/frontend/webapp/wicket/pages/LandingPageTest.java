package com.pairoo.frontend.webapp.wicket.pages;

import com.datazuul.framework.domain.Language;
import com.pairoo.business.api.CountryService;
import com.pairoo.business.api.FavoriteService;
import com.pairoo.business.api.LandingPageActionService;
import com.pairoo.business.api.MembershipService;
import com.pairoo.business.api.MessageService;
import com.pairoo.business.api.PersonProfileService;
import com.pairoo.business.api.UserAccountService;
import com.pairoo.business.api.VisitService;
import com.pairoo.business.services.impl.CountryServiceImpl;
import com.pairoo.business.services.impl.PersonProfileServiceImpl;
import com.pairoo.domain.LandingPageAction;
import com.pairoo.domain.User;
import com.pairoo.domain.UserAccount;
import com.pairoo.domain.Visit;
import com.pairoo.domain.enums.LandingPageActionType;
import com.pairoo.frontend.webapp.wicket.AbstractWicketTest;
import com.pairoo.frontend.webapp.wicket.WicketWebSession;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.MyHomePage;
import java.util.ArrayList;
import java.util.Locale;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class LandingPageTest extends AbstractWicketTest {

    @Override
    @Before
    public void before() throws Exception {
        super.before();
        // 1. setup dependencies and mock objects
        final User user = new User();
        final UserAccount userAccount = new UserAccount();
        userAccount.setPreferredLanguage(Language.ENGLISH);
        userAccount.setUser(user);
        userAccount.setPassword("password");
        userAccount.setUsername("username");

        // mock session
        // final WicketWebSession session = spy(((WicketWebSession) getTester().getSession()));
        // session.setLocale(Locale.ENGLISH);
        // when(session.login(any(String.class), any(String.class))).thenReturn(user);
        // doReturn(user).when(session).login(anyString(), anyString());
        final WicketWebSession session = (((WicketWebSession) getTester().getSession()));
        session.setLocale(Locale.ENGLISH);

        // 2. setup mock services
        final LandingPageActionService landingPageActionService = mock(LandingPageActionService.class);
        final LandingPageAction lpa = new LandingPageAction(userAccount, LandingPageActionType.ACTIVATE_ACCOUNT);
        when(landingPageActionService.getByToken(LandingPageActionType.ACTIVATE_ACCOUNT.name())).thenReturn(lpa);

        final MembershipService membershipService = mock(MembershipService.class);

        final UserAccountService userAccountService = mock(UserAccountService.class);
        when(userAccountService.login(anyString(), anyString())).thenReturn(userAccount);
        when(userAccountService.countUserAccountsOnline()).thenReturn(2);

        final MessageService messageService = mock(MessageService.class);
        final FavoriteService favoriteService = mock(FavoriteService.class);

        final VisitService visitService = mock(VisitService.class);
        when(visitService.getVisitsSinceLastLogin(any(User.class))).thenReturn(
                new ArrayList<Visit>());
        when(visitService.getMaxShownThumbnails()).thenReturn(4);

        // 3. setup mock injection environment
        getAppContext().putBean(FavoriteService.BEAN_ID, favoriteService);
        getAppContext().putBean(LandingPageActionService.BEAN_ID, landingPageActionService);
        getAppContext().putBean(MembershipService.BEAN_ID, membershipService);
        getAppContext().putBean(MessageService.BEAN_ID, messageService);
        getAppContext().putBean(UserAccountService.BEAN_ID, userAccountService);
        getAppContext().putBean(VisitService.BEAN_ID, visitService);
        // non mock (standalone) services
        getAppContext().putBean(CountryService.BEAN_ID, new CountryServiceImpl());
        getAppContext().putBean(PersonProfileService.BEAN_ID, new PersonProfileServiceImpl());
        session.setUserAccountService(userAccountService);
    }

    @Test
    public void testRendering() {
        final PageParameters params = new PageParameters();
        params.add(LandingPage.TOKEN_PARAMNAME, LandingPageActionType.ACTIVATE_ACCOUNT.name());

        getTester().startPage(LandingPage.class, params);
        getTester().assertRenderedPage(MyHomePage.class);
        getTester().dumpPage();
    }
}
