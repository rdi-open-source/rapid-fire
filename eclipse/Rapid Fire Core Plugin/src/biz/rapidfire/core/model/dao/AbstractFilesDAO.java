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

import biz.rapidfire.core.model.FileType;
import biz.rapidfire.core.model.IRapidFireFileResource;

public abstract class AbstractFilesDAO {

    public static final String JOB = "JOB"; //$NON-NLS-1$
    public static final String POSITION = "POSITION"; //$NON-NLS-1$
    public static final String FILE = "FILE"; //$NON-NLS-1$
    public static final String TYPE = "TYPE"; //$NON-NLS-1$
    public static final String COPY_PROGRAM_LIBRARY = "COPY_PROGRAM_LIBRARY"; //$NON-NLS-1$
    public static final String COPY_PROGRAM = "COPY_PROGRAM"; //$NON-NLS-1$
    public static final String CONVERSION_PROGRAM_LIBRARY = "CONVERSION_PROGRAM_LIBRARY"; //$NON-NLS-1$
    public static final String CONVERSION_PROGRAM = "CONVERSION_PROGRAM"; //$NON-NLS-1$

    private IBaseDAO dao;

    public AbstractFilesDAO(IBaseDAO dao) {

        this.dao = dao;
    }

    public List<IRapidFireFileResource> load(final String library, String job) throws Exception {

        final List<IRapidFireFileResource> journalEntries = new ArrayList<IRapidFireFileResource>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {

            String sqlStatement = String.format(getSqlStatement(), library);
            preparedStatement = dao.prepareStatement(sqlStatement);
            preparedStatement.setString(1, job);
            resultSet = preparedStatement.executeQuery();
            resultSet.setFetchSize(50);

            if (resultSet != null) {
                while (resultSet.next()) {
                    journalEntries.add(produceFile(library, resultSet));
                }
            }
        } finally {
            dao.destroy(preparedStatement);
            dao.destroy(resultSet);
        }

        return journalEntries;
    }

    private IRapidFireFileResource produceFile(String dataLibrary, ResultSet resultSet) throws SQLException {

        String job = resultSet.getString(JOB).trim();
        int position = resultSet.getInt(POSITION);

        IRapidFireFileResource fileResource = createFileInstance(dataLibrary, job, position); //$NON-NLS-1$

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

    protected abstract IRapidFireFileResource createFileInstance(String dataLibrary, String job, int position);

    private String getSqlStatement() {

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
            "%s.FILES " +
        "WHERE " +
            "JOB = ?";
        // @formatter:on

        return sqlStatement;
    }
}
