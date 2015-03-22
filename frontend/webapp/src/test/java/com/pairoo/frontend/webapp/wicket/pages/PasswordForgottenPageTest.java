package com.pairoo.frontend.webapp.wicket.pages;

//Let's import Mockito statically so that the code looks clearer
import static org.mockito.Mockito.*;

import com.datazuul.framework.webapp.wicket.components.form.ShinyForm;
import org.junit.Before;
import org.junit.Test;

import com.pairoo.business.api.UserAccountService;
import com.pairoo.domain.User;
import com.pairoo.domain.UserAccount;
import com.pairoo.frontend.webapp.wicket.AbstractWicketTest;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;

public class PasswordForgottenPageTest extends AbstractWicketTest {

    @Override
    @Before
    public void before() throws Exception {
        super.before();
    }

    @Test
    public void testRendering() {
        getTester().startPage(PasswordForgottenPage.class);
        getTester().assertRenderedPage(PasswordForgottenPage.class);
    }

    @Test
    public void show_error_message_if_entered_username_does_not_exist() {
        // GIVEN
        final UserAccountService userAccountService = mock(UserAccountService.class);
        when(userAccountService.getByUsername(anyString())).thenReturn(null);
        getAppContext().putBean(UserAccountService.BEAN_ID, userAccountService);

        // WHEN
        final WicketTester tester = getTester();
        tester.startPage(PasswordForgottenPage.class);
        tester.assertRenderedPage(PasswordForgottenPage.class);
        tester.assertComponent("form", ShinyForm.class);

        // return zipcode not found
        FormTester formTester = tester.newFormTester("form");
        formTester.setValue("userAccount.username", "unknown username");
        formTester.setValue("email", "test@pairoo.de");
        formTester.submit();

        // THEN
        tester.assertErrorMessages("The entered credentials are not valid. Please try again.");

    }

    @Test
    public void show_error_message_if_entered_email_does_not_match() {
        // GIVEN
        UserAccount mockUserAccount = new UserAccount();
        User mockUser = new User();
        mockUser.setEmail("other@pairoo.de");
        mockUserAccount.setUser(mockUser);

        final UserAccountService userAccountService = mock(UserAccountService.class);
        when(userAccountService.getByUsername(anyString())).thenReturn(mockUserAccount);
        getAppContext().putBean(UserAccountService.BEAN_ID, userAccountService);

        // WHEN
        final WicketTester tester = getTester();
        tester.startPage(PasswordForgottenPage.class);
        tester.assertRenderedPage(PasswordForgottenPage.class);
        tester.assertComponent("form", ShinyForm.class);

        // return zipcode not found
        FormTester formTester = tester.newFormTester("form");
        formTester.setValue("userAccount.username", "unknown username");
        formTester.setValue("email", "test@pairoo.de");
        formTester.submit();

        // THEN
        tester.assertErrorMessages("The entered credentials are not valid. Please try again.");

    }
}
