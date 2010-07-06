package com.spreadthesource.tapestry.dbmigration.migrations.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.mapping.ForeignKey;

import com.spreadthesource.tapestry.dbmigration.migrations.DropContext;

public class DropContextImpl extends AbstractMigrationContext implements DropContext
{
    private List<String> queries = new ArrayList<String>();

    public void dropTable(String table)
    {
        org.hibernate.mapping.Table hTable = new org.hibernate.mapping.Table(table);
        String dropSQL = hTable.sqlDropString(dialect, defaultCatalog, defaultSchema);
        queries.add(dropSQL);
    }

    public void dropForeignKey(String tableName, String fkc)
    {
        org.hibernate.mapping.Table hTable = new org.hibernate.mapping.Table(tableName);
        ForeignKey fk = new ForeignKey();
        fk.setName(fkc);
        fk.setTable(hTable);
        queries.add(fk.sqlDropString(dialect, defaultCatalog, defaultSchema));
    }

    public List<String> getQueries()
    {
        return queries;
    }

}
