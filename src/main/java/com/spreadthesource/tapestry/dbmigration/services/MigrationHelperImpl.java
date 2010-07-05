package com.spreadthesource.tapestry.dbmigration.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.tool.hbm2ddl.DatabaseMetadata;
import org.slf4j.Logger;

import com.spreadthesource.tapestry.dbmigration.hibernate.ConnectionHelper;
import com.spreadthesource.tapestry.dbmigration.migrations.MigrationCommand;
import com.spreadthesource.tapestry.dbmigration.migrations.MigrationContext;

public class MigrationHelperImpl implements MigrationHelper
{

    @Inject
    private PrimaryKeyStrategy pkStrategy;

    @Inject
    private DbSource dbSource;

    private DatabaseMetadata databaseMetadata;

    private Logger log;

    private List<String> pendingSql = new ArrayList<String>();

    private Map<String, String> mapping;

    public MigrationHelperImpl(Map<String, String> mapping, Logger log)
    {
        this.mapping = mapping;
        this.log = log;
    }

    public boolean checkIfTableExists(String tableName)
    {
        Connection connection = null;

        ConnectionHelper connectionHelper = dbSource.getConnectionHelper();

        try
        {
            connectionHelper.prepare(true);
            connection = connectionHelper.getConnection();
            databaseMetadata = new DatabaseMetadata(connection, dbSource.getDialect());

            if (databaseMetadata.isTable(tableName))
            {
                log.info("Table " + tableName + " exists, schema is under version control");
                return true;
            }
        }
        catch (SQLException sqle)
        {
            throw new RuntimeException("could not get database metadata", sqle);
        }
        finally
        {
            try
            {
                connectionHelper.release();
            }
            catch (Exception e)
            {
                throw new RuntimeException("Error when closing connection", e);
            }
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    public void add(MigrationCommand command)
    {
        Connection connection = null;
        ConnectionHelper connectionHelper = dbSource.getConnectionHelper();

        try
        {
            // Get the database metadatas
            connectionHelper.prepare(true);
            connection = connectionHelper.getConnection();
            databaseMetadata = new DatabaseMetadata(connection, dbSource.getDialect());

            // Call command
            MigrationContext context = createContextInstance(command);
            context.setConnectionHelper(connectionHelper);
            context.setDatabaseMetadata(databaseMetadata);
            context.setDialect(dbSource.getDialect());
            context.setDefaultCatalog(dbSource.getDefaultCatalog());
            context.setDefaultSchema(dbSource.getDefaultSchema());
            context.setPrimaryKeyStrategy(pkStrategy);
            command.run(context);
            pendingSql.addAll(context.getQueries());
        }
        catch (SQLException sqle)
        {
            throw new RuntimeException("could not get database metadata", sqle);
        }
        finally
        {
            try
            {
                connectionHelper.release();
            }
            catch (Exception e)
            {
                throw new RuntimeException("Error when closing connection", e);
            }
        }

    }

    public List<String> getPendingSql()
    {
        return pendingSql;
    }

    public void reset()
    {
        this.pendingSql.clear();
    }

    @SuppressWarnings("unchecked")
    private MigrationContext createContextInstance(MigrationCommand command)
    {

        for (Class<?> ctxClass : command.getClass().getInterfaces())
        {
            if (mapping.containsKey(ctxClass.getName()))
            {
                // Call command
                String contextClass = mapping.get(ctxClass.getName());

                try
                {
                    MigrationContext context = (MigrationContext) Class.forName(contextClass)
                            .newInstance();
                    return context;
                }
                catch (Exception ex)
                {
                    throw new IllegalArgumentException("Context class not found " + contextClass);
                }
            }
        }

        throw new IllegalArgumentException("Command not supported " + command.getClass().getName());

    }

}
