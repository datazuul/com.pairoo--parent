package com.pairoo.frontend.webapp.wicket.pages.authenticated;

import java.util.Date;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import com.datazuul.framework.webapp.wicket.components.form.ShinyForm;
import com.pairoo.business.api.marketing.PromotionService;
import com.pairoo.business.api.payment.VoucherPaymentService;
import com.pairoo.domain.Membership;
import com.pairoo.domain.User;
import com.pairoo.domain.UserAccount;
import com.pairoo.domain.marketing.Promotion;
import com.pairoo.domain.marketing.enums.PromotionType;
import com.pairoo.domain.payment.PaymentChannel;
import com.pairoo.domain.payment.Transaction;
import com.pairoo.domain.payment.VoucherPayment;
import com.pairoo.domain.payment.VoucherPaymentDuplicateException;

/**
 * @author Ralf Eichinger
 */
public class VoucherInputPage extends AuthenticatedWebPage {

    private static final long serialVersionUID = 1L;
    @SpringBean(name = PromotionService.BEAN_ID)
    private PromotionService promotionService;
    @SpringBean(name = VoucherPaymentService.BEAN_ID)
    private VoucherPaymentService voucherPaymentService;

    public VoucherInputPage(final IModel<User> model) {
	super(model);
    }

    @Override
    protected void onBeforeRender() {
	super.onBeforeRender();
	logEnter(VoucherInputPage.class);
    }

    @Override
    protected void onInitialize() {
	super.onInitialize();

	final Membership membership = new Membership();
	final Transaction paymentTransaction = new Transaction();
	final PaymentChannel paymentChannel = new VoucherPayment();
	membership.setPaymentTransaction(paymentTransaction);
	paymentTransaction.setPaymentChannel(paymentChannel);

	final ShinyForm form = createForm("form", new CompoundPropertyModel<Membership>(membership));
	add(form);

	final FormComponent<String> voucherCodeFC = createVoucherCodeFC("paymentTransaction.paymentChannel.promotionCode");
	form.add(voucherCodeFC);
	form.add(createFormComponentLabel("voucherCodeLabel", voucherCodeFC));
    }

    private FormComponent<String> createVoucherCodeFC(final String id) {
	// ... field
	final FormComponent<String> fc = new RequiredTextField<String>(id);
	fc.setLabel(new ResourceModel("voucher_code"));
	fc.add(new AttributeModifier("maxLength", "255"));
	fc.add(new AttributeModifier("size", "30"));
	// ... validation
	fc.add(StringValidator.maximumLength(255));
	return fc;
    }

    private ShinyForm createForm(final String id, final IModel<Membership> model) {
	final ShinyForm form = new ShinyForm(id, model) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    protected void onSubmit() {
		final Membership membership = (Membership) getDefaultModelObject();
		VoucherPayment voucher = (VoucherPayment) membership.getPaymentTransaction().getPaymentChannel();
		final String promotionCode = voucher.getPromotionCode();

		Promotion promotion = promotionService.getPromotionByCode(promotionCode);

		// TODO all these checks could go to business layer...
		// but then exception handling has to be introduced...
		if (promotion == null || (!PromotionType.VOUCHER.equals(promotion.getPromotionType()))) {
		    error(getString("error.invalid_voucher_code"));
		    return;
		} else if ((new Date()).after(promotion.getValidTo())) {
		    error(getString("error.voucher_expired"));
		    return;
		} else {
		    if (promotion.getUserAccount() != null) {
			// individual voucher (limited to be used by one user
			// account)
			UserAccount userAccount = ((User) getPage().getDefaultModelObject()).getUserAccount();
			if (promotion.getUserAccount() != userAccount) {
			    error(getString("error.invalid_voucher_code"));
			    // do not show "this voucher is not for you..."
			    // to avoid random code attacks
			    return;
			} else if (promotion.isUsed()) {
			    error(getString("error.voucher_already_encashed"));
			    return;
			}
		    }
		}
		final User user = (User) getPage().getDefaultModelObject();
		// use the session user to get membership upgrade immediately
		try {
		    // FIXME: ERROR: duplicate key value violates unique
		    // constraint "paymentchannels_useraccount_id_startdate_key"
		    // Detail: Key (useraccount_id, startdate)=(1, 2012-09-20
		    // 00:00:00) already exists.
		    // insert into PAYMENTCHANNELS
		    // (VERSION_ID, UUID,
		    // PAYMENTCHANNELTYPE, USERACCOUNT_ID, STARTDATE, ENDDATE,
		    // ID) values ('0', 'f78b7f0a-a7a3-4033-b5dd-752e74b9e0da',
		    // 'VOUCHER', '1', '2012-09-20 00:00:00.000000 +01:00:00',
		    // '2012-12-31 23:59:59.000000 +00:00:00', '52') was aborted
		    voucherPaymentService.encashVoucher(user.getUserAccount(), promotion);
		} catch (VoucherPaymentDuplicateException e) {
		    error(getString("error.voucher_already_encashed"));
		    return;
		} catch (Exception e) {
		    final String errmsg = getString("error.voucher_already_encashed");
		    error(errmsg);
		    return;
		}
		info(getString("voucher_successfully_encashed"));
	    }
	};
	return form;
    }
}
