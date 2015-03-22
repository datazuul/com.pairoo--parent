package com.pairoo.frontend.webapp.wicket;

import java.util.Arrays;
import java.util.Locale;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import pl.rabbitsoftware.EnhancedWicketTester;

import com.datazuul.framework.domain.geo.Country;
import com.pairoo.domain.User;
import com.pairoo.domain.enums.BodyType;
import com.pairoo.domain.enums.PartnerType;
import com.pairoo.domain.enums.PartnershipType;
import com.pairoo.frontend.webapp.wicket.pages.HomePage;
import com.pairoo.frontend.webapp.wicket.pages.LogoutPage;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.MyHomePage;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.RegistrationConfirmationPage;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.RegistrationStep1Page;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.RegistrationStep2Page;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.RegistrationStep3Page;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.RegistrationStep4Page;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.RegistrationStep5Page;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.RegistrationStep6Page;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.SuggestionsPage;
import com.pairoo.frontend.webapp.wicket.panels.EditSearchProfile1Panel;
import com.pairoo.frontend.webapp.wicket.panels.EditSearchProfile2Panel;
import com.pairoo.frontend.webapp.wicket.panels.EditSearchProfile3Panel;
import com.pairoo.frontend.webapp.wicket.panels.EditUserProfile1Panel;
import com.pairoo.frontend.webapp.wicket.panels.EditUserProfile2Panel;
import com.pairoo.frontend.webapp.wicket.panels.EditUserProfile3Panel;
import com.pairoo.frontend.webapp.wicket.panels.RegistrationEntryPanel;
import org.apache.wicket.markup.html.form.TextField;

@TestExecutionListeners(TransactionalTestExecutionListener.class)
public class RegistrationITCase extends AbstractITCase {

    @Test
    @Transactional
    public void testRegistrationAndAuthenticatedPages() {
        tester.getSession().setLocale(Locale.GERMANY);
        System.out.println("Locale: " + tester.getSession().getLocale().getCountry());

        tester.setFollowRedirects(true);

        testRegistration();

        testAuthenticatedPages();
    }

    private void testRegistration() {
        // Homepage
        // ========
        tester.startPage(HomePage.class);
        tester.assertNoErrorMessage();
        tester.dumpPage();
        tester.assertRenderedPage(HomePage.class);
        tester.assertComponent("form:registrationEntryPanel", RegistrationEntryPanel.class);

        final EnhancedWicketTester enhanced = new EnhancedWicketTester(tester);

        enhanced.form("form").selectDropDownChoice("registrationEntryPanel:userProfile.partnerType", PartnerType.MALE)
                .setTextFieldValue("registrationEntryPanel:userAccount.username", "adam01")
                .setPasswordTextFieldValue("registrationEntryPanel:userAccount.password", "start123")
                .setTextFieldValue("registrationEntryPanel:email", "ralf.eichinger@pixotec.de")
                .selectDropDownChoice("registrationEntryPanel:searchProfile.partnerType", PartnerType.FEMALE)
                .selectDropDownChoice("registrationEntryPanel:searchProfile.minAge", new Integer(28))
                .selectDropDownChoice("registrationEntryPanel:searchProfile.maxAge", new Integer(35))
                .selectDropDownChoice("registrationEntryPanel:userProfile.geoLocation.country", Country.GERMANY)
                .setTextFieldValue("registrationEntryPanel:userProfile.geoLocation.zipcode", "81245")
                .selectCheckbox("registrationEntryPanel:acceptCheckbox", true);

        tester.submitForm("form");

        // RegistrationStep1Page
        // =====================
        tester.dumpPage();
        tester.assertRenderedPage(RegistrationStep1Page.class);
        final RegistrationStep1Page registrationStep1Page = (RegistrationStep1Page) tester.getLastRenderedPage();
        User user = (User) registrationStep1Page.getDefaultModelObject();

        Assert.assertEquals(PartnerType.MALE, user.getUserProfile().getPartnerType());
        Assert.assertEquals("adam01", user.getUserAccount().getUsername());
        Assert.assertFalse("start123".equals(user.getUserAccount().getPassword()));
        Assert.assertEquals(PartnerType.FEMALE, user.getSearchProfile().getPartnerType());
        Assert.assertEquals(new Integer(28), user.getSearchProfile().getMinAge());
        Assert.assertEquals(new Integer(35), user.getSearchProfile().getMaxAge());
        Assert.assertEquals(Country.GERMANY, user.getUserProfile().getGeoLocation().getCountry());
        Assert.assertEquals("81245", user.getUserProfile().getGeoLocation().getZipcode());

        tester.assertComponent("form:editSearchProfile1Panel", EditSearchProfile1Panel.class);
        enhanced.form("form").selectDropDownChoice("editSearchProfile1Panel:searchProfile.partnershipType",
                PartnershipType.LOVE);

        tester.submitForm("form");

        // RegistrationStep2Page
        // =====================
        tester.dumpPage();
        tester.assertRenderedPage(RegistrationStep2Page.class);
        final RegistrationStep2Page registrationStep2Page = (RegistrationStep2Page) tester.getLastRenderedPage();
        user = (User) registrationStep2Page.getDefaultModelObject();

        Assert.assertEquals(PartnershipType.LOVE, user.getSearchProfile().getPartnershipType());

        tester.assertComponent("form:editSearchProfile2Panel", EditSearchProfile2Panel.class);
        enhanced.form("form").selectCheckGroup("editSearchProfile2Panel:searchProfile.bodyTypes:group",
                new BodyType[]{BodyType.AVERAGE});

        tester.submitForm("form");

        // RegistrationStep3Page
        // =====================
        tester.dumpPage();
        tester.assertRenderedPage(RegistrationStep3Page.class);
        final RegistrationStep3Page registrationStep3Page = (RegistrationStep3Page) tester.getLastRenderedPage();
        user = (User) registrationStep3Page.getDefaultModelObject();

        Assert.assertEquals(Arrays.asList(new BodyType[]{BodyType.AVERAGE}), user.getSearchProfile().getBodyTypes());

        tester.assertComponent("form:editSearchProfile3Panel", EditSearchProfile3Panel.class);
        enhanced.form("form");

        tester.submitForm("form");

        // RegistrationStep4Page
        // =====================
        tester.dumpPage();
        tester.assertRenderedPage(RegistrationStep4Page.class);
        final RegistrationStep4Page registrationStep4Page = (RegistrationStep4Page) tester.getLastRenderedPage();
        user = (User) registrationStep4Page.getDefaultModelObject();

        tester.assertComponent("form:editUserProfile1Panel", EditUserProfile1Panel.class);
        // birthdate, height, weight are required and not set by default
        // set birthdate to: 1970/12/08 (december is value 11)
        enhanced.form("form").selectDropDownChoice("editUserProfile1Panel:userProfile.birthdate:day", new Integer(8))
                .selectDropDownChoice("editUserProfile1Panel:userProfile.birthdate:month", new Integer(11))
                .selectDropDownChoice("editUserProfile1Panel:userProfile.birthdate:year", new Integer(1970))
                .selectDropDownChoice("editUserProfile1Panel:userProfile.appearance.height", new Integer(180));
        tester.submitForm("form");

        // RegistrationStep5Page
        // =====================
        tester.dumpPage();
        tester.assertRenderedPage(RegistrationStep5Page.class);
        final RegistrationStep5Page registrationStep5Page = (RegistrationStep5Page) tester.getLastRenderedPage();
        user = (User) registrationStep5Page.getDefaultModelObject();

        tester.assertComponent("form:editUserProfile2Panel", EditUserProfile2Panel.class);
        // My Motto is required and not set by default
        enhanced.form("form").setTextFieldValue("editUserProfile2Panel:userProfile.motto", "oba heid is keut");
        tester.submitForm("form");

        // RegistrationStep6Page
        // =====================
        tester.dumpPage();
        tester.assertRenderedPage(RegistrationStep6Page.class);
        final RegistrationStep6Page registrationStep6Page = (RegistrationStep6Page) tester.getLastRenderedPage();
        user = (User) registrationStep6Page.getDefaultModelObject();

        tester.assertComponent("form:editUserProfile3Panel", EditUserProfile3Panel.class);
        enhanced.form("form");
        tester.submitForm("form");

        // RegistrationConfirmationPage
        // ============================
        tester.dumpPage();
        tester.assertRenderedPage(RegistrationConfirmationPage.class);
        final RegistrationConfirmationPage registrationConfirmationPage = (RegistrationConfirmationPage) tester
                .getLastRenderedPage();
        user = (User) registrationConfirmationPage.getDefaultModelObject();

        tester.assertComponent("lnkSuggestionsPage", Link.class);
        tester.clickLink("lnkSuggestionsPage");

        // SuggestionsPage
        // ===============
        tester.dumpPage();
        // commented following, because we get the Javascript Browser detection page...

        tester.assertRenderedPage(SuggestionsPage.class);
        tester.assertComponent("myNavi:logout", Link.class);
        final WicketWebSession session = (WicketWebSession) tester.getSession();
        session.onBeforeDestroy();
        tester.clickLink("myNavi:logout");

        // LogoutPage
        // ==========
        tester.dumpPage();
        tester.assertRenderedPage(LogoutPage.class);
        tester.clickLink("lnkToHomePage");

        // HomePage
        // ========
        tester.dumpPage();
        tester.assertRenderedPage(HomePage.class);
        // tester.assertComponent("myNavi:form", Form.class);
        // enhanced.form("myNavi:form").setTextFieldValue("userAccount.username",
        // "adam01")
        // .setPasswordTextFieldValue("userAccount.password", "start123");
        // tester.submitForm("myNavi:form");
        //
        // // MyHomePage
        // // ==========
        // tester.dumpPage();
        //
        // junit.framework.AssertionFailedError: classes not the same, expected
        // 'class
        // com.pairoo.frontend.webapp.wicket.pages.authenticated.MyHomePage',
        // current 'class com.pairoo.frontend.webapp.wicket.pages.LoginPage'

        // tester.assertRenderedPage(MyHomePage.class);
        // tester.assertComponent("myNavi:logout", Link.class);
        // session.onBeforeDestroy();
        // tester.clickLink("myNavi:logout");
        //
        // // LogoutPage
        // // ==========
        // tester.dumpPage();
        // tester.assertRenderedPage(LogoutPage.class);
    }

    private void testAuthenticatedPages() {
        // Homepage
        // ========
        tester.startPage(HomePage.class);
        tester.dumpPage();
        tester.assertRenderedPage(HomePage.class);
        tester.assertComponent("myNaviPanel:myNavi:form", Form.class);

        final EnhancedWicketTester enhanced = new EnhancedWicketTester(tester);

        enhanced.form("myNaviPanel:myNavi:form").setTextFieldValue("userAccount.username", "adam")
                .setPasswordTextFieldValue("userAccount.password", "start123");
        tester.submitForm("myNaviPanel:myNavi:form");

        // MyHomePage
        // ==========
        tester.dumpPage();
        tester.assertRenderedPage(MyHomePage.class);
        tester.assertComponent("myNavi:logout", Link.class);
        final WicketWebSession session = (WicketWebSession) tester.getSession();
        session.onBeforeDestroy();
        tester.clickLink("myNavi:logout");

        // LogoutPage
        // ==========
        tester.dumpPage();
        tester.assertRenderedPage(LogoutPage.class);

        // we introduced @Transactional because adam has visitors on his
        // MyHomePage
        // and getting thumbnail gallery for them had some crazy
        // lazyinitialisation problem...

        // tester.assertComponent("myNavi:form", Form.class);
        // enhanced.form("myNavi:form").setTextFieldValue("userAccount.username",
        // "adam")
        // .setPasswordTextFieldValue("userAccount.password", "start123");
        // tester.submitForm("myNavi:form");
        //
        // // MyHomePage
        // // ==========
        // tester.dumpPage();
        // tester.assertRenderedPage(MyHomePage.class);

        /*
         * junit.framework.AssertionFailedError: classes not the same, expected
         * 'class
         * com.pairoo.frontend.webapp.wicket.pages.authenticated.MyHomePage',
         * current 'class com.pairoo.frontend.webapp.wicket.pages.LoginPage'
         */
    }
}
