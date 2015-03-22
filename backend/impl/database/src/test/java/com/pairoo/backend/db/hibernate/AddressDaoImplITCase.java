package com.pairoo.backend.db.hibernate;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.pairoo.domain.Address;

/**
 * Testing not against Mock, testing against (real) in-memory database.
 * 
 * @author Ralf Eichinger
 */
public class AddressDaoImplITCase extends AbstractITCase {
    @Autowired
    protected AddressDaoImpl dao;

    @Test
    public void testSaveAndFind() {
	Long id = new Long(1);
	Address result = (Address) dao.find(id);
	Assert.assertNull(result);

	Address domainObj = (Address) getBean("Address_unsaved", Address.class);
	dao.save(domainObj);

	result = (Address) dao.find(domainObj.getId());
	Assert.assertNotNull(result);
	Assert.assertEquals(domainObj, result);
    }

    @Test
    public void testCount() {
	Long id = new Long(1);
	Address result = (Address) dao.find(id);
	Assert.assertNull(result);

	Address domainObj1 = (Address) getBean("Address_unsaved", Address.class);
	dao.save(domainObj1);

	Address domainObj2 = (Address) getBean("Address_unsaved", Address.class);
	dao.save(domainObj2);

	long count = dao.count(null);
	Assert.assertEquals(2, count);
    }

    @Test
    public void testFindAll() {
	Long id = new Long(1);
	Address result = (Address) dao.find(id);
	Assert.assertNull(result);

	Address domainObj1 = (Address) getBean("Address_unsaved", Address.class);
	dao.save(domainObj1);

	Address domainObj2 = (Address) getBean("Address_unsaved", Address.class);
	dao.save(domainObj2);

	List<Address> list = dao.findAll();
	Assert.assertEquals(2, list.size());
    }
}
