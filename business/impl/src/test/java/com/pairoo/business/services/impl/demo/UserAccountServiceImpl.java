package com.pairoo.business.services.impl.demo;

import java.util.Date;
import java.util.List;

import com.googlecode.genericdao.search.ISearch;
import com.pairoo.business.api.UserAccountService;
import com.pairoo.business.services.impl.DataRepository;
import com.pairoo.domain.UserAccount;

/**
 * @author Ralf Eichinger
 *
 */
public class UserAccountServiceImpl implements UserAccountService {

    private final DataRepository repo;

    public UserAccountServiceImpl(final DataRepository repo) {
        super();
        this.repo = repo;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.javapro.framework.business.services.DomainObjectService#count(com.
     * googlecode.genericdao.search.ISearch)
     */
    @Override
    public long count(final ISearch search) {
        return 10;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.javapro.framework.business.services.DomainObjectService#countAll()
     */
    @Override
    public Long countAll() {
        return new Long(20);
    }

    @Override
    public int countUserAccountsOnline() {
        return 1;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.javapro.framework.business.services.DomainObjectService#delete(de.
     * javapro.framework.domain.DomainObject)
     */
    @Override
    public boolean delete(final UserAccount object) {
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.javapro.framework.business.services.DomainObjectService#findAll(int,
     * int)
     */
    @Override
    public List<UserAccount> findAll(final int offset, final int max) {
        return repo.getUserAccountList();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.javapro.framework.business.services.DomainObjectService#get(java.io
     * .Serializable)
     */
    @Override
    public UserAccount get(final Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.pairoo.business.api.UserAccountService#getByUsername(java.lang.String
     * )
     */
    @Override
    public UserAccount getByUsername(final String username) {
        if ("adam".equals(username)) {
            return repo.getAdam().getUserAccount();
        } else if ("eva".equals(username)) {
            return repo.getEva().getUserAccount();
        }
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserAccount login(final String username, final String password) {
        UserAccount result = null;
        result = getByUsername(username);
        if (result != null) {
            result.setOnline(true);
        }
        return result;
    }

    @Override
    public void logout(final UserAccount userAccount) {
        userAccount.setOnline(false);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.javapro.framework.business.services.DomainObjectService#save(de.javapro
     * .framework.domain.DomainObject)
     */
    @Override
    public boolean save(final UserAccount object) {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.javapro.framework.business.services.DomainObjectService#search(com
     * .googlecode.genericdao.search.ISearch)
     */
    @Override
    public List<UserAccount> search(final ISearch search) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.javapro.framework.business.services.DomainObjectService#update(de.
     * javapro.framework.domain.DomainObject)
     */
    @Override
    public void update(final UserAccount object) {
        // TODO Auto-generated method stub
    }

    @Override
    public UserAccount login(final UserAccount userAccount) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isPremiumMember(UserAccount userAccount) {
        return true;
    }

    @Override
    public void addRole(UserAccount userAccount, String role) {
        // TODO Auto-generated method stub
    }

    @Override
    public void hashPassword(UserAccount userAccount) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean isPremiumEndDateExceeded(Date premiumEndDate) {
        return false;
    }
}
