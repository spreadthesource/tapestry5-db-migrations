package com.spreadthesource.tapestry.dbmigration.migrations;

import org.hibernate.dialect.Dialect;

/**
 * This context will be used for adding custom SQL queries. 
 *
 * @author ccordenier
 *
 */
public interface SqlContext extends MigrationContext
{

    /**
     * Call this method to add a query that will always be executed.
     *
     * @param query
     */
    void addSql(String query);

    /**
     * Call this method to add a query for only one type of database.
     * @param <T>
     * @param query
     * @param dialect
     */
    <T extends Dialect> void addSql(String query, Class<T> dialect);
    
}
