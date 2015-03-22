package com.pairoo.frontend.webapp.wicket.panels;

import java.util.Date;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.datazuul.framework.util.EnumUtils;
import com.datazuul.framework.webapp.wicket.components.form.ShinyForm;
import com.datazuul.framework.webapp.wicket.model.LoadableDetachableDomainObjectModel;
import com.pairoo.business.api.MessageService;
import com.pairoo.business.api.UserAccountService;
import com.pairoo.business.api.UserService;
import com.pairoo.domain.Message;
import com.pairoo.domain.PersonProfile;
import com.pairoo.domain.User;
import com.pairoo.domain.UserAccount;
import com.pairoo.domain.UserProfile;
import com.pairoo.domain.enums.ImageEntryType;
import com.pairoo.domain.enums.MessageType;
import com.pairoo.domain.enums.MessageViewMode;
import com.pairoo.domain.enums.PartnerType;
import com.pairoo.domain.enums.WinkType;
import com.pairoo.frontend.webapp.wicket.components.ContextImageConstants;
import com.pairoo.frontend.webapp.wicket.components.ProfileImage;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.ProfileDetailsPage;

/**
 * @author Ralf Eichinger
 */
public abstract class MessageContentPanel extends BasePanel {

    private static final long serialVersionUID = 1L;
    @SpringBean(name = MessageService.BEAN_ID)
    private MessageService messageService;
    @SpringBean(name = UserAccountService.BEAN_ID)
    private UserAccountService userAccountService;
    @SpringBean(name = UserService.BEAN_ID)
    private UserService userService;
    private final MessageViewMode viewMode;

    /**
     * @param id
     * @param model
     */
    public MessageContentPanel(final String id, final IModel<Message> model, final MessageViewMode viewMode) {
        super(id, new CompoundPropertyModel<Message>(model));
        this.viewMode = viewMode;

        setOutputMarkupId(true);

        changeState();
    }

    public abstract void changeState();

    public abstract void onAnswerMessage(AjaxRequestTarget target);

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final Message message = (Message) getDefaultModelObject();
        User user = null;
        if (MessageViewMode.INBOX.equals(viewMode)) {
            user = message.getSender();
        } else if (MessageViewMode.OUTBOX.equals(viewMode)) {
            user = message.getReceiver();
        }
        final UserProfile userProfile = user.getUserProfile();

        // link to profile details
        final Link<User> lnkProfile = createProfileDetailsLink(user);
        add(lnkProfile);

        // premium container
        lnkProfile.add(createPremiumContainer("premiumContainer", user.getUserAccount()));

        // profileImage
        lnkProfile.add(createProfileImageComponent(userProfile));

        // username
        lnkProfile.add(createUsernameComponent(user));

        // age
        lnkProfile.add(createAgeComponent(userProfile));

        // message
        add(createMessageTextComponent(message));

        final ShinyForm form = new ShinyForm("form");
        add(form);

        // buttons
        form.add(createDeleteButton(message));
        form.add(createAnswerButton());
    }

    private Component createPremiumContainer(final String id, final UserAccount userAccount) {
        final WebMarkupContainer c = new WebMarkupContainer(id);
        c.setVisible(userAccountService.isPremiumMember(userAccount));
        return c;
    }

    private AjaxButton createAnswerButton() {
        final AjaxButton answerButton = new AjaxButton("answerButton") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(final AjaxRequestTarget target, final Form<?> form) {
                onAnswerMessage(target);
            }

            @Override
            protected void onError(final AjaxRequestTarget target, final Form<?> form) {
                // FIXME ajaxbutton onerror
            }
        };
        if (MessageViewMode.OUTBOX.equals(viewMode)) {
            answerButton.setVisible(false);
        }
        return answerButton;
    }

    private Button createDeleteButton(final Message message) {
        final Button deleteButton = new Button("deleteButton") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onComponentTag(final ComponentTag tag) {
                super.onComponentTag(tag);
                tag.put("onclick", "return confirm('" + getString("confirm.delete.message") + "');");
            }

            @Override
            public void onSubmit() {
                messageService.delete(message);
                info(getString("message.deleted"));
                onDeleteMessage();
            }
        };
        // deleteButton.add(new ConfirmDeleteBehavior() {
        // private static final long serialVersionUID = 1L;
        //
        // @Override
        // protected String getJSMessage() {
        // return getString("confirm.delete.message");
        // }
        // });
        return deleteButton;
    }

    private MultiLineLabel createMessageTextComponent(final Message message) {
        final MultiLineLabel text = new MultiLineLabel("text");
        if (message.getMessageType() == MessageType.WINK) {
            final WinkType winkType = Enum.valueOf(WinkType.class, message.getText());
            text.setDefaultModel(new ResourceModel(EnumUtils.getEnumKey(winkType)));
        }
        return text;
    }

    private Label createAgeComponent(final UserProfile userProfile) {
        final Date birthdate = userProfile.getBirthdate();
        final int age = PersonProfile.getAge(birthdate);
        String ageStr = "--";
        if (age != -1) {
            ageStr = String.valueOf(age);
        }
        final Label lblAge = new Label("lblAge", ageStr);
        return lblAge;
    }

    private Label createUsernameComponent(final User user) {
        return new Label("username", user.getUserAccount().getUsername());
    }

    private Component createProfileImageComponent(final UserProfile userProfile) {
        final ProfileImage profileImage = new ProfileImage("profileImage", new Model<UserProfile>(userProfile),
                ImageEntryType.MIDDLE);
        Component image = profileImage;
        if (!profileImage.exists() || !profileImage.isVisible()) {
            if (PartnerType.FEMALE.equals(userProfile.getPartnerType())) {
                image = new ContextImage("profileImage", ContextImageConstants.GENDER_FEMALE);
            } else if (PartnerType.MALE.equals(userProfile.getPartnerType())) {
                image = new ContextImage("profileImage", ContextImageConstants.GENDER_MALE);
            } else {
                image = new ContextImage("profileImage", ContextImageConstants.GENDER_UNKNOWN);
            }
        }
        return image;
    }

    private Link<User> createProfileDetailsLink(final User user) {
        final Model<User> userModel = new Model<User>(user);

        // link to details
        final Link<User> lnkProfile = new Link<User>("lnkProfile", userModel) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                final Message message = (Message) MessageContentPanel.this.getDefaultModelObject();
                User profileUser = null;
                User viewingUser = null;
                if (MessageViewMode.INBOX.equals(viewMode)) {
                    profileUser = message.getSender();
                    viewingUser = message.getReceiver();
                } else if (MessageViewMode.OUTBOX.equals(viewMode)) {
                    profileUser = message.getReceiver();
                    viewingUser = message.getSender();
                }
                final ProfileDetailsPage nextPage = new ProfileDetailsPage(new LoadableDetachableDomainObjectModel<Long>(profileUser, userService), new LoadableDetachableDomainObjectModel<Long>(viewingUser,
                        userService)) {
                    private static final long serialVersionUID = 1L;

                    @Override
                    protected void onGoBack() {
                        setResponsePage(MessageContentPanel.this.getPage());
                    }
                };
                setResponsePage(nextPage);
            }
        };
        return lnkProfile;
    }

    public abstract void onDeleteMessage();
}
