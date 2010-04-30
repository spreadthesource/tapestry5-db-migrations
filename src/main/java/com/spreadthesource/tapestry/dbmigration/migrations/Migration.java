package com.spreadthesource.tapestry.dbmigration.migrations;


public interface Migration
{
    public void up();

    public void down();
}
