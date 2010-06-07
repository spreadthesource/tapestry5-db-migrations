package com.spreadthesource.tapestry.dbmigration.init;

import java.sql.Types;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;

import com.spreadthesource.tapestry.dbmigration.MigrationSymbolConstants;
import com.spreadthesource.tapestry.dbmigration.annotations.Version;
import com.spreadthesource.tapestry.dbmigration.data.Column;
import com.spreadthesource.tapestry.dbmigration.data.Table;
import com.spreadthesource.tapestry.dbmigration.migrations.MigrationBase;
import com.spreadthesource.tapestry.dbmigration.services.MigrationHelper;

@Version(0)
public class SchemaInitialization extends MigrationBase
{
    private String versioningTableName;

    public SchemaInitialization(
            MigrationHelper helper,
            @Inject @Symbol(MigrationSymbolConstants.VERSIONING_TABLE_NAME) String versioningTableName)
    {
        super(helper);
        
        this.versioningTableName = versioningTableName;
    }

    public void up()
    {
        Table versions = new Table(versioningTableName);
        
        Column id = versions.addColumn("id", Types.INTEGER, 11);
        id.setPrimary(true);
        id.setIdentityGenerator("identity");
        
        versions.addColumn("version", Types.INTEGER);
        
        versions.addColumn("datetime", Types.TIMESTAMP);
        
        createTable(versions);
    }

    public void down()
    {
        // nothing to do
    }

}
