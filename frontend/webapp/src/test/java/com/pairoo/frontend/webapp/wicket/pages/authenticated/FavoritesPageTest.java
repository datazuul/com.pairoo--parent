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
import com.datazuul.framework.domain.geo.GeoLocation;
import com.googlecode.genericdao.search.Search;
import com.pairoo.business.api.CountryService;
import com.pairoo.business.api.FavoriteService;
import com.pairoo.business.api.PersonProfileService;
import com.pairoo.business.api.UserAccountService;
import com.pairoo.business.services.impl.CountryServiceImpl;
import com.pairoo.business.services.impl.PersonProfileServiceImpl;
import com.pairoo.domain.Favorite;
import com.pairoo.domain.SearchProfile;
import com.pairoo.domain.User;
import com.pairoo.domain.enums.Role;
import com.pairoo.frontend.webapp.wicket.AbstractWicketTest;
import com.pairoo.frontend.webapp.wicket.WicketWebSession;

/**
 * @author Ralf Eichinger
 */
public class FavoritesPageTest extends AbstractWicketTest {

    final User mockUser = new User();

    @Before
    @Override
    public void before() throws Exception {
	super.before();

	// 1. setup dependencies and mock objects
	// mock objects
	mockUser.setSearchProfile(new SearchProfile());
	mockUser.getSearchProfile().getGeoArea().setGeoLocation(new GeoLocation());
	mockUser.getUserAccount().setRoles(new Roles(Role.PREMIUM.getCode()));

	User favoriteUser = new User();
	favoriteUser.getUserAccount().setUsername("test");

	// favorites
	final List<Favorite> favorites = new ArrayList<Favorite>();
	final Favorite favorite1 = new Favorite();
	favorite1.setTarget(favoriteUser);
	favorite1.setOwner(mockUser);
	favorite1.setId(new Long(1));
	favorites.add(favorite1);

	// mock services
	final FavoriteService favoriteService = mock(FavoriteService.class);
	final Search search = new Search(Favorite.class);
	when(favoriteService.count(search)).thenReturn(1L, 1L);
	when(favoriteService.search(search)).thenReturn(favorites);
	when(favoriteService.getFavoritesSearchCriteria(mockUser)).thenReturn(search);

	final UserAccountService userAccountService = mock(UserAccountService.class);
	when(userAccountService.isPremiumMember(favoriteUser.getUserAccount())).thenReturn(true);

	// mock session
	final WicketWebSession session = ((WicketWebSession) getTester().getSession());
	session.setUser(mockUser);

	// 2. setup mock injection environment
	getAppContext().putBean(FavoriteService.BEAN_ID, favoriteService);
	getAppContext().putBean(UserAccountService.BEAN_ID, userAccountService);
	// non mock (standalone) services
	getAppContext().putBean(CountryService.BEAN_ID, new CountryServiceImpl());
	getAppContext().putBean(PersonProfileService.BEAN_ID, new PersonProfileServiceImpl());
    }

    @Test
    public void testRendering() throws ServletException {
	final Page page = new FavoritesPage(new Model<User>(mockUser));
	getTester().startPage(page);
	getTester().dumpPage();
	getTester().assertRenderedPage(FavoritesPage.class);
    }
}
