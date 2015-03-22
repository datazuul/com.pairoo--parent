package com.pairoo.frontend.webapp.wicket.panels.authenticated;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
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

import com.datazuul.framework.webapp.wicket.components.form.ShinyForm;
import com.datazuul.framework.webapp.wicket.model.LoadableDetachableDomainObjectModel;
import com.pairoo.business.api.UserService;
import com.pairoo.business.api.payment.payone.ELVCheckService;
import com.pairoo.domain.Membership;
import com.pairoo.domain.User;
import com.pairoo.domain.payment.enums.ELVCountry;
import com.pairoo.domain.payment.payone.PayOneConstants;
import com.pairoo.frontend.webapp.wicket.pages.BasePage;
import com.pairoo.frontend.webapp.wicket.pages.authenticated.PaymentAuthorizationPage;
import com.pairoo.frontend.webapp.wicket.panels.BasePanel;
import org.apache.wicket.ajax.attributes.CallbackParameter;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;

public class PaymentElvPanel extends BasePanel {

    private static final long serialVersionUID = 2L;
    private final static Logger LOGGER = LoggerFactory.getLogger(PaymentElvPanel.class);
    @SpringBean(name = ELVCheckService.BEAN_ID)
    private ELVCheckService elvCheckService;
    @SpringBean(name = UserService.BEAN_ID)
    private UserService userService;
    private Label checkScript;
    AbstractDefaultAjaxBehavior abstractDefaultAjaxBehavior;
    private boolean ajaxValidation = false;
    FormComponent<String> bankAccountFC;
    FormComponent<String> bankAccountHolderFC;
    FormComponent<Integer> bankCodeFC;
    FormComponent<ELVCountry> bankCountryFC;

    public PaymentElvPanel(final String id, final IModel<Membership> membershipModel, IModel<User> model) {
        super(id, membershipModel, model);
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();

        // done here, because in constructor form is not there, yet (component
        // not added to hierarchy)
        String bankAccountFieldId = bankAccountFC.getMarkupId();
        String bankAccountHolderFieldId = bankAccountHolderFC.getMarkupId();
        String bankCodeFieldId = bankCodeFC.getMarkupId();
        String bankCountryFieldId = bankCountryFC.getMarkupId();

        String jsCode = generateJavascriptCode(bankAccountFieldId, bankAccountHolderFieldId, bankCodeFieldId,
                bankCountryFieldId);
        checkScript.setDefaultModelObject(jsCode);

        logEnter(PaymentElvPanel.class);
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
                        Form pageForm = (Form) getPage().get("form");
                        String formId = pageForm.getMarkupId();
                        target.appendJavaScript("document.getElementById('" + formId + "').submit();");
                    } else if ("BLOCKED".equals(status.toString())) {
                        ajaxValidation = false;
                        final User user = userModel.getObject();
                        LOGGER.info("bankaccountcheck failed for user {}: BLOCKED account!", new Object[]{user
                            .getUserAccount().getUsername()});
                        String customerMessage = getString("bankaccount_blocked");
                        // getSession().cleanupFeedbackMessages();
                        getSession().error(customerMessage);
                        target.add(((BasePage) PaymentElvPanel.this.getPage()).getFeedbackPanel());
                    } else {
                        // INVALID, ERROR
                        ajaxValidation = false;
                        StringValue customerMessage = requestParameters.getParameterValue("customermessage");
                        StringValue errorCode = requestParameters.getParameterValue("errorcode");
                        StringValue errorMessage = requestParameters.getParameterValue("errormessage");

                        if (customerMessage != null && !customerMessage.isEmpty()) {
                            final User user = userModel.getObject();
                            LOGGER.info("bankaccountcheck failed for user {}: {}", new Object[]{
                                user.getUserAccount().getUsername(), errorCode + " " + errorMessage});
                            // getSession().cleanupFeedbackMessages();
                            getSession().error(customerMessage);
                            target.add(((BasePage) PaymentElvPanel.this.getPage()).getFeedbackPanel());
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

        bankAccountHolderFC = createBankAccountHolderFC("paymentTransaction.paymentChannel.bankAccountHolder");
        form.add(bankAccountHolderFC);
        form.add(labelForFormComponent("bankAccountHolderLabel", bankAccountHolderFC));

        bankCodeFC = createBankCodeFC("paymentTransaction.paymentChannel.bankCode");
        form.add(bankCodeFC);
        form.add(labelForFormComponent("bankCodeLabel", bankCodeFC));

        bankAccountFC = createBankAccountFC("paymentTransaction.paymentChannel.bankAccount");
        form.add(bankAccountFC);
        form.add(labelForFormComponent("bankAccountLabel", bankAccountFC));

        bankCountryFC = createBankCountryFC("paymentTransaction.paymentChannel.bankCountry");
        form.add(bankCountryFC);
        form.add(labelForFormComponent("bankCountryLabel", bankCountryFC));
    }

    private FormComponent<ELVCountry> createBankCountryFC(String id) {
        // ... values
        final ELVCountry[] elvCountryValues = ELVCountry.values();
        final List<ELVCountry> elvCountries = new ArrayList<ELVCountry>(Arrays.asList(elvCountryValues));
        // ... field
        final DropDownChoice<ELVCountry> fc = new DropDownChoice<ELVCountry>(id, elvCountries,
                new EnumChoiceRenderer<ELVCountry>(this));
        fc.setLabel(new ResourceModel("country"));
        fc.setEscapeModelStrings(false);
        // ... validation
        fc.setRequired(true);

        // until now: just Germany is supported
        fc.setEnabled(false);

        return fc;
    }

    private FormComponent<String> createBankAccountFC(String id) {
        // ... field
        FormComponent<String> fc = new RequiredTextField<String>(id);
        fc.setLabel(new ResourceModel("bankaccount_number"));
        fc.setOutputMarkupId(true);
        // ... validation
        // TODO maxlength etc.
        return fc;
    }

    private FormComponent<Integer> createBankCodeFC(String id) {
        // ... field
        TextField<Integer> fc = new RequiredTextField<Integer>(id);
        fc.setLabel(new ResourceModel("bank_code"));
        fc.setOutputMarkupId(true);
        // ... validation
        // fc.setType(Integer.class);
        // fc.setMaximum(99999999);
        // fc.setMinimum(10000000);
        return fc;
    }

    private FormComponent<String> createBankAccountHolderFC(String id) {
        // ... field
        FormComponent<String> fc = new RequiredTextField<String>(id);
        fc.setLabel(new ResourceModel("bankaccount_holder"));
        fc.setOutputMarkupId(true);
        // ... validation
        // TODO maxlength etc.
        return fc;
    }

    private ShinyForm createForm(String id, final IModel<Membership> model) {
        final ShinyForm form = new ShinyForm(id, new CompoundPropertyModel<Membership>(model)) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                // preauthorization/capture process not needed
                // as we do not send goods, customer gets "product" immediately
                // so just authorization on next page
                if (ajaxValidation) {
                    setResponsePage(new PaymentAuthorizationPage((IModel<Membership>) getDefaultModel(),
                            new LoadableDetachableDomainObjectModel<Long>(userModel.getObject(), userService)));
                }
            }
        };
        form.setOutputMarkupId(true);
        return form;
    }

    private String generateJavascriptCode(String bankAccountFieldId, String bankAccountHolderFieldId,
            String bankCodeFieldId, String bankCountryFieldId) {
        PackageTextTemplate jsTemplate = new PackageTextTemplate(this.getClass(),
                "javascript/Template-bankaccountcheck.js");
        Map<String, Object> parameters = new HashMap<String, Object>();
        // wicket integration params
        Form parentForm = (Form) getPage().get("form");
        parameters.put("formId", parentForm.getMarkupId());
        // outer form (around all in page contained forms), will cause also submit in inner form...


        //parameters.put("feedbackUrl", abstractDefaultAjaxBehavior.getCallbackUrl());
        parameters.put("callbackFunctionBody", abstractDefaultAjaxBehavior.getCallbackFunctionBody(
                CallbackParameter.explicit("status"),
                CallbackParameter.explicit("customermessage"),
                CallbackParameter.explicit("errorcode"),
                CallbackParameter.explicit("errormessage")));

        // standard params
        parameters.put("encoding", elvCheckService.getEncodingParam().get(PayOneConstants.KEY_ENCODING));
        parameters.put("hash", elvCheckService.getHashParam().get(PayOneConstants.KEY_HASH));
        parameters.put("merchantId", elvCheckService.getMerchantIdParam().get(PayOneConstants.KEY_MERCHANT_ID));
        parameters.put("mode", elvCheckService.getModeParam().get(PayOneConstants.KEY_MODE));
        parameters.put("portalId", elvCheckService.getPortalIdParam().get(PayOneConstants.KEY_PORTAL_ID));
        parameters.put("responseType", elvCheckService.getResponseTypeParam().get(PayOneConstants.KEY_RESPONSE_TYPE));

        // bankaccount check params
        parameters.put("subaccountId", elvCheckService.getSubAccountIdParam().get(PayOneConstants.KEY_SUBACCOUNT_ID));
        parameters
                .put("checkType", elvCheckService.getCheckTypeParam().get(PayOneConstants.KEY_BANKACCOUNT_CHECK_TYPE));
        parameters.put("bankAccountFieldId", bankAccountFieldId);
        // not needed for bankaccountcheck:
        // parameters.put("bankAccountHolderFieldId", bankAccountHolderFieldId);
        parameters.put("bankCodeFieldId", bankCodeFieldId);
        // not needed yet: germany is fix. parameters.put("bankCountryFieldId",
        // bankCountryFieldId);
        parameters.put("bankCountryValue", ELVCountry.DE.name());
        parameters.put("language", userModel.getObject().getUserAccount().getPreferredLanguage().getLocale()
                .getLanguage());

        parameters.put("request", elvCheckService.getRequestParam().get(PayOneConstants.KEY_REQUEST));

        String jsCode = new JavaScriptTemplate(jsTemplate).asString(parameters);
        return jsCode;
    }
}
