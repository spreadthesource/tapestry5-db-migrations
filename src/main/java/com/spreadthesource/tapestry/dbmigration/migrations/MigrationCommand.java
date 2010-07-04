package com.spreadthesource.tapestry.dbmigration.migrations;

/**
 * Every command represent an atomic action on the database.
 * 
 * @author ccordenier
 * @param <T>
 *            Each command will be associated to a execution context.
 */
interface MigrationCommand<T extends MigrationContext>
{

    void run(T ctx);

}
