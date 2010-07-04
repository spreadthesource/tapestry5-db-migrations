package com.spreadthesource.tapestry.dbmigration.test.migrations;

import org.apache.tapestry5.ioc.annotations.Inject;

import com.spreadthesource.tapestry.dbmigration.annotations.Version;
import com.spreadthesource.tapestry.dbmigration.migrations.Migration;
import com.spreadthesource.tapestry.dbmigration.services.MigrationHelper;

@Version(20100602)
public class TestAutoIncrement implements Migration
{

    @Inject
    private MigrationHelper helper;

    public void up()
    {
        // Table users = new Table("test");
        //Column id = users.addColumn("id", Types.INTEGER, 11);
        // id.setPrimary(true);
        //id.setIdentityGenerator("identity");

        // createTable(users);
    }

    public void down()
    {
        // dropTable("test");
    }

}
