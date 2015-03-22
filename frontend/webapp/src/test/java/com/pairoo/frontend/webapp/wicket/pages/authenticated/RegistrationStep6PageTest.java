package com.pairoo.frontend.webapp.wicket.pages.authenticated;

import com.pairoo.domain.User;
import com.pairoo.frontend.webapp.wicket.AbstractWicketTest;
import com.pairoo.frontend.webapp.wicket.WicketWebSession;
import javax.servlet.ServletException;
import org.apache.wicket.Page;
import org.apache.wicket.model.Model;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Ralf Eichinger
 */
public class RegistrationStep6PageTest extends AbstractWicketTest {

    final User mockUser = new User();

    @Before
    @Override
    public void before() throws Exception {
        super.before();

        WicketWebSession session = (WicketWebSession) getTester().getSession();
        session.setUser(mockUser);
    }

    @Test
    public void testRendering() throws ServletException {
        final Page page = new RegistrationStep6Page(new Model<User>(mockUser));
        getTester().startPage(page);
        getTester().assertRenderedPage(RegistrationStep6Page.class);
    }
}
