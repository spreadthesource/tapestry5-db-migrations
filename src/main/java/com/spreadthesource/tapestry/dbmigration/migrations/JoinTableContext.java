package com.spreadthesource.tapestry.dbmigration.migrations;

public interface JoinTableContext extends MigrationContext
{

    /**
     * Create a join table with the default primary keys.
     * 
     * @param name The join table name
     * @param tableOne
     * @param tableTwo
     */
    void join(String name, String tableOne, String tableTwo);
    
    /**
     * Create a join table with the default primary keys.
     * 
     * @param tableOne
     * @param tableTwo
     */
    void join(String tableOne, String tableTwo);

    /**
     * Create a join table with the specified primary keys and table names.
     * 
     * @param tableOne
     * @param pkOne
     * @param tableTwo
     * @param pkTwo
     */
    void join(String tableOne, String pkOne, String tableTwo, String pkTwo);
}
