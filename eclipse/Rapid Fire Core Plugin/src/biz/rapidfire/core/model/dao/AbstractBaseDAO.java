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
import java.sql.SQLException;

import biz.rapidfire.core.RapidFireCorePlugin;

import com.ibm.as400.access.AS400;

public abstract class AbstractBaseDAO {

    // protected static final String properties = "thread used=false; extendeddynamic=true; package criteria=select; package cache=true;"; //$NON-NLS-1$
    protected static final String properties = "translate hex=binary; prompt=false; extended dynamic=true; package cache=true"; //$NON-NLS-1$

    private static final String BOOLEAN_Y = "Y"; //$NON-NLS-1$
    private static final String BOOLEAN_N = "N"; //$NON-NLS-1$
    private static final String BOOLEAN_YES = "*YES"; //$NON-NLS-1$
    private static final String BOOLEAN_NO = "*NO"; //$NON-NLS-1$

    protected Connection connection;

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return getConnection().prepareStatement(sql);
    }

    public void destroy() {

        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    connection = null;
                }
            } catch (Throwable e) {
                RapidFireCorePlugin.logError("*** Could not destroy connection ***", e); //$NON-NLS-1$
            }
        }
    }

    public void destroy(Connection connection) throws Exception {
        if (connection != null && !connection.isClosed()) connection.close();
    }

    public void destroy(ResultSet resultSet) throws Exception {
        if (resultSet != null) resultSet.close();
    }

    public void destroy(PreparedStatement preparedStatement) throws Exception {
        if (preparedStatement != null) preparedStatement.close();
    }

    public void rollback(Connection connection) throws Exception {
        if (connection != null && !connection.isClosed()) {
            if (connection.getAutoCommit() == false) connection.rollback();
        }
    }

    public void commit(Connection connection) throws Exception {
        if (connection != null && !connection.isClosed()) {
            if (connection.getAutoCommit() == false) connection.commit();
        }
    }

    public Connection getConnection() {
        return connection;
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

    public abstract String getConnectionName();

}
