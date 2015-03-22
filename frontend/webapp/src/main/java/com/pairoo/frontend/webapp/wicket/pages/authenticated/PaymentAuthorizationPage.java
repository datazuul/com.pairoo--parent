package com.pairoo.frontend.webapp.wicket.pages.authenticated;

import com.datazuul.framework.domain.Language;
import com.datazuul.framework.webapp.wicket.components.form.ShinyForm;
import com.datazuul.framework.webapp.wicket.model.LoadableDetachableDomainObjectModel;
import com.pairoo.business.api.MembershipService;
import com.pairoo.business.api.ProductService;
import com.pairoo.business.api.payment.payone.AuthorizationService;
import com.pairoo.domain.Membership;
import com.pairoo.domain.Product;
import com.pairoo.domain.User;
import com.pairoo.domain.UserAccount;
import com.pairoo.domain.enums.MembershipStatus;
import com.pairoo.domain.enums.ProductDurationType;
import com.pairoo.domain.enums.Role;
import com.pairoo.domain.payment.enums.PaymentChannelType;
import com.pairoo.domain.payment.enums.StatusType;
import com.pairoo.domain.payment.payone.PayOneTransaction;
import com.pairoo.frontend.webapp.wicket.WicketWebSession;
import com.pairoo.frontend.webapp.wicket.components.ContextImageConstants;
import javax.measure.unit.Unit;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.EnumLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.jscience.economics.money.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ralf Eichinger
 */
public class PaymentAuthorizationPage extends AuthenticatedWebPage {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentAuthorizationPage.class);
    @SpringBean(name = AuthorizationService.BEAN_ID)
    private AuthorizationService authorizationService;
    @SpringBean(name = MembershipService.BEAN_ID)
    private MembershipService membershipService;
    @SpringBean(name = ProductService.BEAN_ID)
    private ProductService productService;

    public PaymentAuthorizationPage(IModel<Membership> defaultModel, IModel<User> userModel) {
        super(defaultModel, userModel);
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        logEnter(PaymentAuthorizationPage.class);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        IModel<Membership> membershipModel = (IModel<Membership>) getDefaultModel();

        // form
        final ShinyForm form = createForm("form", new CompoundPropertyModel<>(membershipModel));
        add(form);

        form.add(createProductDurationComponent("product.duration"));
        form.add(createProductRoleComponent("product.role"));

        form.add(createProductPriceComponent("amount", new PropertyModel<Product>(membershipModel, "product")));
        form.add(createProductPriceUnitComponent("amountUnit"));

        form.add(createPaymentChannelImage("paymentChannelLogo", membershipModel));

        // buttons
        final Button backButton = createBackButton("back");
        form.add(backButton);

        form.add(new Button("save"));
    }

    ;

    private Component createProductPriceUnitComponent(String id) {
        User user = userModel.getObject();
        Component c = new Label(id, new Model<>(user.getUserProfile().getGeoLocation().getCountry()
                .getUnitMoney().toString()));
        return c;
    }

    private Component createPaymentChannelImage(String id, IModel<Membership> membershipModel) {
        Membership membership = membershipModel.getObject();
        PaymentChannelType paymentChannelType = membership.getPaymentTransaction().getPaymentChannel()
                .getPaymentChannelType();

        String relativePath = null;
        switch (paymentChannelType) {
            case AMERICAN_EXPRESS:
                relativePath = ContextImageConstants.LOGO_AMERICAN_EXPRESS;
                break;
            case ELEKTRONISCHE_LASTSCHRIFT:
                relativePath = ContextImageConstants.LOGO_ELEKTRONISCHE_LASTSCHRIFT;
                break;
            case MASTERCARD:
                relativePath = ContextImageConstants.LOGO_MASTERCARD;
                break;
            case PAYPAL:
                relativePath = ContextImageConstants.LOGO_PAYPAL;
                break;
            case VISA:
                relativePath = ContextImageConstants.LOGO_VISA;
                break;
            // TODO voucher
            default:
                break;
        }

        return new ContextImage(id, relativePath);
    }

    private Component createProductPriceComponent(String id, PropertyModel<Product> productModel) {
        Product product = productModel.getObject();
        User user = userModel.getObject();
        Language targetLanguage = user.getUserAccount().getPreferredLanguage();
        Unit<Money> targetUnit = user.getUserProfile().getGeoLocation().getCountry().getUnitMoney();
        String totalAmount = productService.getTotalAmountAsString(product, targetUnit, targetLanguage);
        Label c = new Label(id, new Model<>(totalAmount));
        return c;
    }

    private Component createProductRoleComponent(String id) {
        EnumLabel<Role> component = new EnumLabel<>(id);
        return component;
    }

    private Component createProductDurationComponent(String id) {
        EnumLabel<ProductDurationType> component = new EnumLabel<>(id);
        return component;
    }

    private Button createBackButton(String id) {
        final Button backButton = new Button(id) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit() {
                setResponsePage(new PaymentChannelPage((IModel<Membership>) getPage().getDefaultModel(), new LoadableDetachableDomainObjectModel<Long>(userModel.getObject(), userService)));
            }
        };
        backButton.setDefaultFormProcessing(false);
        return backButton;
    }

    private ShinyForm createForm(String id, final CompoundPropertyModel<Membership> model) {
        final ShinyForm form = new ShinyForm(id, model) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                Membership membership = model.getObject();
                User user = (User) userModel.getObject();
                // use the session user to get membership
                // upgrade immediately

                // do payment
                PayOneTransaction transaction = authorizationService.authorize(user, membership);
                membership.setPaymentTransaction(transaction);

                if (StatusType.ERROR.equals(transaction.getStatus())) {
                    LOGGER.info(
                            "authorization error code [{}], message [{}], customer message [{}]",
                            new Object[]{transaction.getErrorCode(), transaction.getErrorMessage(),
                                transaction.getCustomerMessage()});
                    error(transaction.getCustomerMessage());
                    return;
                } else if (StatusType.REDIRECT.equals(transaction.getStatus())) {
                    // redirect to this URL (PayPal page that will return to
                    // wicket url
                    // given in params before when PayPal workflow completed)
                    setResponsePage(new RedirectPage(transaction.getRedirectUrl()));
                    return;
                } else {
                    // authorization succeeded
                    UserAccount userAccount = user.getUserAccount();
                    membershipService.addMembership(userAccount, membership, MembershipStatus.EXTENDED);

                    // refresh user on session
                    ((WicketWebSession) getSession()).reloadUser();
                }

                setResponsePage(new MembershipSummaryPage(model, new LoadableDetachableDomainObjectModel<Long>(userModel.getObject(), userService)));
            }
        };
        return form;
    }
}
