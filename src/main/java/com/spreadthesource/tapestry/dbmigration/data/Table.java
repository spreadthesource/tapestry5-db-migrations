package com.spreadthesource.tapestry.dbmigration.data;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.type.NullableType;

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
     *            should be one of the referenced hibernate NullableType
     */
    public Column addColumn(String columnName, NullableType columnType)
    {
        Column c = new Column(columnName, columnType);
        columns.add(c);
        return c;
    }
    
    public Column addColumn(String columnName, NullableType columnType, int length)
    {
        Column c = this.addColumn(columnName, columnType);
        c.setLength(length);
        
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
