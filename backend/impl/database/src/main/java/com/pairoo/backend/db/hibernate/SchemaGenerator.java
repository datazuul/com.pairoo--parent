package com.pairoo.backend.db.hibernate;

import java.io.File;
import java.io.IOException;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.springframework.core.io.ClassPathResource;

/**
 * @author ralf
 */
public class SchemaGenerator {

    /**
     * @param args
     */
    public static void main(final String[] args) {
	final SchemaGenerator gen = new SchemaGenerator();
	gen.generate(Dialect.HSQL);
	gen.generate(Dialect.POSTGRESQL);
	// gen.generate(Dialect.MYSQL);
	// gen.generate(Dialect.ORACLE);
    }

    /**
     * Method that actually creates the file.
     * 
     * @param dbDialect
     *            to use
     */
    private void generate(final Dialect dialect) {
	final Configuration cfg = new Configuration();

	// add directory containing the hbm.xml files
	final ClassPathResource cpr = new ClassPathResource(".", SchemaGenerator.class);
	File dir = null;
	try {
	    dir = cpr.getFile();
	} catch (final IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	cfg.addDirectory(dir);

	// add dialect
	cfg.setProperty("hibernate.dialect", dialect.getDialectClass());

	final SchemaExport schemaExport = new SchemaExport(cfg);
	schemaExport.setDelimiter(";");
	schemaExport.setOutputFile("ddl_" + dialect.name().toLowerCase() + ".sql");
	schemaExport.create(true, false);
    }

    /**
     * Holds the classnames of hibernate dialects for easy reference.
     */
    private static enum Dialect {
	ORACLE("org.hibernate.dialect.Oracle10gDialect"), POSTGRESQL("org.hibernate.dialect.PostgreSQLDialect"), MYSQL(
		"org.hibernate.dialect.MySQLDialect"), HSQL("org.hibernate.dialect.HSQLDialect");

	private final String dialectClass;

	private Dialect(final String dialectClass) {
	    this.dialectClass = dialectClass;
	}

	public String getDialectClass() {
	    return dialectClass;
	}
    }
}
