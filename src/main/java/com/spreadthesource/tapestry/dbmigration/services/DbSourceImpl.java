package com.spreadthesource.tapestry.dbmigration.services;

import java.util.List;
import java.util.Properties;

import org.apache.tapestry5.hibernate.HibernateConfigurer;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.cfg.Mappings;
import org.hibernate.dialect.Dialect;
import org.hibernate.jdbc.util.FormatStyle;
import org.hibernate.jdbc.util.Formatter;
import org.hibernate.util.PropertiesHelper;
import org.slf4j.Logger;

import com.spreadthesource.tapestry.dbmigration.MigrationSymbolConstants;
import com.spreadthesource.tapestry.dbmigration.hibernate.ConnectionHelper;
import com.spreadthesource.tapestry.dbmigration.hibernate.ManagedProviderConnectionHelper;

public class DbSourceImpl implements DbSource
{

    private Configuration configuration;

    private ConnectionHelper connectionHelper;

    private Dialect dialect;

    private String defaultCatalog;

    private String defaultSchema;

    private Formatter formatter;

    private Mappings mappings;

    public DbSourceImpl(
            List<HibernateConfigurer> hibConfigurers,
            Logger log,
            @Inject @Symbol(MigrationSymbolConstants.DEFAULT_HIBERNATE_CONFIGURATION) boolean defaultConfiguration)
    {
        this.configuration = new Configuration();

        if (defaultConfiguration)
        {
            configuration.configure();
        }

        for (HibernateConfigurer configurer : hibConfigurers)
        {
            configurer.configure(configuration);
        }

        Properties properties = configuration.getProperties();

        this.dialect = Dialect.getDialect(properties);
        this.connectionHelper = new ManagedProviderConnectionHelper(properties);

        this.defaultCatalog = properties.getProperty(Environment.DEFAULT_CATALOG);
        this.defaultSchema = properties.getProperty(Environment.DEFAULT_SCHEMA);

        this.formatter = (PropertiesHelper.getBoolean(Environment.FORMAT_SQL, properties) ? FormatStyle.DDL
                : FormatStyle.NONE).getFormatter();

        mappings = configuration.createMappings();
        configuration.buildMappings();
    }

    public Dialect getDialect()
    {
        return this.dialect;
    }

    public String getDefaultSchema()
    {
        return this.defaultSchema;
    }

    public String getDefaultCatalog()
    {
        return this.defaultCatalog;
    }

    public ConnectionHelper getConnectionHelper()
    {
        return this.connectionHelper;
    }

    public Formatter getFormatter()
    {
        return this.formatter;
    }

    public Mappings getMappings()
    {
        return mappings;
    }
}
