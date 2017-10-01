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

import biz.rapidfire.base.model.dao.AbstractDAOBase;
import biz.rapidfire.core.model.IJob;
import biz.rapidfire.core.model.JobName;
import biz.rapidfire.core.model.Phase;
import biz.rapidfire.core.model.Status;

import com.ibm.as400.access.QSYSObjectPathName;

public abstract class AbstractJobsDAO extends AbstractDAOBase {

    public static final String JOB = "JOB";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String CREATE_ENVIRONMENT = "CREATE_ENVIRONMENT";
    public static final String JOB_QUEUE_LIBRARY = "JOB_QUEUE_LIBRARY";
    public static final String JOB_QUEUE = "JOB_QUEUE";
    public static final String STATUS = "STATUS";
    public static final String PHASE = "PHASE";
    public static final String ERROR = "ERROR";
    public static final String ERROR_TEXT = "ERROR_TEXT";
    public static final String BATCH_JOB = "BATCH_JOB";
    public static final String BATCH_USER = "BATCH_USER";
    public static final String BATCH_NUMBER = "BATCH_NUMBER";
    public static final String STOP_APPLY_CHANGES = "STOP_APPLY_CHANGES";
    public static final String CMONE_FORM = "CMONE_FORM";

    private String library;

    public AbstractJobsDAO(String connectionName, String library) throws Exception {
        super(connectionName);

        this.library = library;
    }

    public List<IJob> load() throws Exception {

        List<IJob> journalEntries = new ArrayList<IJob>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {

            String sqlStatement = String.format(getSqlStatement(), library);
            preparedStatement = prepareStatement(sqlStatement);
            resultSet = preparedStatement.executeQuery();
            resultSet.setFetchSize(50);

            if (resultSet != null) {
                while (resultSet.next()) {
                    journalEntries.add(produceJob(resultSet));
                }
            }

        } finally {
            super.destroy(preparedStatement);
            super.destroy(resultSet);
        }

        return journalEntries;
    }

    private IJob produceJob(ResultSet resultSet) throws SQLException {

        String name = resultSet.getString(JOB);
        String description = resultSet.getString(DESCRIPTION);
        String createEnvironment = resultSet.getString(CREATE_ENVIRONMENT);
        String jobQueueLibrary = resultSet.getString(JOB_QUEUE_LIBRARY);
        String jobQueueName = resultSet.getString(JOB_QUEUE);

        IJob job = createJobInstance(name, description, convertYesNo(createEnvironment),
            new QSYSObjectPathName(jobQueueLibrary, jobQueueName, "JOBQ"));

        String status = resultSet.getString(STATUS);
        String phase = resultSet.getString(PHASE);
        String isError = resultSet.getString(ERROR);
        String errorText = resultSet.getString(ERROR_TEXT);
        String batchJob = resultSet.getString(BATCH_JOB);
        String batchUser = resultSet.getString(BATCH_USER);
        String batchNumber = resultSet.getString(BATCH_NUMBER);
        String isStopApplyChanges = resultSet.getString(STOP_APPLY_CHANGES);
        String cmoneFormNumber = resultSet.getString(CMONE_FORM);

        job.setStatus(Status.valueOf(status));
        job.setPhase(Phase.valueOf(phase));
        job.setError(convertYesNo(isError));
        job.setErrorText(errorText);
        job.setBatchJob(new JobName(batchJob, batchUser, batchNumber));
        job.setStopApplyChanges(convertYesNo(isStopApplyChanges));
        job.setCmoneFormNumber(cmoneFormNumber);

        return job;
    }

    protected abstract IJob createJobInstance(String name, String description2, boolean convertYesNo, QSYSObjectPathName qsysObjectPathName);

    private String getSqlStatement() {

        // @formatter:off
        String sqlStatement = 
            "SELECT " +
                "JOB, " +
                "DESCRIPTION, " +
                "CREATE_ENVIRONMENT, " +
                "JOB_QUEUE_LIBRARY, " +
                "JOB_QUEUE, " +
                "STATUS, " +
                "PHASE, " +
                "ERROR, " +
                "ERROR_TEXT, " +
                "BATCH_JOB, " +
                "BATCH_USER, " +
                "BATCH_NUMBER, " +
                "STOP_APPLY_CHANGES, " +
                "CMONE_FORM " +
            "FROM " +
                "%1.JOBS";
        // @formatter:on

        return sqlStatement;
    }
}
