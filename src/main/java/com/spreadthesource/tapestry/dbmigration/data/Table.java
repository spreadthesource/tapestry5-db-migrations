package com.spreadthesource.tapestry.dbmigration.data;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class Table
{
    private final String name;

    private List<Column> columns;

    public Table(String name)
    {
        this.name = name;
        this.columns = new ArrayList<Column>();
    }

    /**
     * @param columnName
     *            name of the column
     * @param columnType
     *            should be one of the referenced {@link Types}
     */
    public Column addColumn(String columnName, int columnType)
    {
        Column c = new Column(columnName, columnType);
        columns.add(c);
        return c;
    }

    public void dropTable()
    {

    }

    public String getName()
    {
        return this.name;
    }

    public List<Column> getColumns()
    {
        return this.columns;
    }
}
