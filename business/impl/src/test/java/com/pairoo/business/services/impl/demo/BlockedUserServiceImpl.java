/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pairoo.business.services.impl.demo;

import com.googlecode.genericdao.search.ISearch;
import com.googlecode.genericdao.search.Search;
import com.pairoo.business.api.BlockedUserService;
import com.pairoo.business.exceptions.AlreadyExistsException;
import com.pairoo.domain.BlockedUser;
import com.pairoo.domain.User;
import java.util.List;

/**
 *
 * @author reiching
 */
public class BlockedUserServiceImpl implements BlockedUserService {

    @Override
    public void add(User owner, User target) throws AlreadyExistsException {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Search getBlockedUsersSearchCriteria(User owner) {
        //throw new UnsupportedOperationException("Not supported yet.");
        return null;
    }

    @Override
    public long count(ISearch search) {
        // throw new UnsupportedOperationException("Not supported yet.");
        return 0;
    }

    @Override
    public Long countAll() {
        //throw new UnsupportedOperationException("Not supported yet.");
        return new Long(0);
    }

    @Override
    public boolean delete(BlockedUser object) {
        //throw new UnsupportedOperationException("Not supported yet.");
        return true;
    }

    @Override
    public List<BlockedUser> findAll(int offset, int max) {
        //throw new UnsupportedOperationException("Not supported yet.");
        return null;
    }

    @Override
    public BlockedUser get(Long id) {
        //throw new UnsupportedOperationException("Not supported yet.");
        return null;
    }

    @Override
    public boolean save(BlockedUser object) {
        //throw new UnsupportedOperationException("Not supported yet.");
        return true;
    }

    @Override
    public List<BlockedUser> search(ISearch search) {
        //throw new UnsupportedOperationException("Not supported yet.");
        return null;
    }

    @Override
    public void update(BlockedUser object) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

}
