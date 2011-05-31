package com.spreadthesource.tapestry.dbmigration.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.tool.hbm2ddl.DatabaseMetadata;
import org.slf4j.Logger;

import com.spreadthesource.tapestry.dbmigration.command.MigrationCommand;
import com.spreadthesource.tapestry.dbmigration.hibernate.ConnectionHelper;
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

    private Map<Class, Class> mapping;

    public MigrationHelperImpl(Map<Class, Class> mapping, Logger log)
    {
        this.mapping = mapping;
        this.log = log;
    }

    @SuppressWarnings("unchecked")
    public void add(MigrationCommand command)
    {
        Connection connection = null;
        ConnectionHelper connectionHelper = dbSource.getConnectionHelper();

        try
        {
            // Get the database metadatas
            // connectionHelper.prepare(true);
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
            context.setMappings(dbSource.getMappings());

            command.run(context);
            pendingSql.addAll(context.getQueries());
        }
        catch (SQLException sqle)
        {
            throw new RuntimeException("could not get database metadata", sqle);
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

        for (Class<MigrationCommand> commandClass : mapping.keySet())
        {
            if (commandClass.isInstance(command))
            {
                Class<MigrationContext> contextClass = mapping.get(commandClass);
                try
                {
                    MigrationContext context = contextClass.newInstance();
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
