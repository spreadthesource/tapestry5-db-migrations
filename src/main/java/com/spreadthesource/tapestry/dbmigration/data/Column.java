package com.spreadthesource.tapestry.dbmigration.data;

import java.util.Map;

import org.hibernate.cfg.Ejb3Column;
import org.hibernate.type.NullableType;

public class Column
{
    private String name;
    
    private boolean unique;
    
    private int length;
    
    private Map<String, String> meta;
    
    private NullableType type;
    
    public Column(String name, NullableType type) {
        this.name = name;
        this.type = type;
        
        this.length = Ejb3Column.DEFAULT_COLUMN_LENGTH;
    }

    public void setMeta(Map<String, String> meta)
    {
        this.meta = meta;
    }

    public Map<String, String> getMeta()
    {
        return meta;
    }
    
    public NullableType getType() {
        return type;
    }
    
    public int getSQLType() {
        return type.sqlType();
    }
    
    public String getName() {
        return name;
    }

    public void setUnique(boolean unique)
    {
        this.unique = unique;
    }

    public boolean isUnique()
    {
        return unique;
    }

    public void setLength(int length)
    {
        this.length = length;
    }

    public int getLength()
    {
        return length;
    }
}
