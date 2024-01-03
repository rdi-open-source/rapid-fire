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

import biz.rapidfire.core.maintenance.file.shared.FileType;
import biz.rapidfire.core.model.IRapidFireFileResource;
import biz.rapidfire.core.model.IRapidFireJobResource;

public abstract class AbstractFilesDAO {

    public static final String JOB = "JOB"; //$NON-NLS-1$
    public static final String POSITION = "POSITION"; //$NON-NLS-1$
    public static final String FILE = "FILE"; //$NON-NLS-1$
    public static final String TYPE = "TYPE"; //$NON-NLS-1$
    public static final String COPY_PROGRAM_LIBRARY = "COPY_PROGRAM_LIBRARY"; //$NON-NLS-1$
    public static final String COPY_PROGRAM = "COPY_PROGRAM"; //$NON-NLS-1$
    public static final String CONVERSION_PROGRAM_LIBRARY = "CONVERSION_PROGRAM_LIBRARY"; //$NON-NLS-1$
    public static final String CONVERSION_PROGRAM = "CONVERSION_PROGRAM"; //$NON-NLS-1$

    private IJDBCConnection dao;

    public AbstractFilesDAO(IJDBCConnection dao) {

        this.dao = dao;
    }

    public List<IRapidFireFileResource> load(IRapidFireJobResource job, Shell shell) throws Exception {

        final List<IRapidFireFileResource> files = new ArrayList<IRapidFireFileResource>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        if (!dao.checkRapidFireLibrary(shell)) {
            return files;
        }

        try {

            String sqlStatement = getSqlStatement();
            preparedStatement = dao.prepareStatement(sqlStatement);
            preparedStatement.setString(1, job.getName());
            resultSet = preparedStatement.executeQuery();
            resultSet.setFetchSize(50);

            if (resultSet != null) {
                while (resultSet.next()) {
                    files.add(produceFile(resultSet, job));
                }
            }
        } finally {
            dao.closeStatement(preparedStatement);
            dao.closeResultSet(resultSet);
        }

        return files;
    }

    public IRapidFireFileResource load(IRapidFireJobResource job, int position, Shell shell) throws Exception {

        IRapidFireFileResource file = null;

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        if (!dao.checkRapidFireLibrary(shell)) {
            return file;
        }

        try {

            String sqlStatement = getSqlStatement();
            sqlStatement = sqlStatement + " AND POSITION = ?";
            preparedStatement = dao.prepareStatement(sqlStatement);
            preparedStatement.setString(1, job.getName());
            preparedStatement.setInt(2, position);
            resultSet = preparedStatement.executeQuery();
            resultSet.setFetchSize(50);

            if (resultSet != null) {
                while (resultSet.next()) {
                    file = produceFile(resultSet, job);
                }
            }
        } finally {
            dao.closeStatement(preparedStatement);
            dao.closeResultSet(resultSet);
        }

        return file;
    }

    private IRapidFireFileResource produceFile(ResultSet resultSet, IRapidFireJobResource job) throws SQLException {

        // String job = resultSet.getString(JOB).trim();
        int position = resultSet.getInt(POSITION);

        IRapidFireFileResource fileResource = createFileInstance(job, position);

        String name = resultSet.getString(FILE).trim();
        String type = resultSet.getString(TYPE).trim();
        String copyProgramLibrary = resultSet.getString(COPY_PROGRAM_LIBRARY).trim();
        String copyProgramName = resultSet.getString(COPY_PROGRAM).trim();
        String conversionProgramLibrary = resultSet.getString(CONVERSION_PROGRAM_LIBRARY).trim();
        String conversionProgramName = resultSet.getString(CONVERSION_PROGRAM).trim();

        FileType fileType = FileType.find(type);

        fileResource.setName(name);
        fileResource.setFileType(fileType);
        fileResource.setCopyProgramName(copyProgramName);
        fileResource.setCopyProgramLibrary(copyProgramLibrary);
        fileResource.setConversionProgramName(conversionProgramName);
        fileResource.setConversionProgramLibrary(conversionProgramLibrary);

        return fileResource;
    }

    protected abstract IRapidFireFileResource createFileInstance(IRapidFireJobResource job, int position);

    private String getSqlStatement() throws Exception {

        // @formatter:off
        String sqlStatement = 
        "SELECT " +
            "JOB, " +
            "POSITION, " +
            "FILE, " +
            "TYPE, " +
            "COPY_PROGRAM_LIBRARY, " +
            "COPY_PROGRAM, " +
            "CONVERSION_PROGRAM_LIBRARY, " +
            "CONVERSION_PROGRAM " +
        "FROM " +
            IJDBCConnection.LIBRARY +
            "FILES " +
        "WHERE " +
            "JOB = ?";
        // @formatter:on

        return dao.insertLibraryQualifier(sqlStatement);
    }
}
