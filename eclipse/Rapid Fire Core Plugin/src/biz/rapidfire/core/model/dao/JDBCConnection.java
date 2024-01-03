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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.dialogs.MessageDialogAsync;
import biz.rapidfire.core.exceptions.AutoReconnectErrorException;
import biz.rapidfire.core.exceptions.IllegalParameterException;
import biz.rapidfire.core.helpers.RapidFireHelper;
import biz.rapidfire.core.helpers.SqlHelper;

import com.ibm.as400.access.AS400;

class JDBCConnection implements IJDBCConnection {

    private static final int MAX_RETRY_COUNT = 1;
    private static final String BOOLEAN_Y = "Y"; //$NON-NLS-1$
    private static final String BOOLEAN_N = "N"; //$NON-NLS-1$
    private static final String BOOLEAN_YES = "*YES"; //$NON-NLS-1$
    private static final String BOOLEAN_NO = "*NO"; //$NON-NLS-1$

    private String connectionName;
    private AS400 system;
    private Connection connection;
    private String libraryName;
    private boolean isAutoCommit;
    private SqlHelper sqlHelper;

    public JDBCConnection(String connectionName, AS400 system, Connection jdbcConnection, String libraryName, boolean isAutoCommit) throws Exception {

        this.connectionName = connectionName;
        this.system = system;
        this.connection = jdbcConnection;
        this.libraryName = libraryName;
        this.isAutoCommit = isAutoCommit;
        this.sqlHelper = new SqlHelper(this.connection);
    }

    public AS400 getSystem() {
        return system;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public String getConnectionName() {
        return connectionName;
    }

    /**
     * Used by the JDBCConnectionManager when reconnecting a connection.
     * 
     * @param connection - Java connection object
     */
    void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * Used by the JDBCConnectionManager when reconnecting a connection.
     * 
     * @return Java connection object
     */
    Connection getConnection() {
        return connection;
    }

    public boolean checkRapidFireLibrary(Shell shell) {
        return RapidFireHelper.checkRapidFireLibrary(shell, getSystem(), libraryName);
    }

    public PreparedStatement prepareStatement(String sqlStatement) throws Exception {

        sqlStatement = insertLibraryQualifier(sqlStatement);

        boolean isRetry = false;
        int retryCount = MAX_RETRY_COUNT;

        do {

            try {

                return connection.prepareStatement(sqlStatement);

            } catch (SQLException e) {
                if (SqlState._08003.matches(e)) {
                    // TODO: ask user for reconnection?
                    if (retryCount <= 0) {
                        AutoReconnectErrorException exception = new AutoReconnectErrorException(connectionName);
                        throw exception;
                    } else if (!JDBCConnectionManager.getInstance().reconnect(this)) {
                        throw e;
                    } else {
                        isRetry = true;
                        retryCount--;
                    }
                } else {
                    throw e;
                }
            }

        } while (isRetry);

        return null;
    }

    public void closeResultSet(ResultSet resultSet) {

        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                String message = "*** Could not close a result statement ***"; //$NON-NLS-1$
                RapidFireCorePlugin.logError(message, e);
                MessageDialogAsync.displayError(message);
            }
        }
    }

    public CallableStatement prepareCall(String sqlStatement) throws Exception {

        sqlStatement = insertLibraryQualifier(sqlStatement);

        boolean isRetry = false;
        int retryCount = MAX_RETRY_COUNT;

        do {

            try {

                return connection.prepareCall(sqlStatement);

            } catch (SQLException e) {
                if (SqlState._08003.matches(e)) {
                    // TODO: ask user for reconnection?
                    if (retryCount <= 0) {
                        AutoReconnectErrorException exception = new AutoReconnectErrorException(connectionName);
                        throw exception;
                    } else if (!JDBCConnectionManager.getInstance().reconnect(this)) {
                        throw e;
                    } else {
                        isRetry = true;
                        retryCount--;
                    }
                } else {
                    throw e;
                }
            }

        } while (isRetry);

        return null;
    }

    public void closeStatement(PreparedStatement preparedStatement) {

        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                String message = "*** Could not close a prepared statement ***"; //$NON-NLS-1$
                RapidFireCorePlugin.logError(message, e);
                MessageDialogAsync.displayError(message);
            }
        }
    }

    /**
     * Used by the JDBCConnectionManager when closing a connection.
     */
    void close() {

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Used by the JDBCConnectionManager when querying the connection status.
     * 
     * @throws SQLException
     */
    boolean isClosed() throws SQLException {
        return connection.isClosed();
    }

    public String insertLibraryQualifier(String sqlStatement) {
        return sqlStatement.replaceAll(IJDBCConnection.LIBRARY, sqlHelper.quoteName(libraryName) + getSeparator());
    }

    public boolean convertYesNo(String yesNoValue) {

        if (BOOLEAN_YES.equals(yesNoValue) || BOOLEAN_Y.equals(yesNoValue)) {
            return true;
        } else if (BOOLEAN_NO.equals(yesNoValue) || BOOLEAN_N.equals(yesNoValue)) {
            return false;
        }

        throw new IllegalParameterException("yesNoValue", yesNoValue);
    }

    private String getSeparator() {
        return sqlHelper.getCatalogSeparator();
    }

    public boolean isAutoCommit() {
        return isAutoCommit;
    }

    public String getKey() {
        return createKey(getConnectionName(), getLibraryName(), isAutoCommit());
    }

    public static String createKey(String connectionName, String libraryName, boolean isAutoCommit) {

        StringBuilder buffer = new StringBuilder();

        buffer.append(connectionName);
        buffer.append(":"); //$NON-NLS-1$
        buffer.append(libraryName);
        buffer.append(":autocommit="); //$NON-NLS-1$
        buffer.append(isAutoCommit);

        return buffer.toString();
    }

    @Override
    public String toString() {

        StringBuilder buffer = new StringBuilder();

        buffer.append(getConnectionName());
        buffer.append(":"); //$NON-NLS-1$
        buffer.append(getLibraryName());
        buffer.append(":autocommit="); //$NON-NLS-1$
        buffer.append(isAutoCommit());

        return buffer.toString();
    }
}
