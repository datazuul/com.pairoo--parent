package com.pairoo.frontend.webapp.wicket.authorization;

import org.apache.wicket.Session;

import com.datazuul.framework.domain.authorization.Roles;
import com.datazuul.framework.webapp.wicket.authorization.strategies.role.IRoleCheckingStrategy;
import com.pairoo.domain.User;
import com.pairoo.domain.UserAccount;
import com.pairoo.frontend.webapp.wicket.WicketWebSession;

public class UserRolesAuthorizer implements IRoleCheckingStrategy {
    @Override
    public boolean hasAnyRole(Roles roles) {
	boolean result = false;
	WicketWebSession authSession = (WicketWebSession) Session.get();
	User user = authSession.getUser();
	if (user != null && user.getUserAccount() != null) {
	    UserAccount userAccount = user.getUserAccount();
	    Roles rolesUserAccount = userAccount.getRoles();
	    if (rolesUserAccount != null) {
		result = rolesUserAccount.hasAnyRole(roles);
	    }
	}
	return result;
    }
}
