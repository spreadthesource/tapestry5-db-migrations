package com.spreadthesource.tapestry.dbmigration.migrations.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.mapping.Column;
import org.hibernate.mapping.Table;
import org.hibernate.tool.hbm2ddl.TableMetadata;

import com.spreadthesource.tapestry.dbmigration.ColumnDef;
import com.spreadthesource.tapestry.dbmigration.migrations.DefaultMapping;
import com.spreadthesource.tapestry.dbmigration.migrations.UpdateTableContext;
import com.spreadthesource.tapestry.dbmigration.utils.MigrationUtils;

public class UpdateTableContextImpl extends AbstractTableContext implements UpdateTableContext
{

    public List<String> getQueries()
    {
        if (name == null) { throw new IllegalArgumentException(
                "Table name cannot be null to generate constraint query string."); }

        List<String> result = new ArrayList<String>();

        Table hTable = new Table(name);

        for (final ColumnDef column : columns)
        {
            Column hColumn = MigrationUtils.buildHibCol(dialect, column);
            hTable.addColumn(hColumn);
        }

        TableMetadata tableMetadata = databaseMetadata.getTableMetadata(
                name,
                defaultSchema,
                defaultCatalog,
                false);

        Iterator subiter = hTable.sqlAlterStrings(
                dialect,
                new DefaultMapping(),
                tableMetadata,
                defaultCatalog,
                defaultSchema);

        while (subiter.hasNext())
        {
            result.add((String) subiter.next());
        }

        return result;
    }

}
