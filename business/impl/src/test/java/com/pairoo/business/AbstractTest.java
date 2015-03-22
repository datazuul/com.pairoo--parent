package com.pairoo.business;

/**
 * @author Ralf Eichinger
 */
public abstract class AbstractTest extends com.datazuul.framework.business.test.AbstractTest {
    @Override
    protected String[] getConfigLocations() {
	return new String[] { "classpath:/com/pairoo/domain/springBeans-test.xml" };
    }
}
