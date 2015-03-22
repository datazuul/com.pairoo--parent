package com.pairoo.frontend.webapp.wicket.pages.authenticated;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import com.datazuul.framework.webapp.wicket.markup.html.link.MenuGroupMember;
import com.datazuul.framework.webapp.wicket.model.LoadableDetachableDomainObjectModel;
import com.pairoo.domain.SearchProfile;
import com.pairoo.domain.User;
import com.pairoo.domain.search.UserSearchResult;
import com.pairoo.frontend.webapp.wicket.dataprovider.UserSearchDataProvider;
import com.pairoo.frontend.webapp.wicket.pages.groups.MenuGroup;
import com.pairoo.frontend.webapp.wicket.panels.authenticated.UserSearchResultPanel;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;

/**
 * @author Ralf Eichinger
 */
public class SearchResultsPage extends AuthenticatedWebPage implements MenuGroupMember {

    private static final long serialVersionUID = 1L;
    private SearchProfile searchProfile;

    public SearchResultsPage(IModel<User> model, final SearchProfile searchProfile) {
        super(model);
        this.searchProfile = searchProfile;
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        logEnter(SearchResultsPage.class);
    }

    @Override
    protected void onDetach() {
        searchProfile = null;
        super.onDetach();
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        // set up data provider
        User user = (User) getDefaultModelObject();
        final SortableDataProvider<UserSearchResult, String> dataProvider = createDataProvider(user, searchProfile);

        // list
        final DataView<UserSearchResult> dataView = createListComponent("users", dataProvider);
        add(dataView);

        // found profiles label
        add(foundProfilesCounterLabel("found.profiles", dataView));

        // paging
        final PagingNavigator pagingNavigator = createPagingComponent("navigator", dataView);
        add(pagingNavigator);

        if (dataView.getItemCount() == 0) {
            info(getString("no.results"));
        }
    }

    private Label foundProfilesCounterLabel(String id, final DataView<UserSearchResult> dataView) {
        return new Label(id, new StringResourceModel("found.profiles", new Model<Long>(dataView.getItemCount())));
    }

    private PagingNavigator createPagingComponent(String id, final DataView<UserSearchResult> dataView) {
        final PagingNavigator pagingNavigator = new PagingNavigator(id, dataView) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isVisible() {
                return (getPageable().getPageCount() > 1 && dataView.getItemCount() > 0);
            }
        };
        return pagingNavigator;
    }

    private DataView<UserSearchResult> createListComponent(String id, final SortableDataProvider<UserSearchResult, String> userDataProvider) {
        final DataView<UserSearchResult> dataView = new DataView<UserSearchResult>(id, userDataProvider, 20) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final Item<UserSearchResult> item) {
                IModel<UserSearchResult> model = item.getModel();
                UserSearchResult userSearchResult = (UserSearchResult) item.getDefaultModelObject();
                UserSearchResultPanel userSearchResultPanel = new UserSearchResultPanel("resultPanel",
                        model, userSearchResult.getId()) {
                    @Override
                    protected WebPage getResponsePage() {
                        return SearchResultsPage.this;
                    }
                };
                userSearchResultPanel.setRenderBodyOnly(true);
                item.add(userSearchResultPanel);

                // details button
                final Link<Void> detailAction = detailAction("detailAction", userSearchResult);
                item.add(detailAction);
            }

            private Link<Void> detailAction(String id, final UserSearchResult userSearchResult) {
                final Link<Void> detailAction = new Link<Void>(id) {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick() {
                        final ProfileDetailsPage nextPage = new ProfileDetailsPage(
                                new LoadableDetachableDomainObjectModel<Long>(User.class, userSearchResult.getId(), userService),
                                new LoadableDetachableDomainObjectModel<Long>((User) getPage().getDefaultModelObject(), userService)) {
                            private static final long serialVersionUID = 1L;

                            @Override
                            protected void onGoBack() {
                                setResponsePage(SearchResultsPage.this);
                            }
                        };
                        setResponsePage(nextPage);
                    }
                };
                return detailAction;
            }
        };
        return dataView;
    }

    private SortableDataProvider<UserSearchResult, String> createDataProvider(final User user, final SearchProfile searchProfile) {
        final UserSearchDataProvider<UserSearchResult, String> dataProvider = new UserSearchDataProvider<>(
                userService, user, searchProfile);
        return dataProvider;
    }

    @Override
    public Object getMenuGroup() {
        return MenuGroup.SEARCH;
    }
}
