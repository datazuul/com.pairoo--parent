package com.pairoo.frontend.webapp.wicket;

import com.pairoo.business.api.ApplicationService;
import com.pairoo.business.services.impl.ApplicationServiceImpl;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.spring.test.ApplicationContextMock;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;

@Ignore
public abstract class AbstractWicketTest {

    private ApplicationContextMock appctx;
    private WicketTester tester;
    private static WicketWebApplicationMock wicketWebApplicationMock;

    @BeforeClass
    public static void beforeClass() {
        // wicketWebApplicationMock = new WicketWebApplicationMock();
    }

    @Before
    public void before() throws Exception {
//        WicketWebApplication wicketWebApplication = mock(WicketWebApplication.class);
//        when(wicketWebApplication.newSession(any(Request.class), any(Response.class))).thenReturn();
        wicketWebApplicationMock = new WicketWebApplicationMock();
//        {
//            @Override
//            public Session newSession(Request request, Response response) {
//                WicketWebSession mock = Mockito.mock(WicketWebSession.class);
//                Mockito.when(mock.login(Matchers.anyString(), Matchers.anyString())).thenReturn(new User());
//                return mock;
//            }
//        };

        appctx = new ApplicationContextMock();
        getAppContext().putBean(ApplicationService.BEAN_ID, new ApplicationServiceImpl());

        tester = new WicketTester(wicketWebApplicationMock);
        tester.getApplication().getComponentInstantiationListeners()
                .add(new SpringComponentInjector(tester.getApplication(), appctx));
    }

    @After
    public void tearDown() {
        tester.destroy();
    }

    /**
     * @return the application context
     */
    protected ApplicationContextMock getAppContext() {
        return appctx;
    }

    /**
     * @return the tester
     */
    protected WicketTester getTester() {
        return tester;
    }
}
