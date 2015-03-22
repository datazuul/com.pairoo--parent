package com.pairoo.frontend.webapp.wicket.pages.authenticated;

import com.pairoo.business.api.MembershipService;
import com.pairoo.domain.Membership;
import com.pairoo.domain.Product;
import com.pairoo.domain.User;
import com.pairoo.domain.enums.Role;
import com.pairoo.frontend.webapp.wicket.AbstractWicketTest;
import com.pairoo.frontend.webapp.wicket.WicketWebSession;
import java.util.Date;
import javax.servlet.ServletException;
import org.apache.wicket.Page;
import org.apache.wicket.model.Model;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 * @author Ralf Eichinger
 */
public class VoucherConfirmationPageTest extends AbstractWicketTest {

    final User mockUser = new User();

    @Before
    @Override
    public void before() throws Exception {
        super.before();

        // 1. setup dependencies and mock objects
        // mock objects
        Membership mockMembership = new Membership();
        mockMembership.setEndDate(new Date());

        Product mockProduct = new Product();
        mockProduct.setAbo(true);
        mockProduct.setRole(Role.PREMIUM);

        mockMembership.setProduct(mockProduct);

        // mockUser.setSearchProfile(new SearchProfile());
        // UserProfile userProfile = new UserProfile();
        // mockUser.setUserProfile(userProfile);
        // GeoLocation geoLocation = new GeoLocation();
        // geoLocation.setCountry(Country.GERMANY);
        // geoLocation.setLatitude(new Double(0));
        // geoLocation.setLongitude(new Double(0));
        // mockUser.getUserProfile().setGeoLocation(geoLocation);
        //
        // mockUser.getUserAccount().setRoles(new Roles(Roles.PREMIUM));
        //
        // // suggestions
        // final List<User> users = new ArrayList<User>();
        // final User user1 = new User();
        // UserAccount userAccount1 = new UserAccount();
        // user1.setUserAccount(userAccount1);
        // UserProfile userProfile1 = new UserProfile();
        // user1.setUserProfile(userProfile1);
        // GeoLocation geoLocation1 = new GeoLocation();
        // geoLocation1.setCountry(Country.GERMANY);
        // geoLocation1.setLatitude(new Double(0));
        // geoLocation1.setLongitude(new Double(0));
        // user1.getUserProfile().setGeoLocation(geoLocation1);
        // users.add(user1);
        //
        // mock services
        MembershipService membershipService = mock(MembershipService.class);
        when(membershipService.getLastMembership(mockUser.getUserAccount())).thenReturn(mockMembership);
        // final UserService userService =
        // EasyMock.createMock(UserService.class);
        // final Search search = new Search(User.class);
        // EasyMock.expect(userService.getSuggestionsCriteria(mockUser,
        // mockUser.getSearchProfile())).andReturn(search);
        // EasyMock.expect(userService.count(search)).andReturn(1);
        // EasyMock.expect(userService.count(search)).andReturn(1);
        // EasyMock.expect(userService.search(search)).andReturn(users);
        //
        // final UserAccountService userAccountService =
        // EasyMock.createMock(UserAccountService.class);
        // EasyMock.expect(userAccountService.isPremiumMember(user1.getUserAccount())).andReturn(true);
        //
        // EasyMock.replay(userService, userAccountService);
        //
        // final GeoLocationService geoLocationService =
        // EasyMock.createMock(GeoLocationService.class);
        //
        // mock session
        final WicketWebSession session = ((WicketWebSession) getTester().getSession());
        session.setUser(mockUser);
        //
        // 2. setup mock injection environment
        getAppContext().putBean(MembershipService.BEAN_ID, membershipService);
        // getAppContext().putBean(GeoLocationService.BEAN_ID,
        // geoLocationService);
        // getAppContext().putBean(UserAccountService.BEAN_ID,
        // userAccountService);
        // getAppContext().putBean(UserService.BEAN_ID, userService);
        // // non mock (standalone) services
        // getAppContext().putBean(PersonProfileService.BEAN_ID, new
        // PersonProfileServiceImpl());
    }

    @Test
    public void testRendering() throws ServletException {
        final Page page = new VoucherConfirmationPage(new Model<User>(mockUser), true);
        getTester().startPage(page);
        getTester().assertRenderedPage(VoucherConfirmationPage.class);
    }

    @Test
    public void test_Membership_Data_is_shown() throws ServletException {
        final Page page = new VoucherConfirmationPage(new Model<User>(mockUser), true);
        getTester().startPage(page);
        getTester().assertRenderedPage(VoucherConfirmationPage.class);

        getTester().assertContains("Premium");
    }
}
