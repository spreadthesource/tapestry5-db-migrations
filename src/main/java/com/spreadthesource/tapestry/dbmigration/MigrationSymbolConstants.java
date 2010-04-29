package com.spreadthesource.tapestry.dbmigration;

public interface MigrationSymbolConstants
{
    /**
     * Do the migration or make a dry run
     */
    public static final String DO_UPDATE = "spreadthesource.dbmigration.doupdate";
   
    public static final String HALT_ON_ERROR = "spreadthesource.dbmigration.halt-on-error";
    
    public static final String VERSIONING_TABLE_NAME = "spreadthesource.dbmigration.versioning_table_name";
}
