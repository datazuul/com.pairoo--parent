package com.pairoo.business.services.impl;

import java.util.List;

import javax.measure.Measure;
import javax.measure.unit.Unit;

import org.jscience.economics.money.Currency;
import org.jscience.economics.money.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datazuul.framework.business.services.persistence.AbstractDomainObjectServiceImpl;
import com.datazuul.framework.domain.Language;
import com.googlecode.genericdao.search.Search;
import com.pairoo.backend.dao.ProductDao;
import com.pairoo.business.api.ProductService;
import com.pairoo.domain.Product;
import com.pairoo.domain.enums.ProductDurationType;
import com.pairoo.domain.enums.Role;

public class ProductServiceImpl extends AbstractDomainObjectServiceImpl<Long, Product> implements ProductService {
    static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    /**
     * Constructor needed for test handing over a dao.
     * 
     * @param dao
     *            the data access interface
     */
    public ProductServiceImpl(final ProductDao dao) {
	super(dao);
    }

    @Override
    public List<Product> getAllProducts(final Role role) {
	LOGGER.info("get all products with role = {}", role.name());
	final Search search = new Search(Product.class);
	search.addFilterEqual("role", role);
	search.addSortAsc("id");
	return dao.search(search);
    }

    @Override
    public Product getDefaultProduct() {
	LOGGER.debug("get default product with duration type = {}", ProductDurationType.SIX_MONTHS.name());
	final Search search = new Search(Product.class);
	search.addFilterEqual("duration", ProductDurationType.SIX_MONTHS);
	return dao.searchUnique(search);
    }

    @Override
    public Product getStandardProduct() {
	LOGGER.debug("get default product with role type = {}", Role.STANDARD.name());
	final Search search = new Search(Product.class);
	search.addFilterEqual("role", Role.STANDARD);
	return dao.searchUnique(search);
    }

    @Override
    public String getMonthlyRateAsString(final Product product, final Unit<Money> targetCurrency,
	    final Language targetLanguage) {
	final Measure<Float, Money> price = Measure.valueOf(product.getMonthlyRate().floatValue(),
		Currency.getReferenceCurrency());
	final Measure<Float, Money> convertedPrice = price.to(targetCurrency);
	final Float value = convertedPrice.getValue();
	return String.format(targetLanguage.getLocale(), "%.2f", value);
    }

    @Override
    public String getMonthlyRateAsString(final Product product) {
	final Measure<Float, Money> price = Measure.valueOf(product.getMonthlyRate().floatValue(),
		Currency.getReferenceCurrency());
	final Float value = price.getValue();
	return String.format(Language.GERMAN.getLocale(), "%.2f", value);
    }

    @Override
    public int getTotalAmountInCent(final Product product, final Unit<Money> targetCurrency) {
	final float monthlyRateEUR = product.getMonthlyRate();
	final int monthlyRateFactor = product.getDuration().getMonthlyRateFactor();
	final int amountEURCent = (int) (100 * monthlyRateFactor * monthlyRateEUR);
	// in smallest currency unit, e.g. Cent
	final Measure<Integer, Money> amountInEUR = Measure.valueOf(amountEURCent, Currency.getReferenceCurrency());
	final Measure<Integer, Money> amountInPreferredCurrency = amountInEUR.to(targetCurrency);
	return amountInPreferredCurrency.getValue().intValue();
    }

    @Override
    public String getTotalAmountAsString(final Product product, final Unit<Money> targetCurrency,
	    final Language targetLanguage) {
	final int totalAmountInCent = getTotalAmountInCent(product, targetCurrency);
	final float value = (float) totalAmountInCent / 100;
	return String.format(targetLanguage.getLocale(), "%.2f", value);
    }

}
