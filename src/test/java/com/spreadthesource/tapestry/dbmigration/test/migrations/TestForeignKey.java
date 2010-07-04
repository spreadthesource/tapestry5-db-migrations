package com.spreadthesource.tapestry.dbmigration.test.migrations;

import org.apache.tapestry5.ioc.annotations.Inject;

import com.spreadthesource.tapestry.dbmigration.annotations.Version;
import com.spreadthesource.tapestry.dbmigration.migrations.CreateConstraint;
import com.spreadthesource.tapestry.dbmigration.migrations.CreateConstraintContext;
import com.spreadthesource.tapestry.dbmigration.migrations.CreateTable;
import com.spreadthesource.tapestry.dbmigration.migrations.CreateTableContext;
import com.spreadthesource.tapestry.dbmigration.migrations.Drop;
import com.spreadthesource.tapestry.dbmigration.migrations.DropContext;
import com.spreadthesource.tapestry.dbmigration.migrations.Migration;
import com.spreadthesource.tapestry.dbmigration.services.MigrationHelper;

@Version(20100604)
public class TestForeignKey implements Migration
{

    @Inject
    private MigrationHelper helper;

    public void up()
    {
        helper.createTable(new CreateTable()
        {
            public void run(CreateTableContext ctx)
            {
                ctx.setName("tableA");
                ctx.addInteger("id").setPrimary(true).setIdentityGenerator("identity");
            }
        });

        helper.createTable(new CreateTable()
        {
            public void run(CreateTableContext ctx)
            {
                ctx.setName("tableB");
                ctx.addInteger("id").setPrimary(true).setIdentityGenerator("identity");
                ctx.addInteger("tableA_id");
            }
        });

        helper.createConstraint(new CreateConstraint()
        {
            public void run(CreateConstraintContext ctx)
            {
                ctx.setName("tableB");
                ctx.setForeignKey("fk", "tableA", new String[]
                { "id" }, new String[]
                { "id" });
            }
        });
    }

    public void down()
    {
        helper.drop(new Drop()
        {
            public void run(DropContext ctx)
            {
                // drop the constaint and the targeted table
                ctx.dropForeignKey("tableB", "fk");
                ctx.dropTable("tableA");
            }

        });
    }

}
