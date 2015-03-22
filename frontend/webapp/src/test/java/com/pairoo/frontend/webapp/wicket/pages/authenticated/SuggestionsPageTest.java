package com.pairoo.frontend.webapp.wicket.pages.authenticated;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.wicket.Page;
import org.apache.wicket.model.Model;
import org.junit.Before;
import org.junit.Test;

import com.datazuul.framework.domain.authorization.Roles;
import com.datazuul.framework.domain.geo.Country;
import com.datazuul.framework.domain.geo.GeoLocation;
import com.pairoo.business.api.GeoLocationService;
import com.pairoo.business.api.PersonProfileService;
import com.pairoo.business.api.UserAccountService;
import com.pairoo.business.api.UserService;
import com.pairoo.business.services.impl.PersonProfileServiceImpl;
import com.pairoo.domain.SearchProfile;
import com.pairoo.domain.User;
import com.pairoo.domain.UserAccount;
import com.pairoo.domain.UserProfile;
import com.pairoo.domain.enums.Role;
import com.pairoo.frontend.webapp.wicket.AbstractWicketTest;
import com.pairoo.frontend.webapp.wicket.WicketWebSession;

/**
 * @author Ralf Eichinger
 */
public class SuggestionsPageTest extends AbstractWicketTest {

    final User mockUser = new User();

    @Before
    @Override
    public void before() throws Exception {
	super.before();

	// 1. setup dependencies and mock objects
	// mock objects
	mockUser.setSearchProfile(new SearchProfile());
	UserProfile userProfile = new UserProfile();
	mockUser.setUserProfile(userProfile);
	GeoLocation geoLocation = new GeoLocation();
	geoLocation.setCountry(Country.GERMANY);
	geoLocation.setLatitude(new Double(0));
	geoLocation.setLongitude(new Double(0));
	mockUser.getUserProfile().setGeoLocation(geoLocation);

	mockUser.getUserAccount().setRoles(new Roles(Role.PREMIUM.getCode()));

	// suggestions
	final List<User> users = new ArrayList<User>();
	final User user1 = new User();
	UserAccount userAccount1 = new UserAccount();
	user1.setUserAccount(userAccount1);
	UserProfile userProfile1 = new UserProfile();
	user1.setUserProfile(userProfile1);
	GeoLocation geoLocation1 = new GeoLocation();
	geoLocation1.setCountry(Country.GERMANY);
	geoLocation1.setLatitude(new Double(0));
	geoLocation1.setLongitude(new Double(0));
	user1.getUserProfile().setGeoLocation(geoLocation1);
	users.add(user1);

	// mock services
	final UserService userService = mock(UserService.class);

	final UserAccountService userAccountService = mock(UserAccountService.class);
	when(userAccountService.isPremiumMember(user1.getUserAccount())).thenReturn(true);

	final GeoLocationService geoLocationService = mock(GeoLocationService.class);

	// mock session
	final WicketWebSession session = ((WicketWebSession) getTester().getSession());
	session.setUser(mockUser);

	// 2. setup mock injection environment
	getAppContext().putBean(GeoLocationService.BEAN_ID, geoLocationService);
	getAppContext().putBean(UserAccountService.BEAN_ID, userAccountService);
	getAppContext().putBean(UserService.BEAN_ID, userService);
	// non mock (standalone) services
	getAppContext().putBean(PersonProfileService.BEAN_ID, new PersonProfileServiceImpl());
    }

    @Test
    public void testRendering() throws ServletException {
	final Page page = new SuggestionsPage(Model.of(mockUser));
	getTester().startPage(page);
	getTester().assertRenderedPage(SuggestionsPage.class);
    }
}
