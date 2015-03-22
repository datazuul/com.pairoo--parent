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
public class SuggestionsPage extends AuthenticatedWebPage implements MenuGroupMember {

    private static final long serialVersionUID = 1L;

    // localhost:8080
    // private static final String GMAPS_KEY =
    // "ABQIAAAA97_buYctDhaanPL-uED8txTwM0brOpm-All5BF6PoaKBxRWWERTl_Z3abREy_5Ldy_yMuCsn5M0FmQ";
    public SuggestionsPage(IModel<User> model) {
        super(model);
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        logEnter(SuggestionsPage.class);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        // data provider
        User user = (User) getDefaultModelObject();
        SearchProfile searchProfile = user.getSearchProfile();
        final SortableDataProvider<UserSearchResult, String> userDataProvider = createDataProvider(user, searchProfile);

        // map
        // final OpenLayersMap openLayersMap = new OpenLayersMap("map", true);
        // add(openLayersMap);

        // final List<Layer> layers = new ArrayList<Layer>();
        //
        // final Layer layerOSMTilesAtHome = new OSM("Osmarender",
        // OSMLayer.TilesAtHome);
        // final Layer layerOSMMapnik = new OSM("Mapnik", OSMLayer.Mapnik);
        // final Layer layerOSMCycleMap = new OSM("CycleMap",
        // OSMLayer.CycleMap);
        //
        // layers.add(layerOSMMapnik);
        // layers.add(layerOSMTilesAtHome);
        // layers.add(layerOSMCycleMap);
        //
        // final HashMap<String, String> mapOptions = new HashMap<String,
        // String>();
        // final Bounds boundsExtend = new Bounds(new LonLat(-20037508.34,
        // -20037508.34), new LonLat(20037508.34,
        // 20037508.34));
        // mapOptions.put("maxExtent", boundsExtend.getJSconstructor());
        // mapOptions.put("projection",
        // "new OpenLayers.Projection('EPSG:900913')");
        // mapOptions.put("displayProjection",
        // "new OpenLayers.Projection('EPSG:4326')");
        // mapOptions.put("units", "'meters'");
        // mapOptions.put("maxResolution", "156543");
        // mapOptions.put("numZoomLevels", "18");

        // final OpenLayersMap map = new OpenLayersMap("map", false, layers,
        // mapOptions);

        // final OpenLayersMap map = new OpenLayersMap("map");
        // map.addControl(Control.LayerSwitcher);
        // map.addControl(Control.MousePosition);
        // map.addControl(Control.KeyboardDefaults);

        // final GeoLocation geoLocation =
        // user.getUserProfile().getGeoLocation();
        // map.setCenter(new LonLat(geoLocation.getLongitude(),
        // geoLocation.getLatitude()));
        // map.setZoom(3);
        //
        // add(map);

        // google map
        // final List<Layer> layers = new ArrayList<Layer>();
        // final HashMap<String, String> optionsLayer = new HashMap<String,
        // String>();
        // // optionsLayer.put("type", "G_HYBRID_MAP");
        // final Layer layer = new GMap("GMap", GMAPS_KEY, "2", optionsLayer);
        // layers.add(layer);
        // final HashMap<String, String> mapOptions = new HashMap<String,
        // String>();
        // final Bounds boundsExtend = new Bounds(new LonLat(12.91, 55.52), new
        // LonLat(13.29, 55.73));
        // mapOptions.put("maxExtent", boundsExtend.getJSconstructor());
        // final OpenLayersMap gmap = new OpenLayersMap("gmap", true, layers,
        // mapOptions);
        // gmap.setCenter(new LonLat(10.2, 48.9), 13);
        // add(gmap);
        //
        // gmap.setVisible(false);

        // list
        final DataView<UserSearchResult> dataView = createListComponent("users", userDataProvider);
        add(dataView);

        // found profiles label
        add(foundProfilesCounterLabel("found.profiles", dataView));

        // paging
        add(pagingComponent("navigator", dataView));

        if (dataView.getItemCount() == 0) {
            info(getString("no.results"));
        }
    }

    private PagingNavigator pagingComponent(String id, final DataView<UserSearchResult> dataView) {
        final PagingNavigator pagingNavigator = new PagingNavigator(id, dataView) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isVisible() {
                return (getPageable().getPageCount() > 1 && dataView.getItemCount() > 0);
            }
        };
        return pagingNavigator;
    }

    private Label foundProfilesCounterLabel(String id, final DataView<UserSearchResult> dataView) {
        return new Label(id, new StringResourceModel("found.profiles", new Model<Long>(dataView.getItemCount())));
    }

    private DataView<UserSearchResult> createListComponent(String id, final SortableDataProvider<UserSearchResult, String> userDataProvider) {
        final DataView<UserSearchResult> dataView = new DataView<UserSearchResult>(id, userDataProvider, 20) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final Item<UserSearchResult> item) {

                // marker in map
                // final GeoLocation geoLocation =
                // user.getUserProfile().getGeoLocation();
                // if (geoLocation != null && geoLocation.getLatitude() != null
                // && geoLocation.getLongitude() != null) {
                // final int age =
                // personProfileService.getAge(user.getUserProfile().getBirthdate());
                // String ageStr = "--";
                // if (age != -1) {
                // ageStr = String.valueOf(age);
                // }
                // // final Marker marker = new Marker(new
                // // LonLat(geoLocation.getLongitude(),
                // // geoLocation.getLatitude()),
                // // new
                // // GeoMarkerPopupPanel(user.getUserAccount().getUsername() +
                // // ", " + age));
                // // map.addOverlay(marker);
                // }
                IModel<UserSearchResult> model = item.getModel();
                UserSearchResult userSearchResult = (UserSearchResult) item.getDefaultModelObject();
                UserSearchResultPanel userSearchResultPanel = new UserSearchResultPanel("resultPanel",
                        model, userSearchResult.getId()) {
                    @Override
                    protected WebPage getResponsePage() {
                        return SuggestionsPage.this;
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
                                setResponsePage(SuggestionsPage.this);
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
        return MenuGroup.SUGGESTIONS;
    }
}
