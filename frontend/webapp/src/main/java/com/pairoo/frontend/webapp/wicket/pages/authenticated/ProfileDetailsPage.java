package com.pairoo.frontend.webapp.wicket.pages.authenticated;

//import com.visural.wicket.component.fancybox.Fancybox;
//import com.visural.wicket.component.fancybox.FancyboxGroup;
//import com.visural.wicket.component.fancybox.TitlePosition;
//import com.visural.wicket.util.images.ImageReferenceFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.Localizer;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datazuul.framework.domain.geo.Country;
import com.datazuul.framework.webapp.wicket.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import com.datazuul.framework.webapp.wicket.model.LoadableDetachableDomainObjectModel;
import com.pairoo.business.api.FavoriteService;
import com.pairoo.business.api.LandingPageActionService;
import com.pairoo.business.api.MembershipService;
import com.pairoo.business.api.UserAccountService;
import com.pairoo.business.api.VisitService;
import com.pairoo.business.exceptions.AlreadyExistsException;
import com.pairoo.domain.ImageEntry;
import com.pairoo.domain.LandingPageAction;
import com.pairoo.domain.Membership;
import com.pairoo.domain.Message;
import com.pairoo.domain.PersonProfile;
import com.pairoo.domain.User;
import com.pairoo.domain.UserAccount;
import com.pairoo.domain.UserProfile;
import com.pairoo.domain.enums.ImageEntryType;
import com.pairoo.domain.enums.LandingPageActionType;
import com.pairoo.domain.enums.MessageType;
import com.pairoo.domain.enums.PartnerType;
import com.pairoo.domain.enums.Role;
import com.pairoo.frontend.webapp.wicket.components.ContextImageConstants;
import com.pairoo.frontend.webapp.wicket.components.OnlineLabel;
import com.pairoo.frontend.webapp.wicket.components.ProfileImage;
import com.pairoo.frontend.webapp.wicket.pages.LandingPage;
import com.pairoo.frontend.webapp.wicket.panels.authenticated.BlockProfilePanel;
import com.pairoo.frontend.webapp.wicket.panels.authenticated.ReportProfilePanel;
import com.pairoo.frontend.webapp.wicket.panels.authenticated.SendMessagePanel;
import com.pairoo.frontend.webapp.wicket.panels.authenticated.SendWinkPanel;
import com.pairoo.frontend.webapp.wicket.panels.authenticated.WantedProfileAboutMePanel;
import com.pairoo.frontend.webapp.wicket.panels.authenticated.WantedProfileAboutMyLifePanel;
import com.pairoo.frontend.webapp.wicket.panels.authenticated.WantedProfileLookingForPanel;
import com.pairoo.frontend.webapp.wicket.panels.authenticated.WantedProfileWhoFitsToMePanel;

/**
 * @author Ralf Eichinger
 */
public class ProfileDetailsPage extends AuthenticatedWebPage {

    private static final long serialVersionUID = 1L;
    static final Logger LOGGER = LoggerFactory.getLogger(ProfileDetailsPage.class);
    private Panel contentPanel;
    // transient private Fancybox fancyboxLinkProfile = null;
    private Panel previousContentPanel;
    @SpringBean(name = FavoriteService.BEAN_ID)
    private FavoriteService favoriteService;
//    @SpringBean(name = ImageEntryService.BEAN_ID)
//    private ImageEntryService imageEntryService;
    @SpringBean(name = LandingPageActionService.BEAN_ID)
    private LandingPageActionService landingPageActionService;
    @SpringBean(name = MembershipService.BEAN_ID)
    private MembershipService membershipService;
    @SpringBean(name = UserAccountService.BEAN_ID)
    private UserAccountService userAccountService;
    @SpringBean(name = VisitService.BEAN_ID)
    private VisitService visitService;
    public static final String PROFILE_USER_ID = "profileUserId";
    public static final String VISITOR_USER_ID = "visitorUserId";
    boolean showBackLink = true;

    public ProfileDetailsPage(PageParameters params) {
	super(params);
	StringValue visitorUserId = params.get(VISITOR_USER_ID);
	Long vId = visitorUserId.toLong();
	StringValue profileUserId = params.get(PROFILE_USER_ID);
	Long pId = profileUserId.toLong();
	LoadableDetachableDomainObjectModel<Long> visitorModel = new LoadableDetachableDomainObjectModel<>(User.class,
		vId, userService);
	LoadableDetachableDomainObjectModel<Long> profileModel = new LoadableDetachableDomainObjectModel<>(User.class,
		pId, userService);

	setDefaultModel(new CompoundPropertyModel<>(profileModel));
	this.userModel = visitorModel;

	showBackLink = false;
    }

    public ProfileDetailsPage(IModel<User> profileUserModel, IModel<User> userModel) {
	super(new CompoundPropertyModel<>(profileUserModel), userModel);
    }

    @Override
    protected void onBeforeRender() {
	super.onBeforeRender();
	logEnter(ProfileDetailsPage.class);
    }

    @Override
    protected void onInitialize() {
	super.onInitialize();

	final User visitor = userModel.getObject();
	final User profileUser = (User) getDefaultModelObject();
	final UserProfile userProfile = profileUser.getUserProfile();
	final UserAccount userAccount = profileUser.getUserAccount();

	// authorization
	final UserAccount visitorUserAccount = visitor.getUserAccount();
	final Membership currentMembership = membershipService.getCurrentMembership(visitorUserAccount);
	final Role visitorRole = currentMembership.getProduct().getRole();
	if (Role.STANDARD.equals(visitorRole)) {
	    final UserProfile visitorProfile = visitor.getUserProfile();
	    final Country profileCountry = userProfile.getGeoLocation().getCountry();
	    final Country visitorCountry = visitorProfile.getGeoLocation().getCountry();
	    final boolean sameCountry = visitorCountry.equals(profileCountry);
	    // detail page of members in other countries only for premium
	    if (!sameCountry) {
		LOGGER.info("Standard-user '{}' visiting '{}', countries {} and {} do not match!", new Object[] {
			visitor.getUserAccount().getUsername(), userAccount.getUsername(), visitorCountry.name(),
			profileCountry.name() });
		getSession().error(getString("error.restricted_to_premium_members", null));
		throw new RestartResponseException(new MembershipPage(new LoadableDetachableDomainObjectModel<Long>(
			visitor, userService)));
	    }
	}

	LOGGER.info("Visitor '{}' visiting '{}'",
		new Object[] { visitor.getUserAccount().getUsername(), userAccount.getUsername() });

	final boolean storeVisitAndInformVisitedUser = informVisitedUser(visitor, userAccount);
	if (storeVisitAndInformVisitedUser) {
	    // add the visit
	    // generate action link with token being sent in email
	    final LandingPageAction landingPageAction = new LandingPageAction(profileUser.getUserAccount(),
		    LandingPageActionType.VISITORS_PAGE);
	    Locale locale = getLocale();
	    if (userAccount.getPreferredLanguage() != null) {
		locale = userAccount.getPreferredLanguage().getLocale();
	    }
            final String actionLink = LandingPage.createLandingPageActionUrl(getPage(), landingPageAction, locale);

	    visitService.add(profileUser, visitor, actionLink, locale);

	    try {
		// save landing page action
		// FIXME: duplicate key value violates unique constraint
		// "landingpageaction_pkey"
		// [2012-08-26 17:20:19,135] ERROR [ajp-bio-28009-exec-7][]
		// org.hibernate.util.JDBCExceptionReporter.logExceptions -
		// ERROR:
		// duplicate key value violates unique constraint
		// "landingpageaction_pkey"
		// Detail: Key (id)=(57) already exists.
		// [2012-08-26 17:20:19,190] ERROR [ajp-bio-28009-exec-7][]
		// com.pairoo.frontend.webapp.wicket.pages.authenticated.ProfileDetailsPage.<init>
		// - error saving landingpageaction
		// org.springframework.dao.DataIntegrityViolationException:
		// Could
		// not execute JDBC batch update; SQL [insert into
		// LANDINGPAGEACTION
		// (TOKEN, ACTIONTYPE, USERACCOUNT_ID, ID) values (?, ?, ?, ?)];
		// constraint [null]; nested exception is
		// org.hibernate.exception.ConstraintViolationException: Could
		// not
		// execute JDBC batch update
		// at
		// org.springframework.orm.hibernate3.SessionFactoryUtils.convertHibernateAccessException(SessionFactoryUtils.java:641)
		//
		// BatchUpdateException: Batch entry 0 insert into
		// LANDINGPAGEACTION (TOKEN, ACTIONTYPE, USERACCOUNT_ID, ID)
		// values ('b5d75b4f-4e5d-4b6a-a337-70949fb4d63d',
		// 'VISITORS_PAGE', '6', '48') was aborted.
		landingPageActionService.save(landingPageAction);
	    } catch (final Exception e) {
		LOGGER.error("error saving landingpageaction", e);
		try {
		    LOGGER.info("landingpageaction: id {}, token {}, useraccount {}, actiontype {}", new Object[] {
			    landingPageAction.getId(), landingPageAction.getToken(),
			    landingPageAction.getUserAccount().getId(), landingPageAction.getActionType().name() });
		} catch (final Exception ex) {
		}
	    }
	}

	// link to back
	add(backLink("lnkBack"));

	// UserAccount
	// ===========
	// username
	add(new Label("userAccount.username"));

	// premium container
	add(premiumContainer("premiumContainer"));

	// UserProfile
	// ===========
	// images
	final List<ImageEntry> imageEntries = userProfile.getImageEntries();

	// image gallery
	WebMarkupContainer gallery = new WebMarkupContainer("gallery");
	add(gallery);

	final RepeatingView repeating = new RepeatingView("images");
	gallery.add(repeating);

	int imageCount = 0;
	for (final ImageEntry imageEntry : imageEntries) {
	    if (imageEntry != null && imageEntry.isVisible()) {
		LOGGER.debug("creating link for imageentry id [{}] for user profile [{}]",
			new Object[] { imageEntry.getId(), userProfile.getId() });

		ProfileImage profileImage = new ProfileImage("profileImage", imageEntry, ImageEntryType.MIDDLE);

		final Item<ImageEntry> item = new Item<>(repeating.newChildId(), imageCount);
		item.add(new AttributeModifier("data-thumb", profileImage.getImageUrl(ImageEntryType.MIDDLE)));
		repeating.add(item);

		WebComponent image = new WebComponent("image");
		image.add(new AttributeModifier("src", profileImage.getImageUrl(ImageEntryType.NORMAL)));
		item.add(image);

		imageCount++;
	    }
	}

	// no profile image
	final PartnerType partnerType = userProfile.getPartnerType();
	final Component noProfileImage = noProfileImage("noProfileImage", partnerType);
	add(noProfileImage);

	if (imageCount > 0) {
	    noProfileImage.setVisibilityAllowed(false);
	} else {
	    gallery.setVisibilityAllowed(false);
	}

	// online label
	add(onlineLabel("onlineLabel", userAccount));

	// age
	add(ageLabel("lblAge", userProfile));

        boolean visitingOwnProfile = isVisitorsOwnProfile(userAccount, visitor);
	// write me
	add(linkToSendMessage("showSendMessage", visitingOwnProfile));

	// twinkle
	add(linkToSendTwinkle("lnkTwinkle", visitingOwnProfile));

	// add to favorites
	add(addToFavoritesLink("lnkAddFavorite", visitingOwnProfile));

	// report profile (misuse)
	final ModalWindow reportProfileDialog = createReportProfileDialog("reportProfileDialog",
		visitingOwnProfile);
	add(reportProfileDialog);
	add(linkToReportProfileDialog("lnkReportProfile", reportProfileDialog, visitingOwnProfile));

	// block profile
	final ModalWindow blockProfileDialog = createBlockProfileDialog("blockProfileDialog",
		visitingOwnProfile);
	add(blockProfileDialog);
	add(linkToBlockProfileDialog("lnkBlockProfile", blockProfileDialog, visitingOwnProfile));

	// profile tabs
	// ============
	// create a list of ITab objects used to feed the tabbed panel
	final IModel<User> profileUserModel = (IModel<User>) getDefaultModel();

	final List<ITab> tabs = new ArrayList<>();
	tabs.add(new AbstractTab(new ResourceModel("who.am.i")) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public Panel getPanel(final String id) {
		return new WantedProfileAboutMePanel(id, profileUserModel);
	    }
	});

	tabs.add(new AbstractTab(new ResourceModel("the.way.i.live")) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public Panel getPanel(final String id) {
		return new WantedProfileAboutMyLifePanel(id, profileUserModel);
	    }
	});

	tabs.add(new AbstractTab(new ResourceModel("i.am.looking.for")) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public Panel getPanel(final String id) {
		return new WantedProfileLookingForPanel(id, profileUserModel);
	    }
	});

	tabs.add(new AbstractTab(new ResourceModel("your.lifestyle")) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public Panel getPanel(final String id) {
		return new WantedProfileWhoFitsToMePanel(id, profileUserModel);
	    }
	});

	// add the new tabbed panel, attribute modifier only used to switch
	// between different css variations
	contentPanel = new TabbedPanel("tabs", tabs);
	// contentPanel.setSelectedTab(0);
	add(contentPanel);

	previousContentPanel = contentPanel;
    }

    private boolean informVisitedUser(final User visitor, final UserAccount userAccount) {
	boolean visitorIsAdmin = isVisitorAdmin(visitor);
	boolean visitingOwnProfile = isVisitorsOwnProfile(userAccount, visitor);
	return !visitorIsAdmin && !visitingOwnProfile;
    }

    private static boolean isVisitorsOwnProfile(final UserAccount userAccount, final User visitor) {
        return userAccount.getUsername().equals(visitor.getUserAccount().getUsername());
    }

    private static boolean isVisitorAdmin(final User visitor) {
        return visitor.getUserAccount().getRoles().hasRole(Role.ADMIN.getCode());
    }

    private Component premiumContainer(final String id) {
	final WebMarkupContainer c = new WebMarkupContainer(id);
	c.setVisible(userAccountService.isPremiumMember(((User) getDefaultModelObject()).getUserAccount()));
	return c;
    }

    private ModalWindow createReportProfileDialog(String id, final boolean visitingOwnProfile) {
	final ModalWindow modalDialog = new ModalWindow(id);
	modalDialog.setAutoSize(false);
	modalDialog.setResizable(false);
	modalDialog.setInitialWidth(500);
	modalDialog.setUseInitialHeight(false);
	modalDialog.setWidthUnit("px");
	modalDialog.setHeightUnit("px");

	modalDialog.setContent(new ReportProfilePanel(modalDialog.getContentId(), (IModel<User>) getPage()
		.getDefaultModel(), userModel));
	modalDialog.setTitle(new ResourceModel("reportProfileDialog.title"));
	// modalDialog.setCookieName("reportProfileDialog");

	modalDialog.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public boolean onCloseButtonClicked(final AjaxRequestTarget target) {
		// setResult("Modal window 2 - close button");
		return true;
	    }
	});

	modalDialog.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public void onClose(final AjaxRequestTarget target) {
		target.add(getFeedbackPanel());
	    }
	});
	if (visitingOwnProfile) {
	    modalDialog.setVisible(false);
	}
	return modalDialog;
    }

    private AjaxLink<Void> linkToBlockProfileDialog(String id, final ModalWindow modalWindow,
	    final boolean visitingOwnProfile) {
	final AjaxLink<Void> lnkBlockProfile = new AjaxLink<Void>(id) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public void onClick(final AjaxRequestTarget target) {
		modalWindow.show(target);
	    }
	};
	if (visitingOwnProfile) {
	    lnkBlockProfile.setVisible(false);
	}
	return lnkBlockProfile;
    }

    private AjaxLink<Void> linkToReportProfileDialog(String id, final ModalWindow modalWindow,
	    final boolean visitingOwnProfile) {
	final AjaxLink<Void> lnkReportProfile = new AjaxLink<Void>(id) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public void onClick(final AjaxRequestTarget target) {
		modalWindow.show(target);
	    }
	};

	if (visitingOwnProfile) {
	    lnkReportProfile.setVisible(false);
	}
	return lnkReportProfile;
    }

    private Link<Void> addToFavoritesLink(String id, final boolean visitingOwnProfile) {
	final Link<Void> lnkFavorite = new Link<Void>(id) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public void onClick() {
		try {
		    final User visitorUser = userModel.getObject();
		    final User profileUser = (User) getPage().getDefaultModelObject();

		    favoriteService.add(visitorUser, profileUser);
		    info(getString("added_profile_to_favorites", userModel));
		} catch (final AlreadyExistsException e) {
		    warn(getString("favorite_already_exists"));
		}
	    }
	};
	if (visitingOwnProfile) {
	    lnkFavorite.setVisible(false);
	}
	return lnkFavorite;
    }

    private Link<Void> linkToSendTwinkle(String id, final boolean visitingOwnProfile) {
	final Link<Void> lnkTwinkle = new Link<Void>(id) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public void onClick() {
		User visitor = userModel.getObject();

		final User sender = visitor;
		final User receiver = (User) getPage().getDefaultModelObject();

		final Message message = new Message();
		message.setSender(sender);
		message.setReceiver(receiver);
		message.setMessageType(MessageType.WINK);
		final SendWinkPanel sendWinkPanel = new SendWinkPanel("tabs", new Model<>(message)) {
		    private static final long serialVersionUID = 1L;

		    @Override
		    protected void onGoBack() {
			ProfileDetailsPage.this.contentPanel = ProfileDetailsPage.this.previousContentPanel;
			replaceWith(ProfileDetailsPage.this.previousContentPanel);
		    }
		};
		ProfileDetailsPage.this.contentPanel.replaceWith(sendWinkPanel);
		ProfileDetailsPage.this.contentPanel = sendWinkPanel;
	    }
	};
	if (visitingOwnProfile) {
	    lnkTwinkle.setVisible(false);
	}
	return lnkTwinkle;
    }

    private Link<Void> linkToSendMessage(String id, final boolean visitingOwnProfile) {
	final Link<Void> lnkMessage = new Link<Void>(id) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public void onClick() {
		User visitor = userModel.getObject();
		// FIXME why does strategy not work in production?
		if (!visitor.getUserAccount().hasRole(Role.PREMIUM.getCode())) {
		    getSession().info(Localizer.get().getString("error.restricted_to_premium_members", null));
		    throw new RestartResponseException(new MembershipPage(
			    (IModel<User>) new LoadableDetachableDomainObjectModel<Long>(visitor, userService)));
		}
		final User sender = visitor;
		final User receiver = (User) getPage().getDefaultModelObject();
		final Message message = new Message();
		message.setSender(sender);
		message.setReceiver(receiver);
		message.setMessageType(MessageType.TEXT);
		final SendMessagePanel sendMessagePanel = new SendMessagePanel("tabs", new Model<>(message)) {
		    private static final long serialVersionUID = 1L;

		    @Override
		    protected void onGoBack() {
			ProfileDetailsPage.this.contentPanel = ProfileDetailsPage.this.previousContentPanel;
			replaceWith(ProfileDetailsPage.this.previousContentPanel);
		    }
		};
		// authorization
		MetaDataRoleAuthorizationStrategy.authorize(sendMessagePanel, new Action(Action.RENDER),
			Role.PREMIUM.getCode());
		ProfileDetailsPage.this.contentPanel.replaceWith(sendMessagePanel);
		ProfileDetailsPage.this.contentPanel = sendMessagePanel;
	    }
	};
	if (visitingOwnProfile) {
	    lnkMessage.setVisible(false);
	}
	return lnkMessage;
    }

    private OnlineLabel onlineLabel(String id, final UserAccount userAccount) {
	return new OnlineLabel(id, new Model<>(userAccount));
    }

    private Label ageLabel(String id, final UserProfile userProfile) {
	final Date birthdate = userProfile.getBirthdate();
	final int age = PersonProfile.getAge(birthdate);
	String ageStr = "--";
	if (age != -1) {
	    ageStr = String.valueOf(age);
	}
	final Label lblAge = new Label(id, "" + ageStr);
	return lblAge;
    }

    private Component noProfileImage(String id, final PartnerType partnerType) {
	String noProfileImagePath = ContextImageConstants.GENDER_UNKNOWN;
	if (partnerType.equals(PartnerType.FEMALE)) {
	    noProfileImagePath = ContextImageConstants.GENDER_FEMALE;
	} else if (partnerType.equals(PartnerType.MALE)) {
	    noProfileImagePath = ContextImageConstants.GENDER_MALE;
	}
	return new ContextImage(id, noProfileImagePath);
    }

    private Link<Void> backLink(String id) {
	final Link<Void> lnkBack = new Link<Void>(id) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public void onClick() {
		onGoBack();
	    }
	};
	lnkBack.setVisibilityAllowed(showBackLink);
	return lnkBack;
    }

    protected void onGoBack() {
    }

    ;

    private ModalWindow createBlockProfileDialog(String id, final boolean visitingOwnProfile) {
	final ModalWindow modalDialog = new ModalWindow(id);
	modalDialog.setAutoSize(false);
	modalDialog.setResizable(false);
	modalDialog.setInitialWidth(500);
	modalDialog.setUseInitialHeight(false);
	modalDialog.setWidthUnit("px");
	modalDialog.setHeightUnit("px");
	modalDialog.setContent(new BlockProfilePanel(modalDialog.getContentId(), (IModel<User>) getPage()
		.getDefaultModel(), userModel));
	modalDialog.setTitle(new ResourceModel("blockProfileDialog.title"));
	// modalDialog.setCookieName("blockProfileDialog");

	modalDialog.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public boolean onCloseButtonClicked(final AjaxRequestTarget target) {
		// setResult("Modal window 2 - close button");
		return true;
	    }
	});

	modalDialog.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public void onClose(final AjaxRequestTarget target) {
		target.add(getFeedbackPanel());
	    }
	});
	if (visitingOwnProfile) {
	    modalDialog.setVisible(false);
	}
	return modalDialog;
    }
}
