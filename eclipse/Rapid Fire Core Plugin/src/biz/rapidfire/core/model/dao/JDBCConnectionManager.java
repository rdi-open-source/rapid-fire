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
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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
import com.ibm.as400.access.SecureAS400;

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

    private static final boolean isDebugMode = false;

    /**
     * The instance of this Singleton class.
     */
    private static JDBCConnectionManager instance;

    /**
     * The JDBC driver used for creating JDBC connections.
     */
    private AS400JDBCDriver as400JDBCDriver;

    /**
     * Map, that contains another map for storing JDBC connection per
     * "Remote Connection".
     */
    private Map<String, Map<String, JDBCConnection>> cachedHosts;

    /**
     * Private constructor to ensure the Singleton pattern.
     */
    private JDBCConnectionManager() {

        this.cachedHosts = new HashMap<String, Map<String, JDBCConnection>>();

        try {

            try {

                as400JDBCDriver = (AS400JDBCDriver)DriverManager.getDriver("jdbc:as400");

            } catch (SQLException e) {

                as400JDBCDriver = new AS400JDBCDriver();
                DriverManager.registerDriver(as400JDBCDriver);

            }

        } catch (Throwable e) {
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

    /**
     * Returns a JDBC connection for "Read".
     * 
     * @param connectionName - name of the RSE host connection
     * @param libraryName - name of the default library of the JDBC connection
     * @return jdbc connection
     * @throws Exception
     */
    public IJDBCConnection getConnectionForRead(String connectionName, String libraryName) throws Exception {
        return getConnection(connectionName, libraryName, false);
    }

    /**
     * Returns a JDBC connection for "Update".
     * 
     * @param connectionName - name of the RSE host connection
     * @param libraryName - name of the default library of the JDBC connection
     * @return jdbc connection
     * @throws Exception
     */
    public IJDBCConnection getConnectionForUpdate(String connectionName, String libraryName) throws Exception {
        return getConnection(connectionName, libraryName, true);
    }

    /**
     * Returns a JDBC connection for "Update", no auto-commit.
     * 
     * @param connectionName - name of the RSE host connection
     * @param libraryName - name of the default library of the JDBC connection
     * @return jdbc connection
     * @throws Exception
     */
    public IJDBCConnection getConnectionForUpdateNoAutoCommit(String connectionName, String libraryName) throws Exception {
        return getConnection(connectionName, libraryName, false);
    }

    /**
     * @param connectionName - name of the RSE host connection
     * @param libraryName - name of the default library of the JDBC connection
     * @param isCommitControl - specifies whether commit control is enabled
     * @param isAutoCommit - specifies whether auto-commit is enabled
     * @return jdbc connection
     * @throws Exception
     */
    private synchronized IJDBCConnection getConnection(String connectionName, String libraryName, boolean isAutoCommit) throws Exception {

        JDBCConnection jdbcConnection = findJdbcConnection(connectionName, libraryName, isAutoCommit);
        if (jdbcConnection == null) {
            jdbcConnection = produceJdbcConnection(connectionName, libraryName, isAutoCommit);
            findHost(connectionName).put(jdbcConnection.getKey(), jdbcConnection);
            logDebugMessage("Added JDBC connection " + jdbcConnection.hashCode() + " (auto commit=" + jdbcConnection.isAutoCommit() + ") to host "
                + jdbcConnection.getConnectionName());
        } else {
            logDebugMessage("Found JDBC connection " + jdbcConnection.hashCode() + " (auto commit=" + jdbcConnection.isAutoCommit() + ") of host "
                + jdbcConnection.getConnectionName());
        }

        return jdbcConnection;
    }

    /**
     * Reconnects a given JDBC connection.
     * 
     * @param jdbcConnection - jdbc connection that is reconnected
     * @return <i>true</i>, if the connection has been successfully reconnected
     * @throws Exception
     */
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
        boolean isAutoCommit = jdbcConnection.isAutoCommit();

        jdbcConnectionImpl.setConnection(produceConnection(system, libraryName, isAutoCommit));

        startConnection(jdbcConnectionImpl);

        return true;
    }

    /**
     * This method closes a given JDBC connection.
     * 
     * @param jdbcConnection that is closed
     */
    public void close(IJDBCConnection jdbcConnection) {

        Map<String, JDBCConnection> cachedJdbcConnections = findHost(jdbcConnection.getConnectionName());

        closeJdbcConnection(cachedJdbcConnections, jdbcConnection.getKey());
    }

    /**
     * This method is called, when the communication state of given RSE host
     * changes to "connected".
     * 
     * @param connectionName - Name of the RSE host connection
     */
    public void connected(String connectionName) {
        // There is nothing to do here. Connections are created on request.
    }

    /**
     * Disconnects all JDBC connections associated to a given RSE host
     * connection. This method is called, when the communication state of given
     * RSE host changes to "disconnected".
     * 
     * @param connectionName - Name of the RSE host connection
     */
    public void disconnected(String connectionName) {
        closeHost(connectionName);
    }

    /**
     * Returns the JDBC connection that are associated to a given RSE host
     * connection.
     * 
     * @param connectionName - Name of the RSE host connection
     * @return map of JDBC connections that are associated to the host
     */
    private Map<String, JDBCConnection> findHost(String connectionName) {

        Map<String, JDBCConnection> cachedJdbcConnections = cachedHosts.get(connectionName);
        if (cachedJdbcConnections == null) {
            cachedJdbcConnections = new HashMap<String, JDBCConnection>();
            cachedHosts.put(connectionName, cachedJdbcConnections);
            logDebugMessage("Added host " + connectionName);
        }

        return cachedJdbcConnections;
    }

    /**
     * Return the JDBC connection that is identified by the given connection
     * properties.
     * 
     * @param connectionName - name of the RSE host connection
     * @param libraryName - name of the default library
     * @param isCommitControl - specifies whether commitment control is enabled
     * @param isAutoCommit - specifies whether auto-commit is enabled
     * @return
     */
    private JDBCConnection findJdbcConnection(String connectionName, String libraryName, boolean isAutoCommit) {

        Map<String, JDBCConnection> cachedHostConnections = findHost(connectionName);
        JDBCConnection jdbcConnection = cachedHostConnections.get(JDBCConnection.createKey(connectionName, libraryName, isAutoCommit));

        return jdbcConnection;
    }

    private JDBCConnection produceJdbcConnection(String connectionName, String libraryName, boolean isAutoCommit) throws Exception {

        AS400 system = SystemConnectionHelper.getSystem(connectionName);

        Connection connection = produceConnection(system, libraryName, isAutoCommit);

        JDBCConnection jdbcConnection = new JDBCConnection(connectionName, system, connection, libraryName, isAutoCommit);

        startConnection(jdbcConnection);

        return jdbcConnection;
    }

    private Connection produceConnection(AS400 system, String libraryName, boolean isAutoCommit) throws SQLException {

        // Properties of ToolboxConnectorService
        Properties jdbcProperties = new Properties();

        jdbcProperties.put(PROPERTY_PROMPT, JDBC_FALSE);
        jdbcProperties.put(PROPERTY_BIG_DECIMAL, JDBC_FALSE);

        // add schema and library list
        jdbcProperties.put(PROPERTIES_LIBRARIES, libraryName + ",*LIBL"); //$NON-NLS-1$

        // enable SSL, when the remote system connection uses SSL
        if (system instanceof SecureAS400) {
            jdbcProperties.put("secure", "true"); //$NON-NLS-1$ //$NON-NLS-2$
        }

        Connection connection = as400JDBCDriver.connect(system, jdbcProperties, libraryName, true);

        // Remove current library from library list
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("CALL QSYS.QCMDEXC('CHGCURLIB CURLIB(*CRTDFT)',0000000025.00000)");

        return connection;
    }

    /**
     * Closes all JDBC connections of all registered RSE connections.
     */
    private void closeAllHosts() {

        String[] connectionNames = cachedHosts.keySet().toArray(new String[cachedHosts.size()]);

        for (String connectionName : connectionNames) {
            closeHost(connectionName);
        }

        if (cachedHosts.size() != 0) {
            throwRuntimeException("#Cached RSE host connections must be 0.");
        }
    }

    /**
     * Closes all JDBC connections of a given RSE connection.
     * 
     * @param cachedHostConnections - JDBC connections associated to a RSE
     *        connection
     */
    private void closeHost(String connectionName) {

        Map<String, JDBCConnection> cachedHost = findHost(connectionName);

        JDBCConnection[] cachedJdbcConnections = cachedHost.values().toArray(new JDBCConnection[cachedHost.size()]);

        for (JDBCConnection cachedJdbcConnection : cachedJdbcConnections) {
            closeJdbcConnection(cachedHost, cachedJdbcConnection.getKey());
        }

        cachedHosts.remove(connectionName);
        logDebugMessage("Removed host " + connectionName);

        if (cachedHosts.size() != 0) {
            throwRuntimeException("#Cached JDBC host connections must be 0.");
        }
    }

    /**
     * Closes a given JDBC connection.
     * 
     * @param jdbcConnection - JDBC connection that is closed
     */
    private void closeJdbcConnection(Map<String, JDBCConnection> cachedHost, String jdbcConnectionKey) {

        JDBCConnection jdbcConnection = cachedHost.get(jdbcConnectionKey);
        if (jdbcConnection == null) {
            throwRuntimeException("JDBC connection must not be [null].");
        }

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
        } finally {
            cachedHost.remove(jdbcConnection);
            logDebugMessage("Removed JDBC connection " + jdbcConnection.hashCode() + " (auto commit=" + jdbcConnection.isAutoCommit() + ") of host "
                + jdbcConnection.getConnectionName());
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

            logDebugMessage("-> COMMITTED JDBC connection " + jdbcConnection.hashCode() + " (auto commit=" + jdbcConnection.isAutoCommit()
                + ") to host " + jdbcConnection.getConnectionName());

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

            logDebugMessage("-> ROLLED-BACK JDBC connection " + jdbcConnection.hashCode() + " (auto commit=" + jdbcConnection.isAutoCommit()
                + ") to host " + jdbcConnection.getConnectionName());

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

        closeAllHosts();
        instance = null;
    }

    private void throwRuntimeException(String message) {

        if (isDebugMode) {
            throw new RuntimeException(message);
        } else {
            RapidFireCorePlugin.logError(message, null);
        }
    }

    private void logDebugMessage(String message) {

        if (isDebugMode) {
            System.out.println(message);
        }
    }
}