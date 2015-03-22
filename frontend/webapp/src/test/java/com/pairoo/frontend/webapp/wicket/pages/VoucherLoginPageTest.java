package com.pairoo.frontend.webapp.wicket.pages;

import com.datazuul.framework.domain.Language;
import com.pairoo.business.api.CountryService;
import com.pairoo.business.api.MembershipService;
import com.pairoo.business.api.PersonProfileService;
import com.pairoo.business.api.UserAccountService;
import com.pairoo.business.api.UserService;
import com.pairoo.business.api.payment.VoucherPaymentService;
import com.pairoo.business.services.impl.CountryServiceImpl;
import com.pairoo.business.services.impl.PersonProfileServiceImpl;
import com.pairoo.domain.Membership;
import com.pairoo.domain.MockUser;
import com.pairoo.domain.Product;
import com.pairoo.domain.User;
import com.pairoo.domain.UserAccount;
import com.pairoo.domain.marketing.Promotion;
import com.pairoo.frontend.webapp.wicket.AbstractWicketTest;
import com.pairoo.frontend.webapp.wicket.WicketWebSession;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.VoucherConfirmationPage;
import java.util.Locale;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import pl.rabbitsoftware.EnhancedWicketTester;

/**
 * @author Ralf Eichinger
 */
public class VoucherLoginPageTest extends AbstractWicketTest {

    final Promotion mockPromotion = new Promotion();
    final User mockUser = new MockUser();

    @Override
    @Before
    public void before() throws Exception {
        super.before();

        // 1. setup dependencies and mock objects
        final UserService userService = mock(UserService.class);
        when(userService.getRegistrationDefaultUser(any(Locale.class))).thenReturn(mockUser);

        final UserAccount userAccount = new UserAccount();
        userAccount.setPreferredLanguage(Language.ENGLISH);
        userAccount.setUser(mockUser);
        userAccount.setPassword("password");
        userAccount.setUsername("username");
        final UserAccountService userAccountService = mock(UserAccountService.class);
        when(userAccountService.login(anyString(), anyString())).thenReturn(userAccount);

        final VoucherPaymentService voucherPaymentService = mock(VoucherPaymentService.class);

        Membership membershipMock = new Membership();
        membershipMock.setProduct(new Product());
        final MembershipService membershipService = mock(MembershipService.class);
        when(membershipService.getLastMembership(any(UserAccount.class))).thenReturn(membershipMock);

        // mock session
        final WicketWebSession session = (((WicketWebSession) getTester().getSession()));
        session.setLocale(Locale.ENGLISH);
        session.setUserAccountService(userAccountService);


        // 2. setup mock injection environment
        // non mock (standalone) services
        getAppContext().putBean(CountryService.BEAN_ID, new CountryServiceImpl());
        getAppContext().putBean(MembershipService.BEAN_ID, membershipService);
        getAppContext().putBean(PersonProfileService.BEAN_ID, new PersonProfileServiceImpl());
        getAppContext().putBean(UserService.BEAN_ID, userService);
        getAppContext().putBean(VoucherPaymentService.BEAN_ID, voucherPaymentService);
    }

    @Test
    public void encashing_voucher_by_login_forwards_to_VoucherConfirmationPage() {
        final WicketTester tester = getTester();
        Page page = new VoucherLoginPage(new Model<Promotion>(mockPromotion));
        tester.startPage(page);
        tester.assertRenderedPage(VoucherLoginPage.class);
        tester.assertComponent("loginForm", Form.class);

        // tester.dumpPage();

        final EnhancedWicketTester enhanced = new EnhancedWicketTester(tester);
        enhanced.form("loginForm").setTextFieldValue("userAccount.username", "adam01")
                .setPasswordTextFieldValue("userAccount.password", "start123");
        tester.submitForm("loginForm");

        tester.assertRenderedPage(VoucherConfirmationPage.class);
    }
}
