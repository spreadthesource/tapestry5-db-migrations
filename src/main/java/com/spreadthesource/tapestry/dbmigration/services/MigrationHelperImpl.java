package com.spreadthesource.tapestry.dbmigration.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.tapestry5.hibernate.HibernateConfigurer;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.Dialect;
import org.hibernate.jdbc.util.FormatStyle;
import org.hibernate.jdbc.util.Formatter;
import org.hibernate.tool.hbm2ddl.DatabaseMetadata;
import org.hibernate.util.PropertiesHelper;
import org.slf4j.Logger;

import com.spreadthesource.tapestry.dbmigration.MigrationSymbolConstants;
import com.spreadthesource.tapestry.dbmigration.hibernate.ConnectionHelper;
import com.spreadthesource.tapestry.dbmigration.hibernate.ManagedProviderConnectionHelper;
import com.spreadthesource.tapestry.dbmigration.migrations.CreateConstraint;
import com.spreadthesource.tapestry.dbmigration.migrations.CreateConstraintContext;
import com.spreadthesource.tapestry.dbmigration.migrations.CreateConstraintContextImpl;
import com.spreadthesource.tapestry.dbmigration.migrations.CreateTable;
import com.spreadthesource.tapestry.dbmigration.migrations.CreateTableContext;
import com.spreadthesource.tapestry.dbmigration.migrations.CreateTableContextImpl;
import com.spreadthesource.tapestry.dbmigration.migrations.Drop;
import com.spreadthesource.tapestry.dbmigration.migrations.DropContext;
import com.spreadthesource.tapestry.dbmigration.migrations.DropContextImpl;
import com.spreadthesource.tapestry.dbmigration.migrations.JoinTable;
import com.spreadthesource.tapestry.dbmigration.migrations.JoinTableContext;
import com.spreadthesource.tapestry.dbmigration.migrations.JoinTableContextImpl;
import com.spreadthesource.tapestry.dbmigration.migrations.UpdateTable;
import com.spreadthesource.tapestry.dbmigration.migrations.UpdateTableContext;
import com.spreadthesource.tapestry.dbmigration.migrations.UpdateTableContextImpl;

public class MigrationHelperImpl implements MigrationHelper
{

    @Inject
    private PrimaryKeyStrategy pkStrategy;

    private Configuration configuration;

    private ConnectionHelper connectionHelper;

    private DatabaseMetadata databaseMetadata;

    private Dialect dialect;

    private String defaultCatalog;

    private String defaultSchema;

    private Formatter formatter;

    private Logger log;

    private List<String> pendingSql = new ArrayList<String>();

    public MigrationHelperImpl(
            List<HibernateConfigurer> hibConfigurers,
            Logger log,
            @Inject @Symbol(MigrationSymbolConstants.DEFAULT_HIBERNATE_CONFIGURATION) boolean defaultConfiguration)
    {
        this.configuration = new Configuration();

        if (defaultConfiguration)
        {
            configuration.configure();
        }

        for (HibernateConfigurer configurer : hibConfigurers)
        {
            configurer.configure(configuration);
        }

        Properties properties = configuration.getProperties();

        this.dialect = Dialect.getDialect(properties);
        this.connectionHelper = new ManagedProviderConnectionHelper(properties);

        this.defaultCatalog = properties.getProperty(Environment.DEFAULT_CATALOG);
        this.defaultSchema = properties.getProperty(Environment.DEFAULT_SCHEMA);

        this.formatter = (PropertiesHelper.getBoolean(Environment.FORMAT_SQL, properties) ? FormatStyle.DDL
                : FormatStyle.NONE).getFormatter();

        this.log = log;
    }

    public Dialect getDialect()
    {
        return this.dialect;
    }

    public String getDefaultSchema()
    {
        return this.defaultSchema;
    }

    public String getDefaultCatalog()
    {
        return this.defaultCatalog;
    }

    public ConnectionHelper getConnectionHelper()
    {
        return this.connectionHelper;
    }

    public Formatter getFormatter()
    {
        return this.formatter;
    }

    public boolean checkIfTableExists(String tableName)
    {
        Connection connection = null;

        try
        {
            connectionHelper.prepare(true);
            connection = connectionHelper.getConnection();
            databaseMetadata = new DatabaseMetadata(connection, dialect);

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

    public void createTable(CreateTable command)
    {
        CreateTableContext createContext = new CreateTableContextImpl(dialect, defaultCatalog,
                defaultSchema, pkStrategy);
        command.run(createContext);
        pendingSql.addAll(createContext.getQueries());
    }

    public void updateTable(UpdateTable command)
    {
        Connection connection = null;

        try
        {
            // Get the database metadatas
            connectionHelper.prepare(true);
            connection = connectionHelper.getConnection();
            databaseMetadata = new DatabaseMetadata(connection, dialect);

            // Update table
            UpdateTableContext updateContext = new UpdateTableContextImpl(dialect, defaultCatalog,
                    defaultSchema, databaseMetadata);
            command.run(updateContext);
            pendingSql.addAll(updateContext.getQueries());
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

    public void createConstraint(CreateConstraint command)
    {
        CreateConstraintContext ctx = new CreateConstraintContextImpl(dialect, defaultCatalog,
                defaultSchema);
        command.run(ctx);
        pendingSql.addAll(ctx.getQueries());
    }

    public void drop(Drop command)
    {
        DropContext ctx = new DropContextImpl(dialect, defaultCatalog, defaultSchema);
        command.run(ctx);
        pendingSql.addAll(ctx.getQueries());
    }

    public void join(JoinTable command)
    {
        JoinTableContext ctx = new JoinTableContextImpl(dialect, defaultCatalog, defaultSchema,
                pkStrategy);
        command.run(ctx);
        pendingSql.addAll(ctx.getQueries());
    }

    public List<String> getPendingSql()
    {
        return pendingSql;
    }

    public void reset()
    {
        this.pendingSql.clear();
    }

}
