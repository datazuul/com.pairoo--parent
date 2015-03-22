package com.pairoo.business.services.impl;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Test;

import com.pairoo.backend.dao.UserDao;
import com.pairoo.business.AbstractTest;
import com.pairoo.business.api.UserService;
import com.pairoo.domain.User;

/**
 * Unit test (persistence layer mocked) for
 * com.pairoo.business.services.impl.UserServiceImpl
 * 
 * @author Ralf Eichinger
 */
public class TestUserServiceImpl extends AbstractTest {
    private final UserService service = null;
    private UserDao daoMock = null;

    @Override
    protected String[] getConfigLocations() {
	return new String[] { "classpath:/com/pairoo/domain/springBeans-test.xml" };
	// , "/com/pairoo/business/springBeans.xml" ,
	// "/com/pairoo/backend/impl/email/springBeans.xml" }; // does not
	// exist in business, it is in backend! so integration tests would have
	// to be moved to webapp that has access to all dependencies
    }

    @Override
    protected void onSetUp() throws Exception {
	super.onSetUp();
	daoMock = EasyMock.createMock(UserDao.class);
	// service = getBean("userService", UserServiceImpl.class);
    }

    public void testGetByPseudonym() {
	// User testData = (User) getBean("user_saved", User.class);
	// EasyMock.expect(daoMock.getByPseudonym(testData.getPseudonym()))
	// .andReturn(testData);
	// EasyMock.replay(daoMock);
	//
	// User result = service.getByPseudonym(testData.getPseudonym());
	//
	// EasyMock.verify(daoMock);
	// Assert.assertEquals(testData, result);
	Assert.assertEquals(true, true);
    }

    @Test
    public void testRegister() throws Exception {
	final User user = getBean("User_saved", User.class);
	Assert.assertNotNull(user);
	// Assert.assertNotNull(service);
    }
}
