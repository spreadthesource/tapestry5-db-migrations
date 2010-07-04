package com.spreadthesource.tapestry.dbmigration.migrations;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.Mapping;
import org.hibernate.id.factory.DefaultIdentifierGeneratorFactory;
import org.hibernate.id.factory.IdentifierGeneratorFactory;
import org.hibernate.mapping.PrimaryKey;
import org.hibernate.mapping.SimpleValue;
import org.hibernate.type.Type;

import com.spreadthesource.tapestry.dbmigration.ColumnDef;
import com.spreadthesource.tapestry.dbmigration.services.PrimaryKeyStrategy;

/**
 * Implement creation context. This will generate a SQL query to be called from the Migration
 * Manager.
 * 
 * @author ccordenier
 */
public class CreateTableContextImpl implements CreateTableContext
{

    private final Dialect dialect;

    private final String defaultCatalog;

    private final String defaultSchema;

    private final PrimaryKeyStrategy pkStrategy;

    private String name;

    private List<ColumnDef> columns = new ArrayList<ColumnDef>();

    public CreateTableContextImpl(Dialect dialect, String defaultCatalog, String defaultSchema,
            PrimaryKeyStrategy pkStrategy)
    {
        super();
        this.dialect = dialect;
        this.defaultCatalog = defaultCatalog;
        this.defaultSchema = defaultSchema;
        this.pkStrategy = pkStrategy;
    }

    public ColumnDef addBoolean(String name)
    {
        ColumnDef col = new ColumnDef(name, Types.BIT);
        columns.add(col);
        return col;
    }

    public ColumnDef addInteger(String name)
    {
        ColumnDef col = new ColumnDef(name, Types.INTEGER);
        columns.add(col);
        return col;
    }

    public ColumnDef addTimestamp(String name)
    {
        ColumnDef col = new ColumnDef(name, Types.TIMESTAMP);
        columns.add(col);
        return col;
    }

    public ColumnDef addLong(String name)
    {
        ColumnDef col = new ColumnDef(name, Types.BIGINT).setLength(20);
        columns.add(col);
        return col;
    }

    public ColumnDef addString(String name)
    {
        ColumnDef col = new ColumnDef(name, Types.VARCHAR);
        columns.add(col);
        return col;
    }

    public ColumnDef addText(String name)
    {
        ColumnDef col = new ColumnDef(name, Types.LONGVARCHAR);
        columns.add(col);
        return col;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<String> getQueries()
    {
        if (name == null) { throw new IllegalArgumentException("Table name cannot be null"); }

        org.hibernate.mapping.Table hTable = new org.hibernate.mapping.Table(name);
        List<ColumnDef> pks = new ArrayList<ColumnDef>();

        boolean pkFound = false;

        for (final ColumnDef column : columns)
        {
            if (column.isPrimary())
            {
                pkFound = true;
                pks.add(column);
            }
            else
            {
                org.hibernate.mapping.Column hColumn = buildHibCol(column);
                hTable.addColumn(hColumn);
            }
        }

        // Create a default primary key if none is defined
        if (!pkFound)
        {
            List<ColumnDef> cols = pkStrategy.getPrimaryKeys(name);
            PrimaryKey primaryKey = new PrimaryKey();
            for (ColumnDef col : cols)
            {
                org.hibernate.mapping.Column hColumn = buildHibCol(col);
                hTable.addColumn(hColumn);
                primaryKey.addColumn(hColumn);
                if (col.getIdentityGenerator() != null)
                {
                    SimpleValue idValue = new SimpleValue(hTable);
                    idValue.setIdentifierGeneratorStrategy("identity");
                    idValue.setTypeName(dialect.getHibernateTypeName(col.getType()));
                    hColumn.setValue(idValue);
                    hTable.setIdentifierValue(idValue);
                }
                hTable.setPrimaryKey(primaryKey);
            }
        }
        else
        {
            PrimaryKey primaryKey = new PrimaryKey();
            for (ColumnDef col : pks)
            {
                org.hibernate.mapping.Column hColumn = buildHibCol(col);
                hTable.addColumn(hColumn);
                primaryKey.addColumn(hColumn);
                if (col.getIdentityGenerator() != null)
                {
                    SimpleValue idValue = new SimpleValue(hTable);
                    idValue.setIdentifierGeneratorStrategy("identity");
                    idValue.setTypeName(dialect.getHibernateTypeName(col.getType()));
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

        return Arrays.asList(hTable.sqlCreateString(dialect, p, defaultCatalog, defaultSchema));
    }

    /**
     * Build a hibernate column given our wrapper representation.
     * 
     * @param column
     * @return
     */
    private org.hibernate.mapping.Column buildHibCol(final ColumnDef column)
    {
        org.hibernate.mapping.Column hColumn = new org.hibernate.mapping.Column(column.getName());

        String typeName;
        Integer typeLength = column.getLength();

        if (typeLength == null)
        {
            typeName = dialect.getTypeName(column.getType());
        }
        else
        {
            typeName = dialect.getTypeName(column.getType(), typeLength, 0, 0);
        }

        hColumn.setSqlType(typeName);
        hColumn.setUnique(column.isUnique());
        hColumn.setNullable(!column.isNotNull());
        hColumn.setLength(column.getLength());
        hColumn.setScale(3);

        return hColumn;
    }

}
