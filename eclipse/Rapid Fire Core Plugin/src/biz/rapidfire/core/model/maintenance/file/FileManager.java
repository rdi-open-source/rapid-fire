/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model.maintenance.file;

import java.sql.CallableStatement;
import java.sql.Types;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.model.dao.IJDBCConnection;
import biz.rapidfire.core.model.maintenance.AbstractManager;
import biz.rapidfire.core.model.maintenance.Result;
import biz.rapidfire.core.model.maintenance.Success;
import biz.rapidfire.core.model.maintenance.job.JobKey;

public class FileManager extends AbstractManager<FileKey, FileValues> {

    private static final String ERROR_001 = "001"; //$NON-NLS-1$
    private static final String ERROR_002 = "002"; //$NON-NLS-1$
    private static final String ERROR_003 = "003"; //$NON-NLS-1$

    private static final String EMPTY_STRING = ""; //$NON-NLS-1$
    private IJDBCConnection dao;
    private JobKey jobKey;

    public FileManager(IJDBCConnection dao) {
        this.dao = dao;
    }

    @Override
    public void openFiles() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTFILE_openFiles\"()}")); //$NON-NLS-1$ //$NON-NLS-2$
        statement.execute();
    }

    @Override
    public Result initialize(String mode, FileKey key) throws Exception {

        jobKey = new JobKey(key.getJobName());

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTFILE_initialize\"(?, ?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IFileInitialize.MODE, mode);
        statement.setString(IFileInitialize.JOB, key.getJobName());
        statement.setInt(IFileInitialize.POSITION, key.getPosition());
        statement.setString(IFileInitialize.SUCCESS, Success.NO.label());
        statement.setString(IFileInitialize.ERROR_CODE, EMPTY_STRING);

        statement.registerOutParameter(IFileInitialize.SUCCESS, Types.CHAR);
        statement.registerOutParameter(IFileInitialize.ERROR_CODE, Types.CHAR);

        statement.execute();

        String success = statement.getString(IFileInitialize.SUCCESS);
        String errorCode = statement.getString(IFileInitialize.ERROR_CODE);

        String message;
        if (Success.YES.label().equals(success)) {
            message = ""; //$NON-NLS-1$
        } else {
            message = Messages.bind(Messages.Could_not_initialize_file_manager_for_file_at_position_C_of_job_A_in_library_B,
                new Object[] { key.getJobName(), dao.getLibraryName(), key.getPosition(), getErrorMessage(errorCode) });
        }

        Result status = new Result(success, message);

        return status;
    }

    /**
     * Translates the API error code to message text.
     * 
     * @param errorCode - Error code that was returned by the API.
     * @return message text
     */
    private String getErrorMessage(String errorCode) {

        // TODO: use reflection
        if (ERROR_001.equals(errorCode)) {
            return Messages.FileManager_001;
        } else if (ERROR_002.equals(errorCode)) {
            return Messages.FileManager_002;
        } else if (ERROR_003.equals(errorCode)) {
            return Messages.FileManager_003;
        }

        return Messages.bind(Messages.EntityManager_Unknown_error_code_A, errorCode);
    }

    @Override
    public FileValues getValues() throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTFILE_getValues\"(?, ?, ?, ?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setInt(IFileGetValues.POSITION, 0);
        statement.setString(IFileGetValues.FILE, EMPTY_STRING);
        statement.setString(IFileGetValues.TYPE, EMPTY_STRING);
        statement.setString(IFileGetValues.COPY_PROGRAM_LIBRARY_NAME, EMPTY_STRING);
        statement.setString(IFileGetValues.COPY_PROGRAM_NAME, EMPTY_STRING);
        statement.setString(IFileGetValues.CONVERSION_PROGRAM_LIBRARY_NAME, EMPTY_STRING);
        statement.setString(IFileGetValues.CONVERSION_PROGRAM_NAME, EMPTY_STRING);

        statement.registerOutParameter(IFileGetValues.POSITION, Types.INTEGER);
        statement.registerOutParameter(IFileGetValues.FILE, Types.CHAR);
        statement.registerOutParameter(IFileGetValues.TYPE, Types.CHAR);
        statement.registerOutParameter(IFileGetValues.COPY_PROGRAM_LIBRARY_NAME, Types.CHAR);
        statement.registerOutParameter(IFileGetValues.COPY_PROGRAM_NAME, Types.CHAR);
        statement.registerOutParameter(IFileGetValues.CONVERSION_PROGRAM_LIBRARY_NAME, Types.CHAR);
        statement.registerOutParameter(IFileGetValues.CONVERSION_PROGRAM_NAME, Types.CHAR);

        statement.execute();

        FileValues values = new FileValues();
        values.setKey(new FileKey(jobKey, statement.getInt(IFileGetValues.POSITION)));
        values.setFileName(statement.getString(IFileGetValues.FILE));
        values.setType(statement.getString(IFileGetValues.TYPE));
        values.setCopyProgramLibraryName(statement.getString(IFileGetValues.COPY_PROGRAM_LIBRARY_NAME));
        values.setCopyProgramName(statement.getString(IFileGetValues.COPY_PROGRAM_NAME));
        values.setConversionProgramLibraryName(statement.getString(IFileGetValues.CONVERSION_PROGRAM_LIBRARY_NAME));
        values.setConversionProgramName(statement.getString(IFileGetValues.CONVERSION_PROGRAM_NAME));

        return values;
    }

    @Override
    public void setValues(FileValues values) throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTFILE_setValues\"(?, ?, ?, ?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setInt(IFileSetValues.POSITION, values.getKey().getPosition());
        statement.setString(IFileSetValues.FILE, values.getFileName());
        statement.setString(IFileSetValues.TYPE, values.getType());
        statement.setString(IFileSetValues.COPY_PROGRAM_LIBRARY_NAME, values.getCopyProgramLibraryName());
        statement.setString(IFileSetValues.COPY_PROGRAM_NAME, values.getCopyProgramName());
        statement.setString(IFileSetValues.CONVERSION_PROGRAM_LIBRARY_NAME, values.getConversionProgramLibraryName());
        statement.setString(IFileSetValues.CONVERSION_PROGRAM_NAME, values.getConversionProgramName());

        statement.execute();
    }

    @Override
    public Result check() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTFILE_check\"(?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IFileCheck.FIELD_NAME, EMPTY_STRING);
        statement.setString(IFileCheck.MESSAGE, EMPTY_STRING);
        statement.setString(IFileCheck.SUCCESS, Success.NO.label());

        statement.registerOutParameter(1, Types.CHAR);
        statement.registerOutParameter(2, Types.CHAR);
        statement.registerOutParameter(3, Types.CHAR);

        statement.execute();

        String fieldName = statement.getString(IFileCheck.FIELD_NAME);
        String message = statement.getString(IFileCheck.MESSAGE);
        String success = statement.getString(IFileCheck.SUCCESS);

        return new Result(fieldName, message, success);
    }

    @Override
    public void book() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTFILE_book\"()}")); //$NON-NLS-1$ //$NON-NLS-2$
        statement.execute();
    }

    @Override
    public void closeFiles() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTFILE_closeFiles\"()}")); //$NON-NLS-1$ //$NON-NLS-2$
        statement.execute();
    }

}
