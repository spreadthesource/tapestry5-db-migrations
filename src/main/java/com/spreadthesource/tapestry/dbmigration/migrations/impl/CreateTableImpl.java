package com.spreadthesource.tapestry.dbmigration.migrations.impl;

import com.spreadthesource.tapestry.dbmigration.command.CreateTable;
import com.spreadthesource.tapestry.dbmigration.migrations.Table;

public class CreateTableImpl implements CreateTable
{
    private String name;

    public CreateTableImpl(String name)
    {
        super();
        this.name = name;
    }

    public void run(Table ctx)
    {
        ctx.setName(name);
    }

}
