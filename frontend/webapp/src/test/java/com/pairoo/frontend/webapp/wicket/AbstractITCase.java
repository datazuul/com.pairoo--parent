package com.pairoo.frontend.webapp.wicket;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author Ralf Eichinger
 */

@ContextConfiguration(locations = { "classpath:/com/pairoo/frontend/webapp/springBeans.xml" })
public abstract class AbstractITCase extends com.datazuul.framework.persistence.test.AbstractTest {

    protected WicketTester tester;

    @BeforeClass
    public static void setupClass() throws Exception {
	System.setProperty("env", "TEST");
    }

    @Before
    public void setupInstance() {
	WicketWebApplication app = applicationContext.getBean(WicketWebApplication.class);
	app.setApplicationContext(this.applicationContext);
	tester = new WicketTester(app);
    }

}
