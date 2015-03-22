package com.pairoo.business.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datazuul.framework.business.services.persistence.AbstractDomainObjectServiceImpl;
import com.googlecode.genericdao.search.Search;
import com.pairoo.backend.dao.LandingPageActionDao;
import com.pairoo.business.api.LandingPageActionService;
import com.pairoo.domain.LandingPageAction;

/**
 * @author Ralf Eichinger
 */
public class LandingPageActionServiceImpl extends AbstractDomainObjectServiceImpl<Long, LandingPageAction> implements
	LandingPageActionService {
    static final Logger LOGGER = LoggerFactory.getLogger(LandingPageActionServiceImpl.class);

    public LandingPageActionServiceImpl(final LandingPageActionDao dao) {
	super(dao);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.pairoo.business.api.LandingPageActionService#getByToken(java.lang
     * .String)
     */
    @Override
    public LandingPageAction getByToken(final String token) {
	LOGGER.info("token = {}", token);
	final Search search = new Search(LandingPageAction.class);
	search.addFilterEqual("token", token);
	LandingPageAction lpa = null;
	try {
	    lpa = ((LandingPageActionDao) dao).searchUnique(search);
	} catch (Exception e) {
	    e.printStackTrace();
	    // problem of generic dao framework when token does not exist:
	    // Caused by: org.hibernate.NonUniqueResultException: query did not
	    // return a unique result: 94
	    // at
	    // org.hibernate.impl.AbstractQueryImpl.uniqueElement(AbstractQueryImpl.java:899)
	    // at
	    // org.hibernate.impl.AbstractQueryImpl.uniqueResult(AbstractQueryImpl.java:890)
	    // at
	    // com.googlecode.genericdao.search.hibernate.HibernateSearchProcessor.searchUnique(HibernateSearchProcessor.java:191)
	    // at
	    // com.googlecode.genericdao.dao.hibernate.HibernateBaseDAO._searchUnique(HibernateBaseDAO.java:600)
	    // at
	    // com.googlecode.genericdao.dao.hibernate.GenericDAOImpl.searchUnique(GenericDAOImpl.java:125)
	    // at
	    // com.pairoo.business.services.impl.LandingPageActionServiceImpl.getByToken(LandingPageActionServiceImpl.java:35)
	}
	return lpa;
    }

    /**
     * DEPRECATED: no longer like this: If no token is set yet, generates an
     * unique token to be included in an URL and being used as unique key to
     * access related data (user account, landingpage action) from persistence,
     * before saving
     * 
     * @see com.pairoo.business.api.LandingPageActionService#generateLandingPageAction
     *      (com.pairoo.domain.UserAccount,
     *      com.pairoo.domain.enums.LandingPageActionType)
     */
    @Override
    public boolean save(final LandingPageAction landingPageAction) {
	return super.save(landingPageAction);
    }
}
