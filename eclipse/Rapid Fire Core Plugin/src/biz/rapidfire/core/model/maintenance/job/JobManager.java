/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model.maintenance.job;

import java.sql.CallableStatement;
import java.sql.Types;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.model.dao.IJDBCConnection;
import biz.rapidfire.core.model.maintenance.AbstractManager;
import biz.rapidfire.core.model.maintenance.Result;
import biz.rapidfire.core.model.maintenance.Success;

public class JobManager extends AbstractManager<JobKey, JobValues> {

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
    public Result initialize(String mode, JobKey key) throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTJOB_initialize\"(?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IJobInitialize.MODE, mode);
        statement.setString(IJobInitialize.JOB, key.getJobName());
        statement.setString(IJobInitialize.SUCCESS, Success.NO.label());

        statement.registerOutParameter(IJobInitialize.SUCCESS, Types.CHAR);

        statement.execute();

        String success = statement.getString(IJobInitialize.SUCCESS);

        String message;
        if (Success.YES.label().equals(success)) {
            message = ""; //$NON-NLS-1$
        } else {
            message = Messages.bind(Messages.Could_not_initialize_job_manager_for_job_A_in_library_B, key.getJobName(), dao.getLibraryName());
        }

        Result status = new Result(null, message, success);

        return status;
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

        statement.setString(IJobCheck.FIELD_NAME, EMPTY_STRING);
        statement.setString(IJobCheck.MESSAGE, EMPTY_STRING);
        statement.setString(IJobCheck.SUCCESS, Success.NO.label());

        statement.registerOutParameter(1, Types.CHAR);
        statement.registerOutParameter(2, Types.CHAR);
        statement.registerOutParameter(3, Types.CHAR);

        statement.execute();

        String fieldName = statement.getString(IJobCheck.FIELD_NAME);
        String message = statement.getString(IJobCheck.MESSAGE);
        String success = statement.getString(IJobCheck.SUCCESS);

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

}
