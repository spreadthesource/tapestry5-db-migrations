package com.spreadthesource.tapestry.dbmigration.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.hibernate.FetchMode;
import org.hibernate.MappingException;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.Mapping;
import org.hibernate.jdbc.util.FormatStyle;
import org.hibernate.jdbc.util.Formatter;
import org.hibernate.mapping.Value;
import org.hibernate.mapping.ValueVisitor;
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
        configuration.configure("hibernate.h2.cfg.xml");
        // configuration.configure();

        Properties properties = configuration.getProperties();

        this.dialect = Dialect.getDialect(properties);
        this.connectionHelper = new ManagedProviderConnectionHelper(properties);

        this.defaultCatalog = properties.getProperty(Environment.DEFAULT_CATALOG);
        this.defaultSchema = properties.getProperty(Environment.DEFAULT_SCHEMA);

        this.formatter = (PropertiesHelper.getBoolean(Environment.FORMAT_SQL, properties) ? FormatStyle.DDL
                : FormatStyle.NONE).getFormatter();

        this.log = log;
    }

    public String dropTable(String tableName)
    {
        org.hibernate.mapping.Table hTable = new org.hibernate.mapping.Table(tableName);
        String dropSQL = hTable.sqlDropString(dialect, defaultCatalog, defaultSchema);

        return dropSQL;
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

        for (final Column column : table.getColumns())
        {
            org.hibernate.mapping.Column hColumn = new org.hibernate.mapping.Column(column
                    .getName());
            String typeName = getDialect().getTypeName(column.getSQLType());

            Value v = new Value()
            {

                public void setTypeUsingReflection(String className, String propertyName)
                        throws MappingException
                {
                    // TODO Auto-generated method stub

                }

                public boolean isValid(Mapping mapping) throws MappingException
                {
                    // TODO Auto-generated method stub
                    return false;
                }

                public boolean isSimpleValue()
                {
                    // TODO Auto-generated method stub
                    return false;
                }

                public boolean isNullable()
                {
                    // TODO Auto-generated method stub
                    return false;
                }

                public boolean isAlternateUniqueKey()
                {
                    // TODO Auto-generated method stub
                    return false;
                }

                public boolean hasFormula()
                {
                    // TODO Auto-generated method stub
                    return false;
                }

                public Type getType() throws MappingException
                {
                    return column.getType();
                }

                public org.hibernate.mapping.Table getTable()
                {
                    // TODO Auto-generated method stub
                    return null;
                }

                public FetchMode getFetchMode()
                {
                    // TODO Auto-generated method stub
                    return null;
                }

                public boolean[] getColumnUpdateability()
                {
                    // TODO Auto-generated method stub
                    return null;
                }

                public int getColumnSpan()
                {
                    // TODO Auto-generated method stub
                    return 0;
                }

                public Iterator getColumnIterator()
                {
                    // TODO Auto-generated method stub
                    return null;
                }

                public boolean[] getColumnInsertability()
                {
                    // TODO Auto-generated method stub
                    return null;
                }

                public void createForeignKey() throws MappingException
                {
                    // TODO Auto-generated method stub

                }

                public Object accept(ValueVisitor visitor)
                {
                    // TODO Auto-generated method stub
                    return null;
                }
            };

            hColumn.setValue(v);
            // hColumn.setSqlType(typeName);
            hColumn.setUnique(column.isUnique());

            hColumn.setLength(column.getLength());

            hTable.addColumn(hColumn);
        }

        // TODO : still have to know where Mapping are really involved and how much are they usefull
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

        String sql = hTable.sqlCreateString(dialect, p, defaultCatalog, defaultSchema);

        return sql;
    }
}
