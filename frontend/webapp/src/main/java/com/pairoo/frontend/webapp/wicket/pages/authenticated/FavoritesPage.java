package com.pairoo.frontend.webapp.wicket.pages.authenticated;

import com.datazuul.framework.webapp.wicket.markup.html.link.MenuGroupMember;
import com.datazuul.framework.webapp.wicket.model.LoadableDetachableDomainObjectModel;
import com.pairoo.business.api.FavoriteService;
import com.pairoo.domain.Favorite;
import com.pairoo.domain.User;
import com.pairoo.domain.search.FavoriteSearchResult;
import com.pairoo.domain.search.UserSearchResult;
import com.pairoo.frontend.webapp.wicket.dataprovider.FavoritesDataProvider;
import com.pairoo.frontend.webapp.wicket.pages.groups.MenuGroup;
import com.pairoo.frontend.webapp.wicket.panels.authenticated.UserSearchResultPanel;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Show favorites of given user.
 *
 * @author Ralf Eichinger
 */
public class FavoritesPage extends AuthenticatedWebPage implements MenuGroupMember {

    private static final long serialVersionUID = 1L;
    @SpringBean(name = FavoriteService.BEAN_ID)
    private FavoriteService favoriteService;

    public FavoritesPage(IModel<User> model) {
        super(model);
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        logEnter(FavoritesPage.class);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        // data provider
        User user = (User) getDefaultModelObject();
        final SortableDataProvider<FavoriteSearchResult, String> dataProvider = createDataProvider(user);

        // table
        final DataView<FavoriteSearchResult> dataView = createListComponent("users", dataProvider);
        add(dataView);

        // found profiles label
        add(foundProfilesCounterLabel("found.profiles", dataView));

        // paging
        add(pagingComponent("navigator", dataView));

        if (dataView.getItemCount() == 0) {
            info(getString("no.results"));
        }
    }

    private PagingNavigator pagingComponent(String id, final DataView<FavoriteSearchResult> dataView) {
        final PagingNavigator pagingNavigator = new PagingNavigator(id, dataView) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isVisible() {
                return (getPageable().getPageCount() > 1 && dataView.getItemCount() > 0);
            }
        };
        return pagingNavigator;
    }

    private Label foundProfilesCounterLabel(String id, final DataView<FavoriteSearchResult> dataView) {
        Label countLabel = new Label(id, new StringResourceModel("found.profiles", new Model<Long>() {
            @Override
            public Long getObject() {
                return dataView.getItemCount();
            }
        }));
        countLabel.setOutputMarkupId(true);
        return countLabel;
    }

    private SortableDataProvider<FavoriteSearchResult, String> createDataProvider(final User user) {
        final FavoritesDataProvider<FavoriteSearchResult, String> dataProvider = new FavoritesDataProvider<>(
                favoriteService, user);
        return dataProvider;
    }

    private DataView<FavoriteSearchResult> createListComponent(String id, final SortableDataProvider<FavoriteSearchResult, String> userDataProvider) {
        final DataView<FavoriteSearchResult> dataView = new DataView<FavoriteSearchResult>(id, userDataProvider, 20) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final Item<FavoriteSearchResult> item) {
                final FavoriteSearchResult favoriteSearchResult = (FavoriteSearchResult) item.getDefaultModelObject();

                UserSearchResultPanel userSearchResultPanel = new UserSearchResultPanel("resultPanel",
                        Model.of((UserSearchResult) favoriteSearchResult), favoriteSearchResult.getTargetId()) {
                    @Override
                    protected WebPage getResponsePage() {
                        return FavoritesPage.this;
                    }
                };
                userSearchResultPanel.setRenderBodyOnly(true);
                item.add(userSearchResultPanel);

                // details button
                final Link<Void> detailAction = detailAction("detailAction", favoriteSearchResult);
                item.add(detailAction);

                // delete button
                Favorite favorite = new Favorite();
                favorite.setId((favoriteSearchResult).getId());
                final Link<Void> deleteAction = createDeleteLink("deleteAction", favorite);
                item.add(deleteAction);
            }

            private Link<Void> createDeleteLink(final String id, final Favorite favorite) {
                final Link<Void> deleteAction = new Link<Void>(id) {
                    private static final long serialVersionUID = 1L;

                    @Override
                    protected void onComponentTag(final ComponentTag tag) {
                        super.onComponentTag(tag);
                        String onclick = tag.getAttribute("onclick");
                        onclick = "var answer = confirm('" + getString("confirm.delete.favorite") + "');if(answer) {"
                                + onclick + "};";
                        tag.put("onclick", onclick);
                    }

                    @Override
                    public void onClick() {
                        favoriteService.delete(favorite);
                        info(getString("favorite.deleted"));
//                        target.add(countLabel);
                    }
                };
                return deleteAction;
            }

            private Link<Void> detailAction(String id, final FavoriteSearchResult favoriteSearchResult) {
                final Link<Void> detailAction = new Link<Void>(id) {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick() {
                        final ProfileDetailsPage nextPage = new ProfileDetailsPage(
                                new LoadableDetachableDomainObjectModel<Long>(User.class, favoriteSearchResult.getTargetId(), userService),
                                new LoadableDetachableDomainObjectModel<Long>((User) getPage().getDefaultModelObject(), userService)) {
                            private static final long serialVersionUID = 1L;

                            @Override
                            protected void onGoBack() {
                                setResponsePage(FavoritesPage.this);
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

    @Override
    public Object getMenuGroup() {
        return MenuGroup.FAVORITES;
    }
}
