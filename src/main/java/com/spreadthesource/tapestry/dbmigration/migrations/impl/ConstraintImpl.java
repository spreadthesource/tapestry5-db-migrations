package com.spreadthesource.tapestry.dbmigration.migrations.impl;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.hibernate.mapping.Column;
import org.hibernate.mapping.UniqueKey;

import com.spreadthesource.tapestry.dbmigration.migrations.Constraint;

public class ConstraintImpl extends AbstractMigrationContext implements Constraint
{
    private String tableName;

    private List<String> foreignKeys = new ArrayList<String>();

    private Map<String, String[]> uniqueTuples = new Hashtable<String, String[]>();

    public void setForeignKey(String name, String foreignTable, String[] fromToColumns)
    {
        assert name != null;
        assert (fromToColumns.length % 2 == 0);

        List<String> fromColumns = new ArrayList<String>();
        List<String> toColumns = new ArrayList<String>();

        int i = 0;
        for (String column : fromToColumns)
        {
            if (i % 2 == 0)
                fromColumns.add(column);
            else
                toColumns.add(column);

            i++;
        }

        String fkScript = dialect.getAddForeignKeyConstraintString(
                name,
                fromColumns.toArray(new String[0]),
                foreignTable,
                toColumns.toArray(new String[0]),
                false);
        foreignKeys.add(fkScript);
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public void setUnique(String name, String... columns)
    {
        assert name != null;
        uniqueTuples.put(name, columns);
    }

    public List<String> getQueries()
    {
        if (tableName == null) { throw new IllegalArgumentException(
                "Table name cannot be null to generate constraint query string."); }

        List<String> result = new ArrayList<String>();

        org.hibernate.mapping.Table hTable = new org.hibernate.mapping.Table(tableName);

        // Add foreign keys alter queries
        for (String query : foreignKeys)
        {
            StringBuffer buf = new StringBuffer("alter table ").append(
                    hTable.getQualifiedName(dialect, defaultCatalog, defaultSchema)).append(query);
            result.add(buf.toString());
        }

        // Add uniques keys tuples
        for (String name : uniqueTuples.keySet())
        {
            UniqueKey uniqueKey = new UniqueKey();
            uniqueKey.setTable(hTable);
            uniqueKey.setName(name);
            for (String columnName : uniqueTuples.get(name))
            {
                uniqueKey.addColumn(new Column(columnName));
            }
            result.add(uniqueKey.sqlCreateString(dialect, null, defaultCatalog, defaultSchema));
        }

        return result;
    }

}
