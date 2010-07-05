package com.spreadthesource.tapestry.dbmigration.test.migrations;

import org.apache.tapestry5.ioc.annotations.Inject;

import com.spreadthesource.tapestry.dbmigration.annotations.Version;
import com.spreadthesource.tapestry.dbmigration.migrations.Migration;
import com.spreadthesource.tapestry.dbmigration.migrations.UpdateTable;
import com.spreadthesource.tapestry.dbmigration.migrations.UpdateTableContext;
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
            public void run(UpdateTableContext ctx)
            {
                ctx.setName("User");
                ctx.addString("civility");
            }
        });

    }

    public void down()
    {
        // Nothing to do
    }

}
