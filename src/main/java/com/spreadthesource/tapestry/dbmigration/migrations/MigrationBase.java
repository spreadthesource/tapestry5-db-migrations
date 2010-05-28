package com.spreadthesource.tapestry.dbmigration.migrations;

import java.util.ArrayList;
import java.util.List;

import com.spreadthesource.tapestry.dbmigration.data.Table;
import com.spreadthesource.tapestry.dbmigration.services.MigrationHelper;

public abstract class MigrationBase implements Migration
{
    private MigrationHelper helper;
    
    private List<String> pendingSQL;

    public MigrationBase(MigrationHelper helper)
    {
        this.helper = helper;
        
        this.pendingSQL = new ArrayList<String>();
    }

    public final void createTable(Table table)
    {
        addSQL(helper.createTable(table));
    }

    public final void dropTable(String tableName)
    {
        addSQL(helper.dropTable(tableName));
    }
    
    public void addSQL(String sql) {
        pendingSQL.add(sql);
    }
    
    public List<String> getPendingSQL() {
        return pendingSQL;
    }
}
