package com.pairoo.backend.db.hibernate;

import com.datazuul.framework.persistence.dao.hibernate.ExtendedGenericDaoImpl;
import com.pairoo.backend.dao.MessageDao;
import com.pairoo.domain.Message;

/**
 * @author Ralf Eichinger
 */
public class MessageDaoImpl extends ExtendedGenericDaoImpl<Message, Long> implements MessageDao {

}
