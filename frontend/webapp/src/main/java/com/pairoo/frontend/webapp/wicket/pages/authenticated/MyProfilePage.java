package com.pairoo.frontend.webapp.wicket.pages.authenticated;

import com.datazuul.framework.webapp.wicket.markup.html.link.MenuGroupMember;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import com.datazuul.framework.webapp.wicket.model.LoadableDetachableDomainObjectModel;
import com.pairoo.domain.User;
import com.pairoo.frontend.webapp.wicket.pages.groups.MenuGroup;
import com.pairoo.frontend.webapp.wicket.panels.ProfileAboutMePanel;
import com.pairoo.frontend.webapp.wicket.panels.ProfileMyAccountPanel;
import com.pairoo.frontend.webapp.wicket.panels.ProfileMyLifePanel;
import com.pairoo.frontend.webapp.wicket.panels.ProfileMyMatchPanel;
import com.pairoo.frontend.webapp.wicket.panels.ProfileMyPhotosPanel;
import com.pairoo.frontend.webapp.wicket.panels.ProfileMySearchPanel;
import com.pairoo.frontend.webapp.wicket.panels.ProfileMyValuesPanel;

/**
 * @author Ralf Eichinger
 */
public class MyProfilePage extends AuthenticatedWebPage implements MenuGroupMember {

    private static final long serialVersionUID = 1L;

    public MyProfilePage(IModel<User> model) {
        super(model);
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        logEnter(MyProfilePage.class);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        // create a list of ITab objects used to feed the tabbed panel
        final List<ITab> tabs = new ArrayList<ITab>();

        tabs.add(new AbstractTab(new ResourceModel("about_me")) {
            private static final long serialVersionUID = 1L;

            @Override
            public Panel getPanel(final String panelId) {
                return new ProfileAboutMePanel(panelId, new LoadableDetachableDomainObjectModel<Long>((User) getDefaultModelObject(),
                        userService));
            }
        });

        tabs.add(new AbstractTab(new ResourceModel("my_life")) {
            private static final long serialVersionUID = 1L;

            @Override
            public Panel getPanel(final String panelId) {
                return new ProfileMyLifePanel(panelId, new LoadableDetachableDomainObjectModel<Long>((User) getDefaultModelObject(),
                        userService));
            }
        });

        tabs.add(new AbstractTab(new ResourceModel("my_values")) {
            private static final long serialVersionUID = 1L;

            @Override
            public Panel getPanel(final String panelId) {
                return new ProfileMyValuesPanel(panelId, new LoadableDetachableDomainObjectModel<Long>((User) getDefaultModelObject(),
                        userService));
            }
        });

        tabs.add(new AbstractTab(new ResourceModel("i_am_looking_for")) {
            private static final long serialVersionUID = 1L;

            @Override
            public Panel getPanel(final String panelId) {
                return new ProfileMySearchPanel(panelId, new LoadableDetachableDomainObjectModel<Long>((User) getDefaultModelObject(),
                        userService));
            }
        });

        tabs.add(new AbstractTab(new ResourceModel("who_matches_me")) {
            private static final long serialVersionUID = 1L;

            @Override
            public Panel getPanel(final String panelId) {
                return new ProfileMyMatchPanel(panelId, new LoadableDetachableDomainObjectModel<Long>((User) getDefaultModelObject(),
                        userService));
            }
        });

        tabs.add(new AbstractTab(new ResourceModel("photos")) {
            private static final long serialVersionUID = 1L;

            @Override
            public Panel getPanel(final String panelId) {
                return new ProfileMyPhotosPanel(panelId, new LoadableDetachableDomainObjectModel<Long>((User) getDefaultModelObject(),
                        userService));
            }
        });

        tabs.add(new AbstractTab(new ResourceModel("my_account")) {
            private static final long serialVersionUID = 1L;

            @Override
            public Panel getPanel(final String panelId) {
                return new ProfileMyAccountPanel(panelId, new LoadableDetachableDomainObjectModel<Long>((User) getDefaultModelObject(),
                        userService));
            }
        });

        // add the new tabbed panel, attribute modifier only used to switch
        // between different css variations
        final TabbedPanel tabbedPanel = new TabbedPanel("tabs", tabs);
        tabbedPanel.setSelectedTab(0);
        add(tabbedPanel);
    }

    @Override
    public Object getMenuGroup() {
        return MenuGroup.MY_PROFILE;
    }
}
