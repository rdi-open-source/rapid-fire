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
import java.util.HashSet;
import java.util.Set;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.maintenance.AbstractManager;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.Result;
import biz.rapidfire.core.maintenance.Success;
import biz.rapidfire.core.maintenance.file.FileValues;
import biz.rapidfire.core.maintenance.file.shared.FileKey;
import biz.rapidfire.core.maintenance.filecopyprogramgenerator.shared.FileCopyProgramGeneratorAction;
import biz.rapidfire.core.maintenance.job.shared.JobKey;
import biz.rapidfire.core.model.IRapidFireFileResource;
import biz.rapidfire.core.model.dao.IJDBCConnection;
import biz.rapidfire.core.model.dao.JDBCConnectionManager;

public class FileCopyProgramGeneratorManager extends AbstractManager<IRapidFireFileResource, FileKey, FileValues, FileCopyProgramGeneratorAction> {

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

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"CHKSTSE_openFiles\"()}")); //$NON-NLS-1$ //$NON-NLS-2$
        statement.execute();
    }

    @Override
    public Result initialize(MaintenanceMode mode, FileKey key) throws Exception {
        throw new IllegalAccessError("Calling initialize() is not allowed. Method has not been implemented.");
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
        throw new IllegalAccessError("Calling getValues() is not allowed. Method has not been implemented.");
    }

    @Override
    public void setValues(FileValues values) throws Exception {
        throw new IllegalAccessError("Calling setValues() is not allowed. Method has not been implemented.");
    }

    @Override
    public Result check() throws Exception {
        throw new IllegalAccessError("Calling check() is not allowed. Method has not been implemented.");
    }

    @Override
    public void book() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"CHKSTSE_book\"()}")); //$NON-NLS-1$ //$NON-NLS-2$
        statement.execute();
    }

    @Override
    public void closeFiles() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"CHKSTSE_closeFiles\"()}")); //$NON-NLS-1$ //$NON-NLS-2$
        statement.execute();
    }

    @Override
    public Result checkAction(FileKey fileKey, FileCopyProgramGeneratorAction action) throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"CHKSTSE_checkAction\"(?, ?, ?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IFileCopyProgramGeneratorCheckAction.ACTION, action.label());
        statement.setString(IFileCopyProgramGeneratorCheckAction.JOB, fileKey.getJobName());
        statement.setInt(IFileCopyProgramGeneratorCheckAction.POSITION, fileKey.getPosition());
        statement.setString(IFileCopyProgramGeneratorCheckAction.AREA, Success.NO.label());
        statement.setString(IFileCopyProgramGeneratorCheckAction.SUCCESS, Success.NO.label());
        statement.setString(IFileCopyProgramGeneratorCheckAction.MESSAGE, EMPTY_STRING);

        statement.registerOutParameter(IFileCopyProgramGeneratorCheckAction.SUCCESS, Types.CHAR);
        statement.registerOutParameter(IFileCopyProgramGeneratorCheckAction.MESSAGE, Types.CHAR);

        statement.execute();

        String success = statement.getString(IFileCopyProgramGeneratorCheckAction.SUCCESS);
        String message = statement.getString(IFileCopyProgramGeneratorCheckAction.MESSAGE);

        return new Result(null, message, success);
    }

    protected FileCopyProgramGeneratorAction[] getValidActions(FileKey fileKey) throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"CHKSTSE_getValidActions\"(?, ?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IFileCopyProgramGeneratorGetValidActions.JOB, fileKey.getJobName());
        statement.setInt(IFileCopyProgramGeneratorGetValidActions.POSITION, fileKey.getPosition());
        statement.setString(IFileCopyProgramGeneratorGetValidActions.AREA, fileKey.getJobName());
        statement.setInt(IFileCopyProgramGeneratorGetValidActions.NUMBER_ACTIONS, 0);
        statement.setString(IFileCopyProgramGeneratorGetValidActions.ACTIONS, EMPTY_STRING);

        statement.registerOutParameter(IFileCopyProgramGeneratorGetValidActions.NUMBER_ACTIONS, Types.DECIMAL);
        statement.registerOutParameter(IFileCopyProgramGeneratorGetValidActions.ACTIONS, Types.CHAR);

        statement.execute();

        int numberActions = statement.getBigDecimal(IFileCopyProgramGeneratorGetValidActions.NUMBER_ACTIONS).intValue();
        String[] actions = splitActions(statement.getString(IFileCopyProgramGeneratorGetValidActions.ACTIONS), numberActions);

        Set<FileCopyProgramGeneratorAction> fileCopyProgramGeneratorActions = new HashSet<FileCopyProgramGeneratorAction>();
        for (String action : actions) {
            fileCopyProgramGeneratorActions.add(FileCopyProgramGeneratorAction.find(action.trim()));
        }

        Result result = checkAction(FileKey.createNew(new JobKey(fileKey.getJobName())), FileCopyProgramGeneratorAction.CREATE);
        if (result.isSuccessfull()) {
            fileCopyProgramGeneratorActions.add(FileCopyProgramGeneratorAction.CREATE);
        }

        return fileCopyProgramGeneratorActions.toArray(new FileCopyProgramGeneratorAction[fileCopyProgramGeneratorActions.size()]);
    }

    @Override
    public boolean isValidAction(IRapidFireFileResource file, FileCopyProgramGeneratorAction action) throws Exception {

        KeyFileCopyProgramGeneratorActionCache fileCopyProgramGeneratorActionsKey = new KeyFileCopyProgramGeneratorActionCache(file);

        Set<FileCopyProgramGeneratorAction> actionsSet = FileCopyProgramGeneratorActionCache.getInstance().getActions(
            fileCopyProgramGeneratorActionsKey);
        if (actionsSet == null) {
            FileCopyProgramGeneratorAction[] fileCopyProgramGeneratorActions = getValidActions(file.getKey());
            FileCopyProgramGeneratorActionCache.getInstance().putActions(fileCopyProgramGeneratorActionsKey, fileCopyProgramGeneratorActions);
            actionsSet = FileCopyProgramGeneratorActionCache.getInstance().getActions(fileCopyProgramGeneratorActionsKey);
        }

        return actionsSet.contains(action);
    }

    @Override
    public void recoverError() {
        JDBCConnectionManager.getInstance().close(dao);
    }
}
