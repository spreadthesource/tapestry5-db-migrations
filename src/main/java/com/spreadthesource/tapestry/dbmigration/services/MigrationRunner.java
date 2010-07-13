package com.spreadthesource.tapestry.dbmigration.services;

import java.sql.ResultSet;
import java.util.List;

import org.apache.tapestry5.ioc.Invocation;


public interface MigrationRunner
{
    
    /**
     * Simply Check if the table exists in the database metadatas.
     * 
     * @param tableName
     * @return
     */
    boolean checkIfTableExists(String tableName);

    /**
     * Play SQL commands for DELETE, INSERT, UPDATE
     * @param sql
     */
    public void update(String... sql);
    public void update(List<String> sql);
    
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