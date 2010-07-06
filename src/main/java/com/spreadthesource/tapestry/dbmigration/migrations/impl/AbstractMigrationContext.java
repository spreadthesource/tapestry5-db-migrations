package com.spreadthesource.tapestry.dbmigration.migrations.impl;

import org.hibernate.dialect.Dialect;
import org.hibernate.tool.hbm2ddl.DatabaseMetadata;

import com.spreadthesource.tapestry.dbmigration.hibernate.ConnectionHelper;
import com.spreadthesource.tapestry.dbmigration.migrations.MigrationContext;
import com.spreadthesource.tapestry.dbmigration.services.PrimaryKeyStrategy;

/**
 * Base class for MigrationContext.
 * 
 * @author ccordenier
 */
public abstract class AbstractMigrationContext implements MigrationContext
{
    protected Dialect dialect;

    protected String defaultCatalog;

    protected String defaultSchema;

    protected ConnectionHelper connectionHelper;

    protected DatabaseMetadata databaseMetadata;

    protected PrimaryKeyStrategy pkStrategy;

    public void setConnectionHelper(ConnectionHelper helper)
    {
        this.connectionHelper = helper;
    }

    public void setDefaultCatalog(String defaultCatalog)
    {
        this.defaultCatalog = defaultCatalog;
    }

    public void setDefaultSchema(String defaultSchema)
    {
        this.defaultSchema = defaultSchema;
    }

    public void setDialect(Dialect dialect)
    {
        this.dialect = dialect;
    }

    public void setPrimaryKeyStrategy(PrimaryKeyStrategy pkStrategy)
    {
        this.pkStrategy = pkStrategy;
    }

    public void setDatabaseMetadata(DatabaseMetadata databaseMetadata)
    {
        this.databaseMetadata = databaseMetadata;
    }

}
