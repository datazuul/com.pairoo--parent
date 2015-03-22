package com.pairoo.backend.db.flyway;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.datazuul.framework.domain.UUIDGenerator;
import com.googlecode.flyway.core.api.migration.spring.SpringJdbcMigration;

/**
 * In the old schema UUID column was missing. In the previous migration step
 * this column was added to all tables. This class adds now UUIDs for all
 * existing entries in all tables.
 * 
 * @author Ralf Eichinger
 */
public class V20121005_02__DML_Add_UUID implements SpringJdbcMigration {
    private static final Logger LOGGER = LoggerFactory
	    .getLogger(V20121005_02__DML_Add_UUID.class);
    private JdbcTemplate jdbcTemplate;

    @Override
    public void migrate(final JdbcTemplate jdbcTemplate) throws Exception {
	this.jdbcTemplate = jdbcTemplate;

	addUuidToTable("ADDRESS");
	addUuidToTable("APPEARANCE");
	addUuidToTable("BLOCKEDUSER");
	addUuidToTable("CONTACTEVENT");
	addUuidToTable("FAVORITE");
	addUuidToTable("GEOAREA");
	addUuidToTable("GEOLOCATION");
	addUuidToTable("IMAGEENTRY");
	addUuidToTable("LANDINGPAGEACTION");
	addUuidToTable("LIFESTYLE");
	addUuidToTable("MEMBERSHIP");
	addUuidToTable("MESSAGE");
	addUuidToTable("NOTIFICATIONSETTINGS");
	addUuidToTable("PAYMENTCHANNELS");
	addUuidToTable("PERSONALVALUES");
	addUuidToTable("PRODUCT");
	addUuidToTable("PROMOTION");
	addUuidToTable("SEARCHPROFILE");
	addUuidToTable("SUBDIVISION");
	addUuidToTable("TRANSACTION");
	addUuidToTable("USERS");
	addUuidToTable("USERACCOUNT");
	addUuidToTable("USERPROFILE");
	addUuidToTable("VISIT");
    }

    private void addUuidToTable(String tableName) {
	LOGGER.info("adding UUID to {}", tableName);
	final List<Long> ids = this.jdbcTemplate.queryForList("select ID from "
		+ tableName, Long.class);
	for (Long id : ids) {
	    final Object[] params = new Object[] { UUIDGenerator.createId(), id };
	    LOGGER.info("update " + tableName + " set UUID={} where ID={}",
		    params);
	    this.jdbcTemplate.update("update " + tableName
		    + " set UUID=? where ID=?", params);
	}
    }
}
