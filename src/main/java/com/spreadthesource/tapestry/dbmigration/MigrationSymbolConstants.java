package com.spreadthesource.tapestry.dbmigration;

public interface MigrationSymbolConstants
{
    /** Do the migration or make a dry run */
    public static final String DO_UPDATE = "spreadthesource.dbmigration.doupdate";

    /** Stop or continue migration on error */
    public static final String HALT_ON_ERROR = "spreadthesource.dbmigration.halt-on-error";

    /** Use this symbol to set the name of the versionning table */
    public static final String VERSIONING_TABLE_NAME = "spreadthesource.dbmigration.versioning_table_name";

    /** Set this to false to disable default hibernate configuration */
    public static final String DEFAULT_HIBERNATE_CONFIGURATION = "spreadthesource.dbmigration.default_hibernate_configuration";
}
