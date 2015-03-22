package com.pairoo.frontend.webapp.wicket.pages;

//Let's import Mockito statically so that the code looks clearer
import static org.mockito.Mockito.*;

import java.util.Locale;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import com.datazuul.framework.domain.Language;
import com.datazuul.framework.domain.geo.Country;
import com.pairoo.business.api.CountryService;
import com.pairoo.business.api.GeoLocationService;
import com.pairoo.business.api.PersonProfileService;
import com.pairoo.business.api.UserAccountService;
import com.pairoo.business.api.UserService;
import com.pairoo.business.services.impl.CountryServiceImpl;
import com.pairoo.business.services.impl.PersonProfileServiceImpl;
import com.pairoo.domain.MockUser;
import com.pairoo.domain.User;
import com.pairoo.domain.enums.PartnerType;
import com.pairoo.frontend.webapp.wicket.AbstractWicketTest;
import com.pairoo.frontend.webapp.wicket.WicketWebSession;
import com.pairoo.frontend.webapp.wicket.panels.RegistrationEntryPanel;
import com.vividsolutions.jts.util.Assert;
import org.apache.wicket.util.tester.FormTester;

/**
 * @author Ralf Eichinger
 */
public class HomePageTest extends AbstractWicketTest {

    final User mockUser = new MockUser();

    @Override
    @Before
    public void before() throws Exception {
        super.before();

        // 1. setup dependencies and mock objects
        final UserService userService = mock(UserService.class);
        when(userService.getRegistrationDefaultUser(any(Locale.class))).thenReturn(mockUser);


        // final UserService userService = EasyMock.createMock(UserService.class);
        // EasyMock.expect(userService.getRegistrationDefaultUser(EasyMock.anyObject(Locale.class))).andReturn(mockUser);
        // EasyMock.expect(userService.getRegistrationDefaultUser(EasyMock.anyObject(Locale.class))).andReturn(mockUser);
        // EasyMock.replay(userService);

        // 2. setup mock injection environment
        // non mock (standalone) services
        getAppContext().putBean(CountryService.BEAN_ID, new CountryServiceImpl());
        getAppContext().putBean(PersonProfileService.BEAN_ID, new PersonProfileServiceImpl());
        getAppContext().putBean(UserService.BEAN_ID, userService);
    }

    @Test
    public void testRendering() {
        final WicketTester tester = getTester();
        tester.startPage(HomePage.class);
        tester.assertRenderedPage(HomePage.class);
        tester.assertComponent("form:registrationEntryPanel", RegistrationEntryPanel.class);

        tester.dumpPage();

        // not using more than one test method, because otherwise getting:
        // wicket java.lang.IllegalStateException: Application name can only be
        // set once.
        //
        // }
        //
        // @Test
        // public void testParams() {
        // final WicketTester tester = getTester();

        PageParameters params = new PageParameters();
        params.add("tld", "es");
        tester.startPage(HomePage.class, params);
        tester.assertRenderedPage(HomePage.class);

        final WicketWebSession session = ((WicketWebSession) tester.getSession());
        Locale locale = session.getLocale();

        Assert.equals(Language.SPANISH.getLocale(), locale);

        tester.dumpPage();
    }

    @Test
    public void showError_when_invalid_zipcode_entered_in_registration_form() {
        // GIVEN
        final UserAccountService userAccountService = mock(UserAccountService.class);
        when(userAccountService.getByUsername(anyString())).thenReturn(null);
        getAppContext().putBean(UserAccountService.BEAN_ID, userAccountService);

        GeoLocationService geoLocationService = mock(GeoLocationService.class);
        when(geoLocationService.getByCountryAndZipcode(any(Country.class), anyString())).thenReturn(null);
        getAppContext().putBean(GeoLocationService.BEAN_ID, geoLocationService);

        // WHEN
        final WicketTester tester = getTester();
        tester.startPage(HomePage.class);
        tester.assertRenderedPage(HomePage.class);
        tester.assertComponent("form:registrationEntryPanel", RegistrationEntryPanel.class);

        // return zipcode not found
        FormTester formTester = tester.newFormTester("form");
        formTester.setValue("registrationEntryPanel:userProfile.partnerType", PartnerType.MALE.name());
        formTester.setValue("registrationEntryPanel:userAccount.username", "adam02");
        formTester.setValue("registrationEntryPanel:userAccount.password", "password");
        formTester.setValue("registrationEntryPanel:email", "test@pairoo.de");
        formTester.setValue("registrationEntryPanel:searchProfile.partnerType", PartnerType.FEMALE.name());
        formTester.setValue("registrationEntryPanel:searchProfile.minAge", "35");
        formTester.setValue("registrationEntryPanel:searchProfile.maxAge", "40");
        formTester.setValue("registrationEntryPanel:userProfile.geoLocation.country", Country.AUSTRIA.name());
        formTester.setValue("registrationEntryPanel:acceptCheckbox", "on");

        // invalid zipcode
        formTester.setValue("registrationEntryPanel:userProfile.geoLocation.zipcode", "invalid zipcode");

        formTester.submit();

        // THEN
        tester.assertErrorMessages("No city found for the given zipcode.");
    }
}
