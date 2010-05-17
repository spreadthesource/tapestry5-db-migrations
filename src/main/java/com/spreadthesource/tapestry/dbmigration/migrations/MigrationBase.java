package com.spreadthesource.tapestry.dbmigration.migrations;

import com.spreadthesource.tapestry.dbmigration.data.Table;
import com.spreadthesource.tapestry.dbmigration.services.MigrationHelper;

public abstract class MigrationBase implements Migration
{
    private MigrationHelper helper;

    public MigrationBase(MigrationHelper helper)
    {
        this.helper = helper;
    }

    public void createTable(Table table)
    {
        helper.createTable(table);
    }

    public void dropTable(String tableName)
    {
        helper.dropTable(tableName);
    }
}
