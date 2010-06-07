package com.spreadthesource.tapestry.dbmigration.test.migrations;

import java.sql.Types;

import com.spreadthesource.tapestry.dbmigration.annotations.Version;
import com.spreadthesource.tapestry.dbmigration.data.Column;
import com.spreadthesource.tapestry.dbmigration.data.Constraint;
import com.spreadthesource.tapestry.dbmigration.data.Table;
import com.spreadthesource.tapestry.dbmigration.migrations.MigrationBase;
import com.spreadthesource.tapestry.dbmigration.services.MigrationHelper;

@Version(20100604)
public class TestForeignKey extends MigrationBase
{
    public TestForeignKey(MigrationHelper helper)
    {
        super(helper);
    }

    public void up()
    {
        Table tableA = new Table("tableA");
        Column idA = tableA.addColumn("id", Types.INTEGER, 11);
        idA.setPrimary(true);
        idA.setIdentityGenerator("identity");

        createTable(tableA);
        
        Table tableB = new Table("tableB");
        Column idB = tableB.addColumn("id", Types.INTEGER, 11);
        tableB.addColumn("tableA_id", Types.INTEGER, 11);
        idB.setPrimary(true);
        idB.setIdentityGenerator("identity");
        
        Constraint c = tableB.addForeignKey("fk", "tableA");
        c.addConstraint("tableA_id", "id");
        
        createTable(tableB);
    }

    public void down()
    {
        dropTable("tableA");
        dropTable("tableB");
    }

}
