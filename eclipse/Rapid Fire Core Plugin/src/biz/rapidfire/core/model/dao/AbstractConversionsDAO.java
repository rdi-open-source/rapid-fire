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
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.model.IRapidFireConversionResource;
import biz.rapidfire.core.model.IRapidFireFileResource;

public abstract class AbstractConversionsDAO {

    public static final String JOB = "JOB"; //$NON-NLS-1$
    public static final String POSITION = "POSITION"; //$NON-NLS-1$
    public static final String FIELD_TO_CONVERT = "FIELD_TO_CONVERT"; //$NON-NLS-1$
    public static final String RENAME_FIELD_IN_OLD_FILE_TO = "RENAME_FIELD_IN_OLD_FILE_TO"; //$NON-NLS-1$
    public static final String STATEMENT_1 = "STATEMENT_1"; //$NON-NLS-1$
    public static final String STATEMENT_2 = "STATEMENT_2"; //$NON-NLS-1$
    public static final String STATEMENT_3 = "STATEMENT_3"; //$NON-NLS-1$
    public static final String STATEMENT_4 = "STATEMENT_4"; //$NON-NLS-1$
    public static final String STATEMENT_5 = "STATEMENT_5"; //$NON-NLS-1$
    public static final String STATEMENT_6 = "STATEMENT_6"; //$NON-NLS-1$

    private IJDBCConnection dao;

    public AbstractConversionsDAO(IJDBCConnection dao) {

        this.dao = dao;
    }

    public List<IRapidFireConversionResource> load(IRapidFireFileResource file, Shell shell) throws Exception {

        final List<IRapidFireConversionResource> conversions = new ArrayList<IRapidFireConversionResource>();

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
                    conversions.add(produceFile(resultSet, file));
                }
            }
        } finally {
            dao.closeStatement(preparedStatement);
            dao.closeResultSet(resultSet);
        }

        return conversions;
    }

    public IRapidFireConversionResource load(IRapidFireFileResource file, String fieldToConvert, Shell shell) throws Exception {

        IRapidFireConversionResource conversion = null;

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        if (!dao.checkRapidFireLibrary(shell)) {
            return conversion;
        }

        try {

            String sqlStatement = getSqlStatement();
            sqlStatement = sqlStatement + " AND FIELD_TO_CONVERT = ?";
            preparedStatement = dao.prepareStatement(sqlStatement);
            preparedStatement.setString(1, file.getJob());
            preparedStatement.setInt(2, file.getPosition());
            preparedStatement.setString(3, fieldToConvert);
            resultSet = preparedStatement.executeQuery();
            resultSet.setFetchSize(50);

            if (resultSet != null) {
                while (resultSet.next()) {
                    conversion = produceFile(resultSet, file);
                }
            }
        } finally {
            dao.closeStatement(preparedStatement);
            dao.closeResultSet(resultSet);
        }

        return conversion;
    }

    private IRapidFireConversionResource produceFile(ResultSet resultSet, IRapidFireFileResource file) throws SQLException {

        // String job = resultSet.getString(JOB).trim();
        String fieldToConvert = resultSet.getString(FIELD_TO_CONVERT).trim();

        IRapidFireConversionResource conversionResource = createConversionInstance(file, fieldToConvert);

        String newFieldName = resultSet.getString(RENAME_FIELD_IN_OLD_FILE_TO).trim();

        List<String> conversions = new LinkedList<String>();
        conversions.add(resultSet.getString(STATEMENT_1).trim());
        conversions.add(resultSet.getString(STATEMENT_2).trim());
        conversions.add(resultSet.getString(STATEMENT_3).trim());
        conversions.add(resultSet.getString(STATEMENT_4).trim());
        conversions.add(resultSet.getString(STATEMENT_5).trim());
        conversions.add(resultSet.getString(STATEMENT_6).trim());

        conversionResource.setNewFieldName(newFieldName);
        conversionResource.setConversions(conversions.toArray(new String[conversions.size()]));

        return conversionResource;
    }

    protected abstract IRapidFireConversionResource createConversionInstance(IRapidFireFileResource file, String conversion);

    private String getSqlStatement() throws Exception {

        // @formatter:off
        String sqlStatement = 
        "SELECT " +
            "JOB, " +
            "POSITION, " +
            "FIELD_TO_CONVERT, " +
            "RENAME_FIELD_IN_OLD_FILE_TO, " +
            "STATEMENT_1, " +
            "STATEMENT_2, " +
            "STATEMENT_3, " +
            "STATEMENT_4, " +
            "STATEMENT_5, " +
            "STATEMENT_6 " +
        "FROM " +
            IJDBCConnection.LIBRARY +
            "CNVS " +
        "WHERE " +
            "JOB = ? AND " +
            "POSITION = ?";
        // @formatter:on

        return dao.insertLibraryQualifier(sqlStatement);
    }
}
