package com.pairoo.frontend.webapp.wicket.pages.authenticated;

import javax.servlet.ServletException;

import org.apache.wicket.Page;
import org.apache.wicket.model.Model;
import org.junit.Before;
import org.junit.Test;

import com.datazuul.framework.domain.Language;
import com.datazuul.framework.domain.authorization.Roles;
import com.datazuul.framework.domain.geo.Country;
import com.datazuul.framework.domain.geo.GeoLocation;
import com.pairoo.business.api.ProductService;
import com.pairoo.business.services.impl.ProductServiceImpl;
import com.pairoo.domain.Membership;
import com.pairoo.domain.Product;
import com.pairoo.domain.User;
import com.pairoo.domain.UserAccount;
import com.pairoo.domain.enums.ProductDurationType;
import com.pairoo.domain.enums.Role;
import com.pairoo.domain.payment.PaymentChannel;
import com.pairoo.domain.payment.Transaction;
import com.pairoo.domain.payment.enums.PaymentChannelType;
import com.pairoo.frontend.webapp.wicket.AbstractWicketTest;
import com.pairoo.frontend.webapp.wicket.WicketWebSession;

/**
 * @author Ralf Eichinger
 */
public class PaymentAuthorizationPageTest extends AbstractWicketTest {

    private User mockUser;
    final Membership mockMembership = new Membership();

    @Before
    @Override
    public void before() throws Exception {
	super.before();

	// 1. setup dependencies and mock objects
	// mock objects
	mockUser = new User();
	GeoLocation geoLocation = new GeoLocation();
	geoLocation.setCountry(Country.GERMANY);
	mockUser.getUserProfile().setGeoLocation(geoLocation);

	Product product = new Product();
	mockMembership.setProduct(product);
	product.setDuration(ProductDurationType.SIX_MONTHS);
	product.setMonthlyRate((float) 15.0);

	Transaction paymentTransaction = new Transaction();
	mockMembership.setPaymentTransaction(paymentTransaction);

	PaymentChannel paymentChannel = new PaymentChannel();
	paymentTransaction.setPaymentChannel(paymentChannel);
	paymentChannel.setPaymentChannelType(PaymentChannelType.VISA);

	// mock session
	final WicketWebSession session = ((WicketWebSession) getTester().getSession());
	session.setUser(mockUser);
	UserAccount userAccount = new UserAccount();
	userAccount.setPreferredLanguage(Language.ENGLISH);
	userAccount.setRoles(new Roles(Role.PREMIUM.getCode()));
	mockUser.setUserAccount(userAccount);

	// 2. setup mock injection environment
	// non mock (standalone) services
	getAppContext().putBean(ProductService.BEAN_ID, new ProductServiceImpl(null));
    }

    @Test
    public void testRendering() throws ServletException {
	final Page page = new PaymentAuthorizationPage(new Model<Membership>(mockMembership), new Model<User>(mockUser));
	getTester().startPage(page);
	getTester().assertRenderedPage(PaymentAuthorizationPage.class);
	// getTester().dumpPage();
    }
}
