package com.spreadthesource.tapestry.dbmigration.migrations;

import java.util.List;

import org.hibernate.dialect.Dialect;
import org.hibernate.tool.hbm2ddl.DatabaseMetadata;

import com.spreadthesource.tapestry.dbmigration.hibernate.ConnectionHelper;
import com.spreadthesource.tapestry.dbmigration.services.PrimaryKeyStrategy;

/**
 * The context will implement all the logic for building queries.
 * 
 * @author ccordenier
 */
public interface MigrationContext
{
    void setDialect(Dialect dialect);

    void setDefaultCatalog(String defaultCatalog);

    void setDefaultSchema(String defaultSchema);

    void setConnectionHelper(ConnectionHelper helper);

    void setPrimaryKeyStrategy(PrimaryKeyStrategy pkStrategy);

    void setDatabaseMetadata(DatabaseMetadata databaseMetadata);

    List<String> getQueries();
}
