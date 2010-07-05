package com.spreadthesource.tapestry.dbmigration.migrations;

/**
 * Simple interface that defines a migration step. Each step should be able to do a rollback within
 * its down() method.
 * 
 * @author ccordenier
 */
public interface Migration
{
    /**
     * This method is called during a migration processing before going to the next step.
     */
    public void up();

    /**
     * This method should implement the rollback strategy in case of error, or schema downgrade.
     */
    public void down();
}
