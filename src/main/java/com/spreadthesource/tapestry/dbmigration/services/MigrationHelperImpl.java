package com.spreadthesource.tapestry.dbmigration.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.hibernate.MappingException;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.Mapping;
import org.hibernate.jdbc.util.FormatStyle;
import org.hibernate.jdbc.util.Formatter;
import org.hibernate.mapping.UniqueKey;
import org.hibernate.tool.hbm2ddl.DatabaseMetadata;
import org.hibernate.type.IntegerType;
import org.hibernate.type.Type;
import org.hibernate.util.PropertiesHelper;
import org.slf4j.Logger;

import com.spreadthesource.tapestry.dbmigration.data.Column;
import com.spreadthesource.tapestry.dbmigration.data.Table;
import com.spreadthesource.tapestry.dbmigration.hibernate.ConnectionHelper;
import com.spreadthesource.tapestry.dbmigration.hibernate.ManagedProviderConnectionHelper;

public class MigrationHelperImpl implements MigrationHelper
{
    private Configuration configuration;

    private Dialect dialect;

    private ConnectionHelper connectionHelper;

    private String defaultCatalog;

    private String defaultSchema;

    private Formatter formatter;

    private Logger log;

    public MigrationHelperImpl(Logger log)
    {
        this.configuration = new Configuration();
        // configuration.configure("hibernate.h2.cfg.xml");
        configuration.configure();

        Properties properties = configuration.getProperties();

        this.dialect = Dialect.getDialect(properties);
        this.connectionHelper = new ManagedProviderConnectionHelper(properties);

        this.defaultCatalog = properties.getProperty(Environment.DEFAULT_CATALOG);
        this.defaultSchema = properties.getProperty(Environment.DEFAULT_SCHEMA);

        this.formatter = (PropertiesHelper.getBoolean(Environment.FORMAT_SQL, properties) ? FormatStyle.DDL
                : FormatStyle.NONE).getFormatter();

        this.log = log;

    }

    public <T> void addColumn(String tableName, String columnName, T columnType)
    {
        // TODO Auto-generated method stub

    }

    public void createTable(String tableName)
    {
        // TODO Auto-generated method stub

    }

    public void dropTable(String tableName)
    {
        // TODO Auto-generated method stub

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
        DatabaseMetadata meta;

        try
        {
            log.info("fetching database metadata");

            connectionHelper.prepare(true);
            connection = connectionHelper.getConnection();
            meta = new DatabaseMetadata(connection, dialect);

            if (meta.isTable(tableName))
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

    public String createTable(Table table)
    {
        org.hibernate.mapping.Table hTable = new org.hibernate.mapping.Table(table.getName());

        for (Column column : table.getColumns())
        {
            org.hibernate.mapping.Column hColumn = new org.hibernate.mapping.Column(column
                    .getName());
            String typeName = getDialect().getTypeName(column.getType());
            hColumn.setSqlType(typeName);
            hColumn.setUnique(column.isUnique());

            hTable.addColumn(hColumn);
        }

        Mapping p = new Mapping()
        {
            public String getIdentifierPropertyName(String className) throws MappingException
            {
                log.info("className:" + className);
                return "id";
            }

            public Type getIdentifierType(String className) throws MappingException
            {
                log.info("className:" + className);
                return new IntegerType();
            }

            public Type getReferencedPropertyType(String className, String propertyName)
                    throws MappingException
            {
                log.info("className:" + className + " propertyName:" + propertyName);
                return null;
            }

        };

        return hTable.sqlCreateString(dialect, p, defaultCatalog, defaultSchema);
    }

}
