package com.spreadthesource.tapestry.dbmigration.migrations.impl;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.spreadthesource.tapestry.dbmigration.ColumnDef;
import com.spreadthesource.tapestry.dbmigration.migrations.TableContext;

/**
 * Base class for MigrationContext that manipulates Tables.
 * 
 * @author ccordenier
 */
public abstract class AbstractTableContext extends AbstractMigrationContext implements TableContext
{

    protected String name;

    protected List<ColumnDef> columns = new ArrayList<ColumnDef>();

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

    public ColumnDef addDate(String name)
    {
        ColumnDef col = new ColumnDef(name, Types.TIME);
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

}
