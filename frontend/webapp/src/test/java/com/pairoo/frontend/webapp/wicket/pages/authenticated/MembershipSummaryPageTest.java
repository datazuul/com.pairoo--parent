/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pairoo.frontend.webapp.wicket.pages.authenticated;

import com.pairoo.domain.Membership;
import com.pairoo.domain.User;
import com.pairoo.frontend.webapp.wicket.AbstractWicketTest;
import com.pairoo.frontend.webapp.wicket.WicketWebSession;
import javax.servlet.ServletException;
import org.apache.wicket.Page;
import org.apache.wicket.model.Model;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author ralf
 */
public class MembershipSummaryPageTest extends AbstractWicketTest {

    final User mockUser = new User();
    final Membership mockMembership = new Membership();

    @Before
    @Override
    public void before() throws Exception {
        super.before();

        WicketWebSession session = (WicketWebSession) getTester().getSession();
        session.setUser(mockUser);
    }

    @Test
    public void testRendering() throws ServletException {
        final Page page = new MembershipSummaryPage(Model.of(mockMembership), Model.of(mockUser));
        getTester().startPage(page);
        getTester().assertRenderedPage(MembershipSummaryPage.class);
    }
}
