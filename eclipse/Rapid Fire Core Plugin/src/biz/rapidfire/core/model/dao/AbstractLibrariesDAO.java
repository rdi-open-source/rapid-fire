/*******************************************************************************
 * Copyright (c) 2012-2017 iSphere Project Owners
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

import biz.rapidfire.core.model.IRapidFireLibraryResource;

public abstract class AbstractLibrariesDAO {

    public static final String JOB = "JOB"; //$NON-NLS-1$
    public static final String LIBRARY = "LIBRARY"; //$NON-NLS-1$
    public static final String SHADOW_LIBRARY = "SHADOW_LIBRARY"; //$NON-NLS-1$

    private IBaseDAO dao;

    public AbstractLibrariesDAO(IBaseDAO dao) {

        this.dao = dao;
    }

    public List<IRapidFireLibraryResource> load(final String dataLibrary, String job) throws Exception {

        final List<IRapidFireLibraryResource> journalEntries = new ArrayList<IRapidFireLibraryResource>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {

            String sqlStatement = String.format(getSqlStatement(), dataLibrary);
            preparedStatement = dao.prepareStatement(sqlStatement);
            preparedStatement.setString(1, job);
            resultSet = preparedStatement.executeQuery();
            resultSet.setFetchSize(50);

            if (resultSet != null) {
                while (resultSet.next()) {
                    journalEntries.add(produceLibrary(dataLibrary, resultSet));
                }
            }
        } finally {
            dao.destroy(preparedStatement);
            dao.destroy(resultSet);
        }

        return journalEntries;
    }

    private IRapidFireLibraryResource produceLibrary(String dataLibrary, ResultSet resultSet) throws SQLException {

        String job = resultSet.getString(JOB).trim();
        String library = resultSet.getString(LIBRARY).trim();

        IRapidFireLibraryResource libraryResource = createLibraryInstance(dataLibrary, job, library); //$NON-NLS-1$

        String shadowLibrary = resultSet.getString(SHADOW_LIBRARY).trim();

        libraryResource.setShadowLibrary(shadowLibrary);

        return libraryResource;
    }

    protected abstract IRapidFireLibraryResource createLibraryInstance(String dataLibrary, String job, String library);

    private String getSqlStatement() {

        // @formatter:off
        String sqlStatement = 
        "SELECT " +
            "JOB, " +
            "LIBRARY, " +
            "SHADOW_LIBRARY " +
        "FROM " +
            "%s.LIBRARIES " +
        "WHERE " +
            "JOB = ?";
        // @formatter:on

        return sqlStatement;
    }
}
