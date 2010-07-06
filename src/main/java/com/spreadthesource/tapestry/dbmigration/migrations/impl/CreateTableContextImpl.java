package com.spreadthesource.tapestry.dbmigration.migrations.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.engine.Mapping;
import org.hibernate.mapping.PrimaryKey;
import org.hibernate.mapping.SimpleValue;

import com.spreadthesource.tapestry.dbmigration.ColumnDef;
import com.spreadthesource.tapestry.dbmigration.migrations.CreateTableContext;
import com.spreadthesource.tapestry.dbmigration.migrations.DefaultMapping;
import com.spreadthesource.tapestry.dbmigration.utils.MigrationUtils;

/**
 * Implement creation context. This will generate a SQL query to be called from the Migration
 * Manager.
 * 
 * @author ccordenier
 */
public class CreateTableContextImpl extends AbstractTableContext implements CreateTableContext
{

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
                org.hibernate.mapping.Column hColumn = MigrationUtils.buildHibCol(dialect, column);
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
                org.hibernate.mapping.Column hColumn = MigrationUtils.buildHibCol(dialect, col);
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
            }
            hTable.setPrimaryKey(primaryKey);
        }
        else
        {
            PrimaryKey primaryKey = new PrimaryKey();
            for (ColumnDef col : pks)
            {
                org.hibernate.mapping.Column hColumn = MigrationUtils.buildHibCol(dialect, col);
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
            }
            hTable.setPrimaryKey(primaryKey);
        }

        Mapping p = new DefaultMapping();

        return Arrays.asList(hTable.sqlCreateString(dialect, p, defaultCatalog, defaultSchema));
    }

}
