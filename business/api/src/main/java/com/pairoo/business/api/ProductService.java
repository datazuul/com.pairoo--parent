package com.pairoo.business.api;

import java.util.List;

import javax.measure.unit.Unit;

import org.jscience.economics.money.Money;

import com.datazuul.framework.business.services.DomainObjectService;
import com.datazuul.framework.domain.Language;
import com.pairoo.domain.Product;
import com.pairoo.domain.enums.Role;

public interface ProductService extends DomainObjectService<Long, Product> {
    public static final String BEAN_ID = "productService";

    public List<Product> getAllProducts(Role premium);

    public Product getDefaultProduct();

    public String getMonthlyRateAsString(final Product product, final Unit<Money> targetCurrency,
	    final Language targetLanguage);

    public String getMonthlyRateAsString(final Product product);

    public int getTotalAmountInCent(final Product product, final Unit<Money> targetCurrency);

    String getTotalAmountAsString(final Product product, final Unit<Money> targetCurrency, final Language targetLanguage);

    public Product getStandardProduct();
}
