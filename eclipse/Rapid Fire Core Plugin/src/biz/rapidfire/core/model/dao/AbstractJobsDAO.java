/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
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

import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.JobName;
import biz.rapidfire.core.model.Phase;
import biz.rapidfire.core.model.Status;

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

    public List<IRapidFireJobResource> load(final String library, Shell shell) throws Exception {

        final List<IRapidFireJobResource> journalEntries = new ArrayList<IRapidFireJobResource>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {

            if (!dao.checkRapidFireLibrary(shell, library)) {
                return journalEntries;
            }

            String sqlStatement = String.format(getSqlStatement(), library);
            preparedStatement = dao.prepareStatement(sqlStatement, null);
            resultSet = preparedStatement.executeQuery();
            resultSet.setFetchSize(50);

            if (resultSet != null) {
                while (resultSet.next()) {
                    journalEntries.add(produceJob(library, resultSet));
                }
            }
        } finally {
            dao.destroy(preparedStatement);
            dao.destroy(resultSet);
        }

        return journalEntries;
    }

    private IRapidFireJobResource produceJob(String dataLibrary, ResultSet resultSet) throws SQLException {

        String name = resultSet.getString(JOB).trim();

        IRapidFireJobResource jobResource = createJobInstance(dataLibrary, name);

        String description = resultSet.getString(DESCRIPTION).trim();
        String createEnvironment = resultSet.getString(CREATE_ENVIRONMENT).trim();
        String jobQueueLibrary = resultSet.getString(JOB_QUEUE_LIBRARY).trim();
        String jobQueueName = resultSet.getString(JOB_QUEUE).trim();
        String status = resultSet.getString(STATUS).trim();
        String phase = resultSet.getString(PHASE).trim();
        String isError = resultSet.getString(ERROR).trim();
        String errorText = resultSet.getString(ERROR_TEXT).trim();
        String batchJob = resultSet.getString(BATCH_JOB).trim();
        String batchUser = resultSet.getString(BATCH_USER).trim();
        String batchNumber = resultSet.getString(BATCH_NUMBER).trim();
        String isStopApplyChanges = resultSet.getString(STOP_APPLY_CHANGES).trim();
        String cmoneFormNumber = resultSet.getString(CMONE_FORM).trim();

        jobResource.setDescription(description);
        jobResource.setDoCreateEnvironment(dao.convertYesNo(createEnvironment));
        jobResource.setJobQueueName(jobQueueName);
        jobResource.setJobQueueLibrary(jobQueueLibrary);
        jobResource.setStatus(Status.find(status));
        jobResource.setPhase(Phase.find(phase));
        jobResource.setError(dao.convertYesNo(isError));
        jobResource.setErrorText(errorText);
        jobResource.setBatchJob(new JobName(batchJob, batchUser, batchNumber));
        jobResource.setStopApplyChanges(dao.convertYesNo(isStopApplyChanges));
        jobResource.setCmoneFormNumber(cmoneFormNumber);

        return jobResource;
    }

    protected abstract IRapidFireJobResource createJobInstance(String library, String name);

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
            "%s.JOBS";
        // @formatter:on

        return sqlStatement;
    }
}
