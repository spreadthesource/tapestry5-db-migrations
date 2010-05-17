package com.spreadthesource.tapestry.dbmigration.services;

import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.tapestry5.ioc.Invocation;
import org.apache.tapestry5.ioc.ScopeConstants;
import org.apache.tapestry5.ioc.annotations.Scope;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.internal.util.TapestryException;
import org.hibernate.jdbc.util.Formatter;
import org.slf4j.Logger;

import com.spreadthesource.tapestry.dbmigration.MigrationSymbolConstants;
import com.spreadthesource.tapestry.dbmigration.hibernate.ConnectionHelper;

@Scope(ScopeConstants.PERTHREAD)
public class MigrationRunnerImpl implements MigrationRunner
{
    private final List<Exception> exceptions;

    private boolean doUpdate;

    private Logger log;

    private ConnectionHelper connectionHelper;

    private Formatter formatter;

    private String delimiter;

    private boolean haltOnError;

    private Connection connection;

    private Statement stmt;

    private Writer outputFileWriter;

    public MigrationRunnerImpl(Logger log,
            @Symbol(MigrationSymbolConstants.DO_UPDATE) boolean doUpdate,
            @Symbol(MigrationSymbolConstants.HALT_ON_ERROR) boolean haltOnError,
            MigrationHelper helper)
    {
        this.exceptions = new ArrayList<Exception>();
        this.doUpdate = doUpdate;
        this.haltOnError = haltOnError;
        this.log = log;

        this.formatter = helper.getFormatter();
        this.connectionHelper = helper.getConnectionHelper();
    }

    public void update(String... sql)
    {
        List<String> SQLOperations = new ArrayList<String>();

        SQLOperations.addAll(Arrays.asList(sql));

        for (String sqlOperation : SQLOperations)
        {
            String formatted = formatter.format(sqlOperation);

            if (delimiter != null)
            {
                formatted += delimiter;
            }

            if (doUpdate)
            {
                log.debug(sqlOperation);
                try
                {
                    stmt.executeUpdate(formatted);
                }
                catch (SQLException e)
                {
                    throw new TapestryException("SQL Exception", e);
                }
            }

        }

    }

    public ResultSet query(String sql)
    {
        try
        {
            String formatted = formatter.format(sql);

            if (delimiter != null)
            {
                formatted += delimiter;
            }

            log.debug("Executing query: " + formatted);
            if (doUpdate) return stmt.executeQuery(formatted);

        }
        catch (SQLException e)
        {
            throw new TapestryException("SQL Exception during query.", e);
        }

        return null;
    }

    public void inStatement(Invocation invocation)
    {
        connection = null;
        stmt = null;
        outputFileWriter = null;

        exceptions.clear();

        try
        {
            try
            {
                connectionHelper.prepare(true);
                connection = connectionHelper.getConnection();
                stmt = connection.createStatement();
            }
            catch (SQLException sqle)
            {
                exceptions.add(sqle);
                log.error("Could not get database metadata", sqle);
                throw new TapestryException("Could not get database metadata", sqle);
            }

            invocation.proceed();
        }
        catch (Exception e)
        {
            exceptions.add(e);
            if (haltOnError)
            {
                log.error("Could not complete schema update", e);
                throw new TapestryException("SQL Exception in Migration Runner", e);
            }

        }
        finally
        {

            try
            {
                if (stmt != null)
                {
                    stmt.close();
                }
                connectionHelper.release();
            }
            catch (Exception e)
            {
                exceptions.add(e);
                log.error("Error closing connection", e);
            }
            try
            {
                if (outputFileWriter != null)
                {
                    outputFileWriter.close();
                }
            }
            catch (Exception e)
            {
                exceptions.add(e);
                log.error("Error closing connection", e);
            }
        }

    }
}
