package com.spreadthesource.tapestry.dbmigration.test;

import org.apache.tapestry5.ioc.IOCUtilities;
import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;

import com.spreadthesource.tapestry.dbmigration.services.MigrationManager;
import com.spreadthesource.tapestry.dbmigration.services.MigrationModule;
import com.spreadthesource.tapestry.dbmigration.services.TestModule;

public class Main
{

    public static void main(String[] args)
    {
        RegistryBuilder builder = new RegistryBuilder();
        IOCUtilities.addDefaultModules(builder);
        builder.add(MigrationModule.class);
        builder.add(TestModule.class);
        Registry registry = builder.build();
        registry.performRegistryStartup();

        MigrationManager manager = registry.getService(MigrationManager.class);

        manager.initialize();
        System.out.println("Current version number is : " + manager.current());
        manager.up();

    }

}
