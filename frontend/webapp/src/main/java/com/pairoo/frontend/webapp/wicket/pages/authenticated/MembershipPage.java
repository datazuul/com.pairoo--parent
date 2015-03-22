package com.pairoo.frontend.webapp.wicket.pages.authenticated;

import com.datazuul.framework.webapp.wicket.components.form.ShinyForm;
import com.datazuul.framework.webapp.wicket.model.LoadableDetachableDomainObjectModel;
import com.pairoo.business.api.ProductService;
import com.pairoo.domain.Membership;
import com.pairoo.domain.Product;
import com.pairoo.domain.User;
import com.pairoo.domain.UserAccount;
import com.pairoo.frontend.webapp.wicket.panels.MembershipProductComparisonPanel;
import com.pairoo.frontend.webapp.wicket.panels.authenticated.EditMembershipProductPanel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Selection of new membership products the given user can choose to buy.
 *
 * @author Ralf Eichinger
 */
public class MembershipPage extends AuthenticatedWebPage {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(MembershipPage.class);
    @SpringBean(name = ProductService.BEAN_ID)
    private ProductService productService;

    public MembershipPage(IModel<User> model) {
        super(null, model);

        Membership membership = new Membership();
        membership.setProduct(productService.getDefaultProduct());
        setDefaultModel(Model.of(membership));
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        logEnter(MembershipPage.class);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        IModel<Membership> membershipModel = (IModel<Membership>) getDefaultModel();

        final ShinyForm form = createForm("form", membershipModel);
        add(form);

        form.add(new EditMembershipProductPanel("formContent", membershipModel));

        form.add(new Button("next"));

        add(new MembershipProductComparisonPanel("membershipComparisonPanel"));
    }

    private ShinyForm createForm(final String id, final IModel<Membership> model) {
        final ShinyForm form = new ShinyForm(id, model) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                final User user = userModel.getObject();
                final UserAccount ua = user.getUserAccount();
                final Membership membership = (Membership) getDefaultModelObject();
                final Product product = membership.getProduct();

                LOGGER.info("{} selected product {} with monthly rate {} EUR", new Object[]{ua.getUsername(),
                            product.getDuration().name(), productService.getMonthlyRateAsString(product)});
                setResponsePage(new PaymentChannelPage((IModel<Membership>) getDefaultModel(), new LoadableDetachableDomainObjectModel<Long>(user, userService)));
            }
        };
        return form;
    }
}
