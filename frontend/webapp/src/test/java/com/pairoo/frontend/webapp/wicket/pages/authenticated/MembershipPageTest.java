package com.pairoo.frontend.webapp.wicket.pages.authenticated;

import com.datazuul.framework.domain.Language;
import com.pairoo.business.api.ProductService;
import com.pairoo.domain.Product;
import com.pairoo.domain.User;
import com.pairoo.domain.enums.ProductDurationType;
import com.pairoo.domain.enums.Role;
import com.pairoo.frontend.webapp.wicket.AbstractWicketTest;
import com.pairoo.frontend.webapp.wicket.WicketWebSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import org.apache.wicket.Page;
import org.apache.wicket.model.Model;
import org.jscience.economics.money.Currency;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 * @author Ralf Eichinger
 */
public class MembershipPageTest extends AbstractWicketTest {

    final User mockUser = new User();

    @Before
    @Override
    public void before() throws Exception {
        super.before();

        final ProductService productService = mock(ProductService.class);
        Product defaultProduct = new Product();
        defaultProduct.setAbo(true);
        defaultProduct.setDuration(ProductDurationType.SIX_MONTHS);
        defaultProduct.setMonthlyRate(11.30f);
        defaultProduct.setRole(Role.PREMIUM);
        defaultProduct.setStartDate(new Date());
        when(productService.getDefaultProduct()).thenReturn(defaultProduct);

        List<Product> allProducts = new ArrayList<Product>();
        allProducts.add(defaultProduct);
        when(productService.getAllProducts(Role.PREMIUM)).thenReturn(allProducts);

        when(productService.getMonthlyRateAsString(defaultProduct, Currency.EUR, Language.ENGLISH))
                .thenReturn("11,30");

        // 2. setup mock injection environment
        getAppContext().putBean(ProductService.BEAN_ID, productService);

        WicketWebSession session = (WicketWebSession) getTester().getSession();
        session.setUser(mockUser);
    }

    @Test
    public void testRendering() throws ServletException {
        final Page page = new MembershipPage(new Model<User>(mockUser));
        getTester().startPage(page);
        getTester().assertRenderedPage(MembershipPage.class);
        getTester().dumpPage();
    }
}
