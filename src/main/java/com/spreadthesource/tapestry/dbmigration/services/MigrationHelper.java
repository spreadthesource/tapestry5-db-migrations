package com.spreadthesource.tapestry.dbmigration.services;

import org.hibernate.dialect.Dialect;
import org.hibernate.jdbc.util.Formatter;

import com.spreadthesource.tapestry.dbmigration.data.Table;
import com.spreadthesource.tapestry.dbmigration.hibernate.ConnectionHelper;

public interface MigrationHelper
{
    public Dialect getDialect();
    
    public void createTable(String tableName);

    public <T> void addColumn(String tableName, String columnName, T columnType);

    public void dropTable(String tableName);
    
    public String getDefaultSchema();
    
    public String getDefaultCatalog();
    
    public ConnectionHelper getConnectionHelper();
    
    public Formatter getFormatter();
    
    public boolean checkIfTableExists(String tableName);
    
    public String createTable(Table table);

}
