package com.pairoo.backend.db.flyway;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.googlecode.flyway.core.api.migration.spring.SpringJdbcMigration;
import com.pairoo.domain.Product;
import com.pairoo.domain.enums.ProductDurationType;
import com.pairoo.domain.enums.Role;

/**
 * Add products to begin with.
 * 
 * @author Ralf Eichinger
 */
public class V1_0_24__DML_Products implements SpringJdbcMigration {
    private static final Logger LOGGER = LoggerFactory.getLogger(V1_0_24__DML_Products.class);
    private JdbcTemplate jdbcTemplate;

    @Override
    public void migrate(final JdbcTemplate jdbcTemplate) throws Exception {
	this.jdbcTemplate = jdbcTemplate;

	Calendar cal = Calendar.getInstance();
	cal.set(2012, Calendar.AUGUST, 1);

	List<Product> products = new ArrayList<Product>();

	Product standard = new Product();
	standard.setAbo(false);
	standard.setDuration(null);
	standard.setEndDate(null);
	standard.setId(new Long(1));
	standard.setMonthlyRate(null);
	standard.setRole(Role.STANDARD);
	standard.setStartDate(new Timestamp(cal.getTime().getTime()));
	products.add(standard);

	Product premium1Month = new Product();
	premium1Month.setAbo(false);
	premium1Month.setDuration(ProductDurationType.ONE_MONTH);
	premium1Month.setId(new Long(2));
	premium1Month.setMonthlyRate(new Float(35.70));
	premium1Month.setRole(Role.PREMIUM);
	premium1Month.setStartDate(new Timestamp(cal.getTime().getTime()));
	products.add(premium1Month);

	Product premium3Months = new Product();
	premium3Months.setAbo(true);
	premium3Months.setDuration(ProductDurationType.THREE_MONTHS);
	premium3Months.setId(new Long(3));
	premium3Months.setMonthlyRate(new Float(24.90));
	premium3Months.setRole(Role.PREMIUM);
	premium3Months.setStartDate(new Timestamp(cal.getTime().getTime()));
	products.add(premium3Months);

	Product premium6Months = new Product();
	premium6Months.setAbo(true);
	premium6Months.setDuration(ProductDurationType.SIX_MONTHS);
	premium6Months.setId(new Long(4));
	premium6Months.setMonthlyRate(new Float(14.90));
	premium6Months.setRole(Role.PREMIUM);
	premium6Months.setStartDate(new Timestamp(cal.getTime().getTime()));
	products.add(premium6Months);

	Product premium12Months = new Product();
	premium12Months.setAbo(true);
	premium12Months.setDuration(ProductDurationType.TWELVE_MONTHS);
	premium12Months.setId(new Long(5));
	premium12Months.setMonthlyRate(new Float(11.90));
	premium12Months.setRole(Role.PREMIUM);
	premium12Months.setStartDate(new Timestamp(cal.getTime().getTime()));
	products.add(premium12Months);

	for (Product product : products) {
	    String duration = null;
	    if (product.getDuration() != null) {
		duration = product.getDuration().name();
	    }
	    final Object[] params = new Object[] { product.getId(), product.getStartDate(), duration,
		    product.getMonthlyRate(), product.isAbo(), product.getRole().name() };
	    this.jdbcTemplate.update(
		    "insert into PRODUCT (ID, STARTDATE, DURATION, MONTHLYRATE, ABO, ROLE) values (?,?,?,?,?,?)",
		    params);
	    LOGGER.info(
		    "insert into PRODUCT (ID, STARTDATE, DURATION, MONTHLYRATE, ABO, ROLE) values ({}, {}, {}, {}, {}, {})",
		    params);
	}
    }
}
