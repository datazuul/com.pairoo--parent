package com.pairoo.frontend.webapp.wicket.panels;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.datazuul.framework.webapp.wicket.markup.html.link.MenuGroupLink;
import com.datazuul.framework.webapp.wicket.model.LoadableDetachableDomainObjectModel;
import com.pairoo.business.api.UserService;
import com.pairoo.domain.User;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.FavoritesPage;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.MessagesPage;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.MyHomePage;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.MyProfilePage;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.SearchPage;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.SuggestionsPage;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.VisitsPage;
import com.pairoo.frontend.webapp.wicket.pages.groups.MenuGroup;

/**
 * @author Ralf Eichinger
 */
public class MainNaviAuthenticatedPanel extends BasePanel {

    @SpringBean(name = UserService.BEAN_ID)
    private UserService userService;
    // private static final Logger LOGGER =
    // LoggerFactory.getLogger(MainNaviAuthenticatedPanel.class);
    private static final long serialVersionUID = 1L;

    public MainNaviAuthenticatedPanel(final String id, IModel<User> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        User user = (User) getDefaultModelObject();
        IModel<User> model = new LoadableDetachableDomainObjectModel<Long>(user, userService);

        add(linkToMyHomePage("lnkMyHomePage", model));
        add(linkToMessagesPage("lnkMessagesPage", model));
        add(linkToSuggestionsPage("lnkSuggestionsPage", model));
        add(linkToFavoritesPage("lnkFavoritesPage", model));
        add(linkToVisitsPage("lnkVisitorsPage", model));
        add(linkToSearchPage("lnkSearchPage", model));
        add(linkToMyProfilePage("lnkMyProfilePage", model));
    }

    private Link<Void> linkToMyProfilePage(String id, IModel<User> model) {
        MenuGroupLink link = new MenuGroupLink(id, model, MenuGroup.MY_PROFILE, MyProfilePage.class) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                IModel<User> model = (IModel<User>) getDefaultModel();
                setResponsePage(new MyProfilePage(new LoadableDetachableDomainObjectModel<Long>(model.getObject(), userService)));
            }
        };
        return link;
    }

    private Link<Void> linkToSearchPage(String id, IModel<User> model) {
        MenuGroupLink link = new MenuGroupLink(id, model, MenuGroup.SEARCH, SearchPage.class) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                IModel<User> model = (IModel<User>) getDefaultModel();
                setResponsePage(new SearchPage(new LoadableDetachableDomainObjectModel<Long>(model.getObject(), userService)));
            }
        };
        return link;
    }

    private Link<Void> linkToVisitsPage(String id, IModel<User> model) {
        MenuGroupLink link = new MenuGroupLink(id, model, MenuGroup.VISITS, VisitsPage.class) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                IModel<User> model = (IModel<User>) getDefaultModel();
                setResponsePage(new VisitsPage(new LoadableDetachableDomainObjectModel<Long>(model.getObject(), userService)));
            }
        };
        return link;
    }

    private Link<Void> linkToFavoritesPage(String id, IModel<User> model) {
        MenuGroupLink link = new MenuGroupLink(id, model, MenuGroup.FAVORITES, FavoritesPage.class) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                IModel<User> model = (IModel<User>) getDefaultModel();
                setResponsePage(new FavoritesPage(new LoadableDetachableDomainObjectModel<Long>(model.getObject(), userService)));
            }
        };
        return link;
    }

    private Link<Void> linkToSuggestionsPage(String id, IModel<User> model) {
        MenuGroupLink link = new MenuGroupLink(id, model, MenuGroup.SUGGESTIONS, SuggestionsPage.class) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                IModel<User> model = (IModel<User>) getDefaultModel();
                setResponsePage(new SuggestionsPage(new LoadableDetachableDomainObjectModel<Long>(model.getObject(), userService)));
            }
        };
        return link;
    }

    private Link<Void> linkToMessagesPage(String id, IModel<User> model) {
        MenuGroupLink link = new MenuGroupLink(id, model, MenuGroup.MESSAGES, MessagesPage.class) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                IModel<User> model = (IModel<User>) getDefaultModel();
                setResponsePage(new MessagesPage(new LoadableDetachableDomainObjectModel<Long>(model.getObject(), userService)));
            }
        };
        return link;
    }

    private Link<Void> linkToMyHomePage(String id, IModel<User> model) {
        MenuGroupLink link = new MenuGroupLink(id, model, MenuGroup.HOME, MyHomePage.class) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                IModel<User> model = (IModel<User>) getDefaultModel();
                setResponsePage(new MyHomePage(new LoadableDetachableDomainObjectModel<Long>(model.getObject(), userService)));
            }
        };
        return link;
    }
}
