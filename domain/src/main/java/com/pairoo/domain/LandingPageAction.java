package com.pairoo.domain;

import java.util.UUID;

import com.datazuul.framework.domain.AbstractPersistentDomainObject;
import com.pairoo.domain.enums.LandingPageActionType;

/**
 * @author Ralf Eichinger
 */
public class LandingPageAction extends AbstractPersistentDomainObject {
    private static final long serialVersionUID = 1L;

    private String token;
    private LandingPageActionType actionType;
    private UserAccount userAccount;

    @SuppressWarnings("unused")
    private LandingPageAction() {
    }

    /**
     * @param token
     *            unique token for the action (will be generated if null)
     * @param userAccount
     *            user account who uses the action (may be null)
     * @param landingPageActionType
     *            target action to be executed on landing page
     */
    public LandingPageAction(String token, final UserAccount userAccount,
	    final LandingPageActionType landingPageActionType) {
	super();
	if (token == null) {
	    token = generateToken();
	}
	this.token = token;
	this.userAccount = userAccount;
	this.actionType = landingPageActionType;
    }

    /**
     * @param userAccount
     *            user account who uses the action (may be null)
     * @param landingPageActionType
     *            target action to be executed on landing page
     */
    public LandingPageAction(final UserAccount userAccount,
	    final LandingPageActionType landingPageActionType) {
	this(null, userAccount, landingPageActionType);
    }

    private String generateToken() {
	return UUID.randomUUID().toString();
    }

    /**
     * @return the type of the action to be executed
     */
    public LandingPageActionType getActionType() {
	return actionType;
    }

    /**
     * @param actionType
     *            the type of the action to be executed
     */
    public void setActionType(final LandingPageActionType actionType) {
	this.actionType = actionType;
    }

    /**
     * @return useraccount related to the action
     */
    public UserAccount getUserAccount() {
	return userAccount;
    }

    /**
     * @param user
     *            user of the action
     */
    public void setUserAccount(final UserAccount userAccount) {
	this.userAccount = userAccount;
    }

    /**
     * @param token
     *            the token to set
     */
    public void setToken(final String token) {
	this.token = token;
    }

    /**
     * @return the token
     */
    public String getToken() {
	return token;
    }
}
