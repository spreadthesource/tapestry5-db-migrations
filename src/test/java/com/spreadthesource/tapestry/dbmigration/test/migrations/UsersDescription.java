package com.spreadthesource.tapestry.dbmigration.test.migrations;

import com.spreadthesource.tapestry.dbmigration.annotations.Version;
import com.spreadthesource.tapestry.dbmigration.migrations.Migration;

@Version(20100510)
public class UsersDescription implements Migration
{
    public void up()
    {
        // Table users = new Table("users");
        // users.addColumn("description", Types.TIME);

        // createTable(users);
    }

    public void down()
    {
    }

}
