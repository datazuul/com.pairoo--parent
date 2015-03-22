package com.pairoo.frontend.webapp.wicket.panels.authenticated;

import com.pairoo.domain.Membership;
import com.pairoo.domain.User;
import com.pairoo.frontend.webapp.wicket.panels.BasePanel;
import org.apache.wicket.model.IModel;

public class PaymentPayPalPanel extends BasePanel {

    private static final long serialVersionUID = 1L;

    public PaymentPayPalPanel(final String id, final IModel<Membership> membershipModel, IModel<User> model) {
        super(id, membershipModel, model);
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        logEnter(PaymentPayPalPanel.class);
    }
}
