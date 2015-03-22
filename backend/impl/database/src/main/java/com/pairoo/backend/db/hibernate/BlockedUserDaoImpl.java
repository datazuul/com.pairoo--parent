package com.pairoo.backend.db.hibernate;

import com.datazuul.framework.persistence.dao.hibernate.ExtendedGenericDaoImpl;
import com.pairoo.backend.dao.BlockedUserDao;
import com.pairoo.domain.BlockedUser;

/**
 * @author Ralf Eichinger
 */
public class BlockedUserDaoImpl extends ExtendedGenericDaoImpl<BlockedUser, Long> implements BlockedUserDao {
}
