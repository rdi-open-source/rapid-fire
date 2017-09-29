/*******************************************************************************
 * Copyright (c) 2012-2017 iSphere Project Owners
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

import biz.rapidfire.rse.model.dao.AbstractDAOBase;

public class DAOBase extends AbstractDAOBase {

    private static final String BOOLEAN_Y = "Y";
    private static final String BOOLEAN_N = "N";
    private static final String BOOLEAN_YES = "*YES";
    private static final String BOOLEAN_NO = "*NO";

    public DAOBase(String connectionName) throws Exception {
        super(connectionName);
    }

    protected PreparedStatement prepareStatement(String sql) throws SQLException {
        return getConnection().prepareStatement(sql);
    }

    protected void destroy(Connection connection) throws Exception {
        if (connection != null && !connection.isClosed()) connection.close();
    }

    protected void destroy(ResultSet resultSet) throws Exception {
        if (resultSet != null) resultSet.close();
    }

    protected void destroy(PreparedStatement preparedStatement) throws Exception {
        if (preparedStatement != null) preparedStatement.close();
    }

    protected void rollback(Connection connection) throws Exception {
        if (connection != null && !connection.isClosed()) {
            if (connection.getAutoCommit() == false) connection.rollback();
        }
    }

    protected void commit(Connection connection) throws Exception {
        if (connection != null && !connection.isClosed()) {
            if (connection.getAutoCommit() == false) connection.commit();
        }
    }

    protected boolean convertYesNo(String yesNoValue) {

        boolean booleanValue;
        if (BOOLEAN_YES.equals(yesNoValue) || BOOLEAN_Y.equals(yesNoValue)) {
            booleanValue = true;
        } else {
            booleanValue = false;
        }

        return booleanValue;
    }
}
