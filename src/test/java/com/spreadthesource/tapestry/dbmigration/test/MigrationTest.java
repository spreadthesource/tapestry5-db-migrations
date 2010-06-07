package com.spreadthesource.tapestry.dbmigration.test;

import org.apache.tapestry5.ioc.IOCUtilities;
import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;
import org.testng.annotations.Test;

import com.spreadthesource.tapestry.dbmigration.services.MigrationManager;
import com.spreadthesource.tapestry.dbmigration.services.MigrationModule;

public class MigrationTest
{
    @Test
    public void testModuleStartup()
    {
        RegistryBuilder builder = new RegistryBuilder();
        IOCUtilities.addDefaultModules(builder);
        builder.add(MigrationModule.class);
        Registry registry = builder.build();
        registry.performRegistryStartup();

        MigrationManager manager = registry.getService(MigrationManager.class);

        manager.initialize();

        manager.up();
        manager.up();
        manager.down();
    }

}
