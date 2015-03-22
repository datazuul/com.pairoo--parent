package com.pairoo.frontend.webapp.wicket.pages.authenticated;

import com.pairoo.domain.Membership;
import com.pairoo.domain.User;
import org.apache.wicket.model.IModel;

/**
 * @author Ralf Eichinger
 */
public class MembershipSummaryPage extends AuthenticatedWebPage {

    private static final long serialVersionUID = 1L;

    public MembershipSummaryPage(final IModel<Membership> membershipModel, IModel<User> userModel) {
        super(membershipModel, userModel);
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        logEnter(MembershipSummaryPage.class);
    }
}
