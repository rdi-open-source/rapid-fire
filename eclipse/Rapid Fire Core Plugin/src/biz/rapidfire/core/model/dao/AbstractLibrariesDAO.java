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
import biz.rapidfire.core.model.IRapidFireLibraryResource;

public abstract class AbstractLibrariesDAO {

    public static final String JOB = "JOB"; //$NON-NLS-1$
    public static final String LIBRARY = "LIBRARY"; //$NON-NLS-1$
    public static final String SHADOW_LIBRARY = "SHADOW_LIBRARY"; //$NON-NLS-1$

    private IJDBCConnection dao;

    public AbstractLibrariesDAO(IJDBCConnection dao) {

        this.dao = dao;
    }

    public List<IRapidFireLibraryResource> load(IRapidFireJobResource job, Shell shell) throws Exception {

        final List<IRapidFireLibraryResource> libraries = new ArrayList<IRapidFireLibraryResource>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        if (!dao.checkRapidFireLibrary(shell)) {
            return libraries;
        }

        try {

            String sqlStatement = getSqlStatement();
            preparedStatement = dao.prepareStatement(sqlStatement);
            preparedStatement.setString(1, job.getName());
            resultSet = preparedStatement.executeQuery();
            resultSet.setFetchSize(50);

            if (resultSet != null) {
                while (resultSet.next()) {
                    libraries.add(produceLibrary(resultSet, job));
                }
            }
        } finally {
            dao.closeStatement(preparedStatement);
            dao.closeResultSet(resultSet);
        }

        return libraries;
    }

    public IRapidFireLibraryResource load(IRapidFireJobResource job, String libraryName, Shell shell) throws Exception {

        IRapidFireLibraryResource library = null;

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        if (!dao.checkRapidFireLibrary(shell)) {
            return null;
        }

        try {

            String sqlStatement = getSqlStatement();
            sqlStatement = sqlStatement + " AND LIBRARY = ?";
            preparedStatement = dao.prepareStatement(sqlStatement);
            preparedStatement.setString(1, job.getName());
            preparedStatement.setString(2, libraryName);
            resultSet = preparedStatement.executeQuery();
            resultSet.setFetchSize(2);

            if (resultSet != null) {
                while (resultSet.next()) {
                    library = produceLibrary(resultSet, job);
                }
            }

        } finally {
            dao.closeStatement(preparedStatement);
            dao.closeResultSet(resultSet);
        }

        return library;
    }

    private IRapidFireLibraryResource produceLibrary(ResultSet resultSet, IRapidFireJobResource job) throws SQLException {

        // String job = resultSet.getString(JOB).trim();
        String library = resultSet.getString(LIBRARY).trim();

        IRapidFireLibraryResource libraryResource = createLibraryInstance(job, library);

        String shadowLibrary = resultSet.getString(SHADOW_LIBRARY).trim();

        libraryResource.setShadowLibrary(shadowLibrary);

        return libraryResource;
    }

    protected abstract IRapidFireLibraryResource createLibraryInstance(IRapidFireJobResource job, String library);

    private String getSqlStatement() throws Exception {

        // @formatter:off
        String sqlStatement = 
        "SELECT " +
            "JOB, " +
            "LIBRARY, " +
            "SHADOW_LIBRARY " +
        "FROM " +
            IJDBCConnection.LIBRARY +
            "LIBRARIES " +
        "WHERE " +
            "JOB = ?";
        // @formatter:on

        return dao.insertLibraryQualifier(sqlStatement);
    }
}
