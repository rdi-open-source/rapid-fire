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
import biz.rapidfire.core.exceptions.RapidFireAutoCommitException;
import biz.rapidfire.core.exceptions.RapidFireCommitException;
import biz.rapidfire.core.exceptions.RapidFireRollbackException;
import biz.rapidfire.core.exceptions.RapidFireStartConnectionException;
import biz.rapidfire.core.exceptions.RapidFireStopConnectionException;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.maintenance.Success;
import biz.rapidfire.core.model.AutoCommit;
import biz.rapidfire.rsebase.helpers.SystemConnectionHelper;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCDriver;

/**
 * This class manages JDBC connections. Connections are created and cached to be
 * reused while RDi is running. In fact each connection could use commitment
 * control, since RAPIDFIRE_start() is called for each connection. Though the
 * connection manager produces separate connections for each combination of
 * [connectionName], [library], [isCommitControl] and [isAutoCommit]. So there
 * is a maximum of 3 connection per RSE connection.
 */
public class JDBCConnectionManager {

    private static final String ERROR_START_CONNECTION_001 = "001"; //$NON-NLS-1$

    private static final String ERROR_STOP_CONNECTION_001 = "001"; //$NON-NLS-1$

    private static final String ERROR_SET_AUTO_COMMIT_001 = "001"; //$NON-NLS-1$

    private static final String ERROR_COMMIT_001 = "001"; //$NON-NLS-1$

    private static final String ERROR_ROLLBACK_001 = "001"; //$NON-NLS-1$

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

    private Map<String, Map<String, JDBCConnection>> cachedConnections;

    /**
     * Private constructor to ensure the Singleton pattern.
     */
    private JDBCConnectionManager() {

        this.cachedConnections = new HashMap<String, Map<String, JDBCConnection>>();

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

    public IJDBCConnection getConnectionForRead(String connectionName, String libraryName) throws Exception {
        return getConnection(connectionName, libraryName, false, false);
    }

    public IJDBCConnection getConnectionForUpdate(String connectionName, String libraryName) throws Exception {
        return getConnection(connectionName, libraryName, true, true);
    }

    public IJDBCConnection getConnectionForUpdateNoAutoCommit(String connectionName, String libraryName) throws Exception {
        return getConnection(connectionName, libraryName, true, false);
    }

    private synchronized IJDBCConnection getConnection(String connectionName, String libraryName, boolean isCommitControl, boolean isAutoCommit)
        throws Exception {

        if (!isCommitControl) {
            isAutoCommit = false;
        }

        JDBCConnection jdbcConnection = findJdbcConnection(connectionName, libraryName, isCommitControl, isAutoCommit);
        if (jdbcConnection == null) {
            jdbcConnection = produceJdbcConnection(connectionName, libraryName, isCommitControl, isAutoCommit);
            storeJdbcConnection(connectionName, libraryName, isCommitControl, isAutoCommit, jdbcConnection);
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
        AS400 system = SystemConnectionHelper.getSystem(connectionName);
        String libraryName = jdbcConnection.getLibraryName();
        boolean isCommitControl = jdbcConnection.isCommitControl();
        boolean isAutoCommit = jdbcConnection.isAutoCommit();

        jdbcConnectionImpl.setConnection(produceConnection(system, libraryName, isCommitControl, isAutoCommit));

        startConnection(jdbcConnectionImpl);

        return true;
    }

    public void close(IJDBCConnection jdbcConnection) {

        closeConnection((JDBCConnection)jdbcConnection);

        cachedConnections.remove(jdbcConnection.getConnectionName());
    }

    public void connected(String connectionName) {
        // There is nothing to do here. Connections are created on request.
    }

    public void disconnected(String connectionName) {

        Map<String, JDBCConnection> cachedHostConnections = cachedConnections.get(connectionName);
        if (cachedHostConnections != null) {
            closeHostConnections(cachedHostConnections);
        }
    }

    private JDBCConnection findJdbcConnection(String connectionName, String libraryName, boolean isCommitControl, boolean isAutoCommit) {

        Map<String, JDBCConnection> cachedHostConnections = findHostConnections(connectionName);

        String key = createKey(connectionName, libraryName, isCommitControl, isAutoCommit);
        JDBCConnection jdbcConnection = cachedHostConnections.get(key);

        return jdbcConnection;
    }

    private Map<String, JDBCConnection> findHostConnections(String connectionName) {

        Map<String, JDBCConnection> cachedHostConnections = cachedConnections.get(connectionName);
        if (cachedHostConnections == null) {
            cachedHostConnections = new HashMap<String, JDBCConnection>();
            cachedConnections.put(connectionName, cachedHostConnections);
        }

        return cachedHostConnections;
    }

    private void storeJdbcConnection(String connectionName, String libraryName, boolean isCommitControl, boolean isAutoCommit,
        JDBCConnection jdbcConnection) {

        Map<String, JDBCConnection> cachedHostConnections = findHostConnections(connectionName);

        String key = createKey(connectionName, libraryName, isCommitControl, isAutoCommit);
        cachedHostConnections.put(key, jdbcConnection);
    }

    private JDBCConnection produceJdbcConnection(String connectionName, String libraryName, boolean isCommitControl, boolean isAutoCommit)
        throws Exception {

        AS400 system = SystemConnectionHelper.getSystem(connectionName);

        Connection connection = produceConnection(system, libraryName, isCommitControl, isAutoCommit);

        JDBCConnection jdbcConnection = new JDBCConnection(connectionName, system, connection, libraryName, isCommitControl, isAutoCommit);

        startConnection(jdbcConnection);

        return jdbcConnection;
    }

    private Connection produceConnection(AS400 system, String libraryName, boolean isCommitControl, boolean isAutoCommit) throws SQLException {

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

    private String createKey(String connectionName, String libraryName, boolean isCommitControl, boolean isAutoCommit) {

        String key = connectionName + ":" + libraryName + ":commit=" + isCommitControl + ":autocommit=" + isAutoCommit; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        return key;
    }

    private void closeAllConnection() {

        List<String> closedHostKeys = new LinkedList<String>();

        Set<Entry<String, Map<String, JDBCConnection>>> cachedHostEntries = cachedConnections.entrySet();
        for (Entry<String, Map<String, JDBCConnection>> cachedHost : cachedHostEntries) {
            closeHostConnections(cachedHost.getValue());
            closedHostKeys.add(cachedHost.getKey());
        }

        for (String closedHost : closedHostKeys) {
            cachedConnections.remove(closedHost);
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

                try {
                    stopConnection(jdbcConnection);
                } catch (Exception e) {
                    RapidFireCorePlugin.logError("*** Could not stop JDBC connection '" + jdbcConnection.getConnectionName() + "' ***", e); //$NON-NLS-1$ //$NON-NLS-2$
                    MessageDialogAsync.displayError(ExceptionHelper.getLocalizedMessage(e));
                }

                jdbcConnection.close();
            }

        } catch (Exception e) {
            RapidFireCorePlugin.logError("*** Could not close JDBC connection '" + jdbcConnection.getConnectionName() + "' ***", e); //$NON-NLS-1$ //$NON-NLS-2$
            MessageDialogAsync.displayError(ExceptionHelper.getLocalizedMessage(e));
        }
    }

    private void startConnection(JDBCConnection jdbcConnection) throws Exception {

        CallableStatement statement = null;
        try {

            statement = jdbcConnection.prepareCall(jdbcConnection
                .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"RAPIDFIRE_start\"(?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

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

        if (jdbcConnection.isAutoCommit()) {
            setAutoCommit(jdbcConnection, true);
        } else {
            setAutoCommit(jdbcConnection, false);
        }
    }

    public void commit(IJDBCConnection jdbcConnection) throws Exception {

        CallableStatement statement = null;
        try {

            statement = jdbcConnection.prepareCall(jdbcConnection
                .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"RAPIDFIRE_commit\"(?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

            statement.setString(IRapidFireCommit.SUCCESS, EMPTY_STRING);
            statement.setString(IRapidFireCommit.ERROR_CODE, EMPTY_STRING);

            statement.registerOutParameter(IRapidFireCommit.SUCCESS, Types.CHAR);
            statement.registerOutParameter(IRapidFireCommit.ERROR_CODE, Types.CHAR);

            statement.execute();

            String success = getStringTrim(statement, IRapidFireCommit.SUCCESS);
            String errorCode = getStringTrim(statement, IRapidFireCommit.ERROR_CODE);

            if (!Success.YES.label().equals(success)) {
                String message = Messages.bindParameters(Messages.Could_not_commit_transaction_of_connection_A, jdbcConnection.getConnectionName(),
                    getCommitErrorMessage(errorCode));
                throw new RapidFireCommitException(message);
            }

        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    public void rollback(IJDBCConnection jdbcConnection) throws Exception {

        CallableStatement statement = null;
        try {

            statement = jdbcConnection.prepareCall(jdbcConnection
                .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"RAPIDFIRE_rollback\"(?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

            statement.setString(IRapidFireRollback.SUCCESS, EMPTY_STRING);
            statement.setString(IRapidFireRollback.ERROR_CODE, EMPTY_STRING);

            statement.registerOutParameter(IRapidFireRollback.SUCCESS, Types.CHAR);
            statement.registerOutParameter(IRapidFireRollback.ERROR_CODE, Types.CHAR);

            statement.execute();

            String success = getStringTrim(statement, IRapidFireRollback.SUCCESS);
            String errorCode = getStringTrim(statement, IRapidFireRollback.ERROR_CODE);

            if (!Success.YES.label().equals(success)) {
                String message = Messages.bindParameters(Messages.Could_not_rollback_transaction_of_connection_A, jdbcConnection.getConnectionName(),
                    getRollbackErrorMessage(errorCode));
                throw new RapidFireRollbackException(message);
            }

        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    private void setAutoCommit(JDBCConnection jdbcConnection, boolean isAutoCommit) throws Exception {

        CallableStatement statement = null;
        try {

            statement = jdbcConnection.prepareCall(jdbcConnection
                .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"RAPIDFIRE_setAutoCommit\"(?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

            if (isAutoCommit) {
                statement.setString(IRapidFireSetAutoCommit.AUTO_COMMIT, AutoCommit.YES.label());
            } else {
                statement.setString(IRapidFireSetAutoCommit.AUTO_COMMIT, AutoCommit.NO.label());
            }

            statement.setString(IRapidFireSetAutoCommit.SUCCESS, EMPTY_STRING);
            statement.setString(IRapidFireSetAutoCommit.ERROR_CODE, EMPTY_STRING);

            statement.registerOutParameter(IRapidFireSetAutoCommit.SUCCESS, Types.CHAR);
            statement.registerOutParameter(IRapidFireSetAutoCommit.ERROR_CODE, Types.CHAR);

            statement.execute();

            String success = getStringTrim(statement, IRapidFireSetAutoCommit.SUCCESS);
            String errorCode = getStringTrim(statement, IRapidFireSetAutoCommit.ERROR_CODE);

            if (!Success.YES.label().equals(success)) {
                String message = Messages.bindParameters(Messages.Could_set_auto_commit_property_for_connection_A,
                    jdbcConnection.getConnectionName(), getSetAutoCommitErrorMessage(errorCode));
                throw new RapidFireAutoCommitException(message);
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
                .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"RAPIDFIRE_stop\"(?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

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

    /**
     * Translates the API error code to message text.
     * 
     * @param errorCode - Error code that was returned by the API.
     * @return message text
     */
    private String getSetAutoCommitErrorMessage(String errorCode) {

        // TODO: use reflection
        if (ERROR_SET_AUTO_COMMIT_001.equals(errorCode)) {
            return Messages.RapidFire_Set_Auto_Commit_001;
        }

        return Messages.bindParameters(Messages.EntityManager_Unknown_error_code_A, errorCode);
    }

    /**
     * Translates the API error code to message text.
     * 
     * @param errorCode - Error code that was returned by the API.
     * @return message text
     */
    private String getCommitErrorMessage(String errorCode) {

        // TODO: use reflection
        if (ERROR_COMMIT_001.equals(errorCode)) {
            return Messages.RapidFire_Commit_001;
        }

        return Messages.bindParameters(Messages.EntityManager_Unknown_error_code_A, errorCode);
    }

    /**
     * Translates the API error code to message text.
     * 
     * @param errorCode - Error code that was returned by the API.
     * @return message text
     */
    private String getRollbackErrorMessage(String errorCode) {

        // TODO: use reflection
        if (ERROR_ROLLBACK_001.equals(errorCode)) {
            return Messages.RapidFire_Rollback_001;
        }

        return Messages.bindParameters(Messages.EntityManager_Unknown_error_code_A, errorCode);
    }

    protected String getStringTrim(CallableStatement statement, int parameterIndex) throws SQLException {
        return statement.getString(parameterIndex);
    }

    public void destroy() {
        closeAllConnection();
        instance = null;
    }
}