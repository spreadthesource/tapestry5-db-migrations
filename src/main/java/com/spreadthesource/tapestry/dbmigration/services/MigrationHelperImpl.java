package com.spreadthesource.tapestry.dbmigration.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.hibernate.MappingException;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.Mapping;
import org.hibernate.id.factory.DefaultIdentifierGeneratorFactory;
import org.hibernate.id.factory.IdentifierGeneratorFactory;
import org.hibernate.jdbc.util.FormatStyle;
import org.hibernate.jdbc.util.Formatter;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.mapping.PrimaryKey;
import org.hibernate.mapping.SimpleValue;
import org.hibernate.tool.hbm2ddl.DatabaseMetadata;
import org.hibernate.tool.hbm2ddl.TableMetadata;
import org.hibernate.type.Type;
import org.hibernate.util.PropertiesHelper;
import org.slf4j.Logger;

import com.spreadthesource.tapestry.dbmigration.data.Column;
import com.spreadthesource.tapestry.dbmigration.data.Constraint;
import com.spreadthesource.tapestry.dbmigration.data.Table;
import com.spreadthesource.tapestry.dbmigration.hibernate.ConnectionHelper;
import com.spreadthesource.tapestry.dbmigration.hibernate.ManagedProviderConnectionHelper;

public class MigrationHelperImpl implements MigrationHelper
{
    private Configuration configuration;

    private ConnectionHelper connectionHelper;

    private DatabaseMetadata databaseMetadata;

    private Dialect dialect;

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

    public List<String> createTable(Table table)
    {
        List<String> scripts = new ArrayList<String>();

        String tableName = table.getName();

        org.hibernate.mapping.Table hTable = new org.hibernate.mapping.Table(tableName);

        for (final Column column : table.getColumns())
        {
            org.hibernate.mapping.Column hColumn = new org.hibernate.mapping.Column(column
                    .getName());

            String typeName;
            Integer typeLength = column.getLength();

            if (typeLength == null)
            {
                typeName = getDialect().getTypeName(column.getType());
            }
            else
            {
                typeName = getDialect().getTypeName(column.getType(), typeLength, 0, 0);
            }

            hColumn.setSqlType(typeName);
            hColumn.setUnique(column.isUnique());
            hColumn.setNullable(!column.isNotNull());
            hColumn.setLength(column.getLength());
            hColumn.setScale(3);

            hTable.addColumn(hColumn);

            if (column.isPrimary())
            {
                PrimaryKey primaryKey = new PrimaryKey();
                primaryKey.addColumn(hColumn);

                if (column.getIdentityGenerator() != null)
                {
                    SimpleValue idValue = new SimpleValue(hTable);
                    idValue.setIdentifierGeneratorStrategy("identity");

                    idValue.setTypeName(dialect.getTypeName(column.getType()));

                    hColumn.setValue(idValue);

                    hTable.setIdentifierValue(idValue);
                }

                hTable.setPrimaryKey(primaryKey);
            }
        }

        // TODO : still have to know where Mapping are really involved and how much are they usefull
        Mapping p = new Mapping()
        {

            public IdentifierGeneratorFactory getIdentifierGeneratorFactory()
            {
                return new DefaultIdentifierGeneratorFactory();
            }

            public String getIdentifierPropertyName(String arg0) throws MappingException
            {
                // TODO Auto-generated method stub
                return null;
            }

            public Type getIdentifierType(String arg0) throws MappingException
            {
                // TODO Auto-generated method stub
                return null;
            }

            public Type getReferencedPropertyType(String arg0, String arg1) throws MappingException
            {
                // TODO Auto-generated method stub
                return null;
            }

        };

        boolean tableExists = checkIfTableExists(tableName);

        if (tableExists)
        {
            TableMetadata tableMetadata = databaseMetadata.getTableMetadata(
                    tableName,
                    defaultSchema,
                    defaultCatalog,
                    false);

            Iterator subiter = hTable.sqlAlterStrings(
                    dialect,
                    p,
                    tableMetadata,
                    defaultCatalog,
                    defaultSchema);

            while (subiter.hasNext())
            {
                scripts.add((String) subiter.next());
            }

        }
        else
        {
            scripts.add(hTable.sqlCreateString(dialect, p, defaultCatalog, defaultSchema));
        }

        for (Constraint constraint : table.getConstraints())
        {
            ForeignKey fk = new ForeignKey();
            fk.setName(constraint.getName());

            org.hibernate.mapping.Table hFKTable = new org.hibernate.mapping.Table(constraint
                    .getForeignTable());
            fk.setReferencedTable(hFKTable);

            String fkScript = dialect.getAddForeignKeyConstraintString(
                    constraint.getName(),
                    constraint.getColumnsName().toArray(new String[0]),
                    constraint.getForeignTable(),
                    constraint.getForeignColumnsName().toArray(new String[0]),
                    false);

            StringBuffer buf = new StringBuffer("alter table ").append(
                    hTable.getQualifiedName(dialect, defaultCatalog, defaultSchema)).append(
                    fkScript);

            scripts.add(buf.toString());

        }

        return scripts;
    }

    private final static List<org.hibernate.mapping.Column> buildColumnsIdentifiers(
            List<String> columnsName)
    {
        List<org.hibernate.mapping.Column> columnsIdentifiers = new ArrayList<org.hibernate.mapping.Column>();

        for (String columnName : columnsName)
        {
            org.hibernate.mapping.Column c = new org.hibernate.mapping.Column();
            c.setName(columnName);

            columnsIdentifiers.add(c);
        }

        return columnsIdentifiers;
    }
}
