package com.spreadthesource.tapestry.dbmigration.migrations;

public interface DropContext extends MigrationContext
{
    /**
     * This will add a sql query to drop the given table.
     */
    void dropTable(String table);

    /**
     * Drop foreign key constraint.
     * 
     * @param tableName
     *            The name of table in which the constaint has been created.
     * @param fkName
     *            The name of the constraint.
     */
    void dropForeignKey(String tableName, String fkName);
}
