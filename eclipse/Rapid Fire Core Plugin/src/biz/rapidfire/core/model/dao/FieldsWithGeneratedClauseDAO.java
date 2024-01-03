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

import biz.rapidfire.core.model.FieldWithGeneratedClause;
import biz.rapidfire.core.model.IRapidFireJobResource;

public class FieldsWithGeneratedClauseDAO {

    public static final String JOB = "JOB"; //$NON-NLS-1$
    public static final String LIBRARY = "LIBRARY"; //$NON-NLS-1$
    public static final String FILE = "FILE"; //$NON-NLS-1$
    public static final String FIELD = "FIELD"; //$NON-NLS-1$
    public static final String TEXT = "TEXT"; //$NON-NLS-1$

    private IJDBCConnection dao;

    public FieldsWithGeneratedClauseDAO(IJDBCConnection dao) {

        this.dao = dao;
    }

    public List<FieldWithGeneratedClause> load(IRapidFireJobResource job, Shell shell) throws Exception {

        final List<FieldWithGeneratedClause> fields = new ArrayList<FieldWithGeneratedClause>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        if (!dao.checkRapidFireLibrary(shell)) {
            return fields;
        }

        try {

            String sqlStatement = getSqlStatement();
            preparedStatement = dao.prepareStatement(sqlStatement);
            preparedStatement.setString(1, job.getName());
            resultSet = preparedStatement.executeQuery();
            resultSet.setFetchSize(50);

            if (resultSet != null) {
                while (resultSet.next()) {
                    fields.add(produceFields(resultSet, job));
                }
            }
        } finally {
            dao.closeStatement(preparedStatement);
            dao.closeResultSet(resultSet);
        }

        return fields;
    }

    private FieldWithGeneratedClause produceFields(ResultSet resultSet, IRapidFireJobResource job) throws SQLException {

        // String job = resultSet.getString(JOB).trim();
        String library = resultSet.getString(LIBRARY).trim();
        String file = resultSet.getString(FILE).trim();
        String field = resultSet.getString(FIELD).trim();
        String text = resultSet.getString(TEXT).trim();

        FieldWithGeneratedClause _field = new FieldWithGeneratedClause(job, library, file, field, text);

        return _field;
    }

    private String getSqlStatement() throws Exception {

        // @formatter:off
        String sqlStatement = 
        "SELECT " +
            "JOB, " +
            "LIBRARY, " +
            "FILE, " +
            "FIELD, " +
            "TEXT " +
        "FROM " +
            IJDBCConnection.LIBRARY +
            "FLDGENCLS " +
        "WHERE " +
            "JOB = ?";
        // @formatter:on

        return dao.insertLibraryQualifier(sqlStatement);
    }
}
