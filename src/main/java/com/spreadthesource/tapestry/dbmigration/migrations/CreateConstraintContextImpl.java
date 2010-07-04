package com.spreadthesource.tapestry.dbmigration.migrations;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.ioc.internal.util.Defense;
import org.hibernate.dialect.Dialect;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.UniqueKey;

public class CreateConstraintContextImpl implements CreateConstraintContext
{
    private final Dialect dialect;

    private final String defaultCatalog;

    private final String defaultSchema;

    private String name;

    private List<String> foreignKeys = new ArrayList<String>();

    private Map<String, String[]> uniqueTuples = new Hashtable<String, String[]>();

    public CreateConstraintContextImpl(Dialect dialect, String defaultCatalog, String defaultSchema)
    {
        super();
        this.dialect = dialect;
        this.defaultCatalog = defaultCatalog;
        this.defaultSchema = defaultSchema;
    }

    public void setForeignKey(String name, String foreignTable, String[] fromColumns,
            String[] toColumns)
    {
        Defense.notNull(name, "Constraint name");
        String fkScript = dialect.getAddForeignKeyConstraintString(
                name,
                fromColumns,
                foreignTable,
                toColumns,
                false);
        foreignKeys.add(fkScript);
    }

    public void setName(String tableName)
    {
        this.name = tableName;
    }

    public void setUnique(String name, String... columns)
    {
        Defense.notNull(name, "Constraint name");
        uniqueTuples.put(name, columns);
    }

    public List<String> getQueries()
    {
        if (name == null) { throw new IllegalArgumentException(
                "Table name cannot be null to generate constraint query string."); }

        List<String> result = new ArrayList<String>();

        org.hibernate.mapping.Table hTable = new org.hibernate.mapping.Table(name);

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
