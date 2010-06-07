package com.spreadthesource.tapestry.dbmigration.data;

import java.util.ArrayList;
import java.util.List;

public class Table
{
    private final String name;

    private List<Column> columns;

    private List<Constraint> constraints;

    public Table(String name)
    {
        this.name = name;
        this.columns = new ArrayList<Column>();
        this.constraints = new ArrayList<Constraint>();
    }

    /**
     * @param columnName
     *            name of the column
     * @param columnType
     *            should be one of the referenced hibernate NullableType
     */
    public Column addColumn(String columnName, int columnType)
    {
        Column c = new Column(columnName, columnType);
        columns.add(c);
        return c;
    }

    public Column addColumn(String columnName, int columnType, int length)
    {
        Column c = this.addColumn(columnName, columnType);
        c.setLength(length);

        return c;
    }

    public Constraint addForeignKey(String name, String foreignTable)
    {
        Constraint c = new Constraint(name, foreignTable);

        constraints.add(c);

        return c;
    }

    public String getName()
    {
        return name;
    }

    public List<Column> getColumns()
    {
        return columns;
    }

    public List<Constraint> getConstraints()
    {
        return constraints;
    }
}
