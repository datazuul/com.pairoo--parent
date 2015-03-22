package com.pairoo.business.services.impl;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datazuul.framework.business.services.persistence.AbstractDomainObjectServiceImpl;
import com.pairoo.backend.dao.UserProfileDao;
import com.pairoo.business.api.UserProfileService;
import com.pairoo.domain.UserProfile;

/**
 * @author Ralf Eichinger
 */
public class UserProfileServiceImpl extends AbstractDomainObjectServiceImpl<Long, UserProfile> implements
		UserProfileService {

	static final Logger LOGGER = LoggerFactory.getLogger(UserProfileServiceImpl.class);

	/**
	 * Constructor needed for test handing over a mock dao.
	 * 
	 * @param dao
	 *            the data access interface
	 */
	public UserProfileServiceImpl(UserProfileDao dao) {
		super(dao);
	}

	/* (non-Javadoc)
	* @see com.pairoo.business.services.UserProfileService#getRandomBirthdate(java.lang.Integer, java.lang.Integer)
	*/
	@Override
	public Date getRandomBirthdate(Integer minAge, Integer maxAge) {
		int age = minAge + (int) (Math.random() * (maxAge - minAge) + 0.5);
		Calendar now = Calendar.getInstance();
		now.add(Calendar.YEAR, (-1) * age);
		return now.getTime();
	}
}
