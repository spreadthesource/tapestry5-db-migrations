package com.spreadthesource.tapestry.dbmigration.migrations.impl;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.mapping.Table;

import com.spreadthesource.tapestry.dbmigration.ColumnDef;
import com.spreadthesource.tapestry.dbmigration.migrations.DefaultMapping;
import com.spreadthesource.tapestry.dbmigration.migrations.JoinTableContext;
import com.spreadthesource.tapestry.dbmigration.utils.MigrationUtils;

/**
 * Use this context to join tables.
 * 
 * @author ccordenier
 */
public class JoinTableContextImpl extends AbstractMigrationContext implements JoinTableContext
{
    private List<String> queries = new ArrayList<String>();

    public void join(String name, String tableOne, String tableTwo)
    {
        this.join(name, tableOne, pkStrategy.buildColumnId(tableOne), tableTwo, pkStrategy
                .buildColumnId(tableTwo));
    }

    public void join(String tableOne, String tableTwo)
    {
        this.join(tableOne + tableTwo, tableOne, tableTwo);
    }

    public void join(String tableOne, String pkOne, String tableTwo, String pkTwo)
    {
        this.join(tableOne + tableTwo, tableOne, pkOne, tableTwo, pkTwo);
    }

    private void join(String name, String tableOne, String pkOne, String tableTwo, String pkTwo)
    {
        // Create join table
        Table joinTable = new Table(name);
        ColumnDef def1 = new ColumnDef(pkOne, Types.BIGINT).setNotNull(true);
        ColumnDef def2 = new ColumnDef(pkTwo, Types.BIGINT).setNotNull(true);
        joinTable.addColumn(MigrationUtils.buildHibCol(dialect, def1));
        joinTable.addColumn(MigrationUtils.buildHibCol(dialect, def2));
        queries.add(joinTable.sqlCreateString(
                dialect,
                new DefaultMapping(),
                defaultCatalog,
                defaultSchema));
        
        // Create foreign keys
        String fkScript = dialect.getAddForeignKeyConstraintString(
                name + "_to_" + tableOne,
                new String[]
                { pkOne },
                tableOne,
                new String[]
                { pkOne },
                false);

        StringBuffer buf = new StringBuffer("alter table ").append(
                joinTable.getQualifiedName(dialect, defaultCatalog, defaultSchema))
                .append(fkScript);
        queries.add(buf.toString());

        fkScript = dialect.getAddForeignKeyConstraintString(name + "_to_" + tableTwo, new String[]
        { pkTwo }, tableTwo, new String[]
        { pkTwo }, false);

        buf = new StringBuffer("alter table ").append(
                joinTable.getQualifiedName(dialect, defaultCatalog, defaultSchema))
                .append(fkScript);
        queries.add(buf.toString());
    }

    public List<String> getQueries()
    {
        return queries;
    }

}
