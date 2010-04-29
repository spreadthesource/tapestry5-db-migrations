package com.spreadthesource.tapestry.dbmigration.services;

public interface MigrationManager
{
    /**
     * Try to connect to database. If database already exists but migration table is not found, then
     * migration table will be created. If there is not database will also be created if possible
     * (mainly when using embedded databases such as H2 and HSQLDB)
     * 
     * @throws RuntimeException
     */
    public void initialize();

    /**
     * Apply the next following migration if existing. Will run initialize() before.
     * 
     * @return new database version
     */
    public Integer up();

    /**
     * Revert one migration if possible. Will run initialize() before.
     * 
     * @return new database version
     */
    public Integer down();

    /**
     * @return current database version. Will run initialize() before.
     */
    public Integer current();

    /**
     * Run all pending migrations. Will run initialize() before.
     * 
     * @return new database version
     */
    public Integer migrate();
}
