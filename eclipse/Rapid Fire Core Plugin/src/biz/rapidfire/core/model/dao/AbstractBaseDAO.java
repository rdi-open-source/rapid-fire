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

import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.helpers.RapidFireHelper;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCConnection;
import com.ibm.as400.access.Job;

public abstract class AbstractBaseDAO {

    private static final String BOOLEAN_Y = "Y"; //$NON-NLS-1$
    private static final String BOOLEAN_N = "N"; //$NON-NLS-1$
    private static final String BOOLEAN_YES = "*YES"; //$NON-NLS-1$
    private static final String BOOLEAN_NO = "*NO"; //$NON-NLS-1$

    public String insertLibraryQualifier(String sqlStatement, String libraryName) throws Exception {

        return sqlStatement.replaceAll(IBaseDAO.LIBRARY, libraryName + getSeparator(getJdbcConnection(libraryName)));
    }

    public PreparedStatement prepareStatement(String sql, String defaultLibrary) throws Exception {

        // Actually we should call IBMiConnection.getConnection() via the
        // abstract method getJdbcConnection().
        // But because IBMiConnection does not properly handle the "properties"
        // parameter (actually ignoring it), we have to do it this way to set
        // the default schema.
        // Connection connection = getJdbcConnection(defaultLibrary)

        Connection jdbcConnection;

        try {
            jdbcConnection = getJdbcConnection(defaultLibrary);
        } catch (OperationCanceledException e) {
            return null;
        }

        return jdbcConnection.prepareStatement(sql);
    }

    public void destroy(ResultSet resultSet) throws Exception {
        if (resultSet != null) {
            resultSet.close();
        }
    }

    public void destroy(PreparedStatement preparedStatement) throws Exception {
        if (preparedStatement != null) {
            preparedStatement.close();
        }
    }

    public void rollback(Connection connection) throws Exception {
        if (connection != null && !connection.isClosed()) {
            if (connection.getAutoCommit() == false) {
                connection.rollback();
            }
        }
    }

    public void commit(Connection connection) throws Exception {
        if (connection != null && !connection.isClosed()) {
            if (connection.getAutoCommit() == false) {
                connection.commit();
            }
        }
    }

    public boolean convertYesNo(String yesNoValue) {

        boolean booleanValue;
        if (BOOLEAN_YES.equals(yesNoValue) || BOOLEAN_Y.equals(yesNoValue)) {
            booleanValue = true;
        } else {
            booleanValue = false;
        }

        return booleanValue;
    }

    public boolean checkRapidFireLibrary(Shell shell, String libraryName) throws Exception {
        return RapidFireHelper.checkRapidFireLibrary(shell, getSystem(), libraryName);
    }

    protected String getCurrentLibrary(Connection jdbcConnection) throws Exception {

        Job serverJob = getServerJob(jdbcConnection);

        if (serverJob.getCurrentLibraryExistence()) {
            return serverJob.getCurrentLibrary();
        } else {
            return "*CRTDFT"; //$NON-NLS-1$
        }
    }

    protected String setCurrentLibrary(Connection jdbcConnection, String libraryName) throws Exception {

        String currentLibrary = null;
        CallableStatement statement = null;

        try {

            currentLibrary = getCurrentLibrary(jdbcConnection);

            statement = jdbcConnection.prepareCall("CALL QCMDEXC('CHGCURLIB CURLIB(" + libraryName + ")')"); //$NON-NLS-1$ //$NON-NLS-2$
            statement.execute();

        } finally {
            if (statement != null) {
                statement.close();
            }
        }

        return currentLibrary;
    }

    public String getSeparator(Connection jdbcConnection) {

        String separator;
        try {
            separator = jdbcConnection.getMetaData().getCatalogSeparator();
        } catch (SQLException e) {
            RapidFireCorePlugin.logError("*** Could not get the JDBC catalog separator ***", e); //$NON-NLS-1$
            separator = "."; //$NON-NLS-1$
        }

        return separator;
    }

    public abstract AS400 getSystem();

    public abstract Connection getJdbcConnection(String defaultSchema) throws Exception;

    public abstract String getHostName();

    public abstract String getConnectionName();

    protected Job getServerJob(Connection jdbcConnection) {

        AS400JDBCConnection as400JdbcConnection = (AS400JDBCConnection)jdbcConnection;
        String serverJobIdentifier = as400JdbcConnection.getServerJobIdentifier();
        String jobName = serverJobIdentifier.substring(0, 10);
        String userName = serverJobIdentifier.substring(10, 20);
        String jobNumber = serverJobIdentifier.substring(20, 26);

        Job serverJob = new Job(getSystem(), jobName, userName, jobNumber);

        return serverJob;
    }

}
