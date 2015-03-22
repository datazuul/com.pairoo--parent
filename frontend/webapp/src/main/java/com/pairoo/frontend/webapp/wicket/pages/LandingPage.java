package com.pairoo.frontend.webapp.wicket.pages;

import com.datazuul.framework.webapp.wicket.model.LoadableDetachableDomainObjectModel;
import com.pairoo.business.api.LandingPageActionService;
import com.pairoo.business.api.MembershipService;
import com.pairoo.business.api.marketing.PromotionService;
import com.pairoo.domain.LandingPageAction;
import com.pairoo.domain.User;
import com.pairoo.domain.UserAccount;
import com.pairoo.domain.enums.LandingPageActionType;
import static com.pairoo.domain.enums.LandingPageActionType.VISITORS_PAGE;
import com.pairoo.domain.marketing.Promotion;
import com.pairoo.domain.marketing.enums.PromotionType;
import com.pairoo.frontend.webapp.wicket.WicketWebSession;
import static com.pairoo.frontend.webapp.wicket.pages.BasePage.LOGGER;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.MyHomePage;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.VisitsPage;
import com.pairoo.frontend.webapp.wicket.pages.errors.InternalErrorPage;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.NotImplementedException;
import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.protocol.http.RequestUtils;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Landing page for links sent by email or being used on external sites.
 * Determines following page by analyzing given token: Prepares required data
 * that link to following page works correctly.
 *
 * Can be called like this (defined in WicketWebApplication:
 * <ul>
 * <li>http://localhost:28080/portal/landingPage?token=Liebe2013<br/>
 * mountPage("/landingPage", LandingPage.class);</li>
 * <li>http://localhost:28080/portal/landingPage/Liebe2013<br/>
 * mountPage("/landingPage/${token}", LandingPage.class);</li>
 * </ul>
 *
 * @author Ralf Eichinger
 */
public class LandingPage extends NotAuthenticatedWebPage {

    private static final long serialVersionUID = 1L;
    public static final String TOKEN_PARAMNAME = "token";
    public static final String PARAM_PARAMNAME = "param";
    @SpringBean(name = LandingPageActionService.BEAN_ID)
    private LandingPageActionService landingPageActionService;
    @SpringBean(name = MembershipService.BEAN_ID)
    private MembershipService membershipService;
    @SpringBean(name = PromotionService.BEAN_ID)
    private PromotionService promotionService;
    private String param;
    private String token;

    public LandingPage(final PageParameters params) {
        super(params);

        if (params.get(TOKEN_PARAMNAME) != null) {
            token = params.get(TOKEN_PARAMNAME).toString();
        }
        if (params.get(PARAM_PARAMNAME) != null) {
            // not used, yet. But maybe needed for parametrized landing page actions
            param = params.get(PARAM_PARAMNAME).toString();
        }
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        logEnter(LandingPage.class);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        // FIXME? still NullPointerException for token
        // 5350c948-1f84-4e94-b6d7-10b8b41d69c2 ?
        if (token != null) {
            LOGGER.info("LandingPage called with token = {}", token);
            final LandingPageAction lpa = landingPageActionService.getByToken(token);
            if (lpa == null) {
                LOGGER.info("no LandingPageAction found for token = {}", token);
                error(getString("error.invalid_input"));
                Link<Void> nextPageLink = new BookmarkablePageLink("lnkNextPage", InternalErrorPage.class);
                nextPageLink.setVisible(false);
                add(nextPageLink);
            } else {
                LandingPageActionType actionType = lpa.getActionType();
                final UserAccount userAccount = lpa.getUserAccount();

                // map action to following page's class
                switch (actionType) {
                    case ACTIVATE_ACCOUNT:
                        membershipService.activateMembership(userAccount);
                        User user = login(userAccount);
                        getSession().info(getString("activate_account_message"));
                        throw new RestartResponseException(new MyHomePage(new LoadableDetachableDomainObjectModel<Long>(
                                user, userService)));
                    case PROMOTION:
                        // useraccount in LandingPageAction not needed.
                        // if it is a user specific promotion: take useraccount from
                        // promotion table
                        Promotion promotion = promotionService.getPromotionByCode(token);
                        PromotionType promotionType = promotion.getPromotionType();
                        switch (promotionType) {
                            case VOUCHER:
                                throw new RestartResponseException(new VoucherLoginPage(new LoadableDetachableDomainObjectModel<Long>(
                                        promotion, promotionService)));
                            case SPECIAL_OFFER:
                                // throw new RestartResponseException(new
                                // SpecialOfferLoginPage(new
                                // Model<Promotion>(promotion)));
                                break;
                            default:
                                break;
                        }
                    case VISITORS_PAGE:
                        user = login(userAccount);
                        getSession().info(getString("new_visits_message"));
                        throw new RestartResponseException(new VisitsPage(new LoadableDetachableDomainObjectModel<Long>(
                                user, userService)));
                    default:
                        break;
                }
                throw new NotImplementedException("page for action type not implemented: " + actionType.name());
            }
        } else {
            error(getString("error.invalid_input"));
            Link<Void> nextPageLink = new BookmarkablePageLink("lnkNextPage", InternalErrorPage.class);
            nextPageLink.setVisible(false);
            add(nextPageLink);
        }
    }

    private User login(final UserAccount userAccount) {
        // login with username and password does not work (encrypted!)
        // so do dedicated steps of "login"
        WicketWebSession session = (WicketWebSession) getSession();
        return session.login(userAccount);
    }

    /**
     * Creates an url for a specific landingpage action
     *
     * @param callingPage calling page that wants the created Url to be included
     * somewhere
     * @param landingPageAction action to be executed on landing page
     * @param locale preferred language of target user
     * @return url link to trigger action
     */
    public static String createLandingPageActionUrl(final Page callingPage, final LandingPageAction landingPageAction, final Locale locale) {
        final PageParameters params = new PageParameters();
        final String token = landingPageAction.getToken();
        params.add(LandingPage.TOKEN_PARAMNAME, token);
        final String urlFor = callingPage.urlFor(LandingPage.class, params).toString();
        final HttpServletRequest req = (HttpServletRequest) ((WebRequest) RequestCycle.get().getRequest())
                .getContainerRequest();
        String actionUrl = RequestUtils.toAbsolutePath(req.getRequestURL().toString(), urlFor);
        if (locale != null) {
            actionUrl = actionUrl + "?pl=" + locale.getLanguage();
        }
        return actionUrl;
    }
}
