package com.pairoo.frontend.webapp.wicket.pages.authenticated;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.ServletException;

import org.apache.wicket.Page;
import org.apache.wicket.model.Model;
import org.junit.Before;
import org.junit.Test;

import com.datazuul.framework.domain.authorization.Roles;
import com.datazuul.framework.domain.geo.Country;
import com.datazuul.framework.domain.geo.GeoLocation;
import com.pairoo.business.api.ImageEntryService;
import com.pairoo.business.api.MembershipService;
import com.pairoo.business.api.PersonProfileService;
import com.pairoo.business.api.UserAccountService;
import com.pairoo.business.api.VisitService;
import com.pairoo.business.services.impl.PersonProfileServiceImpl;
import com.pairoo.domain.Membership;
import com.pairoo.domain.Product;
import com.pairoo.domain.User;
import com.pairoo.domain.UserAccount;
import com.pairoo.domain.UserProfile;
import com.pairoo.domain.enums.PartnerType;
import com.pairoo.domain.enums.Role;
import com.pairoo.frontend.webapp.wicket.AbstractWicketTest;
import com.pairoo.frontend.webapp.wicket.WicketWebSession;

/**
 * @author Ralf Eichinger
 */
public class ProfileDetailsPageTest extends AbstractWicketTest {

    final User mockProfileUser = new User();
    final User mockVisitorUser = new User();

    @Before
    @Override
    public void before() throws Exception {
        super.before();

        // 1. setup dependencies and mock objects
        final MembershipService membershipService = mock(MembershipService.class);
        Membership membership = new Membership();
        membership.setProduct(new Product());
        when(membershipService.getCurrentMembership(any(UserAccount.class))).thenReturn(membership);

        GeoLocation geoLocation = new GeoLocation();
        geoLocation.setCountry(Country.GERMANY);

        UserProfile userProfile = new UserProfile();
        userProfile.setGeoLocation(geoLocation);
        userProfile.setPartnerType(PartnerType.FEMALE);
        mockProfileUser.setUserProfile(userProfile);

        UserAccount userAccountProfile = new UserAccount();
        userAccountProfile.setUsername("eva");
        mockProfileUser.setUserAccount(userAccountProfile);

        UserProfile userProfileVisitor = new UserProfile();
        userProfileVisitor.setGeoLocation(geoLocation);
        userProfileVisitor.setPartnerType(PartnerType.MALE);
        mockVisitorUser.setUserProfile(userProfileVisitor);

        UserAccount userAccountVisitor = new UserAccount();
        userAccountVisitor.setUsername("adam");
        userAccountVisitor.setRoles(new Roles(Role.STANDARD.getCode()));
        mockVisitorUser.setUserAccount(userAccountVisitor);

        VisitService visitService = mock(VisitService.class);

        final UserAccountService userAccountService = mock(UserAccountService.class);
        final ImageEntryService imageEntryService = mock(ImageEntryService.class);

        getAppContext().putBean(ImageEntryService.BEAN_ID, imageEntryService);
        getAppContext().putBean(MembershipService.BEAN_ID, membershipService);
        getAppContext().putBean(PersonProfileService.BEAN_ID, new PersonProfileServiceImpl());
        getAppContext().putBean(UserAccountService.BEAN_ID, userAccountService);
        getAppContext().putBean(VisitService.BEAN_ID, visitService);

        WicketWebSession session = (WicketWebSession) getTester().getSession();
        session.setUser(mockVisitorUser);
    }

    @Test
    public void testRendering() throws ServletException {
        final Page page = new ProfileDetailsPage(new Model<User>(mockProfileUser), new Model<User>(mockVisitorUser)) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onGoBack() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        getTester().startPage(page);
        getTester().dumpPage();
        getTester().assertRenderedPage(ProfileDetailsPage.class);
    }
}
