package com.spreadthesource.tapestry.dbmigration.services;

import java.sql.ResultSet;

import org.apache.tapestry5.ioc.Invocation;


public interface MigrationRunner
{
    /**
     * Play SQL commands for DELETE, INSERT, UPDATE
     * @param sql
     */
    public void update(String... sql);
    
    /**
     * Play a SQL single SELECT query and returns result
     * @param sql  
     */
    public ResultSet query(String sql);
    
    /**
     * One ring to rule them all.
     * @param invocation
     */
    void inStatement(Invocation invocation);
}