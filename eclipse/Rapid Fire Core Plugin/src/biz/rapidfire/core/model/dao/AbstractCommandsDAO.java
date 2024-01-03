/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.maintenance.command.shared.CommandType;
import biz.rapidfire.core.model.IRapidFireCommandResource;
import biz.rapidfire.core.model.IRapidFireFileResource;

public abstract class AbstractCommandsDAO {

    public static final String JOB = "JOB"; //$NON-NLS-1$
    public static final String POSITION = "POSITION"; //$NON-NLS-1$
    public static final String TYPE = "TYPE"; //$NON-NLS-1$
    public static final String SEQUENCE = "SEQUENCE"; //$NON-NLS-1$
    public static final String COMMAND = "COMMAND"; //$NON-NLS-1$

    private IJDBCConnection dao;

    public AbstractCommandsDAO(IJDBCConnection dao) {

        this.dao = dao;
    }

    public List<IRapidFireCommandResource> load(IRapidFireFileResource file, Shell shell) throws Exception {

        final List<IRapidFireCommandResource> conversions = new ArrayList<IRapidFireCommandResource>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        if (!dao.checkRapidFireLibrary(shell)) {
            return conversions;
        }

        try {

            String sqlStatement = getSqlStatement();
            preparedStatement = dao.prepareStatement(sqlStatement);
            preparedStatement.setString(1, file.getJob());
            preparedStatement.setInt(2, file.getPosition());
            resultSet = preparedStatement.executeQuery();
            resultSet.setFetchSize(50);

            if (resultSet != null) {
                while (resultSet.next()) {
                    conversions.add(produceCommand(resultSet, file));
                }
            }
        } finally {
            dao.closeStatement(preparedStatement);
            dao.closeResultSet(resultSet);
        }

        return conversions;
    }

    public IRapidFireCommandResource load(IRapidFireFileResource file, CommandType commandType, int sequence, Shell shell) throws Exception {

        IRapidFireCommandResource conversion = null;

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        if (!dao.checkRapidFireLibrary(shell)) {
            return conversion;
        }

        try {

            String sqlStatement = getSqlStatement();
            sqlStatement = sqlStatement + " AND TYPE = ? AND SEQUENCE = ?";
            preparedStatement = dao.prepareStatement(sqlStatement);
            preparedStatement.setString(1, file.getJob());
            preparedStatement.setInt(2, file.getPosition());
            preparedStatement.setString(3, commandType.label());
            preparedStatement.setInt(4, sequence);
            resultSet = preparedStatement.executeQuery();
            resultSet.setFetchSize(50);

            if (resultSet != null) {
                while (resultSet.next()) {
                    conversion = produceCommand(resultSet, file);
                }
            }
        } finally {
            dao.closeStatement(preparedStatement);
            dao.closeResultSet(resultSet);
        }

        return conversion;
    }

    private IRapidFireCommandResource produceCommand(ResultSet resultSet, IRapidFireFileResource file) throws SQLException {

        // String job = resultSet.getString(JOB).trim();
        String commandType = resultSet.getString(TYPE).trim();
        int sequence = resultSet.getBigDecimal(SEQUENCE).intValue();

        IRapidFireCommandResource conversionResource = createCommandInstance(file, CommandType.find(commandType), sequence);

        String command = resultSet.getString(COMMAND).trim();

        conversionResource.setCommand(command);

        return conversionResource;
    }

    protected abstract IRapidFireCommandResource createCommandInstance(IRapidFireFileResource file, CommandType commandType, int sequence);

    private String getSqlStatement() throws Exception {

        // @formatter:off
        String sqlStatement = 
        "SELECT " +
            "JOB, " +
            "POSITION, " +
            "TYPE, " +
            "SEQUENCE, " +
            "COMMAND " +
        "FROM " +
            IJDBCConnection.LIBRARY +
            "COMMANDS " +
        "WHERE " +
            "JOB = ? AND " +
            "POSITION = ?";
        // @formatter:on

        return dao.insertLibraryQualifier(sqlStatement);
    }
}
