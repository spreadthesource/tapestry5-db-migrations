package com.spreadthesource.tapestry.dbmigration.test.migrations;

import java.sql.Types;

import com.spreadthesource.tapestry.dbmigration.annotations.Version;
import com.spreadthesource.tapestry.dbmigration.data.Column;
import com.spreadthesource.tapestry.dbmigration.data.Table;
import com.spreadthesource.tapestry.dbmigration.migrations.MigrationBase;
import com.spreadthesource.tapestry.dbmigration.services.MigrationHelper;

@Version(20100602)
public class TestAutoIncrement extends MigrationBase
{
    public TestAutoIncrement(MigrationHelper helper)
    {
        super(helper);
    }

    public void up()
    {
        Table users = new Table("test");
        Column id = users.addColumn("id", Types.INTEGER, 11);
        id.setPrimary(true);
        id.setIdentityGenerator("identity");

        createTable(users);
    }

    public void down()
    {
        dropTable("test");
    }

}
