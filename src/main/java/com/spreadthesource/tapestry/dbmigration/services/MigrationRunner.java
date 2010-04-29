package com.spreadthesource.tapestry.dbmigration.services;

public interface MigrationRunner
{
    /**
     * Play SQL commands
     * @param sql
     */
    public void run(String... sql);
}