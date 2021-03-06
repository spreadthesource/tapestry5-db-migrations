package com.spreadthesource.tapestry.dbmigration.migrations;

/**
 * Use this type of context to add constraint on a given table.
 * 
 * @author ccordenier
 */
public interface Constraint extends MigrationContext
{
    /**
     * Set the name of the table to create on which the constraint is applied.
     * 
     * @param tableName
     */
    void setTableName(String tableName);

    /**
     * Set a unique tuple.
     * 
     * @param name
     * @param columns
     */
    void setUnique(String name, String... columns);

    /**
     * Create foreign keys.
     * 
     * @param name
     * @param foreignTable
     * @param from
     *            and to columns pairs
     */
    void setForeignKey(String name, String foreignTable, String... fromToColumns);
}
