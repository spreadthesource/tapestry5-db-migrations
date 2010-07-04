package com.spreadthesource.tapestry.dbmigration.services;

import java.util.List;

import com.spreadthesource.tapestry.dbmigration.ColumnDef;

/**
 * @author ccordenier
 */
public interface PrimaryKeyStrategy
{

    List<ColumnDef> getPrimaryKeys(String tableName);

}
