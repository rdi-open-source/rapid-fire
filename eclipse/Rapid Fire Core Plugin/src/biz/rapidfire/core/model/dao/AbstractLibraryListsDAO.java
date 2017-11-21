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

import biz.rapidfire.core.model.IRapidFireLibraryListResource;

public abstract class AbstractLibraryListsDAO {

    public static final String JOB = "JOB"; //$NON-NLS-1$
    public static final String LIBRARY_LIST = "LIBRARY_LIST"; //$NON-NLS-1$
    public static final String DESCRIPTION = "DESCRIPTION"; //$NON-NLS-1$

    private IJDBCConnection dao;

    public AbstractLibraryListsDAO(IJDBCConnection dao) {

        this.dao = dao;
    }

    public List<IRapidFireLibraryListResource> load(String job, Shell shell) throws Exception {

        final List<IRapidFireLibraryListResource> libraries = new ArrayList<IRapidFireLibraryListResource>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        if (!dao.checkRapidFireLibrary(shell)) {
            return libraries;
        }

        try {

            String sqlStatement = getSqlStatement();
            preparedStatement = dao.prepareStatement(sqlStatement);
            preparedStatement.setString(1, job);
            resultSet = preparedStatement.executeQuery();
            resultSet.setFetchSize(50);

            if (resultSet != null) {
                while (resultSet.next()) {
                    libraries.add(produceLibrary(dao.getLibraryName(), resultSet));
                }
            }
        } finally {
            dao.closeStatement(preparedStatement);
            dao.closeResultSet(resultSet);
        }

        return libraries;
    }

    private IRapidFireLibraryListResource produceLibrary(String dataLibrary, ResultSet resultSet) throws SQLException {

        String job = resultSet.getString(JOB).trim();
        String library = resultSet.getString(LIBRARY_LIST).trim();

        IRapidFireLibraryListResource libraryListsResource = createLibraryListInstance(dataLibrary, job, library);

        String description = resultSet.getString(DESCRIPTION).trim();

        libraryListsResource.setDescription(description);

        return libraryListsResource;
    }

    protected abstract IRapidFireLibraryListResource createLibraryListInstance(String libraryName, String job, String library);

    private String getSqlStatement() throws Exception {

        // @formatter:off
        String sqlStatement = 
        "SELECT " +
            "JOB, " +
            "LIBRARY_LIST, " +
            "DESCRIPTION " +
        "FROM " +
            IJDBCConnection.LIBRARY +
            "LIBLS " +
        "WHERE " +
            "JOB = ?";
        // @formatter:on

        return dao.insertLibraryQualifier(sqlStatement);
    }
}
