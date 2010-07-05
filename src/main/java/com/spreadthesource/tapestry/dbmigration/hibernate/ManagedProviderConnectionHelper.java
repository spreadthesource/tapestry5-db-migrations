package com.spreadthesource.tapestry.dbmigration.hibernate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.hibernate.connection.ConnectionProvider;
import org.hibernate.connection.ConnectionProviderFactory;
import org.hibernate.util.JDBCExceptionReporter;

public class ManagedProviderConnectionHelper implements ConnectionHelper
{
    private Properties cfgProperties;

    private ConnectionProvider connectionProvider;

    private Connection connection;

    public ManagedProviderConnectionHelper(Properties cfgProperties)
    {
        this.cfgProperties = cfgProperties;
    }

    public void prepare(boolean needsAutoCommit) throws SQLException
    {
        connectionProvider = ConnectionProviderFactory.newConnectionProvider(cfgProperties);
        connection = connectionProvider.getConnection();
        if (needsAutoCommit && !connection.getAutoCommit())
        {
            connection.commit();
            connection.setAutoCommit(true);
        }
    }

    public Connection getConnection() throws SQLException
    {
        return connection;
    }

    public void release() throws SQLException
    {
        if (connection != null)
        {
            try
            {
                JDBCExceptionReporter.logAndClearWarnings(connection);
                connectionProvider.closeConnection(connection);
            }
            finally
            {
                connectionProvider.close();
            }
        }
        connection = null;
    }
}
