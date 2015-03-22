package com.pairoo.frontend.webapp.wicket.panels;

import java.util.Date;

import org.apache.wicket.Component;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.EnumLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.datazuul.framework.webapp.wicket.components.form.ShinyForm;
import com.datazuul.framework.webapp.wicket.model.LoadableDetachableDomainObjectModel;
import com.pairoo.business.api.MembershipService;
import com.pairoo.business.api.UserService;
import com.pairoo.domain.Membership;
import com.pairoo.domain.User;
import com.pairoo.domain.UserAccount;
import com.pairoo.domain.enums.Role;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.ChangePasswordPage;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.DeleteProfilePage;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.MembershipPage;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.ResignMembershipPage;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.VoucherInputPage;

/**
 * @author Ralf Eichinger
 */
public class ProfileMyAccountPanel extends BasePanel {

    private static final long serialVersionUID = 1L;
    @SpringBean(name = MembershipService.BEAN_ID)
    private MembershipService membershipService;
    @SpringBean(name = UserService.BEAN_ID)
    private UserService userService;

    public ProfileMyAccountPanel(final String id, final IModel<User> model) {
        super(id, new CompoundPropertyModel<User>(model));
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        logEnter(ProfileMyAccountPanel.class);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        IModel<User> model = (IModel<User>) getDefaultModel();
        final User user = (User) getDefaultModelObject();
        final UserAccount userAccount = user.getUserAccount();

        add(usernameLabel("userAccount.username"));
        add(emailLabel("email"));

        // ---------------- membership: startDate
        // FIXME TransientObjectException (userAccount)
        Membership firstMembership = membershipService.getFirstMembership(userAccount);
        final Date startDateValue = firstMembership.getStartDate();
        add(createMembershipStartDateComponent("membership.startDate", new Model<Date>(startDateValue)));

        // ---------------- membership: type
        Membership currentMembership = membershipService.getCurrentMembership(userAccount);
        Role role = currentMembership.getProduct().getRole();
        add(currentMembershipTypeLabel("membership.product.role", role));

        // ---------------- membership: paidUntil
        final WebMarkupContainer paidUntilContainer = new WebMarkupContainer("paidUntilContainer");
        if (currentMembership.getProduct().getRole() == Role.STANDARD) {
            paidUntilContainer.setVisible(false);
        }
        add(paidUntilContainer);
        paidUntilContainer.add(membershipPaidUntilLabel("membership.paidUntil",
                new Model<Date>(userAccount.getPremiumEndDate())));

        // ---------------- userAccount: lastLogin
        final Date lastLogin = userAccount.getLastLogin();
        add(lastLoginLabel("lastLogin", Model.of(lastLogin)));

        // ---------------- form
        final ShinyForm form = createForm("form", model);
        add(form);

        // ---------------- message notification: onNewMessage
        final CheckBox onNewMessage = createOnNewMessageCheckbox("userAccount.notificationSettings.onNewMessage");
        form.add(onNewMessage);
        form.add(labelForFormComponent("onNewMessageLabel", onNewMessage));

        // ---------------- message notification: onVisit
        final CheckBox onVisit = createOnVisitCheckbox("userAccount.notificationSettings.onVisit");
        form.add(onVisit);
        form.add(labelForFormComponent("onVisitLabel", onVisit));

        // ---------------- message notification: onNewSuggestions
        final CheckBox onNewSuggestions = createOnNewSuggestionsCheckbox("userAccount.notificationSettings.onNewSuggestions");
        form.add(onNewSuggestions);
        form.add(labelForFormComponent("onNewSuggestionsLabel", onNewSuggestions));

        // ---------------- emails: weeklyStatistic
        final CheckBox weeklyStatistic = createWeeklyStatisticCheckbox("userAccount.notificationSettings.weeklyStatistic");
        form.add(weeklyStatistic);
        form.add(labelForFormComponent("weeklyStatisticLabel", weeklyStatistic));

        // ---------------- emails: weekendSuggestions
        final CheckBox weekendSuggestions = createWeekendSuggestionsCheckbox("userAccount.notificationSettings.weekendSuggestions");
        form.add(weekendSuggestions);
        form.add(labelForFormComponent("weekendSuggestionsLabel", weekendSuggestions));

        // ---------------- emails: newsletter
        final CheckBox newsletter = createNewsletterCheckbox("userAccount.notificationSettings.newsletter");
        form.add(newsletter);
        form.add(labelForFormComponent("newsletterLabel", newsletter));

        // ---------------- links
        add(linkToChangePasswordPage("lnkChangePasswordPage", model));
        add(linkToMembershipPage("lnkMembershipPage", model));
        add(linkToVoucherInputPage("lnkVoucher", model));
        add(linkToResignMembershipPage("lnkResignMembershipPage", model));
        add(linkToDeleteProfilePage("lnkDeleteProfilePage", model));
    }

    private Component linkToVoucherInputPage(final String id, final IModel<User> model) {
        return new Link<User>(id, model) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                setResponsePage(new VoucherInputPage(new LoadableDetachableDomainObjectModel<Long>(model.getObject(), userService)));
            }
        };
    }

    private Link<User> linkToDeleteProfilePage(String id, final IModel<User> model) {
        Link<User> link = new Link<User>(id, model) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                setResponsePage(new DeleteProfilePage(new LoadableDetachableDomainObjectModel<Long>(model.getObject(), userService)));
            }
        };
        link.setEnabled(false); // FIXME implement delete profile and then set
        // to true
        return link;
    }

    private Link<User> linkToResignMembershipPage(String id, final IModel<User> model) {
        Link<User> link = new Link<User>(id, model) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                setResponsePage(new ResignMembershipPage(new LoadableDetachableDomainObjectModel<Long>(model.getObject(), userService)));
            }
        };
        link.setEnabled(false); // FIXME implement resign membership and then
        // set to true
        return link;
    }

    private Link<User> linkToMembershipPage(String id, final IModel<User> model) {
        return new Link<User>(id) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                setResponsePage(new MembershipPage(new LoadableDetachableDomainObjectModel<Long>(model.getObject(), userService)));
            }
        };
    }

    private Link<User> linkToChangePasswordPage(String id, final IModel<User> model) {
        return new Link<User>(id, model) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                setResponsePage(new ChangePasswordPage(new LoadableDetachableDomainObjectModel<Long>(model.getObject(), userService)));
            }
        };
    }

    private CheckBox createNewsletterCheckbox(String id) {
        // ... field
        final CheckBox newsletter = new CheckBox(id);
        newsletter.setLabel(new ResourceModel("newsletterLabel"));
        // ... validation
        return newsletter;
    }

    private CheckBox createWeekendSuggestionsCheckbox(String id) {
        // ... field
        final CheckBox weekendSuggestions = new CheckBox(id);
        weekendSuggestions.setLabel(new ResourceModel("weekendSuggestionsLabel"));
        // ... validation
        return weekendSuggestions;
    }

    private CheckBox createWeeklyStatisticCheckbox(String id) {
        // ... field
        final CheckBox weeklyStatistic = new CheckBox(id);
        weeklyStatistic.setLabel(new ResourceModel("weeklyStatisticLabel"));
        // ... validation
        return weeklyStatistic;
    }

    private CheckBox createOnNewSuggestionsCheckbox(String id) {
        // ... field
        final CheckBox onNewSuggestions = new CheckBox(id);
        onNewSuggestions.setLabel(new ResourceModel("onNewSuggestionsLabel"));
        // ... validation
        return onNewSuggestions;
    }

    private CheckBox createOnVisitCheckbox(String id) {
        // ... field
        final CheckBox onVisit = new CheckBox(id);

        onVisit.setLabel(new ResourceModel("onVisitLabel"));
        return onVisit;
    }

    private CheckBox createOnNewMessageCheckbox(String id) {
        // ... field
        final CheckBox onNewMessage = new CheckBox(id);
        onNewMessage.setLabel(new ResourceModel("onNewMessageLabel"));
        return onNewMessage;
    }

    private ShinyForm createForm(String id, final IModel<User> model) {
        final ShinyForm form = new ShinyForm(id, model) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                userService.save(model.getObject());
                info(getString("saved"));
            }
        };
        return form;
    }

    private DateLabel lastLoginLabel(String id, IModel<Date> model) {
        final DateLabel lastLogin = DateLabel.forDatePattern(id, model, "dd-MMM-yy HH:mm");
        return lastLogin;
    }

    private DateLabel membershipPaidUntilLabel(String id, IModel<Date> model) {
        final DateLabel paidUntil = DateLabel.forDatePattern(id, model, "dd-MMM-yy HH:mm");
        return paidUntil;
    }

    private EnumLabel<Role> currentMembershipTypeLabel(String id, Role role) {
        return new EnumLabel<Role>(id, role);
    }

    private DateLabel createMembershipStartDateComponent(String id, IModel<Date> model) {
        final DateLabel startDate = DateLabel.forDatePattern(id, model, "dd-MMM-yy HH:mm");
        return startDate;
    }

    private Label emailLabel(String id) {
        final Label email = new Label(id);
        return email;
    }

    private Label usernameLabel(String id) {
        final Label pseudonym = new Label(id);
        return pseudonym;
    }
}
