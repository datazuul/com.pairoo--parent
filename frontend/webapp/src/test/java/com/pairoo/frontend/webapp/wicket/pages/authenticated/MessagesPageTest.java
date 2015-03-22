/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pairoo.frontend.webapp.wicket.pages.authenticated;

import com.googlecode.genericdao.search.Search;
import com.pairoo.business.api.MessageService;
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
 *
 * @author ralf
 */
public class MessagesPageTest extends AbstractWicketTest {

    final User mockUser = new User();
    final Search mockSearch = new Search();

    @Before
    @Override
    public void before() throws Exception {
        super.before();

        // 1. setup dependencies and mock objects
        final MessageService messageService = mock(MessageService.class);
        when(messageService.getInboxSearchCriteria(mockUser)).thenReturn(mockSearch);

        // 2. setup mock injection environment
        getAppContext().putBean(MessageService.BEAN_ID, messageService);

        WicketWebSession session = (WicketWebSession) getTester().getSession();
        session.setUser(mockUser);
    }

    @Test
    public void testRendering() throws ServletException {
        final Page page = new MessagesPage(Model.of(mockUser));
        getTester().startPage(page);
        getTester().assertRenderedPage(MessagesPage.class);
        getTester().dumpPage();
    }
}
