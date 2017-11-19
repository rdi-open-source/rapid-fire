/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.dialogs.MessageDialogAsync;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.rsebase.model.dao.AbstractDAOManager;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCDriver;

public class JDBCConnectionManager extends AbstractDAOManager {

    private static final String PROPERTY_PROMPT = "prompt";
    private static final String PROPERTY_BIG_DECIMAL = "big decimal";
    private static final String PROPERTIES_LIBRARIES = "libraries";
    private static final String PROPERTY_TRANSACTION_ISOLATION = "transaction isolation";

    private static final String JDBC_TRANSACTION_ISOLATION_NONE = "none";
    private static final String JDBC_TRANSACTION_ISOLATION_READ_UNCOMMITED = "read uncommited";
    private static final String JDBC_TRANSACTION_ISOLATION_READ_COMMITTED = "read commited";
    private static final String JDBC_TRANSACTION_ISOLATION_REPEATABLE_READ = "repeatable read";
    private static final String JDBC_TRANSACTION_ISOLATION_SERIALIZABLE = "serializable";

    private static final String JDBC_FALSE = "false";
    private static final String JDBC_TRUE = "true";

    /**
     * The instance of this Singleton class.
     */
    private static JDBCConnectionManager instance;

    private AS400JDBCDriver as400JDBCDriver;

    private Map<String, IJDBCConnection> baseDAOs;

    /**
     * Private constructor to ensure the Singleton pattern.
     */
    private JDBCConnectionManager() {

        this.baseDAOs = new HashMap<String, IJDBCConnection>();

        try {

            as400JDBCDriver = new AS400JDBCDriver();
            DriverManager.registerDriver(as400JDBCDriver);

        } catch (SQLException e) {
            MessageDialogAsync.displayError("Could not register the AS400 JDBC Driver. The reported error is:\n\n" //$NON-NLS-1$
                + ExceptionHelper.getLocalizedMessage(e) + "\n\nRefer to the Eclipse error log for details."); //$NON-NLS-1$
            RapidFireCorePlugin.logError("*** Could not register the AS400 JDBC Driver ***", e); //$NON-NLS-1$
        }
    }

    /**
     * Thread-safe method that returns the instance of this Singleton class.
     */
    public synchronized static JDBCConnectionManager getInstance() {
        if (instance == null) {
            instance = new JDBCConnectionManager();
        }
        return instance;
    }

    public IJDBCConnection getBaseDAO(String connectionName, String libraryName, boolean isCommitControl) throws Exception {

        String key = createKey(connectionName, libraryName, isCommitControl);

        IJDBCConnection baseDAO = baseDAOs.get(key);
        if (baseDAO == null) {
            baseDAO = produceBaseDAO(connectionName, libraryName, isCommitControl);
            baseDAOs.put(key, baseDAO);
        }

        return baseDAO;
    }

    public boolean reconnect(IJDBCConnection jdbcConnection) throws Exception {

        String key = createKey(jdbcConnection);

        try {
            if (!jdbcConnection.getJdbcConnection().isClosed()) {
                jdbcConnection.getJdbcConnection().close();
            }
        } catch (SQLException e) {
            // ignore errors, because there is nothing we can do here.
        }

        baseDAOs.remove(key);

        IJDBCConnection localJdbcConnection = produceBaseDAO(jdbcConnection.getConnectionName(), jdbcConnection.getLibraryName(),
            jdbcConnection.isCommitControl());

        ((JDBCConnection)jdbcConnection).setJdbcConnection(localJdbcConnection.getJdbcConnection());

        return true;
    }

    protected IJDBCConnection produceBaseDAO(String connectionName, String libraryName, boolean isCommitControl) throws Exception {

        // Properties of ToolboxConnectorService
        Properties jdbcProperties = new Properties();

        jdbcProperties.put(PROPERTY_PROMPT, JDBC_FALSE);
        jdbcProperties.put(PROPERTY_BIG_DECIMAL, JDBC_FALSE);

        // add schema and library list
        jdbcProperties.put(PROPERTIES_LIBRARIES, libraryName + ",*LIBL"); //$NON-NLS-1$

        // add isolation level
        if (isCommitControl) {
            jdbcProperties.put(PROPERTY_TRANSACTION_ISOLATION, JDBC_TRANSACTION_ISOLATION_READ_UNCOMMITED);
        } else {
            jdbcProperties.put(PROPERTY_TRANSACTION_ISOLATION, JDBC_TRANSACTION_ISOLATION_NONE);
        }

        AS400 system = getSystem(connectionName);
        Connection jdbcConnection = as400JDBCDriver.connect(system, jdbcProperties, libraryName, true);

        CallableStatement statement = null;
        try {
            statement = jdbcConnection.prepareCall("CALL QCMDEXC('CALL STRCNN')");
            statement.execute();
        } finally {
            if (statement != null) {
                statement.close();
            }
        }

        return new JDBCConnection(connectionName, system, jdbcConnection, libraryName, isCommitControl);
    }

    private String createKey(IJDBCConnection jdbcConnection) {
        return createKey(jdbcConnection.getConnectionName(), jdbcConnection.getLibraryName(), jdbcConnection.isCommitControl());
    }

    private String createKey(String connectionName, String libraryName, boolean isCommitControl) {
        String key = connectionName + ":" + libraryName + ":commit=" + isCommitControl;
        return key;
    }

    private void closeAllConnection() {

        Collection<IJDBCConnection> daos = baseDAOs.values();
        for (IJDBCConnection dao : daos) {
            try {
                Connection jdbcConnection = dao.getJdbcConnection();
                if (!jdbcConnection.isClosed()) {

                    CallableStatement statement = null;
                    try {
                        statement = jdbcConnection.prepareCall("CALL QCMDEXC('CALL ENDCNN')");
                        statement.execute();
                    } finally {
                        if (statement != null) {
                            statement.close();
                        }
                    }

                    jdbcConnection.close();
                }
            } catch (Exception e) {
                RapidFireCorePlugin.logError("*** Could not close JDBC connection '" + dao.getConnectionName() + "' ***", e); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
    }

    public void destroy() {
        closeAllConnection();
        instance = null;
    }
}