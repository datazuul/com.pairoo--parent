package com.pairoo.backend.db.hibernate;

import org.junit.BeforeClass;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Ralf Eichinger
 */

@ContextConfiguration(locations = { "classpath:/com/pairoo/domain/springBeans-test.xml",
	"classpath:/com/pairoo/backend/db/springBeans.xml" })
@TestExecutionListeners(TransactionalTestExecutionListener.class)
@Transactional
public abstract class AbstractITCase extends com.datazuul.framework.persistence.test.AbstractTest {

    @BeforeClass
    public static void setup() throws Exception {
	System.setProperty("env", "TEST");
    }
}
