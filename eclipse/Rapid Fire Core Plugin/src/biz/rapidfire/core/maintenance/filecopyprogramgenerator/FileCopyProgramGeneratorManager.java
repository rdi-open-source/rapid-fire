/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.filecopyprogramgenerator;

import java.sql.CallableStatement;
import java.sql.Types;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.maintenance.AbstractManager;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.Result;
import biz.rapidfire.core.maintenance.Success;
import biz.rapidfire.core.maintenance.file.shared.FileKey;
import biz.rapidfire.core.maintenance.filecopyprogramgenerator.shared.FileCopyProgramGeneratorAction;
import biz.rapidfire.core.maintenance.job.shared.JobKey;
import biz.rapidfire.core.model.IRapidFireFileResource;
import biz.rapidfire.core.model.dao.IJDBCConnection;
import biz.rapidfire.core.model.dao.JDBCConnectionManager;

public class FileCopyProgramGeneratorManager extends
    AbstractManager<IRapidFireFileResource, FileKey, FileCopyProgramGeneratorValues, FileCopyProgramGeneratorAction> {

    private static final String ERROR_001 = "001"; //$NON-NLS-1$
    private static final String ERROR_002 = "002"; //$NON-NLS-1$
    private static final String ERROR_003 = "003"; //$NON-NLS-1$

    private static final String EMPTY_STRING = ""; //$NON-NLS-1$
    private IJDBCConnection dao;
    private JobKey jobKey;

    public FileCopyProgramGeneratorManager(IJDBCConnection dao) {
        this.dao = dao;
    }

    @Override
    public void openFiles() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"GNRCPYPGM_openFiles\"()}")); //$NON-NLS-1$ //$NON-NLS-2$
        statement.execute();
    }

    @Override
    public Result initialize(MaintenanceMode mode, FileKey key) throws Exception {

        jobKey = new JobKey(key.getJobName());

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"GNRCPYPGM_initialize\"(?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IFileCopyProgramGeneratorInitialize.JOB, key.getJobName());
        statement.setInt(IFileCopyProgramGeneratorInitialize.POSITION, key.getPosition());
        statement.setString(IFileCopyProgramGeneratorInitialize.SUCCESS, Success.NO.label());
        statement.setString(IFileCopyProgramGeneratorInitialize.ERROR_CODE, EMPTY_STRING);

        statement.registerOutParameter(IFileCopyProgramGeneratorInitialize.SUCCESS, Types.CHAR);
        statement.registerOutParameter(IFileCopyProgramGeneratorInitialize.ERROR_CODE, Types.CHAR);

        statement.execute();

        String success = getStringTrim(statement, IFileCopyProgramGeneratorInitialize.SUCCESS);
        String errorCode = getStringTrim(statement, IFileCopyProgramGeneratorInitialize.ERROR_CODE);

        String message;
        if (Success.YES.label().equals(success)) {
            message = null;
        } else {
            message = Messages.bindParameters(
                Messages.Could_not_initialize_file_copy_program_generator_manager_for_file_at_position_C_of_job_A_in_library_B, key.getJobName(),
                dao.getLibraryName(), key.getPosition(), getErrorMessage(errorCode));
        }

        Result result = new Result(success, message);

        return result;
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

        return Messages.bindParameters(Messages.EntityManager_Unknown_error_code_A, errorCode);
    }

    @Override
    public FileCopyProgramGeneratorValues getValues() throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"GNRCPYPGM_getValues\"(?, ?, ?, ?, ?, ?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IFileCopyProgramGeneratorGetValues.SOURCE_FILE, EMPTY_STRING);
        statement.setString(IFileCopyProgramGeneratorGetValues.SOURCE_FILE_LIBRARY, EMPTY_STRING);
        statement.setString(IFileCopyProgramGeneratorGetValues.SOURCE_MEMBER, EMPTY_STRING);
        statement.setString(IFileCopyProgramGeneratorGetValues.REPLACE, EMPTY_STRING);
        statement.setString(IFileCopyProgramGeneratorGetValues.AREA, EMPTY_STRING);
        statement.setString(IFileCopyProgramGeneratorGetValues.LIBRARY, EMPTY_STRING);
        statement.setString(IFileCopyProgramGeneratorGetValues.SHADOW_LIBRARY, EMPTY_STRING);
        statement.setString(IFileCopyProgramGeneratorGetValues.CONVERSION_PROGRAM, EMPTY_STRING);
        statement.setString(IFileCopyProgramGeneratorGetValues.CONVERSION_PROGRAM_LIBRARY, EMPTY_STRING);

        statement.registerOutParameter(IFileCopyProgramGeneratorGetValues.SOURCE_FILE, Types.CHAR);
        statement.registerOutParameter(IFileCopyProgramGeneratorGetValues.SOURCE_FILE_LIBRARY, Types.CHAR);
        statement.registerOutParameter(IFileCopyProgramGeneratorGetValues.SOURCE_MEMBER, Types.CHAR);
        statement.registerOutParameter(IFileCopyProgramGeneratorGetValues.REPLACE, Types.CHAR);
        statement.registerOutParameter(IFileCopyProgramGeneratorGetValues.AREA, Types.CHAR);
        statement.registerOutParameter(IFileCopyProgramGeneratorGetValues.LIBRARY, Types.CHAR);
        statement.registerOutParameter(IFileCopyProgramGeneratorGetValues.SHADOW_LIBRARY, Types.CHAR);
        statement.registerOutParameter(IFileCopyProgramGeneratorGetValues.CONVERSION_PROGRAM, Types.CHAR);
        statement.registerOutParameter(IFileCopyProgramGeneratorGetValues.CONVERSION_PROGRAM_LIBRARY, Types.CHAR);

        statement.execute();

        FileCopyProgramGeneratorValues values = new FileCopyProgramGeneratorValues();
        values.setSourceFile(getStringTrim(statement, IFileCopyProgramGeneratorGetValues.SOURCE_FILE));
        values.setSourceFileLibrary(getStringTrim(statement, IFileCopyProgramGeneratorGetValues.SOURCE_FILE_LIBRARY));
        values.setSourceMember(getStringTrim(statement, IFileCopyProgramGeneratorGetValues.SOURCE_MEMBER));
        values.setReplace(getStringTrim(statement, IFileCopyProgramGeneratorGetValues.REPLACE));
        values.setArea(getStringTrim(statement, IFileCopyProgramGeneratorGetValues.AREA));
        values.setLibrary(getStringTrim(statement, IFileCopyProgramGeneratorGetValues.LIBRARY));
        values.setShadowLibrary(getStringTrim(statement, IFileCopyProgramGeneratorGetValues.SHADOW_LIBRARY));
        values.setConversionProgram(getStringTrim(statement, IFileCopyProgramGeneratorGetValues.CONVERSION_PROGRAM));
        values.setConversionProgramLibrary(getStringTrim(statement, IFileCopyProgramGeneratorGetValues.CONVERSION_PROGRAM_LIBRARY));

        return values;
    }

    @Override
    public void setValues(FileCopyProgramGeneratorValues values) throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"GNRCPYPGM_setValues\"(?, ?, ?, ?, ?, ?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IFileCopyProgramGeneratorSetValues.SOURCE_FILE, values.getSourceFile());
        statement.setString(IFileCopyProgramGeneratorSetValues.SOURCE_FILE_LIBRARY, values.getSourceFileLibrary());
        statement.setString(IFileCopyProgramGeneratorSetValues.SOURCE_MEMBER, values.getSourceMember());
        statement.setString(IFileCopyProgramGeneratorSetValues.REPLACE, values.getReplace());
        statement.setString(IFileCopyProgramGeneratorSetValues.AREA, values.getArea());
        statement.setString(IFileCopyProgramGeneratorSetValues.LIBRARY, values.getLibrary());
        statement.setString(IFileCopyProgramGeneratorSetValues.SHADOW_LIBRARY, values.getShadowLibrary());
        statement.setString(IFileCopyProgramGeneratorSetValues.CONVERSION_PROGRAM, values.getConversionProgram());
        statement.setString(IFileCopyProgramGeneratorSetValues.CONVERSION_PROGRAM_LIBRARY, values.getConversionProgramLibrary());

        statement.execute();
    }

    @Override
    public Result check() throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"GNRCPYPGM_check\"(?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IFileCopyProgramGeneratorCheck.SUCCESS, Success.NO.label());
        statement.setString(IFileCopyProgramGeneratorCheck.FIELD_NAME, EMPTY_STRING);
        statement.setString(IFileCopyProgramGeneratorCheck.MESSAGE, EMPTY_STRING);

        statement.registerOutParameter(IFileCopyProgramGeneratorCheck.SUCCESS, Types.CHAR);
        statement.registerOutParameter(IFileCopyProgramGeneratorCheck.FIELD_NAME, Types.CHAR);
        statement.registerOutParameter(IFileCopyProgramGeneratorCheck.MESSAGE, Types.CHAR);

        statement.execute();

        String success = getStringTrim(statement, IFileCopyProgramGeneratorCheck.SUCCESS);
        String fieldName = getStringTrim(statement, IFileCopyProgramGeneratorCheck.FIELD_NAME);
        String message = getStringTrim(statement, IFileCopyProgramGeneratorCheck.MESSAGE);

        return new Result(fieldName, message, success);
    }

    @Override
    public Result book() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"GNRCPYPGM_book\"(?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IFileCopyProgramGeneratorBook.SUCCESS, Success.NO.label());
        statement.setString(IFileCopyProgramGeneratorBook.MESSAGE, EMPTY_STRING);

        statement.registerOutParameter(IFileCopyProgramGeneratorBook.SUCCESS, Types.CHAR);
        statement.registerOutParameter(IFileCopyProgramGeneratorBook.MESSAGE, Types.CHAR);

        statement.execute();

        String success = getStringTrim(statement, IFileCopyProgramGeneratorBook.SUCCESS);
        String message = getStringTrim(statement, IFileCopyProgramGeneratorBook.MESSAGE);

        return new Result(null, message, success);
    }

    @Override
    public void closeFiles() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"GNRCPYPGM_closeFiles\"()}")); //$NON-NLS-1$ //$NON-NLS-2$
        statement.execute();
    }

    @Override
    public Result checkAction(FileKey key, FileCopyProgramGeneratorAction fileAction) throws Exception {

        return Result.createSuccessResult();
    }

    @Override
    public void recoverError() {
        JDBCConnectionManager.getInstance().close(dao);
    }
}
