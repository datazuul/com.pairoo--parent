package com.pairoo.business.services.impl.demo;

import java.util.List;
import java.util.Locale;

import com.googlecode.genericdao.search.ISearch;
import com.pairoo.business.api.UserService;
import com.pairoo.business.exceptions.RegistrationException;
import com.pairoo.business.exceptions.ReportProfileException;
import com.pairoo.domain.SearchProfile;
import com.pairoo.domain.User;
import com.pairoo.domain.marketing.Promotion;
import com.pairoo.domain.search.UserSearchResult;

public class UserServiceImpl implements UserService {

    @Override
    public long count(final ISearch search) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Long countAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean delete(final User object) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<User> findAll(final int offset, final int max) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public User get(final Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<User> getByEmail(final String email) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public User register(final User user, final Locale locale, final String activationLink) {
        return user;
    }

    @Override
    public boolean save(final User object) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<User> search(final ISearch search) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void update(final User object) {
        // TODO Auto-generated method stub
    }

    @Override
    public void resetPassword(final User user, final Locale locale) {
        // TODO Auto-generated method stub
    }

    @Override
    public void changePassword(final User user, final Locale locale, final String oldPassword,
            final String newPassword, final String confirmNewPassword) {
        // TODO Auto-generated method stub
    }

    @Override
    public void reportProfile(User reporter, User profileUser, String text, Locale locale)
            throws ReportProfileException {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public User getRegistrationDefaultUser(Locale locale) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void registerAndEncashVoucher(User user, Locale preferredLocale, String activationLink, Promotion promotion)
            throws RegistrationException {
        // TODO Auto-generated method stub
    }

    @Override
    public List<UserSearchResult> search(User user, SearchProfile searchProfile, long first, long count) {
        return null;
    }

    @Override
    public long count(User user, SearchProfile searchProfile) {
        return 0;
    }
}
