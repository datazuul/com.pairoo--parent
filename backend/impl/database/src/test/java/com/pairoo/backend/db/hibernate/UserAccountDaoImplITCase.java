package com.pairoo.backend.db.hibernate;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.pairoo.domain.UserAccount;

/**
 * Testing not against Mock, testing against (real) in-memory database.
 * 
 * @author Ralf Eichinger
 */
public class UserAccountDaoImplITCase extends AbstractITCase {
    @Autowired
    protected UserAccountDaoImpl dao;

    @Test
    public void testDelete() {
	// save a new domain object
	final UserAccount domainObj = getBean("UserAccount_unsaved", UserAccount.class);
	dao.save(domainObj);
	final Long id = domainObj.getId();

	UserAccount result = dao.find(id);
	Assert.assertNotNull(result);
	Assert.assertEquals(domainObj, result);

	dao.remove(domainObj);

	result = dao.find(id);
	Assert.assertNull(result);
    }

    @Test
    public void testSave() {
	final Long id = new Long(1);
	UserAccount result = dao.find(id);
	Assert.assertNull(result);

	final UserAccount domainObj = getBean("UserAccount_unsaved", UserAccount.class);
	dao.save(domainObj);

	result = dao.find(domainObj.getId());
	Assert.assertNotNull(result);
	Assert.assertEquals(domainObj, result);
    }

    @Test
    public void testUpdate() {
	final Long id = new Long(1);
	UserAccount result = dao.find(id);
	Assert.assertNull(result);

	final UserAccount domainObj = getBean("UserAccount_unsaved", UserAccount.class);
	dao.save(domainObj);

	result = dao.find(domainObj.getId());
	Assert.assertNotNull(result);
	Assert.assertEquals(domainObj, result);

	final String newFieldValue = "12345678";
	domainObj.setPassword(newFieldValue);

	dao.save(domainObj);

	result = dao.find(domainObj.getId());
	Assert.assertNotNull(result);
	Assert.assertEquals(domainObj, result);
	Assert.assertEquals(newFieldValue, result.getPassword());
    }

    @Test
    public void testGet() {
	final Long id = new Long(1);
	// no result found:
	UserAccount domainObj = dao.find(id);
	Assert.assertNull("No DomainObject found", domainObj);

	// save a new domain object
	domainObj = getBean("UserAccount_unsaved", UserAccount.class);
	dao.save(domainObj);

	// get the saved domain object
	final UserAccount result = dao.find(domainObj.getId());

	Assert.assertNotNull(result);
	Assert.assertEquals(domainObj, result);
    }

    @Test
    public void testFindAll() {
	final List<UserAccount> domainObjectList = new ArrayList<UserAccount>();
	for (int i = 0; i < 10; i++) {
	    final UserAccount domainObj = getBean("UserAccount_unsaved", UserAccount.class);
	    // make unique fields unique:
	    domainObj.setUsername(i + domainObj.getUsername());

	    domainObjectList.add(domainObj);
	    dao.save(domainObj);
	}

	final List<UserAccount> result = dao.findAll();

	Assert.assertEquals(10, result.size());
    }

    @Test
    public void testCountAll() {
	final List<UserAccount> domainObjectList = new ArrayList<UserAccount>();
	for (int i = 0; i < 10; i++) {
	    final UserAccount domainObj = getBean("UserAccount_unsaved", UserAccount.class);
	    // make unique fields unique:
	    domainObj.setUsername(i + domainObj.getUsername());

	    domainObjectList.add(domainObj);
	    dao.save(domainObj);
	}

	final Long result = new Long(dao.count(null));
	Assert.assertEquals(new Long(10), result);
    }

}
