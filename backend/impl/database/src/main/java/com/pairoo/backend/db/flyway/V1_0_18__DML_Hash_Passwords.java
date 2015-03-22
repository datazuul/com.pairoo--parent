package com.pairoo.backend.db.flyway;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.datazuul.framework.security.HashResult;
import com.datazuul.framework.security.HashUtil;
import com.googlecode.flyway.core.api.migration.spring.SpringJdbcMigration;
import com.pairoo.domain.UserAccount;

/**
 * In the old schema UserAccount stored (only during development phase)
 * passwords in cleartext. Now they start to be hashed with salt. Migrate
 * cleartext passwords to salted hashes.
 * 
 * @author Ralf Eichinger
 */
public class V1_0_18__DML_Hash_Passwords implements SpringJdbcMigration {
    private static final Logger LOGGER = LoggerFactory.getLogger(V1_0_18__DML_Hash_Passwords.class);
    private JdbcTemplate jdbcTemplate;

    @Override
    public void migrate(final JdbcTemplate jdbcTemplate) throws Exception {
	this.jdbcTemplate = jdbcTemplate;

	LOGGER.info("select ID, PASSWORD from USERACCOUNT");
	final List<UserAccount> userAccounts = this.jdbcTemplate.query("select ID, PASSWORD from USERACCOUNT",
		new RowMapper() {
		    @Override
		    public Object mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			final Long id = rs.getLong("ID");
			final String password = rs.getString("PASSWORD");
			// copy data to a UserAccount object
			final UserAccount userAccount = new UserAccount();
			userAccount.setId(id);
			userAccount.setPassword(password);
			return userAccount;
		    }
		});
	for (final UserAccount userAccount : userAccounts) {
	    String password = userAccount.getPassword();
	    HashResult hashResult = HashUtil.hash(password);
	    userAccount.setPassword(hashResult.getDigest());
	    userAccount.setPasswordSalt(hashResult.getSalt());

	    final Object[] params = new Object[] { userAccount.getPassword(), userAccount.getPasswordSalt(),
		    userAccount.getId() };
	    LOGGER.info("update USERACCOUNT set PASSWORD={}, PASSWORD_SALT={} where ID={}", params);
	    this.jdbcTemplate.update("update USERACCOUNT set PASSWORD=?, PASSWORD_SALT=? where ID=?", params);
	}
    }
}
