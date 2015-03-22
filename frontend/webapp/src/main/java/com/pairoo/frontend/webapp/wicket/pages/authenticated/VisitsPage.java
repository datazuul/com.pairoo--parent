package com.pairoo.frontend.webapp.wicket.pages.authenticated;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.datazuul.framework.webapp.wicket.markup.html.link.MenuGroupMember;
import com.datazuul.framework.webapp.wicket.model.LoadableDetachableDomainObjectModel;
import com.pairoo.business.api.VisitService;
import com.pairoo.domain.User;
import com.pairoo.domain.search.UserSearchResult;
import com.pairoo.domain.search.VisitSearchResult;
import com.pairoo.frontend.webapp.wicket.dataprovider.VisitsDataProvider;
import com.pairoo.frontend.webapp.wicket.pages.groups.MenuGroup;
import com.pairoo.frontend.webapp.wicket.panels.authenticated.VisitSearchResultPanel;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;

/**
 * @author Ralf Eichinger
 */
public class VisitsPage extends AuthenticatedWebPage implements MenuGroupMember {

    private static final long serialVersionUID = 1L;
    @SpringBean(name = VisitService.BEAN_ID)
    private VisitService visitService;

    public VisitsPage(IModel<User> model) {
        super(model);
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        logEnter(VisitsPage.class);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        // data provider
        User user = (User) getDefaultModelObject();
        final SortableDataProvider<VisitSearchResult, String> dataProvider = createDataProvider(user);

        // list
        final DataView<VisitSearchResult> dataView = createListComponent("users", dataProvider);
        add(dataView);

        // paging
        add(pagingComponent("navigator", dataView));

        if (dataView.getItemCount() == 0) {
            info(getString("no.results"));
        }
    }

    private SortableDataProvider<VisitSearchResult, String> createDataProvider(final User user) {
        final VisitsDataProvider<VisitSearchResult, String> dataProvider = new VisitsDataProvider<>(
                visitService, user);
        return dataProvider;
    }

    private DataView<VisitSearchResult> createListComponent(String id, final SortableDataProvider<VisitSearchResult, String> dataProvider) {
        final DataView<VisitSearchResult> dataView = new DataView<VisitSearchResult>(id, dataProvider, 20) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final Item<VisitSearchResult> item) {
                final VisitSearchResult searchResult = (VisitSearchResult) item.getDefaultModelObject();

                // details link
                final Link<Void> detailAction = detailAction("detailAction", searchResult);
                item.add(detailAction);

                VisitSearchResultPanel searchResultPanel = new VisitSearchResultPanel("resultPanel", Model.of((UserSearchResult) searchResult));
                searchResultPanel.setRenderBodyOnly(true);
                detailAction.add(searchResultPanel);
            }

            private Link<Void> detailAction(String id, final VisitSearchResult searchResult) {
                final Link<Void> detailAction = new Link<Void>(id) {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick() {
                        final ProfileDetailsPage nextPage = new ProfileDetailsPage(
                                new LoadableDetachableDomainObjectModel<Long>(User.class, searchResult.getVisitorId(), userService),
                                new LoadableDetachableDomainObjectModel<Long>((User) getPage().getDefaultModelObject(), userService)) {
                            private static final long serialVersionUID = 1L;

                            @Override
                            protected void onGoBack() {
                                setResponsePage(VisitsPage.this);
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

    private PagingNavigator pagingComponent(String id, final DataView<VisitSearchResult> dataView) {
        final PagingNavigator pagingNavigator = new PagingNavigator(id, dataView) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isVisible() {
                return (getPageable().getPageCount() > 1 && dataView.getItemCount() > 0);
            }
        };
        return pagingNavigator;
    }

    @Override
    public Object getMenuGroup() {
        return MenuGroup.VISITS;
    }
}
