package com.spreadthesource.tapestry.dbmigration;

import java.util.Map;

import org.hibernate.cfg.Ejb3Column;

public class ColumnDef
{
    private String name;
    
    private String identityGenerator;
    
    private boolean notNull;
    
    private boolean primary;
    
    private boolean unique;
    
    private Integer length;
    
    private Map<String, String> meta;
    
    private int type;
    
    public ColumnDef(String name, int type) {
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
    
    public int getType() {
        return type;
    }
    
    //public int getSQLType() {
        //return type.sqlType();
    //}
    
    public String getName() {
        return name;
    }

    public ColumnDef setUnique(boolean unique)
    {
        this.unique = unique;
        return this;
    }

    public boolean isUnique()
    {
        return unique || primary;
    }

    public ColumnDef setLength(int length)
    {
        this.length = length;
        return this;
    }

    public Integer getLength()
    {
        return length;
    }

    public ColumnDef setPrimary(boolean primary)
    {
        this.primary = primary;
        return this;
    }

    public boolean isPrimary()
    {
        return primary;
    }

    public ColumnDef setNotNull(boolean notNull)
    {
        this.notNull = notNull;
        return this;
    }

    public boolean isNotNull()
    {
        return notNull;
    }


    public ColumnDef setIdentityGenerator(String identityGenerator)
    {
        this.identityGenerator = identityGenerator;
        return this;
    }

    public String getIdentityGenerator()
    {
        return identityGenerator;
    }
}
