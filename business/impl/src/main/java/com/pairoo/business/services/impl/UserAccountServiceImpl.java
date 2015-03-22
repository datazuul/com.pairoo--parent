package com.pairoo.business.services.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datazuul.framework.business.services.persistence.AbstractDomainObjectServiceImpl;
import com.datazuul.framework.domain.authorization.Roles;
import com.datazuul.framework.security.HashResult;
import com.datazuul.framework.security.HashUtil;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import com.pairoo.backend.dao.UserAccountDao;
import com.pairoo.business.api.UserAccountService;
import com.pairoo.domain.UserAccount;
import com.pairoo.domain.enums.Role;

/**
 * @author Ralf Eichinger
 */
public class UserAccountServiceImpl extends AbstractDomainObjectServiceImpl<Long, UserAccount> implements
	UserAccountService {

    static final Logger LOGGER = LoggerFactory.getLogger(UserAccountServiceImpl.class);

    public UserAccountServiceImpl(final UserAccountDao dao) {
	super(dao);
    }

    @Override
    public int countUserAccountsOnline() {
	final UserAccount example = new UserAccount();
	example.setOnline(true);
	final Filter filter = dao.getFilterFromExample(example);
	final Search search = new Search();
	search.addFilters(filter);

	return dao.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.pairoo.business.services.UserAccountService#getByPseudonym(java.lang
     * .String)
     */
    @Override
    public UserAccount getByUsername(final String username) {
	LOGGER.info("username = {}", username);
	final Search search = new Search(UserAccount.class);
	search.addFilterEqual("username", username);
	return ((UserAccountDao) dao).searchUnique(search);
    }

    @Override
    public UserAccount login(final String username, final String password) {
	LOGGER.info("tries to login: username = {}", username);
	// find matching useraccount with given username and password
	final Search search = new Search(UserAccount.class);
	search.addFilterEqual("username", username);
	final UserAccount userAccount = ((UserAccountDao) dao).searchUnique(search);

	boolean userExists;
	String digest;
	String salt;
	if (userAccount != null) {
	    userExists = true;
	    digest = userAccount.getPassword();
	    salt = userAccount.getPasswordSalt();
	} else {
	    userExists = false;
	    // TIME RESISTANT ATTACK (Even if the user does not exist the
	    // Computation time is equal to the time needed for a legitimate
	    // user)
	    digest = "000000000000000000000000000=";
	    salt = "00000000000=";
	}

	byte[] bDigest;
	byte[] bSalt;
	try {
	    bDigest = HashUtil.base64ToByte(digest);
	    bSalt = HashUtil.base64ToByte(salt);
	    // Compute the new DIGEST
	    byte[] proposedDigest = HashUtil.hash(password, bSalt);
	    if (Arrays.equals(proposedDigest, bDigest) && userExists) {
		return onSuccessfulLogin(userAccount);
	    }
	} catch (IOException ex) {
	    LOGGER.error("base64 to byte method failed!", ex);
	    return null;
	}
	return null;
    }

    @Override
    public UserAccount login(final UserAccount userAccount) {
	LOGGER.info("logging in: useraccount = {}", userAccount.getUsername());
	return onSuccessfulLogin(userAccount);
    }

    private UserAccount onSuccessfulLogin(final UserAccount userAccount) {
	// if login successful
	if (userAccount != null) {
	    // shift last login to previous login
	    userAccount.setPreviousLogin(userAccount.getLastLogin());

	    // update last login
	    userAccount.setLastLogin(new Date());

	    // set status to "online"
	    userAccount.setOnline(true);

	    dao.save(userAccount);
	}
	return userAccount;
    }

    @Override
    public void logout(final UserAccount userAccount) {
	userAccount.setOnline(false);
	dao.save(userAccount);

	LOGGER.info("logged out username = {}", userAccount.getUsername());
    }

    @Override
    public boolean isPremiumMember(UserAccount userAccount) {
	Date premiumEndDate = userAccount.getPremiumEndDate();
	return !isPremiumEndDateExceeded(premiumEndDate);
    }

    @Override
    public void addRole(UserAccount userAccount, String role) {
	Roles roles = userAccount.getRoles();
	if (roles == null) {
	    roles = new Roles(Role.STANDARD.getCode());
	    LOGGER.info("adding role FREE to useraccount {}", new Object[] { userAccount.getUsername() });
	}
	if (!roles.hasRole(role)) {
	    roles.add(role);
	    LOGGER.info("adding role {} to useraccount {}", new Object[] { role, userAccount.getUsername() });
	}
	userAccount.setRoles(roles);
    }

    @Override
    public void hashPassword(UserAccount userAccount) {
	final String passwd = userAccount.getPassword();
	final HashResult hashResult = HashUtil.hash(passwd);
	userAccount.setPasswordSalt(hashResult.getSalt());
	userAccount.setPassword(hashResult.getDigest());
    }

    @Override
    public boolean isPremiumEndDateExceeded(Date premiumEndDate) {
	boolean result = false;
	if (premiumEndDate == null || premiumEndDate.before(new Date())) {
	    result = true;
	}
	return result;
    }
}
