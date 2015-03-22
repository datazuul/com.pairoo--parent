package com.pairoo.frontend.webapp.wicket.pages.authenticated;

import com.datazuul.framework.webapp.wicket.markup.html.link.MenuGroupMember;
import com.datazuul.framework.webapp.wicket.model.LoadableDetachableDomainObjectModel;
import com.pairoo.business.api.FavoriteService;
import com.pairoo.business.api.MessageService;
import com.pairoo.business.api.UserAccountService;
import com.pairoo.business.api.VisitService;
import com.pairoo.domain.ImageEntry;
import com.pairoo.domain.User;
import com.pairoo.domain.UserAccount;
import com.pairoo.domain.UserProfile;
import com.pairoo.domain.Visit;
import com.pairoo.domain.enums.ImageEntryType;
import com.pairoo.domain.enums.PartnerType;
import com.pairoo.frontend.webapp.wicket.components.OnlineLabel;
import com.pairoo.frontend.webapp.wicket.components.ProfileImage;
import com.pairoo.frontend.webapp.wicket.pages.groups.MenuGroup;
import com.pairoo.frontend.webapp.wicket.panels.BasicSearchPanel;
import java.util.List;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * @author Ralf Eichinger
 */
public class MyHomePage extends AuthenticatedWebPage implements MenuGroupMember {

    private static final long serialVersionUID = 1L;
    @SpringBean(name = FavoriteService.BEAN_ID)
    private FavoriteService favoriteService;
    @SpringBean(name = MessageService.BEAN_ID)
    private MessageService messageService;
    @SpringBean(name = UserAccountService.BEAN_ID)
    private UserAccountService userAccountService;
    @SpringBean(name = VisitService.BEAN_ID)
    private VisitService visitService;

    public MyHomePage(IModel<User> model) {
        super(model);
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        logEnter(MyHomePage.class);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        IModel<User> model = (IModel<User>) getPage().getDefaultModel();
        final User user = (User) getPage().getDefaultModelObject();

	if (user.getUserProfile().getBirthdate() == null) {
	    info(getString("info.missing_birthdate"));
	}
	
        // search box
        add(basicSearchPanel("searchPanel"));

        // welcome text
        add(welcomeText("welcome.text"));

        // members online counter
        add(membersOnlineCounter("members.online"));

        // unread messages
        final Link<User> lnkMessagesPage = linkToMessagesPage("lnkMessagesPage", model);
        add(lnkMessagesPage);
        lnkMessagesPage.add(unreadMessagesLabel("new.messages"));

        // favorites online
        final Link<User> lnkFavoritesPage = linkToFavoritesPage("lnkFavoritesPage", model);
        add(lnkFavoritesPage);
        lnkFavoritesPage.add(favoritesOnlineCounter("favorites.online"));

        // new visits since previous login
        final List<Visit> visitsSinceLastLogin = visitService.getVisitsSinceLastLogin(user);
        final int visitCount = visitsSinceLastLogin.size();

        // link to page
        final Link<User> lnkVisitsPage = linkToVisitsPage("lnkVisitsPage", model);
        add(lnkVisitsPage);
        lnkVisitsPage.add(newVisitsCounterLabel("new.visits", visitCount));

        // image gallery of thumbnails
        final RepeatingView repeating = createThumbnailsComponent("images", visitsSinceLastLogin);
        add(repeating);

        // more link
        final Link<User> lnkMore = linkMore("lnkMore", model, visitCount);
        add(lnkMore);
        lnkMore.add(new Label("lblMore", new StringResourceModel("more", null)));
    }

    private RepeatingView createThumbnailsComponent(String id, final List<Visit> visits) {
        final RepeatingView repeating = new RepeatingView(id) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isVisible() {
                return (visits.size() > 0);
            }
        };
        final int maxVisitCount = visitService.getMaxShownThumbnails();
        final int visitsSinceLastLogin = visits.size();
        if (visitsSinceLastLogin > 0) {
            int visitCount = visitsSinceLastLogin;
            if (visitsSinceLastLogin > maxVisitCount) {
                visitCount = maxVisitCount;
            }
            for (int i = 0; i < visitCount; i++) {
                final Item<ImageEntry> item = new Item<ImageEntry>(repeating.newChildId(), i);

                final Visit visit = visits.get(i);
                final User visitor = visit.getVisitor();
                final UserProfile userProfile = visitor.getUserProfile();

                final Link<Void> lnkDetails = new Link<Void>("lnkDetails") {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick() {
                        setResponsePage(new ProfileDetailsPage(new LoadableDetachableDomainObjectModel<Long>(visitor, userService),
                                new LoadableDetachableDomainObjectModel<Long>((User) getPage().getDefaultModelObject(), userService)) {
                            private static final long serialVersionUID = 1L;

                            @Override
                            protected void onGoBack() {
                                setResponsePage(MyHomePage.this);
                            }
                        });

                    }
                };
                item.add(lnkDetails);

                // profileImage
                final ProfileImage profileImage = new ProfileImage("profileImage", new Model<UserProfile>(userProfile),
                        ImageEntryType.MIDDLE);
                if (!profileImage.exists() || !profileImage.isVisible()) {
                    if (PartnerType.FEMALE.equals(userProfile.getPartnerType())) {
                        lnkDetails.add(new ContextImage("profileImage", "images/female-gender-sign.jpg"));
                    } else if (PartnerType.MALE.equals(userProfile.getPartnerType())) {
                        lnkDetails.add(new ContextImage("profileImage", "images/male-gender-sign.jpg"));
                    } else {
                        lnkDetails.add(new ContextImage("profileImage", "images/unknown-gender-sign.jpg"));
                    }
                } else {
                    lnkDetails.add(profileImage);
                }
                lnkDetails.get(0).add(new AttributeModifier("width", "50"));
                repeating.add(item);

                // premium container
                lnkDetails.add(createPremiumContainer("premiumContainer", visitor.getUserAccount()));

                // online label
                lnkDetails.add(new OnlineLabel("onlineLabel", new Model<UserAccount>(visitor.getUserAccount())));
            }
        }
        return repeating;
    }

    private Component createPremiumContainer(final String id, final UserAccount userAccount) {
        final WebMarkupContainer c = new WebMarkupContainer(id);
        c.setVisible(userAccountService.isPremiumMember(userAccount));
        return c;
    }

    private Label newVisitsCounterLabel(String id, final int visitCount) {
        return new Label(id, new StringResourceModel("new.visits", new Model<Integer>(visitCount)));
    }

    private Link<User> linkToVisitsPage(String id, IModel<User> model) {
        Link<User> link = new Link<User>(id, model) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                IModel<User> model = (IModel<User>) getDefaultModel();
                setResponsePage(new VisitsPage(new LoadableDetachableDomainObjectModel<Long>(model.getObject(), userService)));
            }
        };
        return link;
    }

    private Link<User> linkMore(String id, IModel<User> model, final int visitCount) {
        Link<User> link = new Link<User>(id, model) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                IModel<User> model = (IModel<User>) getDefaultModel();
                setResponsePage(new VisitsPage(new LoadableDetachableDomainObjectModel<Long>(model.getObject(), userService)));
            }

            @Override
            public boolean isVisible() {
                return visitCount > 0;
            }
        };
        return link;
    }

    private Label favoritesOnlineCounter(String id) {
        User user = (User) getPage().getDefaultModelObject();
        final int favoritesCount = favoriteService.countFavoritesOnline(user);
        return new Label(id, new StringResourceModel(id, new Model<Integer>(favoritesCount)));
    }

    private Link<User> linkToFavoritesPage(String id, IModel<User> model) {
        Link<User> link = new Link<User>(id, model) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                IModel<User> model = (IModel<User>) getDefaultModel();
                setResponsePage(new FavoritesPage(new LoadableDetachableDomainObjectModel<Long>(model.getObject(), userService)));
            }
        };
        return link;
    }

    private Label unreadMessagesLabel(String id) {
        User user = (User) getPage().getDefaultModelObject();
        final long msgCount = messageService.countUnreadInboxMessages(user);
        return new Label(id, new StringResourceModel(id, new Model<Long>(msgCount)));
    }

    private Link<User> linkToMessagesPage(String id, IModel<User> model) {
        Link<User> link = new Link<User>(id, model) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                IModel<User> model = (IModel<User>) getPage().getDefaultModel();
                setResponsePage(new MessagesPage(new LoadableDetachableDomainObjectModel<Long>(model.getObject(), userService)));
            }
        };
        return link;
    }

    private Label membersOnlineCounter(String id) {
        final int membersOnline = userAccountService.countUserAccountsOnline();
        return new Label(id, new StringResourceModel(id, new Model<Integer>(membersOnline)));
    }

    private Component welcomeText(String id) {
        User user = (User) getDefaultModelObject();
        return new Label(id, new StringResourceModel("welcome.text", new Model<User>(user)));
    }

    private BasicSearchPanel basicSearchPanel(String id) {
        return new BasicSearchPanel(id, (IModel<User>) getPage().getDefaultModel());
    }

    @Override
    public Object getMenuGroup() {
        return MenuGroup.HOME;
    }
}
