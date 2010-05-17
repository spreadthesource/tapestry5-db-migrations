package com.spreadthesource.tapestry.dbmigration.services;

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
     * Create a Table , add the Table creation SQL to pending SQL and also return SQL
     * 
     * @param table
     * @return creation SQL
     */
    public String createTable(Table table);

    /**
     * Drop a Table by adding SQL drop code to pending SQL and also return it
     * 
     * @param tableName
     * @return drop table SQL
     */
    public String dropTable(String tableName);

    public boolean checkIfTableExists(String tableName);

    /**
     * Return pending SQL commands and clear the stack
     */
    public String[] getPendingSQL();

}
