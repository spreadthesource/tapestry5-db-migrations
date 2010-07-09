package com.spreadthesource.tapestry.dbmigration.command;

import com.spreadthesource.tapestry.dbmigration.migrations.MigrationContext;

/**
 * Every command represents a list of consistent queries to execute on the database.
 * 
 * @author ccordenier
 * @param <T>
 *            Each command will be associated to a execution context.
 */
public interface MigrationCommand<T extends MigrationContext>
{

    void run(T ctx);

}
