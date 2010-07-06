package com.spreadthesource.tapestry.dbmigration.migrations.impl;

import com.spreadthesource.tapestry.dbmigration.migrations.CreateTable;
import com.spreadthesource.tapestry.dbmigration.migrations.CreateTableContext;

public class CreateTableImpl implements CreateTable
{
    private String name;

    public CreateTableImpl(String name)
    {
        super();
        this.name = name;
    }

    public void run(CreateTableContext ctx)
    {
        ctx.setName(name);
    }

}
