/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.eclipse.core.runtime.OperationCanceledException;

import com.ibm.as400.access.AS400;

public abstract class AbstractBaseDAO {

    private static final String BOOLEAN_Y = "Y"; //$NON-NLS-1$
    private static final String BOOLEAN_N = "N"; //$NON-NLS-1$
    private static final String BOOLEAN_YES = "*YES"; //$NON-NLS-1$
    private static final String BOOLEAN_NO = "*NO"; //$NON-NLS-1$

    public PreparedStatement prepareStatement(String sql, String defaultLibrary) throws Exception {

        // Actually we should call IBMiConnection.getConnection() via the
        // abstract method getJdbcConnection().
        // But because IBMiConnection does not properly handle the "properties"
        // parameter (actually ignoring it), we have to do it this way to set
        // the default schema.
        // Connection connection = getJdbcConnection(defaultLibrary)
        JdbcConnectionService service = JdbcConnectionManager.getInstance().getJdbcConnectionService(getHostName(), getSystem());

        Connection jdbcConnection;

        try {
            jdbcConnection = service.getJdbcConnection(getUser(), getPassword(), defaultLibrary);
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

    public abstract AS400 getSystem() throws Exception;

    protected abstract String getUser();

    protected abstract String getPassword();

    public abstract String getHostName();

    public abstract String getConnectionName();

}
