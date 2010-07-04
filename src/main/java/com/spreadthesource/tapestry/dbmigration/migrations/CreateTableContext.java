package com.spreadthesource.tapestry.dbmigration.migrations;

import com.spreadthesource.tapestry.dbmigration.ColumnDef;

/**
 * This context will allow you to create table and its column definition.
 * 
 * @author ccordenier
 */
public interface CreateTableContext extends MigrationContext
{

    void setName(String name);

    ColumnDef addString(String name);

    ColumnDef addText(String name);

    ColumnDef addBoolean(String name);

    ColumnDef addLong(String name);

    ColumnDef addInteger(String name);

    ColumnDef addTimestamp(String name);

}
