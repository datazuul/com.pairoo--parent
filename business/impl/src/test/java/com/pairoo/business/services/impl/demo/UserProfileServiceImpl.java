package com.pairoo.business.services.impl.demo;

import java.util.Date;
import java.util.List;

import com.googlecode.genericdao.search.ISearch;
import com.pairoo.business.api.UserProfileService;
import com.pairoo.domain.UserProfile;

/**
 * @author Ralf Eichinger
 */
public class UserProfileServiceImpl implements UserProfileService {

	/* (non-Javadoc)
	 * @see de.javapro.framework.business.services.DomainObjectService#count(com.googlecode.genericdao.search.ISearch)
	 */
	@Override
	public long count(ISearch search) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see de.javapro.framework.business.services.DomainObjectService#countAll()
	 */
	@Override
	public Long countAll() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.javapro.framework.business.services.DomainObjectService#delete(de.javapro.framework.domain.DomainObject)
	 */
	@Override
	public boolean delete(UserProfile object) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see de.javapro.framework.business.services.DomainObjectService#findAll(int, int)
	 */
	@Override
	public List<UserProfile> findAll(int offset, int max) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.javapro.framework.business.services.DomainObjectService#get(java.io.Serializable)
	 */
	@Override
	public UserProfile get(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.javapro.framework.business.services.DomainObjectService#save(de.javapro.framework.domain.DomainObject)
	 */
	@Override
	public boolean save(UserProfile object) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see de.javapro.framework.business.services.DomainObjectService#search(com.googlecode.genericdao.search.ISearch)
	 */
	@Override
	public List<UserProfile> search(ISearch search) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.javapro.framework.business.services.DomainObjectService#update(de.javapro.framework.domain.DomainObject)
	 */
	@Override
	public void update(UserProfile object) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.pairoo.business.api.UserProfileService#getRandomBirthdate(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Date getRandomBirthdate(Integer minAge, Integer maxAge) {
		// TODO Auto-generated method stub
		return null;
	}

}
