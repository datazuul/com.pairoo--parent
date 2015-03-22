package com.pairoo.business.api;

import com.datazuul.framework.business.services.DomainObjectService;
import com.pairoo.domain.LandingPageAction;

/**
 * @author Ralf Eichinger
 */
public interface LandingPageActionService extends DomainObjectService<Long, LandingPageAction> {
    public static final String BEAN_ID = "landingPageActionService";

    /**
     * Find landing page action for the given token.
     * 
     * @param token
     *            token of the landing page action
     * @return landing page action
     */
    public LandingPageAction getByToken(String token);
}
