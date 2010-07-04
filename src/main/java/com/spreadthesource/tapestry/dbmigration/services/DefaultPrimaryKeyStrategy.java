package com.spreadthesource.tapestry.dbmigration.services;

import java.sql.Types;
import java.util.Arrays;
import java.util.List;

import com.spreadthesource.tapestry.dbmigration.ColumnDef;
import com.spreadthesource.tapestry.dbmigration.utils.MigrationUtils;

public class DefaultPrimaryKeyStrategy implements PrimaryKeyStrategy
{

    public List<ColumnDef> getPrimaryKeys(String tableName)
    {
        ColumnDef pk = new ColumnDef(MigrationUtils.buildPkColumnId(tableName), Types.BIGINT).setUnique(
                true).setPrimary(true).setIdentityGenerator("identity");
        return Arrays.asList(pk);
    }

}
