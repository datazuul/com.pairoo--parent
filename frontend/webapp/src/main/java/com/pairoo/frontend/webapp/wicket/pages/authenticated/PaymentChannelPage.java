package com.pairoo.frontend.webapp.wicket.pages.authenticated;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datazuul.framework.webapp.wicket.components.form.ShinyForm;
import com.datazuul.framework.webapp.wicket.model.LoadableDetachableDomainObjectModel;
import com.pairoo.business.api.payment.PaymentChannelService;
import com.pairoo.domain.Membership;
import com.pairoo.domain.User;
import com.pairoo.domain.payment.PaymentChannel;
import com.pairoo.domain.payment.Transaction;
import com.pairoo.domain.payment.enums.PaymentChannelType;
import com.pairoo.frontend.webapp.wicket.panels.BasePanel;
import com.pairoo.frontend.webapp.wicket.panels.authenticated.PaymentCreditCardPanel;
import com.pairoo.frontend.webapp.wicket.panels.authenticated.PaymentElvPanel;
import com.pairoo.frontend.webapp.wicket.panels.authenticated.PaymentPayPalPanel;
import com.pairoo.frontend.webapp.wicket.panels.authenticated.PersonalDataFormComponentPanel;
import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.resource.ContextRelativeResource;

/**
 * @author Ralf Eichinger
 */
public class PaymentChannelPage extends AuthenticatedWebPage {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentChannelPage.class);
    @SpringBean(name = PaymentChannelService.BEAN_ID)
    private PaymentChannelService paymentChannelService;
    Form<?> form;
    BasePanel formContentPanel;
    BasePanel personalDataFormPanel;
    ListView<PaymentChannelType> channelTypesList;
    WebMarkupContainer tabpanel;

    public PaymentChannelPage(IModel<Membership> defaultModel, IModel<User> userModel) {
        super(defaultModel, userModel);
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        logEnter(PaymentChannelPage.class);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.render(JavaScriptReferenceHeaderItem.forUrl("https://secure.pay1.de/client-api/js/ajax.js"));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        IModel<Membership> membershipModel = (IModel<Membership>) getDefaultModel();
        final Membership membership = membershipModel.getObject();
        // set default
        if (membership.getPaymentTransaction() == null
                || membership.getPaymentTransaction().getPaymentChannel() == null) {
            PaymentChannel paymentChannel = paymentChannelService.getDefaultPaymentChannel();
            if (membership.getPaymentTransaction() == null) {
                membership.setPaymentTransaction(new Transaction());
            }
            membership.getPaymentTransaction().setPaymentChannel(paymentChannel);
        }

        // form
        form = createForm("form", new CompoundPropertyModel<>(membershipModel));
        add(form);

        tabpanel = new WebMarkupContainer("tabpanel");
        tabpanel.setOutputMarkupId(true);
        form.add(tabpanel);

        // final List<PaymentChannelType> availableChannelTypes = Arrays.asList(PaymentChannelType.values());
        final List<PaymentChannelType> availableChannelTypes = new ArrayList<>();
        availableChannelTypes.add(PaymentChannelType.VISA);
        availableChannelTypes.add(PaymentChannelType.MASTERCARD);
        availableChannelTypes.add(PaymentChannelType.AMERICAN_EXPRESS);
//        availableChannelTypes.add(PaymentChannelType.ELEKTRONISCHE_LASTSCHRIFT);
        channelTypesList = new ListView("listview", availableChannelTypes) {
            @Override
            protected void populateItem(final ListItem item) {
                PaymentChannelType type = (PaymentChannelType) item.getDefaultModelObject();
                if (type.equals(membership.getPaymentTransaction().getPaymentChannel().getPaymentChannelType())) {
                    item.add(new AttributeModifier("class", "active"));
                }

                // link
                final AjaxLink<Void> ajaxLink = new AjaxLink<Void>("paymentChannelLink") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        PaymentChannelType paymentChannelType = (PaymentChannelType) item.getDefaultModelObject();
                        LOGGER.info("payment channel changed to {}", paymentChannelType.name());

                        // update payment channel in model
                        User user = (User) userModel.getObject();
                        PaymentChannel paymentChannel = paymentChannelService
                                .getDefaultPaymentChannel(user, paymentChannelType);

                        IModel<Membership> membershipModel = (IModel<Membership>) PaymentChannelPage.this.getDefaultModel();
                        membershipModel.getObject().getPaymentTransaction().setPaymentChannel(paymentChannel);

                        // update payment channel specific form
                        BasePanel paymentChannelPanel = null;
                        if (PaymentChannelType.AMERICAN_EXPRESS.equals(paymentChannelType)
                                || PaymentChannelType.MASTERCARD.equals(paymentChannelType)
                                || PaymentChannelType.VISA.equals(paymentChannelType)) {
                            paymentChannelPanel = new PaymentCreditCardPanel(formContentPanel.getId(), membershipModel, userModel);
                            personalDataFormPanel.setVisible(true);
                        } else if (PaymentChannelType.ELEKTRONISCHE_LASTSCHRIFT.equals(paymentChannelType)) {
                            paymentChannelPanel = new PaymentElvPanel(formContentPanel.getId(), membershipModel, userModel);
                            personalDataFormPanel.setVisible(true);

                        } else if (PaymentChannelType.PAYPAL.equals(paymentChannelType)) {
                            paymentChannelPanel = new PaymentPayPalPanel(formContentPanel.getId(), membershipModel, userModel);
                            personalDataFormPanel.setVisible(false);
                        }
                        formContentPanel.replaceWith(paymentChannelPanel);
                        formContentPanel = paymentChannelPanel;
                        target.add(formContentPanel, personalDataFormPanel, tabpanel);
                    }
                };
                item.add(ajaxLink);

                // image
                String logoUrl = "images/logos/";
                String logoFilename;
                switch (type) {
                    case AMERICAN_EXPRESS:
                        logoFilename = "amex-88x60.png";
                        break;
                    case ELEKTRONISCHE_LASTSCHRIFT:
                        logoFilename = "elv-88x60.png";
                        break;
                    case MASTERCARD:
                        logoFilename = "mastercard-88x60.png";
                        break;
                    case PAYPAL:
                        logoFilename = "paypal-88x60.png";
                        break;
                    case VISA:
                        logoFilename = "visa-88x60.png";
                        break;
                    case VOUCHER:
                        logoFilename = "voucher-88x60.png";
                        break;
                    default:
                        logoFilename = "";
                        break;
                }
                ajaxLink.add(new Image("logo", new ContextRelativeResource(logoUrl + logoFilename)));
            }
        };
        channelTypesList.setOutputMarkupId(true);
        tabpanel.add(channelTypesList);

        // panel payment channel data
        formContentPanel = new PaymentCreditCardPanel("formContent", membershipModel, userModel);
        formContentPanel.setOutputMarkupId(true);
        form.add(formContentPanel);

        // panel personal data
        personalDataFormPanel = new PersonalDataFormComponentPanel("personalData", userModel);
        personalDataFormPanel.setOutputMarkupId(true);
        personalDataFormPanel.setOutputMarkupPlaceholderTag(true);
        form.add(personalDataFormPanel);
        // getFeedbackPanel().add(new
        // ClientAndServerValidatingFeedbackBehavior((Form<?>)
        // personalDataFormPanel.get("form")));

        // buttons
        final Button backButton = createBackButton("back");
        form.add(backButton);

        final Button nextButton = new Button("next");
        form.add(nextButton);
        nextButton.add(new AjaxFormValidatingBehavior(form, "onclick") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                super.onSubmit(target);
                addFC(target, getForm());
                // don't add whole panels, because non wicket values get lost
                // (pan, cvc)!
                // target.add(formContentPanel);
                // target.add(personalDataFormPanel);
                target.appendJavaScript("submitRequest();");
            }

            @Override
            protected void onError(AjaxRequestTarget target) {
                super.onSubmit(target);
                addFC(target, getForm());
                // don't add whole panels, because non wicket values get lost
                // (pan, cvc)!
                // target.add(formContentPanel);
                // target.add(personalDataFormPanel);
                target.add(getFeedbackPanel());
            }

            private void addFC(final AjaxRequestTarget target, Form<?> form) {
                form.visitFormComponents(new IVisitor<FormComponent<?>, Void>() {
                    @Override
                    public void component(FormComponent fc, IVisit visit) {
                        if (!(fc instanceof FormComponentPanel<?>)) {
                            target.add(fc);
                        } else {
                            LOGGER.debug("skipping formcomponent " + fc.getInputName());
                        }
                    }
                });
            }
        });
        nextButton.setDefaultFormProcessing(false);
    }

    private Button createBackButton(String id) {
        final Button backButton = new Button(id) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit() {
                setResponsePage(new MembershipPage(new LoadableDetachableDomainObjectModel<Long>(userModel.getObject(),
                        userService)));
            }
        };
        backButton.setDefaultFormProcessing(false);
        return backButton;
    }

    private ShinyForm createForm(String id, CompoundPropertyModel<Membership> model) {
        final ShinyForm form = new ShinyForm(id, model) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                // now handled in subforms in inner panel, because
                // behavior is different for payment channels
                // e.g. Paypal redirects...
                // setResponsePage(new PaymentAuthorizationPage((IModel<User>)
                // getDefaultModel()));
            }
        };
        form.setOutputMarkupId(true);
        // ... behavior
        // getFeedbackPanel().add(new
        // ClientAndServerValidatingFeedbackBehavior(form));
        return form;
    }
}
