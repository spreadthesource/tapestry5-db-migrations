package com.spreadthesource.tapestry.dbmigration.services;

import org.hibernate.cfg.Mappings;
import org.hibernate.dialect.Dialect;
import org.hibernate.jdbc.util.Formatter;

import com.spreadthesource.tapestry.dbmigration.hibernate.ConnectionHelper;

/**
 * Database source to get access to the database to populate.
 *
 * @author ccordenier
 */
public interface DbSource
{

    ConnectionHelper getConnectionHelper();

    String getDefaultCatalog();

    String getDefaultSchema();

    Dialect getDialect();

    Formatter getFormatter();

    Mappings getMappings();
}
