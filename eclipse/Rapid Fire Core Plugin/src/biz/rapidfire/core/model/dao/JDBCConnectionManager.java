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
import java.sql.Types;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.dialogs.MessageDialogAsync;
import biz.rapidfire.core.exceptions.RapidFireStartConnectionException;
import biz.rapidfire.core.exceptions.RapidFireStopConnectionException;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.maintenance.Success;
import biz.rapidfire.rsebase.model.dao.AbstractDAOManager;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCDriver;

public class JDBCConnectionManager extends AbstractDAOManager {

    private static final String ERROR_START_CONNECTION_001 = "001"; //$NON-NLS-1$

    private static final String ERROR_STOP_CONNECTION_001 = "001"; //$NON-NLS-1$

    private static final String EMPTY_STRING = ""; //$NON-NLS-1$

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

    private Map<String, Map<String, JDBCConnection>> cachedHosts;

    /**
     * Private constructor to ensure the Singleton pattern.
     */
    private JDBCConnectionManager() {

        this.cachedHosts = new HashMap<String, Map<String, JDBCConnection>>();

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

    public IJDBCConnection getConnection(String connectionName, String libraryName, boolean isCommitControl) throws Exception {

        JDBCConnection jdbcConnection = findJdbcConnection(connectionName, libraryName, isCommitControl);
        if (jdbcConnection == null) {
            jdbcConnection = produceJdbcConnection(connectionName, libraryName, isCommitControl);
            storeJdbcConnection(connectionName, libraryName, isCommitControl, jdbcConnection);
        }

        return jdbcConnection;
    }

    public boolean reconnect(IJDBCConnection jdbcConnection) throws Exception {

        JDBCConnection jdbcConnectionImpl = (JDBCConnection)jdbcConnection;

        try {
            if (!jdbcConnectionImpl.isClosed()) {
                jdbcConnectionImpl.close();
            }
        } catch (SQLException e) {
            // ignore errors, because there is nothing we can do here.
        }

        String connectionName = jdbcConnection.getConnectionName();
        AS400 system = getSystem(connectionName);
        String libraryName = jdbcConnection.getLibraryName();
        boolean isCommitControl = jdbcConnection.isCommitControl();

        jdbcConnectionImpl.setConnection(produceConnection(system, libraryName, isCommitControl));

        startConnection(jdbcConnectionImpl);

        return true;
    }

    public void connected(String connectionName) {
        // There is nothing to do here. Connections are created on request.
    }

    public void disconnected(String connectionName) {

        Map<String, JDBCConnection> cachedHostConnections = cachedHosts.get(connectionName);
        if (cachedHostConnections != null) {
            closeHostConnections(cachedHostConnections);
        }
    }

    private JDBCConnection findJdbcConnection(String connectionName, String libraryName, boolean isCommitControl) {

        Map<String, JDBCConnection> cachedHostConnections = findHostConnections(connectionName);

        String key = createKey(connectionName, libraryName, isCommitControl);
        JDBCConnection jdbcConnection = cachedHostConnections.get(key);

        return jdbcConnection;
    }

    private Map<String, JDBCConnection> findHostConnections(String connectionName) {

        Map<String, JDBCConnection> cachedHostConnections = cachedHosts.get(connectionName);
        if (cachedHostConnections == null) {
            cachedHostConnections = new HashMap<String, JDBCConnection>();
            cachedHosts.put(connectionName, cachedHostConnections);
        }

        return cachedHostConnections;
    }

    private void storeJdbcConnection(String connectionName, String libraryName, boolean isCommitControl, JDBCConnection jdbcConnection) {

        Map<String, JDBCConnection> cachedHostConnections = findHostConnections(connectionName);

        String key = createKey(connectionName, libraryName, isCommitControl);
        cachedHostConnections.put(key, jdbcConnection);
    }

    private JDBCConnection produceJdbcConnection(String connectionName, String libraryName, boolean isCommitControl) throws Exception {

        AS400 system = getSystem(connectionName);

        Connection connection = produceConnection(system, libraryName, isCommitControl);

        JDBCConnection jdbcConnection = new JDBCConnection(connectionName, system, connection, libraryName, isCommitControl);

        startConnection(jdbcConnection);

        return jdbcConnection;
    }

    private Connection produceConnection(AS400 system, String libraryName, boolean isCommitControl) throws SQLException {

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

        Connection connection = as400JDBCDriver.connect(system, jdbcProperties, libraryName, true);

        return connection;
    }

    private String createKey(String connectionName, String libraryName, boolean isCommitControl) {

        String key = connectionName + ":" + libraryName + ":commit=" + isCommitControl; //$NON-NLS-1$ //$NON-NLS-2$

        return key;
    }

    private void closeAllConnection() {

        List<String> closedHostKeys = new LinkedList<String>();

        Set<Entry<String, Map<String, JDBCConnection>>> cachedHostEntries = cachedHosts.entrySet();
        for (Entry<String, Map<String, JDBCConnection>> cachedHost : cachedHostEntries) {
            closeHostConnections(cachedHost.getValue());
            closedHostKeys.add(cachedHost.getKey());
        }

        for (String closedHost : closedHostKeys) {
            cachedHosts.remove(closedHost);
        }
    }

    private void closeHostConnections(Map<String, JDBCConnection> cachedConnections) {

        List<String> closedConnectionKeys = new LinkedList<String>();

        Set<Entry<String, JDBCConnection>> cachedJdbcConnectionEntries = cachedConnections.entrySet();
        for (Entry<String, JDBCConnection> jdbcConnection : cachedJdbcConnectionEntries) {
            closeConnection(jdbcConnection.getValue());
            closedConnectionKeys.add(jdbcConnection.getKey());
        }

        for (String closedConnection : closedConnectionKeys) {
            cachedConnections.remove(closedConnection);
        }
    }

    private void closeConnection(JDBCConnection jdbcConnection) {

        try {

            if (!jdbcConnection.isClosed()) {

                stopConnection(jdbcConnection);

                jdbcConnection.close();
            }

        } catch (Exception e) {
            RapidFireCorePlugin.logError("*** Could not close JDBC connection '" + jdbcConnection.getConnectionName() + "' ***", e); //$NON-NLS-1$ //$NON-NLS-2$
            MessageDialogAsync.displayError(ExceptionHelper.getLocalizedMessage(e));
        }
    }

    private void startConnection(JDBCConnection jdbcConnection) throws Exception, SQLException {

        CallableStatement statement = null;
        try {
            statement = jdbcConnection.prepareCall(jdbcConnection
                .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"RAPIDFIRE_start\"(?, ?)}")); //$NON-NLS-1$

            statement.setString(IRapidFireStart.SUCCESS, EMPTY_STRING);
            statement.setString(IRapidFireStart.ERROR_CODE, EMPTY_STRING);

            statement.registerOutParameter(IRapidFireStart.SUCCESS, Types.CHAR);
            statement.registerOutParameter(IRapidFireStart.ERROR_CODE, Types.CHAR);

            statement.execute();

            String success = getStringTrim(statement, IRapidFireStart.SUCCESS);
            String errorCode = getStringTrim(statement, IRapidFireStart.ERROR_CODE);

            if (!Success.YES.label().equals(success)) {
                String message = Messages.bindParameters(Messages.Could_not_start_a_Rapid_Fire_JDBC_connection,
                    getStartConnectionErrorMessage(errorCode));
                throw new RapidFireStartConnectionException(message);
            }

        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    /**
     * Translates the API error code to message text.
     * 
     * @param errorCode - Error code that was returned by the API.
     * @return message text
     */
    private String getStartConnectionErrorMessage(String errorCode) {

        // TODO: use reflection
        if (ERROR_START_CONNECTION_001.equals(errorCode)) {
            return Messages.RapidFire_Start_001;
        }

        return Messages.bindParameters(Messages.EntityManager_Unknown_error_code_A, errorCode);
    }

    private void stopConnection(JDBCConnection jdbcConnection) throws Exception, SQLException {

        CallableStatement statement = null;
        try {
            statement = jdbcConnection.prepareCall(jdbcConnection
                .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"RAPIDFIRE_stop\"(?, ?)}")); //$NON-NLS-1$

            statement.setString(IRapidFireStop.SUCCESS, EMPTY_STRING);
            statement.setString(IRapidFireStop.ERROR_CODE, EMPTY_STRING);

            statement.registerOutParameter(IRapidFireStop.SUCCESS, Types.CHAR);
            statement.registerOutParameter(IRapidFireStop.ERROR_CODE, Types.CHAR);

            statement.execute();

            String success = getStringTrim(statement, IRapidFireStop.SUCCESS);
            String errorCode = getStringTrim(statement, IRapidFireStop.ERROR_CODE);

            if (!Success.YES.label().equals(success)) {
                String message = Messages.bindParameters(Messages.Could_not_stop_the_Rapid_Fire_JDBC_connection,
                    getStopConnectionErrorMessage(errorCode));
                throw new RapidFireStopConnectionException(message);
            }

        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    /**
     * Translates the API error code to message text.
     * 
     * @param errorCode - Error code that was returned by the API.
     * @return message text
     */
    private String getStopConnectionErrorMessage(String errorCode) {

        // TODO: use reflection
        if (ERROR_STOP_CONNECTION_001.equals(errorCode)) {
            return Messages.RapidFire_Stop_001;
        }

        return Messages.bindParameters(Messages.EntityManager_Unknown_error_code_A, errorCode);
    }

    public void destroy() {
        closeAllConnection();
        instance = null;
    }
}