package com.spreadthesource.tapestry.dbmigration.migrations;

import com.spreadthesource.tapestry.dbmigration.ColumnDef;

public interface TableContext extends MigrationContext
{

    /**
     * Set the name of the table to create or update.
     * 
     * @param name
     */
    void setName(String name);

    ColumnDef addString(String name);

    ColumnDef addText(String name);

    ColumnDef addBoolean(String name);

    ColumnDef addLong(String name);

    ColumnDef addInteger(String name);

    ColumnDef addTimestamp(String name);

    ColumnDef addDate(String name);

}
