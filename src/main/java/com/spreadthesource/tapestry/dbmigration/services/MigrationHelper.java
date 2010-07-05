package com.spreadthesource.tapestry.dbmigration.services;

import java.util.List;

import org.hibernate.dialect.Dialect;
import org.hibernate.jdbc.util.Formatter;

import com.spreadthesource.tapestry.dbmigration.hibernate.ConnectionHelper;
import com.spreadthesource.tapestry.dbmigration.migrations.CreateConstraint;
import com.spreadthesource.tapestry.dbmigration.migrations.CreateTable;
import com.spreadthesource.tapestry.dbmigration.migrations.Drop;
import com.spreadthesource.tapestry.dbmigration.migrations.JoinTable;
import com.spreadthesource.tapestry.dbmigration.migrations.UpdateTable;

public interface MigrationHelper
{
    Dialect getDialect();

    String getDefaultSchema();

    String getDefaultCatalog();

    ConnectionHelper getConnectionHelper();

    Formatter getFormatter();

    boolean checkIfTableExists(String tableName);

    /**
     * Call this method to your create table commands.
     * 
     * @param command
     */
    void createTable(CreateTable command);

    /**
     * Call this method to update a table definition.
     * 
     * @param command
     */
    void updateTable(UpdateTable command);

    /**
     * Call this method to add constraints on your schema.
     * 
     * @param constraint
     */
    void createConstraint(CreateConstraint constraint);

    /**
     * Call this method to create drop SQL strings.
     * 
     * @param dropCtx
     */
    void drop(Drop command);

    /**
     * Call this method to join tables.
     * 
     * @param command
     */
    void join(JoinTable command);

    /**
     * Get all the SQL queries for the current migration unit.
     * 
     * @return
     */
    List<String> getPendingSql();

    /**
     * Remove all the current pending sql queries.
     */
    void reset();

}
