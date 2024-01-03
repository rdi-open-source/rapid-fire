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

import biz.rapidfire.core.model.IRapidFireAreaResource;
import biz.rapidfire.core.model.IRapidFireFileResource;

public abstract class AbstractAreasDAO {

    public static final String JOB = "JOB"; //$NON-NLS-1$
    public static final String POSITION = "POSITION"; //$NON-NLS-1$
    public static final String AREA = "AREA"; //$NON-NLS-1$
    public static final String LIBRARY = "LIBRARY"; //$NON-NLS-1$
    public static final String LIBRARY_LIST = "LIBRARY_LIST"; //$NON-NLS-1$
    public static final String LIBRARY_CCSID = "CCSID"; //$NON-NLS-1$
    public static final String COMMAND_EXTENSION = "COMMAND_EXTENSION"; //$NON-NLS-1$

    private IJDBCConnection dao;

    public AbstractAreasDAO(IJDBCConnection dao) {

        this.dao = dao;
    }

    public List<IRapidFireAreaResource> load(IRapidFireFileResource file, Shell shell) throws Exception {

        final List<IRapidFireAreaResource> areas = new ArrayList<IRapidFireAreaResource>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        if (!dao.checkRapidFireLibrary(shell)) {
            return areas;
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
                    areas.add(produceFile(resultSet, file));
                }
            }
        } finally {
            dao.closeStatement(preparedStatement);
            dao.closeResultSet(resultSet);
        }

        return areas;
    }

    public IRapidFireAreaResource load(IRapidFireFileResource file, String areaName, Shell shell) throws Exception {

        IRapidFireAreaResource area = null;

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        if (!dao.checkRapidFireLibrary(shell)) {
            return area;
        }

        try {

            String sqlStatement = getSqlStatement();
            sqlStatement = sqlStatement + " AND AREA = ?";
            preparedStatement = dao.prepareStatement(sqlStatement);
            preparedStatement.setString(1, file.getJob());
            preparedStatement.setInt(2, file.getPosition());
            preparedStatement.setString(3, areaName);
            resultSet = preparedStatement.executeQuery();
            resultSet.setFetchSize(50);

            if (resultSet != null) {
                while (resultSet.next()) {
                    area = produceFile(resultSet, file);
                }
            }
        } finally {
            dao.closeStatement(preparedStatement);
            dao.closeResultSet(resultSet);
        }

        return area;
    }

    private IRapidFireAreaResource produceFile(ResultSet resultSet, IRapidFireFileResource file) throws SQLException {

        // String job = resultSet.getString(JOB).trim();
        String area = resultSet.getString(AREA).trim();

        IRapidFireAreaResource areaResource = createAreaInstance(file, area);

        String library = resultSet.getString(LIBRARY).trim();
        String libraryList = resultSet.getString(LIBRARY_LIST).trim();
        String libraryCcsid = resultSet.getString(LIBRARY_CCSID).trim();
        String commandExtension = resultSet.getString(COMMAND_EXTENSION).trim();

        areaResource.setLibrary(library);
        areaResource.setLibraryList(libraryList);
        areaResource.setLibraryCcsid(libraryCcsid);
        areaResource.setCommandExtension(commandExtension);

        return areaResource;
    }

    protected abstract IRapidFireAreaResource createAreaInstance(IRapidFireFileResource file, String area);

    private String getSqlStatement() throws Exception {

        // @formatter:off
        String sqlStatement = 
        "SELECT " +
            "JOB, " +
            "POSITION, " +
            "AREA, " +
            "LIBRARY, " +
            "LIBRARY_LIST, " +
            "CCSID, " +
            "COMMAND_EXTENSION, " +
            "FILE, " +
            "WORKFILE_RECORDS, " +
            "WORKFILE_CHANGES, " +
            "WORKFILE_CHANGESLOG, " +
            "JOURNALING, " +
            "JOURNAL_LIBRARY, " +
            "JOURNAL, " +
            "RECORDS_TO_COPY, " +
            "RECORDS_COPIED, " +
            "TIME_CONSUMED, " +
            "CHANGES_TO_APPLY, " +
            "CHANGES_APPLIED " +
        "FROM " +
            IJDBCConnection.LIBRARY +
            "AREAS " +
        "WHERE " +
            "JOB = ? AND " +
            "POSITION = ?";
        // @formatter:on

        return dao.insertLibraryQualifier(sqlStatement);
    }
}
