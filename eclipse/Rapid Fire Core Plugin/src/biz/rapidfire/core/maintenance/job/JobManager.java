/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.job;

import java.sql.CallableStatement;
import java.sql.Types;
import java.util.Set;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.maintenance.AbstractManager;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.Result;
import biz.rapidfire.core.maintenance.Success;
import biz.rapidfire.core.maintenance.job.shared.DeleteShadowLibraries;
import biz.rapidfire.core.maintenance.job.shared.JobAction;
import biz.rapidfire.core.maintenance.job.shared.JobKey;
import biz.rapidfire.core.maintenance.job.shared.JobTestMode;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.dao.IJDBCConnection;
import biz.rapidfire.core.model.dao.JDBCConnectionManager;

public class JobManager extends AbstractManager<IRapidFireJobResource, JobKey, JobValues, JobAction> {

    private static final String ERROR_001 = "001"; //$NON-NLS-1$
    private static final String ERROR_002 = "002"; //$NON-NLS-1$
    private static final String ERROR_003 = "003"; //$NON-NLS-1$

    private static final String EMPTY_STRING = ""; //$NON-NLS-1$
    private IJDBCConnection dao;

    public JobManager(IJDBCConnection dao) {
        this.dao = dao;
    }

    @Override
    public void openFiles() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTJOB_openFiles\"()}")); //$NON-NLS-1$ //$NON-NLS-2$
        statement.execute();
    }

    @Override
    public Result initialize(MaintenanceMode mode, JobKey key) throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTJOB_initialize\"(?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IJobInitialize.MODE, mode.label());
        statement.setString(IJobInitialize.JOB, key.getJobName());
        statement.setString(IJobInitialize.SUCCESS, Success.NO.label());
        statement.setString(IJobInitialize.ERROR_CODE, EMPTY_STRING);

        statement.registerOutParameter(IJobInitialize.SUCCESS, Types.CHAR);
        statement.registerOutParameter(IJobInitialize.ERROR_CODE, Types.CHAR);

        statement.execute();

        String success = statement.getString(IJobInitialize.SUCCESS);
        String errorCode = statement.getString(IJobInitialize.ERROR_CODE);

        String message;
        if (Success.YES.label().equals(success)) {
            message = null;
        } else {
            message = Messages.bindParameters(Messages.Could_not_initialize_job_manager_for_job_A_in_library_B, key.getJobName(),
                dao.getLibraryName(), getErrorMessage(errorCode));
        }

        Result result = new Result(null, message, success);

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

        return Messages.bind(Messages.EntityManager_Unknown_error_code_A, errorCode);
    }

    @Override
    public JobValues getValues() throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTJOB_getValues\"(?, ?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IJobGetValues.JOB, EMPTY_STRING);
        statement.setString(IJobGetValues.DESCRIPTION, EMPTY_STRING);
        statement.setString(IJobGetValues.CREATE_ENVIRONMENT, EMPTY_STRING);
        statement.setString(IJobGetValues.JOB_QUEUE_NAME, EMPTY_STRING);
        statement.setString(IJobGetValues.JOB_QUEUE_LIBRARY_NAME, EMPTY_STRING);

        statement.registerOutParameter(IJobGetValues.JOB, Types.CHAR);
        statement.registerOutParameter(IJobGetValues.DESCRIPTION, Types.CHAR);
        statement.registerOutParameter(IJobGetValues.CREATE_ENVIRONMENT, Types.CHAR);
        statement.registerOutParameter(IJobGetValues.JOB_QUEUE_NAME, Types.CHAR);
        statement.registerOutParameter(IJobGetValues.JOB_QUEUE_LIBRARY_NAME, Types.CHAR);

        statement.execute();

        JobValues values = new JobValues();
        values.getKey().setJobName(statement.getString(IJobGetValues.JOB));
        values.setDescription(statement.getString(IJobGetValues.DESCRIPTION));
        values.setCreateEnvironment(statement.getString(IJobGetValues.CREATE_ENVIRONMENT));
        values.setJobQueueName(statement.getString(IJobGetValues.JOB_QUEUE_NAME));
        values.setJobQueueLibraryName(statement.getString(IJobGetValues.JOB_QUEUE_LIBRARY_NAME));

        return values;
    }

    @Override
    public void setValues(JobValues values) throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTJOB_setValues\"(?, ?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IJobSetValues.JOB, values.getKey().getJobName());
        statement.setString(IJobSetValues.DESCRIPTION, values.getDescription());
        statement.setString(IJobSetValues.CREATE_ENVIRONMENT, values.getCreateEnvironment());
        statement.setString(IJobSetValues.JOB_QUEUE_NAME, values.getJobQueueName());
        statement.setString(IJobSetValues.JOB_QUEUE_LIBRARY_NAME, values.getJobQueueLibraryName());

        statement.execute();
    }

    @Override
    public Result check() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTJOB_check\"(?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IJobCheck.SUCCESS, Success.NO.label());
        statement.setString(IJobCheck.FIELD_NAME, EMPTY_STRING);
        statement.setString(IJobCheck.MESSAGE, EMPTY_STRING);

        statement.registerOutParameter(IJobCheck.SUCCESS, Types.CHAR);
        statement.registerOutParameter(IJobCheck.FIELD_NAME, Types.CHAR);
        statement.registerOutParameter(IJobCheck.MESSAGE, Types.CHAR);

        statement.execute();

        String success = statement.getString(IJobCheck.SUCCESS);
        String fieldName = statement.getString(IJobCheck.FIELD_NAME);
        String message = statement.getString(IJobCheck.MESSAGE);

        return new Result(fieldName, message, success);
    }

    @Override
    public void book() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTJOB_book\"()}")); //$NON-NLS-1$ //$NON-NLS-2$
        statement.execute();
    }

    @Override
    public void closeFiles() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTJOB_closeFiles\"()}")); //$NON-NLS-1$ //$NON-NLS-2$
        statement.execute();
    }

    @Override
    public Result checkAction(JobKey key, JobAction jobAction) throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTJOB_checkAction\"(?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IJobCheckAction.ACTION, jobAction.label());
        statement.setString(IJobCheckAction.JOB, key.getJobName());
        statement.setString(IJobCheckAction.SUCCESS, Success.NO.label());
        statement.setString(IJobCheckAction.MESSAGE, EMPTY_STRING);

        statement.registerOutParameter(IJobCheckAction.SUCCESS, Types.CHAR);
        statement.registerOutParameter(IJobCheckAction.MESSAGE, Types.CHAR);

        statement.execute();

        String success = statement.getString(IJobCheckAction.SUCCESS);
        String message = statement.getString(IJobCheckAction.MESSAGE);

        return new Result(null, message, success);
    }

    public JobAction[] getValidActions(IRapidFireJobResource job) throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTJOB_getValidActions\"(?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IJobGetValidActions.JOB, job.getName());
        statement.setInt(IJobGetValidActions.NUMBER_ACTIONS, 0);
        statement.setString(IJobGetValidActions.ACTIONS, EMPTY_STRING);

        statement.registerOutParameter(IJobGetValidActions.NUMBER_ACTIONS, Types.DECIMAL);
        statement.registerOutParameter(IJobGetValidActions.ACTIONS, Types.CHAR);

        statement.execute();

        int numberActions = statement.getBigDecimal(IJobGetValidActions.NUMBER_ACTIONS).intValue();
        String[] actions = splitActions(statement.getString(IJobGetValidActions.ACTIONS), numberActions);

        JobAction[] jobActions = new JobAction[actions.length];
        for (int i = 0; i < jobActions.length; i++) {
            jobActions[i] = JobAction.find(actions[i].trim());
        }

        return jobActions;
    }

    public Result testJob(JobKey key) throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"JOB_start\"(?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IJobStart.JOB, key.getJobName());
        statement.setString(IJobStart.TEST, JobTestMode.NO.label());

        statement.execute();

        return Result.createSuccessResult();
    }

    public Result startJob(JobKey key) throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"JOB_start\"(?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IJobStart.JOB, key.getJobName());
        statement.setString(IJobStart.TEST, JobTestMode.YES.label());

        statement.execute();

        return Result.createSuccessResult();
    }

    public Result endJob(JobKey key) throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"JOB_end\"(?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IJobEnd.JOB, key.getJobName());

        statement.execute();

        return Result.createSuccessResult();
    }

    public Result resetJob(JobKey key, DeleteShadowLibraries deleteShadowLibraries) throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"JOB_reset\"(?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IJobReset.JOB, key.getJobName());
        statement.setString(IJobReset.DELETE_SHADOW_LIBRARIES, deleteShadowLibraries.label());

        statement.execute();

        return Result.createSuccessResult();
    }

    public Result resetJobAfterAbortion(JobKey key, DeleteShadowLibraries deleteShadowLibraries) throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"JOB_reset\"(?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IJobReset.JOB, key.getJobName());
        statement.setString(IJobReset.DELETE_SHADOW_LIBRARIES, deleteShadowLibraries.label());

        statement.execute();

        return Result.createSuccessResult();
    }

    public boolean isValidAction(IRapidFireJobResource job, JobAction action) throws Exception {

        KeyJobActionCache jobActionsKey = new KeyJobActionCache(job);

        Set<JobAction> actionsSet = JobActionCache.getInstance().getActions(jobActionsKey);
        if (actionsSet == null) {
            JobAction[] jobActions = getValidActions(job);
            JobActionCache.getInstance().putActions(jobActionsKey, jobActions);
            actionsSet = JobActionCache.getInstance().getActions(jobActionsKey);
        }

        return actionsSet.contains(action);
    }

    @Override
    public void recoverError() {
        JDBCConnectionManager.getInstance().close(dao);
    }
}
