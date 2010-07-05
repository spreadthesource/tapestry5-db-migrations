package com.spreadthesource.tapestry.dbmigration.services;

import java.util.List;

import com.spreadthesource.tapestry.dbmigration.migrations.MigrationCommand;

public interface MigrationHelper
{
    /**
     * Check if the table exists.
     * 
     * @param tableName
     * @return
     */
    boolean checkIfTableExists(String tableName);

    /**
     * Generic method to add a Command that will generate SQL queries.
     * 
     * @param <T>
     * @param command
     */
    @SuppressWarnings("unchecked")
    void add(MigrationCommand command);

    /**
     * Get all the SQL queries for the current migration unit.
     * 
     * @return
     */
    List<String> getPendingSql();

    /**
     * Clear all the current pending sql queries.
     */
    void reset();

}
