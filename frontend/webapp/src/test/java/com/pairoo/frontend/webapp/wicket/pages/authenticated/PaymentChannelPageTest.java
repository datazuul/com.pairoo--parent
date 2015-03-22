package com.pairoo.frontend.webapp.wicket.pages.authenticated;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import javax.servlet.ServletException;

import org.apache.wicket.Page;
import org.apache.wicket.model.Model;
import org.junit.Before;
import org.junit.Test;

import com.datazuul.framework.domain.Language;
import com.datazuul.framework.domain.authorization.Roles;
import com.pairoo.business.api.CountryService;
import com.pairoo.business.api.payment.PaymentChannelService;
import com.pairoo.business.api.payment.payone.CreditCardCheckService;
import com.pairoo.business.services.impl.CountryServiceImpl;
import com.pairoo.business.services.impl.payment.PaymentChannelServiceImpl;
import com.pairoo.domain.Membership;
import com.pairoo.domain.Product;
import com.pairoo.domain.User;
import com.pairoo.domain.UserAccount;
import com.pairoo.domain.enums.ProductDurationType;
import com.pairoo.domain.enums.Role;
import com.pairoo.domain.payment.enums.PaymentChannelType;
import com.pairoo.domain.payment.payone.PayOneConstants;
import com.pairoo.frontend.webapp.wicket.AbstractWicketTest;
import com.pairoo.frontend.webapp.wicket.WicketWebSession;

/**
 * @author Ralf Eichinger
 */
public class PaymentChannelPageTest extends AbstractWicketTest {

    final User mockUser = new User();
    final Membership mockMembership = new Membership();

    @Before
    @Override
    public void before() throws Exception {
	super.before();

	// 1. setup dependencies and mock objects
	// mock objects
	Product product = new Product();
	product.setDuration(ProductDurationType.SIX_MONTHS);
	mockMembership.setProduct(product);

	// 2. setup mock injection environment
	// mock services
	final CreditCardCheckService creditCardCheckService = mock(CreditCardCheckService.class);

	HashMap<String, String> encodingParam = new HashMap<String, String>();
	encodingParam.put(PayOneConstants.KEY_ENCODING, PayOneConstants.ENCODING_UTF8);
	when(creditCardCheckService.getEncodingParam()).thenReturn(encodingParam);

	HashMap<String, String> hashParam = new HashMap<String, String>();
	hashParam.put(PayOneConstants.KEY_HASH, "32f74f269f21132bd93234325a574a0d");
	when(creditCardCheckService.getHashParam()).thenReturn(hashParam);

	HashMap<String, String> merchantIdParam = new HashMap<String, String>();
	merchantIdParam.put(PayOneConstants.KEY_MERCHANT_ID, "19360");
	when(creditCardCheckService.getMerchantIdParam()).thenReturn(merchantIdParam);

	HashMap<String, String> modeParam = new HashMap<String, String>();
	modeParam.put(PayOneConstants.KEY_MODE, PayOneConstants.MODE_TEST);
	when(creditCardCheckService.getModeParam()).thenReturn(modeParam);

	HashMap<String, String> portalIdParam = new HashMap<String, String>();
	portalIdParam.put(PayOneConstants.KEY_PORTAL_ID, "2013939");
	when(creditCardCheckService.getPortalIdParam()).thenReturn(portalIdParam);

	HashMap<String, String> responseTypeParam = new HashMap<String, String>();
	responseTypeParam.put(PayOneConstants.KEY_RESPONSE_TYPE, PayOneConstants.RESPONSE_TYPE_JSON);
	when(creditCardCheckService.getResponseTypeParam()).thenReturn(responseTypeParam);

	HashMap<String, String> subAccountIdParam = new HashMap<String, String>();
	subAccountIdParam.put(PayOneConstants.KEY_SUBACCOUNT_ID, "19426");
	when(creditCardCheckService.getSubAccountIdParam()).thenReturn(subAccountIdParam);

	HashMap<String, String> cardTypeParam = new HashMap<String, String>();
	cardTypeParam.put(PayOneConstants.KEY_CARD_TYPE, PayOneConstants.CARD_TYPE_VISA);
	when(creditCardCheckService.getCardTypeParam(PaymentChannelType.VISA)).thenReturn(cardTypeParam);

	HashMap<String, String> requestParam = new HashMap<String, String>();
	requestParam.put(PayOneConstants.KEY_REQUEST, PayOneConstants.REQUEST_CREDITCARD_CHECK);
	when(creditCardCheckService.getRequestParam()).thenReturn(requestParam);

	HashMap<String, String> storeCardDataParam = new HashMap<String, String>();
	storeCardDataParam.put(PayOneConstants.KEY_STORE_CARDDATA, PayOneConstants.STORE_CARDDATA_TRUE);
	when(creditCardCheckService.getStoreCardDataParam()).thenReturn(storeCardDataParam);

	// mock session
	final WicketWebSession session = ((WicketWebSession) getTester().getSession());
	session.setUser(mockUser);
	UserAccount userAccount = new UserAccount();
	userAccount.setPreferredLanguage(Language.ENGLISH);
	mockUser.setUserAccount(userAccount);

	userAccount.setRoles(new Roles(Role.PREMIUM.getCode()));

	getAppContext().putBean(CreditCardCheckService.BEAN_ID, creditCardCheckService);

	// non mock (standalone) services
	getAppContext().putBean(CountryService.BEAN_ID, new CountryServiceImpl());
	getAppContext().putBean(PaymentChannelService.BEAN_ID, new PaymentChannelServiceImpl(null));
    }

    @Test
    public void testRendering() throws ServletException {
	final Page page = new PaymentChannelPage(new Model<Membership>(mockMembership), new Model<User>(mockUser));
	getTester().startPage(page);
	getTester().assertRenderedPage(PaymentChannelPage.class);
	getTester().dumpPage();

	// getTester().submitForm("form");
    }
}
