package com.spreadthesource.tapestry.dbmigration.test.migrations;

import java.sql.Types;

import com.spreadthesource.tapestry.dbmigration.annotations.Version;
import com.spreadthesource.tapestry.dbmigration.data.Column;
import com.spreadthesource.tapestry.dbmigration.data.Table;
import com.spreadthesource.tapestry.dbmigration.migrations.MigrationBase;
import com.spreadthesource.tapestry.dbmigration.services.MigrationHelper;

@Version(20100430)
public class InitialSchema extends MigrationBase
{
    public InitialSchema(MigrationHelper helper)
    {
        super(helper);
    }

    public void up()
    { 
        Table users = new Table("users");
        Column name = users.addColumn("name", Types.CLOB);
        name.setUnique(true);
        
        Column password = users.addColumn("password", Types.VARCHAR);
        password.setNotNull(true);
        
        createTable(users);
    }

    public void down()
    {
        dropTable("users");
    }

}
