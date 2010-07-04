package com.spreadthesource.tapestry.dbmigration.migrations;

/**
 * Simple interface that defines a migration step.
 * 
 * @author ccordenier
 */
public interface Migration
{
    public void up();

    public void down();
}
