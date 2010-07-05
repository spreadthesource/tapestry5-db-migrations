package com.spreadthesource.tapestry.dbmigration.migrations;

import java.util.List;

/**
 * The context will implement all the logic for building queries.
 *
 * @author ccordenier
 *
 */
public interface MigrationContext
{
    List<String> getQueries();
}
