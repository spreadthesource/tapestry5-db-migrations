package com.spreadthesource.tapestry.dbmigration.services;

import java.io.Writer;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.tapestry5.ioc.annotations.Symbol;
import org.hibernate.JDBCException;
import org.hibernate.dialect.Dialect;
import org.hibernate.jdbc.util.Formatter;
import org.hibernate.tool.hbm2ddl.DatabaseMetadata;
import org.slf4j.Logger;

import com.spreadthesource.tapestry.dbmigration.MigrationSymbolConstants;
import com.spreadthesource.tapestry.dbmigration.hibernate.ConnectionHelper;

public class MigrationRunnerImpl implements MigrationRunner
{
    private List<String> sql;

    private final List<Exception> exceptions;

    private boolean doUpdate;

    private Logger log;

    private Dialect dialect;

    private ConnectionHelper connectionHelper;

    private Formatter formatter;

    private String delimiter;

    private boolean haltOnError;

    public MigrationRunnerImpl(Logger log,
            @Symbol(MigrationSymbolConstants.DO_UPDATE) boolean doUpdate,
            @Symbol(MigrationSymbolConstants.HALT_ON_ERROR) boolean haltOnError,
            MigrationHelper helper)
    {
        this.sql = new ArrayList<String>();
        this.exceptions = new ArrayList<Exception>();
        this.doUpdate = doUpdate;
        this.haltOnError = haltOnError;
        this.log = log;
        
        this.formatter = helper.getFormatter();
        this.connectionHelper = helper.getConnectionHelper();
        this.dialect = helper.getDialect();
    }

    public void addSQL(List<String> sql)
    {
        this.sql.addAll(sql);
    }

    public void run(String... sql)
    {

        Connection connection = null;
        Statement stmt = null;
        Writer outputFileWriter = null;

        exceptions.clear();

        try
        {
            DatabaseMetadata meta;
            try
            {
                log.info("fetching database metadata");

                connectionHelper.prepare(true);
                connection = connectionHelper.getConnection();
                meta = new DatabaseMetadata(connection, dialect);
                stmt = connection.createStatement();
            }
            catch (SQLException sqle)
            {
                exceptions.add(sqle);
                log.error("could not get database metadata", sqle);
                throw sqle;
            }

            log.info("updating schema");

            List<String> SQLOperations = new ArrayList<String>();

            // SQLOperations.add(checkSchemaVersioning(meta));

            SQLOperations.addAll(Arrays.asList(sql));

            for (String sqlOperation : SQLOperations)
            {
                String formatted = formatter.format(sqlOperation);
                try
                {
                    if (delimiter != null)
                    {
                        formatted += delimiter;
                    }

                    if (doUpdate)
                    {
                        log.debug(sqlOperation);
                        stmt.executeUpdate(formatted);
                    }
                }
                catch (SQLException e)
                {
                    if (haltOnError) { throw new JDBCException("Error during DDL export", e); }
                    exceptions.add(e);
                    log.error("Unsuccessful: " + sqlOperation);
                    log.error(e.getMessage());
                }
            }

        }
        catch (Exception e)
        {
            exceptions.add(e);
            log.error("could not complete schema update", e);
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
        this.sql.clear();
    }

}
