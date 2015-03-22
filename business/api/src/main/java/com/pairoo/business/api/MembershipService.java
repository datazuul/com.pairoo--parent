package com.pairoo.business.api;

import com.datazuul.framework.business.services.DomainObjectService;
import com.pairoo.domain.Membership;
import com.pairoo.domain.UserAccount;
import com.pairoo.domain.enums.MembershipStatus;

public interface MembershipService extends DomainObjectService<Long, Membership> {
    public static final String BEAN_ID = "membershipService";

    /**
     * Adds a new membership to an user.
     * If new membership is valid (already in active time frame), user's current
     * membership is immediately updated, too.
     * 
     * @param userAccount user to add new membership to
     * @param membership new membership
     */
    void addMembership(UserAccount userAccount, Membership membership, MembershipStatus membershipStatus);

    Membership getFirstMembership(UserAccount userAccount);

    Membership getCurrentMembership(UserAccount userAccount);

    Membership getLastMembership(UserAccount userAccount);

    void activateMembership(UserAccount userAccount);
}
