package com.spreadthesource.tapestry.dbmigration.test.migrations;

import org.apache.tapestry5.ioc.annotations.Inject;

import com.spreadthesource.tapestry.dbmigration.annotations.Version;
import com.spreadthesource.tapestry.dbmigration.command.Drop;
import com.spreadthesource.tapestry.dbmigration.command.Sql;
import com.spreadthesource.tapestry.dbmigration.command.UpdateTable;
import com.spreadthesource.tapestry.dbmigration.migrations.DropContext;
import com.spreadthesource.tapestry.dbmigration.migrations.Migration;
import com.spreadthesource.tapestry.dbmigration.migrations.SqlQuery;
import com.spreadthesource.tapestry.dbmigration.migrations.Table;
import com.spreadthesource.tapestry.dbmigration.services.MigrationHelper;

@Version(20100510)
public class UsersDescription implements Migration
{
    @Inject
    private MigrationHelper helper;

    public void up()
    {
        helper.add(new UpdateTable()
        {
            public void run(Table ctx)
            {
                ctx.setName("User");
                ctx.addString("civility");
            }
        });

        helper.add(new Sql()
        {
            public void run(SqlQuery ctx)
            {
                ctx.addSql("create Table CustomSqlQuery(id int)");
            }
        });

    }

    public void down()
    {
        helper.add(new Drop()
        {
            public void run(DropContext ctx)
            {
                ctx.dropTable("CustomSqlQuery");

            }
        });
    }

}
