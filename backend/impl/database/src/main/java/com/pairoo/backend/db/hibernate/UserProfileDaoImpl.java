package com.pairoo.backend.db.hibernate;

import com.datazuul.framework.persistence.dao.hibernate.ExtendedGenericDaoImpl;
import com.pairoo.backend.dao.UserProfileDao;
import com.pairoo.domain.UserProfile;

/**
 * @author Ralf Eichinger
 */
public class UserProfileDaoImpl extends ExtendedGenericDaoImpl<UserProfile, Long> implements UserProfileDao {

}
