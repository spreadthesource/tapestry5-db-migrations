package com.spreadthesource.tapestry.dbmigration.services;


public interface Migration
{
    public void up();

    public void down();
}
