package com.spreadthesource.tapestry.dbmigration.utils;

import java.util.Arrays;
import java.util.List;

public class MigrationUtils
{
    public final static boolean checkIfImplements(Class<?> clazz, Class<?> inter)
    {
        List<Class<?>> interfaces = Arrays.asList(clazz.getInterfaces());

        if (interfaces.contains(inter)) return true;

        Class<?> parent = clazz.getSuperclass();
        while (parent != Object.class)
            return checkIfImplements(parent, inter);

        return false;
    }

    public final static String buildPkColumnId(String tableName)
    {
        return (tableName.charAt(0) + tableName.substring(1).replaceAll("([A-Z])", "_$1"))
                .toLowerCase()
                + "_id";
    }
}
