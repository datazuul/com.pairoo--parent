package com.pairoo.business.services.impl.marketing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datazuul.framework.business.services.persistence.AbstractDomainObjectServiceImpl;
import com.googlecode.genericdao.search.Search;
import com.pairoo.backend.dao.marketing.PromotionDao;
import com.pairoo.business.api.marketing.PromotionService;
import com.pairoo.domain.marketing.Promotion;

public class PromotionServiceImpl extends
	AbstractDomainObjectServiceImpl<Long, Promotion> implements
	PromotionService {
    static final Logger LOGGER = LoggerFactory
	    .getLogger(PromotionServiceImpl.class);

    /**
     * Constructor needed for test handing over a dao.
     * 
     * @param dao
     *            the data access interface
     */
    public PromotionServiceImpl(final PromotionDao dao) {
	super(dao);
    }

    @Override
    public Promotion getPromotionByCode(String promotionCode) {
	LOGGER.info("promotion code = {}", promotionCode);
	final Search search = new Search(Promotion.class);
	search.addFilterEqual("code", promotionCode);
	return ((PromotionDao) dao).searchUnique(search);
    }
}
