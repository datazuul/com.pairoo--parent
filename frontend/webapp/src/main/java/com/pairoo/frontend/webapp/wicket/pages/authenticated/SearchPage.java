package com.pairoo.frontend.webapp.wicket.pages.authenticated;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import com.datazuul.framework.webapp.wicket.components.form.ShinyForm;
import com.datazuul.framework.webapp.wicket.markup.html.link.MenuGroupMember;
import com.datazuul.framework.webapp.wicket.model.LoadableDetachableDomainObjectModel;
import com.pairoo.business.api.UserAccountService;
import com.pairoo.domain.User;
import com.pairoo.domain.UserAccount;
import com.pairoo.frontend.webapp.wicket.pages.groups.MenuGroup;
import com.pairoo.frontend.webapp.wicket.panels.BasicSearchPanel;

/**
 * @author Ralf Eichinger
 */
public class SearchPage extends AuthenticatedWebPage implements MenuGroupMember {

    private static final long serialVersionUID = 1L;
    @SpringBean(name = UserAccountService.BEAN_ID)
    private UserAccountService userAccountService;

    public SearchPage(IModel<User> model) {
        super(model);
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        logEnter(SearchPage.class);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        // username search
        final ShinyForm usernameSearchForm = createForm("usernameSearchForm");
        add(usernameSearchForm);
        usernameSearchForm.add(createUsernameFormComponent("username"));

        // search box
        add(new BasicSearchPanel("searchPanel", (IModel<User>) getDefaultModel()));
    }

    private FormComponent<String> createUsernameFormComponent(String id) {
        // ... field
        final TextField<String> usernameField = new TextField<String>(id);
        usernameField.setLabel(new ResourceModel("username"));
        usernameField.add(new AttributeModifier("maxLength", "15"));
        usernameField.add(new AttributeModifier("size", "15"));
        // ... validation
        usernameField.add(StringValidator.lengthBetween(3, 15));
        usernameField.setRequired(true);
        return usernameField;
    }

    private ShinyForm createForm(String id) {
        final UserAccount userAccount = new UserAccount();
        final ShinyForm usernameSearchForm = new ShinyForm(id, new CompoundPropertyModel<UserAccount>(userAccount)) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                final UserAccount ua = (UserAccount) getDefaultModelObject();
                final UserAccount userAccount = userAccountService.getByUsername(ua.getUsername());
                if (userAccount == null) {
                    info(getString("no.results"));
                } else {
                    final User user = userAccount.getUser();
                    setResponsePage(new ProfileDetailsPage(
                            new LoadableDetachableDomainObjectModel<Long>(user, userService),
                            new LoadableDetachableDomainObjectModel<Long>((User) getPage().getDefaultModelObject(), userService)) {
                        private static final long serialVersionUID = 1L;

                        @Override
                        protected void onGoBack() {
                            setResponsePage(SearchPage.this);
                        }
                    });
                }
            }
        };
        return usernameSearchForm;
    }

    @Override
    public Object getMenuGroup() {
        return MenuGroup.SEARCH;
    }
}
