package com.pairoo.business.api;

import com.datazuul.framework.business.services.DomainObjectService;
import com.pairoo.domain.UserAccount;
import java.util.Date;

/**
 * @author ralf
 */
public interface UserAccountService extends DomainObjectService<Long, UserAccount> {

    public static final String BEAN_ID = "userAccountService";

    public void addRole(UserAccount userAccount, String role);

    /**
     * Count all user accounts currently logged in.
     *
     * @return online user accounts
     */
    public int countUserAccountsOnline();

    /**
     * Find UserAccount matching the (unique) username.
     *
     * @param username search param
     * @return matching UserAccount
     */
    public UserAccount getByUsername(String username);

    /**
     * @param username login credential username
     * @param password login credential password
     * @return matching useraccount or null
     */
    public UserAccount login(String username, String password);

    /**
     * @param userAccount useraccount to log in
     * @return useraccount
     */
    public UserAccount login(UserAccount userAccount);

    /**
     * Execute logout business logic for given user account.
     *
     * @param userAccount user account to be logged out
     */
    public void logout(UserAccount userAccount);

    public boolean isPremiumMember(UserAccount userAccount);

    public boolean isPremiumEndDateExceeded(Date premiumEndDate);

    /**
     * @param userAccount user account whose password should be hashed
     */
    public void hashPassword(UserAccount userAccount);
}
