package com.pairoo.business.api;

import java.util.Date;

import com.datazuul.framework.business.services.DomainObjectService;
import com.pairoo.domain.UserProfile;

/**
 * @author Ralf Eichinger
 */
public interface UserProfileService extends DomainObjectService<Long, UserProfile> {
	public static final String BEAN_ID = "userProfileService";

	/**
	 * Gets a random birthdate within a given age range.
	 * Day and month of birthdate are the same than today's date.
	 * @param minAge minimal age
	 * @param maxAge maximal age
	 * @return random date of birth
	 */
	public Date getRandomBirthdate(Integer minAge, Integer maxAge);
}
