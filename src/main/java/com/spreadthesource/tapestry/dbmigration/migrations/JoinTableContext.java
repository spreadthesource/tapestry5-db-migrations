package com.spreadthesource.tapestry.dbmigration.migrations;

public interface JoinTableContext extends MigrationContext
{

    /**
     * Create a join table with the default
     * 
     * @param tableOne
     * @param tableTwo
     */
    void join(String tableOne, String tableTwo);

}
