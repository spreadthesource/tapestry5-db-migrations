package com.spreadthesource.tapestry.dbmigration.migrations;

import com.spreadthesource.tapestry.dbmigration.ColumnDef;

/**
 * This context will allow you to create table and its column definition.
 * 
 * @author ccordenier
 */
public interface Table extends MigrationContext
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

    /**
     * Add a non-type column, type will have to be set manually on the column def.
     *
     * @param name
     * @return
     */
    ColumnDef add(String name);

}
