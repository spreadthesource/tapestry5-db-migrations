package com.spreadthesource.tapestry.dbmigration.services;

import java.util.List;

import org.hibernate.dialect.Dialect;
import org.hibernate.jdbc.util.Formatter;

import com.spreadthesource.tapestry.dbmigration.data.Table;
import com.spreadthesource.tapestry.dbmigration.hibernate.ConnectionHelper;

public interface MigrationHelper
{
    public Dialect getDialect();

    public String getDefaultSchema();

    public String getDefaultCatalog();

    public ConnectionHelper getConnectionHelper();

    public Formatter getFormatter();

    /**
     * Generates SQL to create a table 
     * 
     * @param table
     * @return creation SQL
     */
    public List<String> createTable(Table table);

    /**
     * Generates SQL to do drop Table query
     * 
     * @param tableName
     * @return drop table SQL
     */
    public String dropTable(String tableName);

    public boolean checkIfTableExists(String tableName);
}
