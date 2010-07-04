package com.spreadthesource.tapestry.dbmigration.init;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;

import com.spreadthesource.tapestry.dbmigration.MigrationSymbolConstants;
import com.spreadthesource.tapestry.dbmigration.annotations.Version;
import com.spreadthesource.tapestry.dbmigration.migrations.CreateTable;
import com.spreadthesource.tapestry.dbmigration.migrations.CreateTableContext;
import com.spreadthesource.tapestry.dbmigration.migrations.Migration;
import com.spreadthesource.tapestry.dbmigration.services.MigrationHelper;

@Version(0)
public class SchemaInitialization implements Migration
{
    private String versioningTableName;

    @Inject
    private MigrationHelper helper;

    public SchemaInitialization(
            MigrationHelper helper,
            @Inject @Symbol(MigrationSymbolConstants.VERSIONING_TABLE_NAME) String versioningTableName)
    {
        this.versioningTableName = versioningTableName;
    }

    public void up()
    {
        helper.createTable(new CreateTable()
        {
            public void run(CreateTableContext ctx)
            {
                ctx.setName(versioningTableName);
                ctx.addInteger("id").setPrimary(true).setIdentityGenerator("identity");
                ctx.addInteger("version");
                ctx.addTimestamp("datetime");
            }
        });
    }

    public void down()
    {
        // nothing to do
    }

}
