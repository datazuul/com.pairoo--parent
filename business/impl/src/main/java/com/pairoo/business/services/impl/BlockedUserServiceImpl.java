package com.pairoo.business.services.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datazuul.framework.business.services.persistence.AbstractDomainObjectServiceImpl;
import com.googlecode.genericdao.search.Search;
import com.pairoo.backend.dao.BlockedUserDao;
import com.pairoo.business.api.BlockedUserService;
import com.pairoo.business.exceptions.AlreadyExistsException;
import com.pairoo.domain.BlockedUser;
import com.pairoo.domain.User;

/**
 * @author Ralf Eichinger
 */
public class BlockedUserServiceImpl extends AbstractDomainObjectServiceImpl<Long, BlockedUser> implements BlockedUserService {

	static final Logger LOGGER = LoggerFactory.getLogger(BlockedUserServiceImpl.class);

	/**
	 * Constructor needed for test handing over a mock dao.
	 *
	 * @param dao
	 *            the data access interface
	 */
	public BlockedUserServiceImpl(final BlockedUserDao dao) {
		super(dao);
	}

	@Override
	public void add(final User owner, final User target) throws AlreadyExistsException {
		// search if already favorite
		final Search search = new Search(BlockedUser.class);
		search.addFilterEqual("owner.id", owner.getId());
		search.addFilterEqual("target.id", target.getId());
		final List<BlockedUser> listBlockedUser = search(search);

		BlockedUser blockedUser = null;
		if (!listBlockedUser.isEmpty()) {
			// if already BlockedUser: do nothing
			throw new AlreadyExistsException();
		} else {
			// if not BlockedUser, yet
			blockedUser = new BlockedUser();
			blockedUser.setTimeStamp(new Date());
			blockedUser.setOwner(owner);
			blockedUser.setTarget(target);

			// 1. save
			final boolean saved = save(blockedUser);

			// 2. notifications?
		}
	}

	@Override
	public Search getBlockedUsersSearchCriteria(final User owner) {
		final Search search = new Search(BlockedUser.class);

		// owner is given user
		search.addFilterEqual("owner.id", owner.getId());

		return search;
	}
}
