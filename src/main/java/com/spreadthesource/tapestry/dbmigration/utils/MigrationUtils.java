package com.spreadthesource.tapestry.dbmigration.utils;

import java.util.Arrays;
import java.util.List;

import org.hibernate.dialect.Dialect;

import com.spreadthesource.tapestry.dbmigration.ColumnDef;

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
        return singularizeWords((tableName.charAt(0) + tableName.substring(1).replaceAll("([A-Z])", "_$1"))
                .toLowerCase(), "_")
                + "_id";
    }
    
    public final static String singularizeWords(String words, String separator) {
        StringBuffer singularWords = new StringBuffer();
        
        String[] wordsArray = words.split(separator);
        
        int length = wordsArray.length;
        for (int i = 0; i < length;i++)
        {
            String word = wordsArray[i];
            
            singularWords.append(singularize(word));
            
            if (i + 1 < length)
                singularWords.append("_");
        }
        
        return singularWords.toString();   
    }
    
    /**
     * Convert a plural word to a singular word.
     * If word ends with "ies", like activities, then "ies" is replaced by "y".
     * Else final "s" is removed.
     */
    public final static String singularize(String pluralWord) {
        if (!pluralWord.endsWith("s"))
            return pluralWord;
        
        int length = pluralWord.length();
        return pluralWord.endsWith("ies") ? pluralWord.substring(0, length - 3) + "y" : pluralWord.substring(0, length - 1);
    }

    /**
     * Build a hibernate column given our wrapper representation.
     * 
     * @param column
     * @return
     */
    public static org.hibernate.mapping.Column buildHibCol(Dialect dialect, final ColumnDef column)
    {
        org.hibernate.mapping.Column hColumn = new org.hibernate.mapping.Column(column.getName());

        String typeName;
        Integer typeLength = column.getLength();

        if (typeLength == null)
        {
            typeName = dialect.getTypeName(column.getType());
        }
        else
        {
            typeName = dialect.getTypeName(column.getType(), typeLength, 0, 0);
        }

        hColumn.setSqlType(typeName);
        hColumn.setUnique(column.isUnique());
        hColumn.setNullable(!column.isNotNull());
        hColumn.setLength(column.getLength());
        hColumn.setScale(3);

        return hColumn;
    }

}
