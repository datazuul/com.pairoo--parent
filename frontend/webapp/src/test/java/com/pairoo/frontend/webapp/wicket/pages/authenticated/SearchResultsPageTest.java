package com.pairoo.frontend.webapp.wicket.pages.authenticated;

import com.pairoo.business.api.UserService;
import com.pairoo.domain.SearchProfile;
import com.pairoo.domain.User;
import com.pairoo.frontend.webapp.wicket.AbstractWicketTest;
import com.pairoo.frontend.webapp.wicket.WicketWebSession;
import javax.servlet.ServletException;
import org.apache.wicket.Page;
import org.apache.wicket.model.Model;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 * @author Ralf Eichinger
 */
public class SearchResultsPageTest extends AbstractWicketTest {

    final User mockUser = new User();

    @Before
    @Override
    public void before() throws Exception {
        super.before();

        final UserService userService = mock(UserService.class);
        getAppContext().putBean(UserService.BEAN_ID, userService);

        WicketWebSession session = (WicketWebSession) getTester().getSession();
        session.setUser(mockUser);
    }

    @Test
    public void testRendering() throws ServletException {
        final Page page = new SearchResultsPage(new Model<User>(mockUser), new SearchProfile());
        getTester().startPage(page);
        getTester().assertRenderedPage(SearchResultsPage.class);
    }
}
