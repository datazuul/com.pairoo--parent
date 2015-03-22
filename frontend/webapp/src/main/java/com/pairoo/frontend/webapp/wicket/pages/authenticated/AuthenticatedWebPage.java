package com.pairoo.frontend.webapp.wicket.pages.authenticated;

import com.datazuul.framework.webapp.wicket.markup.html.link.MenuGroupLink;
import com.datazuul.framework.webapp.wicket.model.LoadableDetachableDomainObjectModel;
import com.datazuul.framework.webapp.wicket.visitor.MenuGroupLinkVisitor;
import com.pairoo.domain.User;
import com.pairoo.frontend.webapp.wicket.pages.BasePage;
import com.pairoo.frontend.webapp.wicket.panels.MainNaviAuthenticatedPanel;
import com.pairoo.frontend.webapp.wicket.panels.MyNaviAuthenticatedPanel;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class AuthenticatedWebPage extends BasePage {

    private static final long serialVersionUID = 2L;

    @SuppressWarnings("unused")
    private AuthenticatedWebPage() {
    }

    public AuthenticatedWebPage(final PageParameters params) {
        super(params);
    }

    public AuthenticatedWebPage(final IModel<?> model) {
        super(model);
    }

    public AuthenticatedWebPage(final IModel<?> model, final IModel<User> userModel) {
        super(model, userModel);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        // my navigation
        IModel<User> model = userModel;
        if (model == null) {
            model = (IModel<User>) getDefaultModel();
        }
        add(myNavigation("myNavi", model));

        // main navigation
        final MainNaviAuthenticatedPanel mainNaviPanel = createMainNavigationComponent("mainNavi");
        add(mainNaviPanel);
        mainNaviPanel.visitChildren(MenuGroupLink.class, new MenuGroupLinkVisitor());
    }

    private MainNaviAuthenticatedPanel createMainNavigationComponent(String id) {
        User user = getSessionUser();
        IModel<User> model = null;
        if (user != null) {
            model = new LoadableDetachableDomainObjectModel<Long>(user, userService);
        }
        return new MainNaviAuthenticatedPanel(id, model);
    }

    private Component myNavigation(String id, IModel<User> model) {
        MyNaviAuthenticatedPanel c = new MyNaviAuthenticatedPanel(id, model);
        c.setRenderBodyOnly(true);
        return c;
    }
}
