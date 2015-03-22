package com.pairoo.backend.db.hibernate;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.pairoo.domain.Message;
import com.pairoo.domain.User;
import com.pairoo.domain.enums.MessageType;

/**
 * Testing not against Mock, testing against (real) in-memory database.
 * 
 * @author Ralf Eichinger
 */
public class MessageDaoImplITCase extends AbstractITCase {
	@Autowired
	protected MessageDaoImpl dao;

	private Message createDomainObject() {
		final Message domainObj = new Message();
		domainObj.setMessageType(MessageType.TEXT);

		final User sender = new User();
		sender.setId(new Long(1));
		domainObj.setSender(sender);

		final User receiver = new User();
		receiver.setId(new Long(2));
		domainObj.setReceiver(receiver);

		domainObj.setSubject("Test");
		domainObj.setText("This is a test.");
		return domainObj;
	}

	// @Test
	// public void testCountAll() {
	// final List<Message> domainObjectList = new ArrayList<Message>();
	// for (int i = 0; i < 50; i++) {
	// final Message domainObj = createDomainObject();
	//
	// // make unique fields unique:
	// // ...
	//
	// domainObjectList.add(domainObj);
	// dao.save(domainObj);
	// }
	//
	// final Long result = new Long(dao.count(null));
	// Assert.assertEquals(new Long(50), result);
	// }

	@Test
	public void testDelete() {
		// save a new domain object
		final Message domainObj = createDomainObject();
		dao.save(domainObj);
		final Long id = domainObj.getId();

		Message result = dao.find(id);
		Assert.assertNotNull(result);
		Assert.assertEquals(domainObj, result);

		dao.remove(domainObj);

		result = dao.find(id);
		Assert.assertNull(result);
	}

	// @Test
	// public void testFindAll() {
	// final List<Message> domainObjectList = new ArrayList<Message>();
	// for (int i = 0; i < 50; i++) {
	// final Message domainObj = createDomainObject();
	//
	// // make unique fields unique:
	// // ...
	//
	// domainObjectList.add(domainObj);
	// dao.save(domainObj);
	// }
	//
	// final List<Message> result = dao.findAll();
	//
	// Assert.assertEquals(50, result.size());
	// }

	@Test
	public void testGet() {
		final Long id = new Long(1);
		// no result found:
		Message domainObj = dao.find(id);
		Assert.assertNull("No DomainObject found", domainObj);

		// save a new domain object
		domainObj = createDomainObject();
		dao.save(domainObj);

		// get the saved domain object
		final Message result = dao.find(domainObj.getId());

		Assert.assertNotNull(result);
		Assert.assertEquals(domainObj, result);
	}

	@Test
	public void testSave() {
		final Long id = new Long(1);
		Message result = dao.find(id);
		Assert.assertNull(result);

		final Message domainObj = createDomainObject();
		dao.save(domainObj);

		result = dao.find(domainObj.getId());
		Assert.assertNotNull(result);
		Assert.assertEquals(domainObj, result);
	}

	@Test
	public void testUpdate() {
		final Long id = new Long(1);
		Message result = dao.find(id);
		Assert.assertNull(result);

		final Message domainObj = createDomainObject();
		dao.save(domainObj);

		result = dao.find(domainObj.getId());
		Assert.assertNotNull(result);
		Assert.assertEquals(domainObj, result);

		final String newFieldValue = "12345678";
		domainObj.setSubject(newFieldValue);

		dao.save(domainObj);

		result = dao.find(domainObj.getId());
		Assert.assertNotNull(result);
		Assert.assertEquals(domainObj, result);
		Assert.assertEquals(newFieldValue, result.getSubject());
	}
}
