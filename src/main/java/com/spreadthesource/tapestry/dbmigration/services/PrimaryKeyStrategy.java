package com.spreadthesource.tapestry.dbmigration.services;

import java.util.List;

import com.spreadthesource.tapestry.dbmigration.ColumnDef;

/**
 * @author ccordenier
 */
public interface PrimaryKeyStrategy
{

    /**
     * Get the default list of primary columns.
     *
     * @param tableName The name of table.
     * @return
     */
    List<ColumnDef> getPrimaryKeys(String tableName);
    
    /**
     * This method is called to build a default id column name.
     *
     * @param tableName The name of the table.
     * @return
     */
    String buildColumnId(String tableName);

}
