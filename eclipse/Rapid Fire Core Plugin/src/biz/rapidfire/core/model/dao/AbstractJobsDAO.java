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

import biz.rapidfire.core.model.IRapidFireInstanceResource;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.JobName;
import biz.rapidfire.core.model.Phase;
import biz.rapidfire.core.model.Status;

import com.ibm.as400.access.QSYSObjectPathName;

public abstract class AbstractJobsDAO {

    public static final String JOB = "JOB"; //$NON-NLS-1$
    public static final String DESCRIPTION = "DESCRIPTION"; //$NON-NLS-1$
    public static final String CREATE_ENVIRONMENT = "CREATE_ENVIRONMENT"; //$NON-NLS-1$
    public static final String JOB_QUEUE_LIBRARY = "JOB_QUEUE_LIBRARY"; //$NON-NLS-1$
    public static final String JOB_QUEUE = "JOB_QUEUE"; //$NON-NLS-1$
    public static final String STATUS = "STATUS"; //$NON-NLS-1$
    public static final String PHASE = "PHASE"; //$NON-NLS-1$
    public static final String ERROR = "ERROR"; //$NON-NLS-1$
    public static final String ERROR_TEXT = "ERROR_TEXT"; //$NON-NLS-1$
    public static final String BATCH_JOB = "BATCH_JOB"; //$NON-NLS-1$
    public static final String BATCH_USER = "BATCH_USER"; //$NON-NLS-1$
    public static final String BATCH_NUMBER = "BATCH_NUMBER"; //$NON-NLS-1$
    public static final String STOP_APPLY_CHANGES = "STOP_APPLY_CHANGES"; //$NON-NLS-1$
    public static final String CMONE_FORM = "CMONE_FORM"; //$NON-NLS-1$

    private IBaseDAO dao;

    public AbstractJobsDAO(IBaseDAO dao) {

        this.dao = dao;
    }

    public List<IRapidFireJobResource> load(IRapidFireInstanceResource parent) throws Exception {

        List<IRapidFireJobResource> journalEntries = new ArrayList<IRapidFireJobResource>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {

            String sqlStatement = String.format(getSqlStatement(), parent.getLibrary());
            preparedStatement = dao.prepareStatement(sqlStatement);
            resultSet = preparedStatement.executeQuery();
            resultSet.setFetchSize(50);

            if (resultSet != null) {
                while (resultSet.next()) {
                    journalEntries.add(produceJob(parent, resultSet));
                }
            }

        } finally {
            dao.destroy(preparedStatement);
            dao.destroy(resultSet);
        }

        return journalEntries;
    }

    private IRapidFireJobResource produceJob(IRapidFireInstanceResource parent, ResultSet resultSet) throws SQLException {

        String name = resultSet.getString(JOB).trim();
        String description = resultSet.getString(DESCRIPTION).trim();
        String createEnvironment = resultSet.getString(CREATE_ENVIRONMENT).trim();
        String jobQueueLibrary = resultSet.getString(JOB_QUEUE_LIBRARY).trim();
        String jobQueueName = resultSet.getString(JOB_QUEUE).trim();

        IRapidFireJobResource job = createJobInstance(parent, name, description, dao.convertYesNo(createEnvironment), new QSYSObjectPathName(
            jobQueueLibrary, jobQueueName, "JOBQ")); //$NON-NLS-1$

        String status = resultSet.getString(STATUS).trim();
        String phase = resultSet.getString(PHASE).trim();
        String isError = resultSet.getString(ERROR).trim();
        String errorText = resultSet.getString(ERROR_TEXT).trim();
        String batchJob = resultSet.getString(BATCH_JOB).trim();
        String batchUser = resultSet.getString(BATCH_USER).trim();
        String batchNumber = resultSet.getString(BATCH_NUMBER).trim();
        String isStopApplyChanges = resultSet.getString(STOP_APPLY_CHANGES).trim();
        String cmoneFormNumber = resultSet.getString(CMONE_FORM).trim();

        job.setStatus(Status.find(status));
        job.setPhase(Phase.find(phase));
        job.setError(dao.convertYesNo(isError));
        job.setErrorText(errorText);
        job.setBatchJob(new JobName(batchJob, batchUser, batchNumber));
        job.setStopApplyChanges(dao.convertYesNo(isStopApplyChanges));
        job.setCmoneFormNumber(cmoneFormNumber);

        return job;
    }

    protected abstract IRapidFireJobResource createJobInstance(IRapidFireInstanceResource parent, String name, String description,
        boolean doCreateEnvironment, QSYSObjectPathName jobQueue);

    private String getSqlStatement() {

        // @formatter:off
        String sqlStatement = 
            Messages.AbstractJobsDAO_14 +
                Messages.AbstractJobsDAO_15 +
                Messages.AbstractJobsDAO_16 +
                Messages.AbstractJobsDAO_17 +
                Messages.AbstractJobsDAO_18 +
                Messages.AbstractJobsDAO_19 +
                Messages.AbstractJobsDAO_20 +
                Messages.AbstractJobsDAO_21 +
                Messages.AbstractJobsDAO_22 +
                Messages.AbstractJobsDAO_23 +
                Messages.AbstractJobsDAO_24 +
                Messages.AbstractJobsDAO_25 +
                Messages.AbstractJobsDAO_26 +
                Messages.AbstractJobsDAO_27 +
                Messages.AbstractJobsDAO_28 +
            Messages.AbstractJobsDAO_29 +
                Messages.AbstractJobsDAO_30;
        // @formatter:on

        return sqlStatement;
    }
}
