package com.spreadthesource.tapestry.dbmigration.test.migrations;

import org.apache.tapestry5.ioc.annotations.Inject;

import com.spreadthesource.tapestry.dbmigration.annotations.Version;
import com.spreadthesource.tapestry.dbmigration.command.CreateConstraint;
import com.spreadthesource.tapestry.dbmigration.command.CreateTable;
import com.spreadthesource.tapestry.dbmigration.command.Drop;
import com.spreadthesource.tapestry.dbmigration.migrations.Constraint;
import com.spreadthesource.tapestry.dbmigration.migrations.DropContext;
import com.spreadthesource.tapestry.dbmigration.migrations.Migration;
import com.spreadthesource.tapestry.dbmigration.migrations.Table;
import com.spreadthesource.tapestry.dbmigration.services.MigrationHelper;

@Version(20100604)
public class TestForeignKey implements Migration
{

    @Inject
    private MigrationHelper helper;

    public void up()
    {
        helper.add(new CreateTable()
        {
            public void run(Table ctx)
            {
                ctx.setName("tableA");
                ctx.addInteger("id").setPrimary(true).setIdentityGenerator("identity");
            }
        });

        helper.add(new CreateTable()
        {
            public void run(Table ctx)
            {
                ctx.setName("tableB");
                ctx.addInteger("id").setPrimary(true).setIdentityGenerator("identity");
                ctx.addInteger("tableA_id");
            }
        });

        helper.add(new CreateConstraint()
        {
            public void run(Constraint ctx)
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
        helper.add(new Drop()
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
