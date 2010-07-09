package com.spreadthesource.tapestry.dbmigration.migrations.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.dialect.Dialect;

import com.spreadthesource.tapestry.dbmigration.migrations.SqlQuery;

public class SqlQueryImpl extends AbstractMigrationContext implements SqlQuery
{

    List<String> queries = new ArrayList<String>();

    public void addSql(String query)
    {
        queries.add(query);
    }

    public <T extends Dialect> void addSql(String query, Class<T> dialect)
    {
        // Check the targeted db
        if (this.dialect.getClass().equals(dialect))
        {
            queries.add(query);
        }
    }

    public List<String> getQueries()
    {
        return queries;
    }

}
