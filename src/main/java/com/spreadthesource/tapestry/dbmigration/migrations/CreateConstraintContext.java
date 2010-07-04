package com.spreadthesource.tapestry.dbmigration.migrations;

/**
 * Use this type of context to add constraint on a given table.
 * 
 * @author ccordenier
 */
public interface CreateConstraintContext extends MigrationContext
{
    void setName(String tableName);

    void setUnique(String name, String... columns);

    void setForeignKey(String name, String foreignTable, String[] fromColumns, String[] toColumns);
}
