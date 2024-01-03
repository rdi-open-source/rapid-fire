/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.file;

import java.sql.CallableStatement;
import java.sql.Types;
import java.util.HashSet;
import java.util.Set;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.maintenance.AbstractManager;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.Result;
import biz.rapidfire.core.maintenance.Success;
import biz.rapidfire.core.maintenance.file.shared.FileAction;
import biz.rapidfire.core.maintenance.file.shared.FileKey;
import biz.rapidfire.core.maintenance.job.shared.JobKey;
import biz.rapidfire.core.model.IRapidFireFileResource;
import biz.rapidfire.core.model.dao.IJDBCConnection;
import biz.rapidfire.core.model.dao.JDBCConnectionManager;

public class FileManager extends AbstractManager<IRapidFireFileResource, FileKey, FileValues, FileAction> {

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
    public Result initialize(MaintenanceMode mode, FileKey key) throws Exception {

        jobKey = new JobKey(key.getJobName());

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTFILE_initialize\"(?, ?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IFileInitialize.MODE, mode.label());
        statement.setString(IFileInitialize.JOB, key.getJobName());
        statement.setInt(IFileInitialize.POSITION, key.getPosition());
        statement.setString(IFileInitialize.SUCCESS, Success.NO.label());
        statement.setString(IFileInitialize.ERROR_CODE, EMPTY_STRING);

        statement.registerOutParameter(IFileInitialize.SUCCESS, Types.CHAR);
        statement.registerOutParameter(IFileInitialize.ERROR_CODE, Types.CHAR);

        statement.execute();

        String success = getStringTrim(statement, IFileInitialize.SUCCESS);
        String errorCode = getStringTrim(statement, IFileInitialize.ERROR_CODE);

        String message;
        if (Success.YES.label().equals(success)) {
            message = null;
        } else {
            message = Messages.bindParameters(Messages.Could_not_initialize_file_manager_for_file_at_position_C_of_job_A_in_library_B,
                key.getJobName(), dao.getLibraryName(), key.getPosition(), getErrorMessage(errorCode));
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
        values.setKey(new FileKey(jobKey, getInt(statement, IFileGetValues.POSITION)));
        values.setFileName(getStringTrim(statement, IFileGetValues.FILE));
        values.setFileType(getStringTrim(statement, IFileGetValues.TYPE));
        values.setCopyProgramLibraryName(getStringTrim(statement, IFileGetValues.COPY_PROGRAM_LIBRARY_NAME));
        values.setCopyProgramName(getStringTrim(statement, IFileGetValues.COPY_PROGRAM_NAME));
        values.setConversionProgramLibraryName(getStringTrim(statement, IFileGetValues.CONVERSION_PROGRAM_LIBRARY_NAME));
        values.setConversionProgramName(getStringTrim(statement, IFileGetValues.CONVERSION_PROGRAM_NAME));

        return values;
    }

    @Override
    public void setValues(FileValues values) throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTFILE_setValues\"(?, ?, ?, ?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setInt(IFileSetValues.POSITION, values.getKey().getPosition());
        statement.setString(IFileSetValues.FILE, values.getFileName());
        statement.setString(IFileSetValues.TYPE, values.getFileType());
        statement.setString(IFileSetValues.COPY_PROGRAM_LIBRARY_NAME, values.getCopyProgramLibraryName());
        statement.setString(IFileSetValues.COPY_PROGRAM_NAME, values.getCopyProgramName());
        statement.setString(IFileSetValues.CONVERSION_PROGRAM_LIBRARY_NAME, values.getConversionProgramLibraryName());
        statement.setString(IFileSetValues.CONVERSION_PROGRAM_NAME, values.getConversionProgramName());

        statement.execute();
    }

    @Override
    public Result check() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTFILE_check\"(?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IFileCheck.SUCCESS, Success.NO.label());
        statement.setString(IFileCheck.FIELD_NAME, EMPTY_STRING);
        statement.setString(IFileCheck.MESSAGE, EMPTY_STRING);

        statement.registerOutParameter(IFileCheck.SUCCESS, Types.CHAR);
        statement.registerOutParameter(IFileCheck.FIELD_NAME, Types.CHAR);
        statement.registerOutParameter(IFileCheck.MESSAGE, Types.CHAR);

        statement.execute();

        String success = getStringTrim(statement, IFileCheck.SUCCESS);
        String fieldName = getStringTrim(statement, IFileCheck.FIELD_NAME);
        String message = getStringTrim(statement, IFileCheck.MESSAGE);

        return new Result(fieldName, message, success);
    }

    @Override
    public Result book() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTFILE_book\"()}")); //$NON-NLS-1$ //$NON-NLS-2$
        statement.execute();

        return null;
    }

    @Override
    public void closeFiles() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTFILE_closeFiles\"()}")); //$NON-NLS-1$ //$NON-NLS-2$
        statement.execute();
    }

    @Override
    public Result checkAction(FileKey key, FileAction fileAction) throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTFILE_checkAction\"(?, ?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IFileCheckAction.ACTION, fileAction.label());
        statement.setString(IFileCheckAction.JOB, key.getJobName());
        statement.setInt(IFileCheckAction.POSITION, key.getPosition());
        statement.setString(IFileCheckAction.SUCCESS, Success.NO.label());
        statement.setString(IFileCheckAction.MESSAGE, EMPTY_STRING);

        statement.registerOutParameter(IFileCheckAction.SUCCESS, Types.CHAR);
        statement.registerOutParameter(IFileCheckAction.MESSAGE, Types.CHAR);

        statement.execute();

        String success = statement.getString(IFileCheckAction.SUCCESS);
        String message = statement.getString(IFileCheckAction.MESSAGE);

        return new Result(null, message, success);
    }

    protected FileAction[] getValidActions(FileKey fileKey) throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTFILE_getValidActions\"(?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IFileGetValidActions.JOB, fileKey.getJobName());
        statement.setInt(IFileGetValidActions.POSITION, fileKey.getPosition());
        statement.setInt(IFileGetValidActions.NUMBER_ACTIONS, 0);
        statement.setString(IFileGetValidActions.ACTIONS, EMPTY_STRING);

        statement.registerOutParameter(IFileGetValidActions.NUMBER_ACTIONS, Types.DECIMAL);
        statement.registerOutParameter(IFileGetValidActions.ACTIONS, Types.CHAR);

        statement.execute();

        int numberActions = statement.getBigDecimal(IFileGetValidActions.NUMBER_ACTIONS).intValue();
        String[] actions = splitActions(statement.getString(IFileGetValidActions.ACTIONS), numberActions);

        Set<FileAction> fileActions = new HashSet<FileAction>();
        for (String action : actions) {
            fileActions.add(FileAction.find(action.trim()));
        }

        Result result = checkAction(FileKey.createNew(new JobKey(fileKey.getJobName())), FileAction.CREATE);
        if (result.isSuccessfull()) {
            fileActions.add(FileAction.CREATE);
        }

        return fileActions.toArray(new FileAction[fileActions.size()]);
    }

    @Override
    public boolean isValidAction(IRapidFireFileResource file, FileAction action) throws Exception {

        if (isActionCacheEnabled()) {
            return isValidCachedAction(file, action);
        } else {
            return isValidUncachedAction(file, action);
        }
    }

    private boolean isValidUncachedAction(IRapidFireFileResource file, FileAction action) throws Exception {

        Result result = checkAction(file.getKey(), action);

        return result.isSuccessfull();
    }

    private boolean isValidCachedAction(IRapidFireFileResource file, FileAction action) throws Exception {

        KeyFileActionCache fileActionsKey = new KeyFileActionCache(file);

        Set<FileAction> actionsSet = FileActionCache.getInstance().getActions(fileActionsKey);
        if (actionsSet == null) {
            FileAction[] fileActions = getValidActions(file.getKey());
            FileActionCache.getInstance().putActions(fileActionsKey, fileActions);
            actionsSet = FileActionCache.getInstance().getActions(fileActionsKey);
        }

        return actionsSet.contains(action);
    }

    @Override
    public void recoverError() {
        JDBCConnectionManager.getInstance().close(dao);
    }
}
