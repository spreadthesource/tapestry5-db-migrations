package com.spreadthesource.tapestry.dbmigration.test.migrations;

import org.hibernate.Hibernate;

import com.spreadthesource.tapestry.dbmigration.annotations.Version;
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
        users.addColumn("name", Hibernate.STRING);
        users.addColumn("password", Hibernate.STRING);
        
        createTable(users);
    }

    public void down()
    {
        dropTable("users");
    }

}
