package com.pairoo.backend.db.hibernate.marketing;

import com.datazuul.framework.persistence.dao.hibernate.ExtendedGenericDaoImpl;
import com.pairoo.backend.dao.marketing.PromotionDao;
import com.pairoo.domain.marketing.Promotion;

/**
 * @author Ralf Eichinger
 */
public class PromotionDaoImpl extends ExtendedGenericDaoImpl<Promotion, Long> implements PromotionDao {
}
