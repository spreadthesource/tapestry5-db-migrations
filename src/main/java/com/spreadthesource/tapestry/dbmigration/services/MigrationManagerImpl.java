package com.spreadthesource.tapestry.dbmigration.services;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.tapestry5.ioc.ObjectLocator;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.services.ClassNameLocator;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.sql.Insert;
import org.hibernate.sql.JoinFragment;
import org.hibernate.sql.QuerySelect;
import org.slf4j.Logger;

import com.spreadthesource.tapestry.dbmigration.MigrationSymbolConstants;
import com.spreadthesource.tapestry.dbmigration.annotations.Version;
import com.spreadthesource.tapestry.dbmigration.init.SchemaInitialization;
import com.spreadthesource.tapestry.dbmigration.migrations.Migration;
import com.spreadthesource.tapestry.dbmigration.migrations.MigrationBase;
import com.spreadthesource.tapestry.dbmigration.utils.MigrationUtils;

public class MigrationManagerImpl implements MigrationManager
{
    private final SortedMap<Integer, String> classes;

    private Logger log;

    private MigrationRunner runner;

    private MigrationHelper helper;

    private ObjectLocator objectLocator;

    private String versioningTableName;

    public MigrationManagerImpl(
            Collection<String> packages,
            Logger logger,
            Session session,
            ClassNameLocator classNameLocator,
            ObjectLocator objectLocator,
            MigrationRunner runner,
            MigrationHelper helper,
            @Inject @Symbol(MigrationSymbolConstants.VERSIONING_TABLE_NAME) String versioningTableName)
    {
        this.log = logger;
        this.classes = new TreeMap<Integer, String>();
        this.versioningTableName = versioningTableName;
        this.helper = helper;
        this.runner = runner;
        this.objectLocator = objectLocator;

        for (String packageName : packages)
        {
            log.debug("Looking for migrations into: " + packageName);

            Collection<String> classesForPackage = classNameLocator.locateClassNames(packageName);

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
        QuerySelect q = new QuerySelect(helper.getDialect());
        q.addSelectFragmentString("version");

        JoinFragment from = q.getJoinFragment();
        from.addJoins(" " + versioningTableName, "");

        q.addOrderBy("id DESC");

        ResultSet r = runner.query(q.toQueryString());

        try
        {
            if (r.next()) {
                Integer version = r.getInt("version");
                log.debug("Current version is : " + version);
                return version;
            }
                
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public Integer down()
    {
        Integer current = current();
        if (current.equals(0))
            return current;
        
        String className = classes.get(current);

        if (className == null)
            throw new RuntimeException("Can not play down() for migration version: " + current);

        Migration migration = getMigration(className);

        migration.down();
        
        runner.update(migration.getPendingSQL());

        SortedMap<Integer, String> previousMigrations = (new TreeMap<Integer, String>(classes)).headMap(current);

        if (previousMigrations.size() < 1) {
            recordVersion(0);
            return 0;
        }

        Integer previous = previousMigrations.lastKey();

        recordVersion(previous);

        return current();
    }

    public Integer up()
    {
        Integer current = current();
        
        SortedMap<Integer, String> pendingMigrations = (new TreeMap<Integer, String>(classes)).tailMap(current);

        pendingMigrations.remove(current);
        Iterator<Integer> iterator = pendingMigrations.keySet().iterator();

        if (iterator.hasNext())
        {
            Integer next = iterator.next();

            String className = classes.get(next);

            Migration migration = getMigration(className);

            migration.up();

            runner.update(migration.getPendingSQL());

            current = getMigrationVersion(className);

            recordVersion(current);
        }

        return current;
    }

    public void initialize()
    {
        if (helper.checkIfTableExists(versioningTableName)) return;

        log.info("Schema is not versionned. Creating versionning table: " + versioningTableName
                + " (playing " + SchemaInitialization.class.getCanonicalName() + ")");

        Migration schemaInitialization = getMigration(SchemaInitialization.class.getCanonicalName());

        schemaInitialization.up();

        runner.update(schemaInitialization.getPendingSQL());

        recordVersion(0);
    }

    public Integer migrate()
    {
        Integer current = current();
        
        if (current == null)
            initialize();
        
        Integer next = up();

        while (!current.equals(next))
        {
            current = next;
            
            next = up();
        }

        return current;
    }

    public Integer reset()
    {
        Integer current = current();
        Integer previous = down();

        while (previous != current)
        {
            current = previous;
            previous = down();
        }

        return current;
    }

    private void recordVersion(Integer version)
    {
        Date now = new Date(System.currentTimeMillis());

        Insert insert = new Insert(helper.getDialect());
        insert.setTableName(versioningTableName);
        insert.addColumn("version", version.toString());
        insert.addColumn("datetime","'" + Hibernate.TIMESTAMP.toString(now) + "'");

        runner.update(insert.toStatementString());
    }

    private Integer getMigrationVersion(String className)
    {
        try
        {
            if (MigrationUtils.checkIfImplements(Class.forName(className), MigrationBase.class)) return null;

            Version version = Class.forName(className).getAnnotation(Version.class);

            if (version == null) return null;

            return version.value();
        }
        catch (ClassNotFoundException e)
        {
            throw new RuntimeException(e);
        }
    }

    private Migration getMigration(String className)
    {
        try
        {
            if (MigrationUtils.checkIfImplements(Class.forName(className), MigrationBase.class)) return null;

            Version version = Class.forName(className).getAnnotation(Version.class);

            if (version == null) return null;

            return (Migration) objectLocator.autobuild(Class.forName(className));
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            log.error("Error when trying to get an instance of migration", e);
        }

        return null;
    }
}
