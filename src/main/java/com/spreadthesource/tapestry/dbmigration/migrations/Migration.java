package com.spreadthesource.tapestry.dbmigration.migrations;

import java.util.List;


public interface Migration
{
    public void up();

    public void down();
    
    public List<String> getPendingSQL();
}
