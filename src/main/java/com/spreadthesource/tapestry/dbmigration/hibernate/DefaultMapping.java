/**
 * 
 */
package com.spreadthesource.tapestry.dbmigration.hibernate;

import org.hibernate.MappingException;
import org.hibernate.engine.Mapping;
import org.hibernate.id.factory.DefaultIdentifierGeneratorFactory;
import org.hibernate.id.factory.IdentifierGeneratorFactory;
import org.hibernate.type.Type;

/**
 * TODO Default empty mapping
 * 
 * @author ccordenier
 */
public class DefaultMapping implements Mapping
{
    public IdentifierGeneratorFactory getIdentifierGeneratorFactory()
    {
        return new DefaultIdentifierGeneratorFactory();
    }

    public String getIdentifierPropertyName(String arg0) throws MappingException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Type getIdentifierType(String arg0) throws MappingException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Type getReferencedPropertyType(String arg0, String arg1) throws MappingException
    {
        // TODO Auto-generated method stub
        return null;
    }
}