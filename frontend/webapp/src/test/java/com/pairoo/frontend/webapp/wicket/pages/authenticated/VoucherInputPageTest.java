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
public class VoucherInputPageTest extends AbstractWicketTest {

    final User mockUser = new User();

    @Before
    @Override
    public void before() throws Exception {
        super.before();

        // mock session
        final WicketWebSession session = ((WicketWebSession) getTester().getSession());
        session.setUser(mockUser);
    }

    @Test
    public void testRendering() throws ServletException {
        final Page page = new VoucherInputPage(new Model<User>(mockUser));
        getTester().startPage(page);
        getTester().dumpPage();
        getTester().assertRenderedPage(VoucherInputPage.class);
    }
}
