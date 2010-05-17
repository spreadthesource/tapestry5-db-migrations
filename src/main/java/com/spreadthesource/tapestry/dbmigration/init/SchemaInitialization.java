package com.spreadthesource.tapestry.dbmigration.init;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.hibernate.Hibernate;

import com.spreadthesource.tapestry.dbmigration.MigrationSymbolConstants;
import com.spreadthesource.tapestry.dbmigration.annotations.Version;
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
        versions.addColumn("version", Hibernate.INTEGER);
        versions.addColumn("datetime", Hibernate.TIMESTAMP);
        
        createTable(versions);
    }

    public void down()
    {
        // nothing to do
    }

}
