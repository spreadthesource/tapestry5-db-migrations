package com.spreadthesource.tapestry.dbmigration.migrations;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.dialect.Dialect;
import org.hibernate.mapping.Table;

import com.spreadthesource.tapestry.dbmigration.ColumnDef;
import com.spreadthesource.tapestry.dbmigration.services.PrimaryKeyStrategy;
import com.spreadthesource.tapestry.dbmigration.utils.MigrationUtils;

/**
 * Use this context to join tables.
 * 
 * @author ccordenier
 */
public class JoinTableContextImpl implements JoinTableContext
{
    private final Dialect dialect;

    private final String defaultCatalog;

    private final String defaultSchema;

    private final PrimaryKeyStrategy pkStrategy;

    private List<String> queries = new ArrayList<String>();

    public JoinTableContextImpl(Dialect dialect, String defaultCatalog, String defaultSchema,
            PrimaryKeyStrategy pkStrategy)
    {
        super();
        this.dialect = dialect;
        this.defaultCatalog = defaultCatalog;
        this.defaultSchema = defaultSchema;
        this.pkStrategy = pkStrategy;
    }

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
        ColumnDef def1 = new ColumnDef(pkOne, Types.BIGINT).setPrimary(true).setIdentityGenerator(
                "identity").setUnique(true);
        ColumnDef def2 = new ColumnDef(pkTwo, Types.BIGINT).setPrimary(true).setIdentityGenerator(
                "identity").setUnique(true);
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
