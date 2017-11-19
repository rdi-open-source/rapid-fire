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
import biz.rapidfire.core.exceptions.IllegalParameterException;
import biz.rapidfire.core.helpers.RapidFireHelper;

import com.ibm.as400.access.AS400;

class JDBCConnection implements IJDBCConnection {

    private static final String BOOLEAN_Y = "Y"; //$NON-NLS-1$
    private static final String BOOLEAN_N = "N"; //$NON-NLS-1$
    private static final String BOOLEAN_YES = "*YES"; //$NON-NLS-1$
    private static final String BOOLEAN_NO = "*NO"; //$NON-NLS-1$

    private String connectionName;
    private AS400 system;
    private Connection jdbcConnection;
    private String libraryName;
    private boolean isCommitControl;

    public JDBCConnection(String connectionName, AS400 system, Connection jdbcConnection, String libraryName, boolean isCommitControl)
        throws Exception {

        this.connectionName = connectionName;
        this.system = system;
        this.jdbcConnection = jdbcConnection;
        this.libraryName = libraryName;
        this.isCommitControl = isCommitControl;
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

    public Connection getJdbcConnection() {
        return jdbcConnection;
    }

    /**
     * Used by the JDBCConnectionManager when reconnecting a connection.
     * 
     * @param jdbcConnection
     */
    void setJdbcConnection(Connection jdbcConnection) {
        this.jdbcConnection = jdbcConnection;
    }

    public boolean checkRapidFireLibrary(Shell shell) {
        return RapidFireHelper.checkRapidFireLibrary(shell, getSystem(), libraryName);
    }

    public PreparedStatement prepareStatement(String sqlStatement) throws Exception {

        Connection jdbcConnection;

        sqlStatement = insertLibraryQualifier(sqlStatement);
        jdbcConnection = getJdbcConnection();

        boolean isRetry = false;

        do {

            try {

                return jdbcConnection.prepareStatement(sqlStatement);

            } catch (SQLException e) {
                if ("08003".equals(e.getSQLState())) {
                    // TODO: ask user for reconnection.
                    if (!JDBCConnectionManager.getInstance().reconnect(this)) {
                        throw e;
                    } else {
                        isRetry = true;
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
        Connection jdbcConnection = getJdbcConnection();

        boolean isRetry = false;

        do {

            try {

                return jdbcConnection.prepareCall(sqlStatement);

            } catch (SQLException e) {
                if ("08003".equals(e.getSQLState())) {
                    // TODO: ask user for reconnection.
                    if (!JDBCConnectionManager.getInstance().reconnect(this)) {
                        throw e;
                    } else {
                        isRetry = true;
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

    public void closeJdbcConnection() {

        try {
            jdbcConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String insertLibraryQualifier(String sqlStatement) {
        return sqlStatement.replaceAll(IJDBCConnection.LIBRARY, libraryName + getSeparator(getJdbcConnection()));
    }

    public boolean convertYesNo(String yesNoValue) {

        if (BOOLEAN_YES.equals(yesNoValue) || BOOLEAN_Y.equals(yesNoValue)) {
            return true;
        } else if (BOOLEAN_NO.equals(yesNoValue) || BOOLEAN_N.equals(yesNoValue)) {
            return false;
        }

        throw new IllegalParameterException("yesNoValue", yesNoValue);
    }

    private String getSeparator(Connection jdbcConnection) {

        String separator;
        try {
            separator = jdbcConnection.getMetaData().getCatalogSeparator();
        } catch (SQLException e) {
            RapidFireCorePlugin.logError("*** Could not get the JDBC catalog separator ***", e); //$NON-NLS-1$
            separator = "."; //$NON-NLS-1$
        }

        return separator;
    }

    public boolean isCommitControl() {
        return isCommitControl;
    }

    @Override
    public String toString() {

        StringBuilder buffer = new StringBuilder();

        buffer.append(getConnectionName());
        buffer.append(":");
        buffer.append(getLibraryName());
        buffer.append(":");
        buffer.append("Commit=");
        buffer.append(isCommitControl());

        return buffer.toString();
    }
}
