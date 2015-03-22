package com.pairoo.business.api.marketing;

import com.datazuul.framework.business.services.DomainObjectService;
import com.pairoo.domain.marketing.Promotion;

public interface PromotionService extends DomainObjectService<Long, Promotion> {
    public static final String BEAN_ID = "promotionService";

    public Promotion getPromotionByCode(String promotionCode);
}
