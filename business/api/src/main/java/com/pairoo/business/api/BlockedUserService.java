package com.pairoo.business.api;

import com.datazuul.framework.business.services.DomainObjectService;
import com.googlecode.genericdao.search.Search;
import com.pairoo.business.exceptions.AlreadyExistsException;
import com.pairoo.domain.BlockedUser;
import com.pairoo.domain.User;

/**
 * @author Ralf Eichinger
 */
public interface BlockedUserService extends DomainObjectService<Long, BlockedUser> {
	public static final String BEAN_ID = "blockedUserService";

    /**
     * Add an user as blocked user: avoids getting in touch with him again in any way.
     *
     * @param owner the user who wants to block another user
     * @param target the user to be blocked
     * @throws AlreadyExistsException if already blocked
     */
	public void add(User owner, User target) throws AlreadyExistsException;

	/**
	 * Create a Search for getting the blocked users of the given user
	 * @param owner owner of the blocked users
	 * @return search criteria object
	 */
	public Search getBlockedUsersSearchCriteria(User owner);
}
