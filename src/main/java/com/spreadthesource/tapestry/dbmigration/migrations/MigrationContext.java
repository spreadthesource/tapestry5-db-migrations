package com.spreadthesource.tapestry.dbmigration.migrations;

import java.util.List;

public interface MigrationContext
{
    List<String> getQueries();
}
