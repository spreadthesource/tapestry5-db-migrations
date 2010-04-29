package com.spreadthesource.tapestry.dbmigration.services;

import java.sql.Types;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.services.ClassNameLocator;
import org.hibernate.Session;
import org.slf4j.Logger;

import com.spreadthesource.tapestry.dbmigration.MigrationSymbolConstants;
import com.spreadthesource.tapestry.dbmigration.annotations.Version;
import com.spreadthesource.tapestry.dbmigration.data.Table;

public class MigrationManagerImpl implements MigrationManager
{
    private final SortedMap<Integer, String> classes;

    private Logger log;

    private MigrationRunner runner;

    private MigrationHelper helper;

    private String versioningTableName;

    public MigrationManagerImpl(
            Collection<String> packages,
            Logger logger,
            Session session,
            ClassNameLocator locator,
            MigrationRunner runner,
            MigrationHelper helper,
            @Inject @Symbol(MigrationSymbolConstants.VERSIONING_TABLE_NAME) String versioningTableName)
    {
        this.log = logger;
        this.classes = new TreeMap<Integer, String>();
        this.versioningTableName = versioningTableName;
        this.helper = helper;
        this.runner = runner;

        for (String packageName : packages)
        {
            log.debug("Looking for migrations into: " + packageName);

            Collection<String> classesForPackage = locator.locateClassNames(packageName);

            for (String className : classesForPackage)
            {
                Integer version = getMigrationVersion(className);

                if (version != null)
                {
                    if (classes.containsKey(version)) { throw new RuntimeException(
                            "More than one migration are having the same version"); }

                    log.debug("Found: " + className + " version:" + version);
                    classes.put(version, className);
                }
            }
        }
    }

    public Integer current()
    {
        // get current version number
        return null;
    }

    public Integer down()
    {
        // 1 - get current version number (with current)
        // 2 - get corresponding class
        // 3 - play down()
        // 4 - get generated SQL and play it with MigrationRunner
        // 5 - delete last version in versionning table
        return null;
    }

    public Integer migrate()
    {
        for (Integer version : classes.keySet())
        {
            // 1 - check this.current() is contained in this.classes
            // 2 - check this.current() != last key of this.classes
            // 3 - play this.up() method

            log.debug(version + " ");
        }
        return null;
    }

    public Integer up()
    {
        // 1 - get current version number (with current)
        // 2 - get corresponding class
        // 3 - play up()
        // 4 - get generated SQL and play it with MigrationRunner
        // 5 - delete last version in versionning table
        return null;
    }

    public void initialize()
    {
        versioningTableName = "nanane";
        
        if (helper.checkIfTableExists(versioningTableName)) return;

        log.debug("Schema is not versionned. Creation versionning table: " + versioningTableName);

        Table versions = new Table(versioningTableName);
        versions.addColumn("version", Types.INTEGER);
        versions.addColumn("anotherCol", Types.DECIMAL);
        versions.addColumn("anotherCol2", Types.CLOB);
        

        String sql = helper.createTable(versions);
        
        runner.run(sql);
    }

    private Integer getMigrationVersion(String className)
    {
        try
        {
            List<Class<?>> interfaces = Arrays.asList(Class.forName(className).getInterfaces());

            if (!interfaces.contains(Migration.class)) return null;

            Version version = Class.forName(className).getAnnotation(Version.class);

            if (version == null) return null;

            return version.value();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
