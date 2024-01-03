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
import java.util.HashSet;
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
import biz.rapidfire.core.maintenance.reapplychanges.IReapplyChangesGetValidActions;
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
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTJOB_getValues\"(?, ?, ?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IJobGetValues.JOB, EMPTY_STRING);
        statement.setString(IJobGetValues.DESCRIPTION, EMPTY_STRING);
        statement.setString(IJobGetValues.CREATE_ENVIRONMENT, EMPTY_STRING);
        statement.setString(IJobGetValues.JOB_QUEUE_NAME, EMPTY_STRING);
        statement.setString(IJobGetValues.JOB_QUEUE_LIBRARY_NAME, EMPTY_STRING);
        statement.setString(IJobGetValues.CANCEL_ASP_THRESHOLD_EXCEEDS, EMPTY_STRING);

        statement.registerOutParameter(IJobGetValues.JOB, Types.CHAR);
        statement.registerOutParameter(IJobGetValues.DESCRIPTION, Types.CHAR);
        statement.registerOutParameter(IJobGetValues.CREATE_ENVIRONMENT, Types.CHAR);
        statement.registerOutParameter(IJobGetValues.JOB_QUEUE_NAME, Types.CHAR);
        statement.registerOutParameter(IJobGetValues.JOB_QUEUE_LIBRARY_NAME, Types.CHAR);
        statement.registerOutParameter(IJobGetValues.CANCEL_ASP_THRESHOLD_EXCEEDS, Types.CHAR);

        statement.execute();

        JobValues values = new JobValues();
        values.getKey().setJobName(statement.getString(IJobGetValues.JOB));
        values.setDescription(statement.getString(IJobGetValues.DESCRIPTION));
        values.setCreateEnvironment(statement.getString(IJobGetValues.CREATE_ENVIRONMENT));
        values.setJobQueueName(statement.getString(IJobGetValues.JOB_QUEUE_NAME));
        values.setJobQueueLibraryName(statement.getString(IJobGetValues.JOB_QUEUE_LIBRARY_NAME));
        values.setCancelASPThresholdExceeds(statement.getString(IJobGetValues.CANCEL_ASP_THRESHOLD_EXCEEDS));

        return values;
    }

    @Override
    public void setValues(JobValues values) throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTJOB_setValues\"(?, ?, ?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IJobSetValues.JOB, values.getKey().getJobName());
        statement.setString(IJobSetValues.DESCRIPTION, values.getDescription());
        statement.setString(IJobSetValues.CREATE_ENVIRONMENT, values.getCreateEnvironment());
        statement.setString(IJobSetValues.JOB_QUEUE_NAME, values.getJobQueueName());
        statement.setString(IJobSetValues.JOB_QUEUE_LIBRARY_NAME, values.getJobQueueLibraryName());
        statement.setString(IJobSetValues.CANCEL_ASP_THRESHOLD_EXCEEDS, values.getCancelASPThresholdExceeds());

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
    public Result book() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTJOB_book\"()}")); //$NON-NLS-1$ //$NON-NLS-2$
        statement.execute();

        return null;
    }

    @Override
    public void closeFiles() throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTJOB_closeFiles\"()}")); //$NON-NLS-1$ //$NON-NLS-2$
        statement.execute();
    }

    @Override
    public Result checkAction(JobKey key, JobAction jobAction) throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY //$NON-NLS-1$
            + "\"MNTJOB_checkAction\"(?, ?, ?, ?)}")); //$NON-NLS-1$

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

    protected JobAction[] getValidActions(JobKey jobKey) throws Exception {

        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY //$NON-NLS-1$
            + "\"MNTJOB_getValidActions\"(?, ?, ?)}")); //$NON-NLS-1$

        statement.setString(IJobGetValidActions.JOB, jobKey.getJobName());
        statement.setInt(IJobGetValidActions.NUMBER_ACTIONS, 0);
        statement.setString(IJobGetValidActions.ACTIONS, EMPTY_STRING);

        statement.registerOutParameter(IJobGetValidActions.NUMBER_ACTIONS, Types.DECIMAL);
        statement.registerOutParameter(IJobGetValidActions.ACTIONS, Types.CHAR);

        statement.execute();

        int numberActions = statement.getBigDecimal(IJobGetValidActions.NUMBER_ACTIONS).intValue();
        String[] actions = splitActions(statement.getString(IJobGetValidActions.ACTIONS), numberActions);

        Set<JobAction> jobActions = new HashSet<JobAction>();
        for (String action : actions) {
            jobActions.add(JobAction.find(action.trim()));
        }

        Result result = checkAction(JobKey.createNew(), JobAction.CREATE);
        if (result.isSuccessfull()) {
            jobActions.add(JobAction.CREATE);
        }
        jobActions.add(JobAction.RFRJOBSTS);

        return jobActions.toArray(new JobAction[jobActions.size()]);
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

    public int buildFieldsWithGeneratedClause(JobKey key) throws Exception {
 
        CallableStatement statement = dao.prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"FLDGENCLS_build\"(?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IFieldsWithGeneratedClause.JOB, key.getJobName());
        statement.setInt(IFieldsWithGeneratedClause.RESULT, 0);

        statement.registerOutParameter(IFieldsWithGeneratedClause.RESULT, Types.INTEGER);
        
        statement.execute();

        int result = statement.getInt(IFieldsWithGeneratedClause.RESULT);
        
        return result;
    }
    
    @Override
    public boolean isValidAction(IRapidFireJobResource job, JobAction action) throws Exception {

        if (isActionCacheEnabled()) {
            return isValidCachedAction(job, action);
        } else {
            return isValidUncachedAction(job, action);
        }
    }

    private boolean isValidUncachedAction(IRapidFireJobResource job, JobAction action) throws Exception {

        Result result = checkAction(job.getKey(), action);

        return result.isSuccessfull();
    }

    private boolean isValidCachedAction(IRapidFireJobResource job, JobAction action) throws Exception {

        KeyJobActionCache jobActionsKey = new KeyJobActionCache(job);

        Set<JobAction> actionsSet = JobActionCache.getInstance().getActions(jobActionsKey);
        if (actionsSet == null) {
            JobAction[] jobActions = getValidActions(job.getKey());
            JobActionCache.getInstance().putActions(jobActionsKey, jobActions);
            actionsSet = JobActionCache.getInstance().getActions(jobActionsKey);
        }

        return actionsSet.contains(action);
    }

    @Override
    public void recoverError() {
        JDBCConnectionManager.getInstance().close(dao);
    }

	public IJDBCConnection getDao() {
		return dao;
	}

}
