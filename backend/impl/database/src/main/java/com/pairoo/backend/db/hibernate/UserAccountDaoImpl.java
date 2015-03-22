package com.pairoo.backend.db.hibernate;

import com.datazuul.framework.persistence.dao.hibernate.ExtendedGenericDaoImpl;
import com.pairoo.backend.dao.UserAccountDao;
import com.pairoo.domain.UserAccount;

/**
 * @author Ralf Eichinger
 */
public class UserAccountDaoImpl extends ExtendedGenericDaoImpl<UserAccount, Long> implements UserAccountDao {
}