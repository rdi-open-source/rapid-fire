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

import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.IRapidFireLibraryListResource;

public abstract class AbstractLibraryListsDAO {

    public static final String JOB = "JOB"; //$NON-NLS-1$
    public static final String LIBRARY_LIST = "LIBRARY_LIST"; //$NON-NLS-1$
    public static final String DESCRIPTION = "DESCRIPTION"; //$NON-NLS-1$

    public static final String SEQUENCE = "SEQUENCE"; //$NON-NLS-1$
    public static final String LIBRARY = "LIBRARY"; //$NON-NLS-1$

    private IJDBCConnection dao;

    public AbstractLibraryListsDAO(IJDBCConnection dao) {

        this.dao = dao;
    }

    public List<IRapidFireLibraryListResource> load(IRapidFireJobResource job, Shell shell) throws Exception {

        final List<IRapidFireLibraryListResource> libraryLists = new ArrayList<IRapidFireLibraryListResource>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatementLibraryList = null;

        if (!dao.checkRapidFireLibrary(shell)) {
            return libraryLists;
        }

        try {

            String sqlStatement = getSqlStatement();
            preparedStatement = dao.prepareStatement(sqlStatement);
            preparedStatement.setString(1, job.getName());
            resultSet = preparedStatement.executeQuery();
            resultSet.setFetchSize(50);

            preparedStatementLibraryList = prepareStatementLibraryList(getSqlStatementToLoadLibraries());

            if (resultSet != null) {
                while (resultSet.next()) {
                    libraryLists.add(produceLibraryList(resultSet, job, preparedStatementLibraryList));
                }
            }
        } finally {
            dao.closeStatement(preparedStatement);
            dao.closeResultSet(resultSet);
            dao.closeStatement(preparedStatementLibraryList);
        }

        return libraryLists;
    }

    public IRapidFireLibraryListResource load(IRapidFireJobResource job, String libraryListName, Shell shell) throws Exception {

        IRapidFireLibraryListResource libraryList = null;

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatementLibraryList = null;

        if (!dao.checkRapidFireLibrary(shell)) {
            return libraryList;
        }

        try {

            String sqlStatement = getSqlStatement();
            sqlStatement = sqlStatement + " AND LIBRARY_LIST = ?";
            preparedStatement = dao.prepareStatement(sqlStatement);
            preparedStatement.setString(1, job.getName());
            preparedStatement.setString(2, libraryListName);
            resultSet = preparedStatement.executeQuery();
            resultSet.setFetchSize(50);

            preparedStatementLibraryList = prepareStatementLibraryList(getSqlStatementToLoadLibraries());

            if (resultSet != null) {
                while (resultSet.next()) {
                    libraryList = produceLibraryList(resultSet, job, preparedStatementLibraryList);
                }
            }
        } finally {
            dao.closeStatement(preparedStatement);
            dao.closeResultSet(resultSet);
            dao.closeStatement(preparedStatementLibraryList);
        }

        return libraryList;
    }

    private IRapidFireLibraryListResource produceLibraryList(ResultSet resultSet, IRapidFireJobResource job,
        PreparedStatement preparedStatementLibraryList) throws SQLException {

        // String job = resultSet.getString(JOB).trim();
        String libraryListName = resultSet.getString(LIBRARY_LIST).trim();

        IRapidFireLibraryListResource libraryListsResource = createLibraryListInstance(job, libraryListName);

        String description = resultSet.getString(DESCRIPTION).trim();

        libraryListsResource.setDescription(description);

        preparedStatementLibraryList.setString(1, job.getName());
        preparedStatementLibraryList.setString(2, libraryListName);

        ResultSet resultSetLibraryListEntries = null;

        try {

            resultSetLibraryListEntries = preparedStatementLibraryList.executeQuery();
            if (resultSetLibraryListEntries != null) {
                while (resultSetLibraryListEntries.next()) {
                    int sequenceNumber = resultSetLibraryListEntries.getInt(SEQUENCE);
                    String libraryName = resultSetLibraryListEntries.getString(LIBRARY);
                    libraryListsResource.addLibraryListEntry(sequenceNumber, libraryName);
                }
            }

        } finally {
            if (resultSetLibraryListEntries != null) {
                dao.closeResultSet(resultSetLibraryListEntries);
            }
        }

        return libraryListsResource;
    }

    private PreparedStatement prepareStatementLibraryList(String sqlStatement) throws Exception {

        PreparedStatement preparedStatement = dao.prepareStatement(sqlStatement);

        return preparedStatement;
    }

    protected abstract IRapidFireLibraryListResource createLibraryListInstance(IRapidFireJobResource job, String library);

    private String getSqlStatement() throws Exception {

        // @formatter:off
        String sqlStatement = 
        "SELECT " +
            "JOB, " +
            "LIBRARY_LIST, " +
            "DESCRIPTION " +
        "FROM " +
            IJDBCConnection.LIBRARY +
            "LIBRARY_LISTS " +
        "WHERE " +
            "JOB = ?";
        // @formatter:on

        return dao.insertLibraryQualifier(sqlStatement);
    }

    private String getSqlStatementToLoadLibraries() throws Exception {

        // @formatter:off
        String sqlStatement = 
        "SELECT " +
            "A.JOB, " +
            "A.LIBRARY_LIST, " +
            "B.SEQUENCE, " +
            "B.LIBRARY " +
        "FROM " +
            IJDBCConnection.LIBRARY +
            "LIBRARY_LISTS A " +
        "LEFT JOIN " +
            IJDBCConnection.LIBRARY +
            "LIBRARY_LIST_ENTRIES B " +
        "ON " +
            "A.JOB = B.JOB AND " +
            "A.LIBRARY_LIST = B.LIBRARY_LIST " +
        "WHERE " +
            "A.JOB = ? AND " +
            "A.LIBRARY_LIST = ?" +
        "ORDER BY " +
            "B.JOB, " +
            "B.LIBRARY_LIST, " +
            "B.SEQUENCE";
        // @formatter:on

        return dao.insertLibraryQualifier(sqlStatement);
    }
}
