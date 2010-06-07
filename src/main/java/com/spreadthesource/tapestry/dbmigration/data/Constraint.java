package com.spreadthesource.tapestry.dbmigration.data;

import java.util.ArrayList;
import java.util.List;

public class Constraint
{
    private String name;

    private List<String> columnsName;

    private String foreignTable;

    private List<String> foreignColumnsName;

    public Constraint(String name, String foreignTable)
    {
        this.name = name;
        this.foreignTable = foreignTable;
        this.columnsName = new ArrayList<String>();
        this.foreignColumnsName = new ArrayList<String>();
    }


    public String getForeignTable()
    {
        return foreignTable;
    }

    public void addConstraint(String column, String foreignColumn)
    {
        columnsName.add(column);
        foreignColumnsName.add(foreignColumn);
    }

    public String getName()
    {
        return this.name;
    }

    public List<String> getColumnsName()
    {
        return this.columnsName;
    }

    public List<String> getForeignColumnsName()
    {
        return this.foreignColumnsName;
    }
}
