package com.spreadthesource.tapestry.dbmigration.data;

import java.util.Map;

public class Column
{
    private String name;
    
    private Map<String, String> meta;
    
    private int type;
    
    public Column(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public void setMeta(Map<String, String> meta)
    {
        this.meta = meta;
    }

    public Map<String, String> getMeta()
    {
        return meta;
    }
    
    public int getType() {
        return type;
    }
    
    public String getName() {
        return name;
    }
}
