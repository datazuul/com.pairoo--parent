package com.pairoo.frontend.webapp.wicket.pages.authenticated;

import com.datazuul.framework.webapp.wicket.model.LoadableDetachableDomainObjectModel;
import com.pairoo.business.api.MembershipService;
import com.pairoo.domain.Membership;
import com.pairoo.domain.User;
import com.pairoo.domain.UserAccount;
import com.pairoo.domain.enums.Role;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.EnumLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * @author Ralf Eichinger
 */
public class VoucherConfirmationPage extends AuthenticatedWebPage {

    private static final long serialVersionUID = 1L;
    @SpringBean(name = MembershipService.BEAN_ID)
    private MembershipService membershipService;
    private boolean newlyRegistered;
    private IModel<User> userModel;

    public VoucherConfirmationPage(IModel<User> model, boolean newlyRegistered) {
        super((IModel) null);

        this.newlyRegistered = newlyRegistered;

        userModel = model;
        User user = model.getObject();
        UserAccount userAccount = user.getUserAccount();
        Membership lastMembership = membershipService.getLastMembership(userAccount);
        setDefaultModel(new CompoundPropertyModel<Membership>(lastMembership));
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        logEnter(VoucherConfirmationPage.class);
    }

    @Override
    protected void onDetach() {
        userModel.detach();
        super.onDetach();
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        // page title
        add(createTitleComponent("title"));

        // confirmation text for newly registered users
        Component confirmationMailText = createConfirmationMailTextComponent("confirmationMailText");
        if (!newlyRegistered) {
            confirmationMailText.setVisible(false);
        }
        add(confirmationMailText);

        // membership's product role
        add(productRoleLabel("product.role"));

        // membership end date
        add(new Label("endDate"));

        // subscription status
        add(subscriptionStatusLabel("subscription.status"));

        add(linkToNextPage("lnkNextPage", new LoadableDetachableDomainObjectModel<Long>(userModel.getObject(),
                userService)));
    }

    private Component createConfirmationMailTextComponent(String id) {
        Label c = new Label(id);
        String resourceKey = "confirmation_mail_text";
        c.setDefaultModel(new ResourceModel(resourceKey));
        c.setEscapeModelStrings(false);
        return c;
    }

    private Component createTitleComponent(String id) {
        Label c = new Label(id);
        String resourceKey = "title.login";
        if (newlyRegistered) {
            resourceKey = "title.registration";
        }
        c.setDefaultModel(new ResourceModel(resourceKey));
        return c;
    }

    private Component subscriptionStatusLabel(String id) {
        Label c = new Label(id);
        Membership membership = (Membership) getDefaultModelObject();
        String resourceKey;
        if (membership.getProduct().isAbo()) {
            resourceKey = "activated";
        } else {
            resourceKey = "not_activated";
        }
        c.setDefaultModel(new ResourceModel(resourceKey));
        return c;
    }

    private Link<User> linkToNextPage(String id, IModel<User> model) {
        Link<User> link = new Link<User>(id, model) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                IModel<User> model = (IModel<User>) getDefaultModel();
                setResponsePage(new MyHomePage(new LoadableDetachableDomainObjectModel<Long>(model.getObject(), userService)));
            }
        };
        return link;
    }

    private Component productRoleLabel(String id) {
        EnumLabel<Role> component = new EnumLabel<Role>(id);
        return component;
    }
}
