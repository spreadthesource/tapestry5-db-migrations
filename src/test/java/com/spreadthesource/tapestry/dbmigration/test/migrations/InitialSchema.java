package com.spreadthesource.tapestry.dbmigration.test.migrations;

import org.apache.tapestry5.ioc.annotations.Inject;

import com.spreadthesource.tapestry.dbmigration.annotations.Version;
import com.spreadthesource.tapestry.dbmigration.command.CreateConstraint;
import com.spreadthesource.tapestry.dbmigration.command.CreateTable;
import com.spreadthesource.tapestry.dbmigration.command.Drop;
import com.spreadthesource.tapestry.dbmigration.command.JoinTable;
import com.spreadthesource.tapestry.dbmigration.migrations.Constraint;
import com.spreadthesource.tapestry.dbmigration.migrations.DropContext;
import com.spreadthesource.tapestry.dbmigration.migrations.JoinTableContext;
import com.spreadthesource.tapestry.dbmigration.migrations.Migration;
import com.spreadthesource.tapestry.dbmigration.migrations.Table;
import com.spreadthesource.tapestry.dbmigration.services.MigrationHelper;

@Version(20100430)
public class InitialSchema implements Migration
{
    @Inject
    private MigrationHelper helper;

    public void up()
    {

        // Table Authority
        helper.add(new CreateTable()
        {
            public void run(Table ctx)
            {
                ctx.setName("Authority");
                ctx.addString("label").setUnique(true);
                ctx.addText("description");
                ctx.addText("id");
            }
        });

        // Table Authority
        helper.add(new CreateTable()
        {
            public void run(Table ctx)
            {
                ctx.setName("User");
                ctx.addString("username").setUnique(true).setNotNull(true);
                ctx.addString("password").setNotNull(true);
            }
        });

        // Add constraints
        helper.add(new CreateConstraint()
        {
            public void run(Constraint ctx)
            {
                ctx.setTableName("Authority");
                ctx.setUnique("AuthorityUnicity", "label", "id");
            }
        });

        helper.add(new JoinTable()
        {

            public void run(JoinTableContext ctx)
            {
                ctx.join("User", "Authority");
            }
        });

    }

    public void down()
    {
        helper.add(new Drop()
        {
            public void run(DropContext ctx)
            {
                ctx.dropTable("UserAuthority");
                ctx.dropTable("User");
                ctx.dropTable("Authority");
            }
        });
    }
}
