package com.pairoo.frontend.webapp.admin.wicket.pages;

import java.util.List;
import java.util.Locale;

import org.apache.wicket.Application;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.FormComponentLabel;
import org.apache.wicket.markup.html.form.SimpleFormComponentLabel;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datazuul.framework.domain.Language;
import com.datazuul.framework.webapp.wicket.components.locale.LanguageSwitcherPanel;
import com.datazuul.framework.webapp.wicket.visitor.BookmarkablePageLinkVisitor;
import com.pairoo.business.api.ApplicationService;
import com.pairoo.business.api.UserAccountService;
import com.pairoo.domain.User;
import com.pairoo.domain.UserAccount;
import com.pairoo.frontend.webapp.admin.wicket.WicketWebApplication;
import com.pairoo.frontend.webapp.wicket.WicketWebSession;
import com.pairoo.frontend.webapp.wicket.components.ContextImageConstants;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.MyHomePage;
import com.pairoo.frontend.webapp.wicket.panels.FooterNaviPanel;
import com.pairoo.frontend.webapp.wicket.panels.MainNaviPanel;
import com.pairoo.frontend.webapp.wicket.panels.MyNaviPanel;
import com.pairoo.frontend.webapp.wicket.panels.SupportPanel;
import org.apache.wicket.markup.head.IHeaderResponse;

/**
 * Base page class. <p> The markup of this files provides some common html as
 * well as includes a reference to the css file that all other pages inherit
 * through wicket's markup inheritance feature.
 *
 * @author Ralf Eichinger
 */
public class BasePage extends WebPage {

    private static final long serialVersionUID = 1L;
    static final Logger LOGGER = LoggerFactory.getLogger(BasePage.class);
    @SpringBean(name = ApplicationService.BEAN_ID)
    protected ApplicationService applicationService;
    @SpringBean(name = UserAccountService.BEAN_ID)
    private UserAccountService userAccountService;
    protected MyNaviPanel myNaviPanel = null;
    private FeedbackPanel feedbackPanel;
    protected Component emailSupport;

    public BasePage(final IModel<?> model) {
        super(model);
        // add(new GenericUniformBehavior("input:text, textarea"));
        addComponents();
    }

    /**
     * Add Javascript Libraries to head (DOJO, EXT_CORE, JQUERY, JQUERY_UI,
     * MOOTOOLS_CORE, MOOTOOLS_MORE, PROTOTYPE, SCRIPTACULOUS, SWFOBJECT, YUI).
     *
     * @see
     * org.apache.wicket.Component#renderHead(org.apache.wicket.markup.html.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse response) {
        super.renderHead(response);
        // final IHeaderContributor header = JSLib
        // .getHeaderContribution(VersionDescriptor.alwaysLatest(Library.PROTOTYPE));
        // header.renderHead(response);
    }

    public BasePage() {
        super((IModel<?>) null);
        addComponents();
    }

    public BasePage(final PageParameters params) {
        super(params);
        addComponents();
    }

    private void addComponents() {
        // version
        add(createVersionComponent());

        // add(new com.google.excanvas.ExCanvasHeaderContributor());

        // language switcher panel
        // add(new SwitchLanguagePanel("switchLanguagePanel",
        // Arrays.asList(Locale
        // .getAvailableLocales())));
        // WicketWebApplication.LOCALES));

        // home link
        add(createHomeLink());

        // feedback
        add(createFeedbackComponent());

        // personal navigation
        myNaviPanel = createPersonalNavigationComponent();
        add(myNaviPanel);

        // main navigation
        final MainNaviPanel mainNaviPanel = createMainNavigationComponent();
        add(mainNaviPanel);
        mainNaviPanel.visitChildren(BookmarkablePageLink.class,
                new BookmarkablePageLinkVisitor());

        // language switcher
        add(createLanguageSwitcherComponent());

        // footer navigation
        add(createFooterNavigationComponent());

        // support panel
        // TODO support panel replaced with static content until phone support
        // is there:
        Component supportPanel = createSupportPanel("supportPanel");
        supportPanel.setVisible(false);
        add(supportPanel);

        emailSupport = createEmailSupportComponent("emailSupport");
        add(emailSupport);
    }

    private Component createVersionComponent() {
        WebComponent c = new WebComponent("version");;
        c.add(new AttributeModifier("content", ((WicketWebApplication) Application.get()).getVersion()));
        return c;
    }

    private Component createEmailSupportComponent(String id) {
        IModel<String> pathModel = new Model<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String getObject() {
                String contextRelativePath = ContextImageConstants.TEXT_IMAGES_PATH
                        + "/"
                        + getLocale().getLanguage()
                        + "/"
                        + ContextImageConstants.EMAIL_SUPPORT;
                return contextRelativePath;
            }
        };
        Component c = new ContextImage(id, pathModel);
        return c;
    }

    private Component createSupportPanel(String id) {
        SupportPanel supportPanel = new SupportPanel(id);
        return supportPanel;
    }

    private FooterNaviPanel createFooterNavigationComponent() {
        return new FooterNaviPanel("footerNavi");
    }

    private LanguageSwitcherPanel createLanguageSwitcherComponent() {
        final List<Language> availableTranslationLanguages = applicationService
                .getAvailableTranslationLanguages();
        final LanguageSwitcherPanel switchLanguagePanel = new LanguageSwitcherPanel(
                "switchLanguagePanel", availableTranslationLanguages) {
            private static final long serialVersionUID = 1L;

            public void setLocale(final Locale locale) {
                final WicketWebSession session = (WicketWebSession) getSession();
                // set locale/language for current session
                session.setLocale(locale);
                // store as preferred language
                final Language language = applicationService
                        .getLanguage(locale);
                final User user = session.getUser();
                if (user != null) {
                    final UserAccount userAccount = user.getUserAccount();
                    userAccount.setPreferredLanguage(language);
                    userAccountService.save(userAccount);
                }
            }
        };
        return switchLanguagePanel;
    }

    private MainNaviPanel createMainNavigationComponent() {
        return new MainNaviPanel("mainNavi");
    }

    private MyNaviPanel createPersonalNavigationComponent() {
        return new MyNaviPanel("myNaviPanel");
    }

    private FeedbackPanel createFeedbackComponent() {
        this.feedbackPanel = new FeedbackPanel("status");
        this.feedbackPanel.setOutputMarkupId(true);
        return feedbackPanel;
    }

    private BookmarkablePageLink<Void> createHomeLink() {
        BookmarkablePageLink<Void> lnkHomePage = null;
        if (!isAuthenticated()) {
            lnkHomePage = new BookmarkablePageLink<Void>("lnkHomePage",
                    getApplication().getHomePage());
        } else {
            lnkHomePage = new BookmarkablePageLink<Void>("lnkHomePage",
                    MyHomePage.class);
        }
        return lnkHomePage;
    }

    /**
     * Hint: never use this.getClass() when calling this method. It will not
     * work.
     *
     * @param pageClass class of page
     */
    protected void logEnter(final Class<? extends Page> pageClass) {
        logEnter(pageClass.getSimpleName());

    }

    private void logEnter(String className) {
        if (className != null && !className.isEmpty()) {
            WicketWebSession session = (WicketWebSession) getSession();
            String username = "(not logged in)";
            if (session.isAuthenticated()) {
                username = session.getUser().getUserAccount().getUsername();
            }
            LOGGER.info("'{}'/'{}' ENTERING page '{}'",
                    new Object[]{session.getId(), username, className});
        } else {
            LOGGER.info("ENTERING unknown page");
        }
    }

    public FeedbackPanel getFeedbackPanel() {
        return this.feedbackPanel;
    }

    public User getUser() {
        final WicketWebSession session = (WicketWebSession) getSession();
        return session.getUser();
    }

    public boolean isAuthenticated() {
        return getUser() != null;
    }

    protected FormComponentLabel createFormComponentLabel(final String id,
            final FormComponent<?> formComponent) {
        return new SimpleFormComponentLabel(id, formComponent);
    }
}
