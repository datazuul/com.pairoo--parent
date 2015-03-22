package com.pairoo.frontend.webapp.wicket.panels.authenticated;

import com.datazuul.framework.webapp.wicket.components.form.CreditCardExpirationDateFormComponentPanel;
import com.datazuul.framework.webapp.wicket.components.form.ShinyForm;
import com.datazuul.framework.webapp.wicket.model.LoadableDetachableDomainObjectModel;
import com.pairoo.business.api.UserService;
import com.pairoo.business.api.payment.payone.CreditCardCheckService;
import com.pairoo.domain.Membership;
import com.pairoo.domain.User;
import com.pairoo.domain.payment.CreditCardAccount;
import com.pairoo.domain.payment.payone.PayOneConstants;
import com.pairoo.frontend.webapp.wicket.pages.BasePage;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.PaymentAuthorizationPage;
import com.pairoo.frontend.webapp.wicket.panels.BasePanel;
import java.util.HashMap;
import java.util.Map;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.CallbackParameter;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.util.template.JavaScriptTemplate;
import org.apache.wicket.util.template.PackageTextTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaymentCreditCardPanel extends BasePanel {

    private static final long serialVersionUID = 2L;
    private final static Logger LOGGER = LoggerFactory.getLogger(PaymentCreditCardPanel.class);
    @SpringBean(name = CreditCardCheckService.BEAN_ID)
    private CreditCardCheckService creditCardCheckService;
    @SpringBean(name = UserService.BEAN_ID)
    private UserService userService;
    AbstractDefaultAjaxBehavior abstractDefaultAjaxBehavior;
    private Label checkScript;
    private FormComponent<String> cardHolder;
    private CreditCardExpirationDateFormComponentPanel expiryDate;
    private boolean ajaxValidation = false;

    public PaymentCreditCardPanel(final String id, final IModel<Membership> membershipModel, IModel<User> model) {
        super(id, membershipModel, model);
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();

        // done here, because in constructor form is not there, yet (component
        // not added to hierarchy)
        String holderNameFieldId = cardHolder.getMarkupId();
        String monthFieldId = expiryDate.getMonthFieldMarkupId();
        String yearFieldId = expiryDate.getYearFieldMarkupId();
        String jsCode = generateJavascriptCode(holderNameFieldId, monthFieldId, yearFieldId);
        checkScript.setDefaultModelObject(jsCode);

        logEnter(PaymentCreditCardPanel.class);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        checkScript = new Label("checkScript", new Model<String>(null));
        checkScript.setEscapeModelStrings(false);
        add(checkScript);

        abstractDefaultAjaxBehavior = new AbstractDefaultAjaxBehavior() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void respond(AjaxRequestTarget target) {
                // callback in error case
                IRequestParameters requestParameters = RequestCycle.get().getRequest().getRequestParameters();
                StringValue status = requestParameters.getParameterValue("status");
                if (status != null && !status.isEmpty()) {
                    if ("VALID".equals(status.toString())) {
                        ajaxValidation = true;
                        StringValue pseudocardpan = requestParameters.getParameterValue("pseudocardpan");
                        StringValue truncatedcardpan = requestParameters.getParameterValue("truncatedcardpan");
                        Membership membership = (Membership) getDefaultModelObject();
                        CreditCardAccount creditCardAccount = (CreditCardAccount) membership.getPaymentTransaction()
                                .getPaymentChannel();
                        creditCardAccount.setPseudopan(pseudocardpan.toString());
                        creditCardAccount.setTruncatedPan(truncatedcardpan.toString());
                        Form pageForm = (Form) getPage().get("form");
                        String formId = pageForm.getMarkupId();
                        target.appendJavaScript("document.getElementById('" + formId + "').submit();");
                    } else {
                        ajaxValidation = false;
                        StringValue customerMessage = requestParameters.getParameterValue("customermessage");
                        StringValue errorCode = requestParameters.getParameterValue("errorcode");
                        StringValue errorMessage = requestParameters.getParameterValue("errormessage");

                        if (customerMessage != null && !customerMessage.isEmpty()) {
                            final User user = userModel.getObject();
                            LOGGER.info("creditcardcheck failed for user {}: {}", new Object[]{
                                user.getUserAccount().getUsername(), errorCode + " " + errorMessage});
                            // getSession().cleanupFeedbackMessages();
                            getSession().error(customerMessage);
                            target.add(((BasePage) PaymentCreditCardPanel.this.getPage()).getFeedbackPanel());
                        }
                    }
                }
            }

            @Override
            public void renderHead(Component component, IHeaderResponse response) {
                super.renderHead(component, response);
                response.render(OnDomReadyHeaderItem.forScript(getCallbackScript().toString()));
            }
        };
        add(abstractDefaultAjaxBehavior);

        // form
        final ShinyForm form = createForm("form", (IModel<Membership>) getDefaultModel());
        add(form);

        // name of credit card holder
        cardHolder = createCardHolderFC("paymentTransaction.paymentChannel.holderName");
        form.add(cardHolder);
        form.add(labelForFormComponent("cardHolderLabel", cardHolder));

        // valid until date
        expiryDate = createExpiryDateFC("paymentTransaction.paymentChannel.validThru");
        form.add(expiryDate);
        form.add(labelForFormComponent("expiryDateLabel", expiryDate));
    }

    private CreditCardExpirationDateFormComponentPanel createExpiryDateFC(String id) {
        // ... field
        CreditCardExpirationDateFormComponentPanel fc = new CreditCardExpirationDateFormComponentPanel(id);
        fc.setLabel(new ResourceModel("expiry_date"));
        // ... validation
        fc.setRequired(true);
        return fc;
    }

    private FormComponent<String> createCardHolderFC(String id) {
        // ... field
        FormComponent<String> fc = new RequiredTextField<String>(id);
        fc.setLabel(new ResourceModel("cardholder"));
        fc.setOutputMarkupId(true);
        // ... validation
        // TODO maxlength etc.
        return fc;
    }

    private String generateJavascriptCode(String holderNameFieldId, String monthFieldId, String yearFieldId) {
        PackageTextTemplate jsTemplate = new PackageTextTemplate(this.getClass(),
                "javascript/Template-creditcardcheck.js");
        Map<String, Object> parameters = new HashMap<String, Object>();
        // wicket integration params
        Form parentForm = (Form) getPage().get("form");
        parameters.put("formId", parentForm.getMarkupId());
        // outer form (around all in page contained forms), will cause also submit in inner form...
        parameters.put("callbackFunctionBody", abstractDefaultAjaxBehavior.getCallbackFunctionBody(
                CallbackParameter.explicit("status"),
                CallbackParameter.explicit("pseudocardpan"),
                CallbackParameter.explicit("truncatedcardpan"),
                CallbackParameter.explicit("customermessage"),
                CallbackParameter.explicit("errorcode"),
                CallbackParameter.explicit("errormessage")));

        /* 
         * getCallbackFunction:
         * 
         * function (status,pseudocardpan,truncatedcardpan,customermessage,errorcode,errormessage) {
         *   var attrs = {"u":"./page?5-1.IBehaviorListener.0-form-formContent","c":"formContent27"};
         *   var params = {'status': status,'pseudocardpan': pseudocardpan,'truncatedcardpan': truncatedcardpan,'customermessage': customermessage,'errorcode': errorcode,'errormessage': errormessage};
         *   attrs.ep = params;
         *   Wicket.Ajax.ajax(attrs);
         * }
         */
        parameters.put("holderNameFieldId", holderNameFieldId);
        parameters.put("monthFieldId", monthFieldId);
        parameters.put("yearFieldId", yearFieldId);

        // standard params
        parameters.put("encoding", creditCardCheckService.getEncodingParam().get(PayOneConstants.KEY_ENCODING));
        parameters.put("hash", creditCardCheckService.getHashParam().get(PayOneConstants.KEY_HASH));
        parameters.put("merchantId", creditCardCheckService.getMerchantIdParam().get(PayOneConstants.KEY_MERCHANT_ID));
        parameters.put("mode", creditCardCheckService.getModeParam().get(PayOneConstants.KEY_MODE));
        parameters.put("portalId", creditCardCheckService.getPortalIdParam().get(PayOneConstants.KEY_PORTAL_ID));
        parameters.put("responseType",
                creditCardCheckService.getResponseTypeParam().get(PayOneConstants.KEY_RESPONSE_TYPE));

        // creditcardcheck params
        parameters.put("subaccountId",
                creditCardCheckService.getSubAccountIdParam().get(PayOneConstants.KEY_SUBACCOUNT_ID));
        // done via javascript: parameters.put("cardcvc2", cardCVC2);
        // done via javascript: parameters.put("cardexpiredate",
        // creditCardCheckService.getCardExpireDateParam(cardExpireDate));
        // done via javascript: parameters.put("cardpan", cardPan);

        Membership membership = (Membership) getDefaultModelObject();
        parameters.put(
                "cardType",
                creditCardCheckService.getCardTypeParam(
                membership.getPaymentTransaction().getPaymentChannel().getPaymentChannelType()).get(
                PayOneConstants.KEY_CARD_TYPE));
        parameters.put("language", userModel.getObject().getUserAccount().getPreferredLanguage().getLocale()
                .getLanguage());
        parameters.put("request", creditCardCheckService.getRequestParam().get(PayOneConstants.KEY_REQUEST));
        parameters.put("storeCardData",
                creditCardCheckService.getStoreCardDataParam().get(PayOneConstants.KEY_STORE_CARDDATA));

        String jsCode = new JavaScriptTemplate(jsTemplate).asString(parameters);
        return jsCode;
    }

    private ShinyForm createForm(String id, final IModel<Membership> model) {
        final ShinyForm form = new ShinyForm(id, new CompoundPropertyModel(model)) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                // preauthorization/capture process not needed
                // as we do not send goods, customer gets "product" immediately
                // so just authorization on next page
                if (ajaxValidation) {
                    setResponsePage(new PaymentAuthorizationPage((IModel<Membership>) getDefaultModel(), new LoadableDetachableDomainObjectModel<Long>(userModel.getObject(), userService)));
                }
            }
        };
        form.setOutputMarkupId(true);
        return form;
    }
}
