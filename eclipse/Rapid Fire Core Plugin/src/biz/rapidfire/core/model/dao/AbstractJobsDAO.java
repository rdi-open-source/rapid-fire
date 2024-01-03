/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model.dao;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.helpers.RapidFireHelper;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.JobName;
import biz.rapidfire.core.model.Phase;
import biz.rapidfire.core.model.Status;
import biz.rapidfire.core.subsystem.IRapidFireSubSystem;

public abstract class AbstractJobsDAO {

    public static final String JOB = "JOB"; //$NON-NLS-1$
    public static final String DESCRIPTION = "DESCRIPTION"; //$NON-NLS-1$
    public static final String CREATE_ENVIRONMENT = "CREATE_ENVIRONMENT"; //$NON-NLS-1$
    public static final String JOB_QUEUE_LIBRARY = "JOB_QUEUE_LIBRARY"; //$NON-NLS-1$
    public static final String JOB_QUEUE = "JOB_QUEUE"; //$NON-NLS-1$
    public static final String CANCEL_ASP_THRESHOLD_EXCEEDS = "CANCEL_ASP_THRESHOLD_EXCEEDS"; //$NON-NLS-1$
    public static final String STATUS = "STATUS"; //$NON-NLS-1$
    public static final String PHASE = "PHASE"; //$NON-NLS-1$
    public static final String ERROR = "ERROR"; //$NON-NLS-1$
    public static final String ERROR_TEXT = "ERROR_TEXT"; //$NON-NLS-1$
    public static final String BATCH_JOB = "BATCH_JOB"; //$NON-NLS-1$
    public static final String BATCH_USER = "BATCH_USER"; //$NON-NLS-1$
    public static final String BATCH_NUMBER = "BATCH_NUMBER"; //$NON-NLS-1$
    public static final String STOP_APPLY_CHANGES = "STOP_APPLY_CHANGES"; //$NON-NLS-1$
    public static final String CMONE_FORM = "CMONE_FORM"; //$NON-NLS-1$

    private IJDBCConnection dao;
    private String rapidFireLibraryVersion = null;
    
    public AbstractJobsDAO(IJDBCConnection dao) {
        this.dao = dao;
        rapidFireLibraryVersion = RapidFireHelper.getRapidFireLibraryVersionUnformatted(dao.getSystem(), dao.getLibraryName());
    }

    public List<IRapidFireJobResource> load(IRapidFireSubSystem subSystem, Shell shell) throws Exception {

        final List<IRapidFireJobResource> jobs = new ArrayList<IRapidFireJobResource>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        if (!dao.checkRapidFireLibrary(shell)) {
            return null;
        }

        try {

            String sqlStatement = getSqlStatement();
            preparedStatement = dao.prepareStatement(sqlStatement);
            resultSet = preparedStatement.executeQuery();
            resultSet.setFetchSize(50);

            if (resultSet != null) {
                while (resultSet.next()) {
                    jobs.add(produceJob(subSystem, dao.getLibraryName(), resultSet));
                }
            }
        } finally {
            dao.closeStatement(preparedStatement);
            dao.closeResultSet(resultSet);
        }

        return jobs;
    }

    public IRapidFireJobResource load(IRapidFireSubSystem subSystem, String jobName, Shell shell) throws Exception {

        IRapidFireJobResource job = null;

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        if (!dao.checkRapidFireLibrary(shell)) {
            return null;
        }

        try {

            String sqlStatement = getSqlStatement();
            sqlStatement = sqlStatement + " WHERE JOB = ?";
            preparedStatement = dao.prepareStatement(sqlStatement);
            preparedStatement.setString(1, jobName);
            resultSet = preparedStatement.executeQuery();
            resultSet.setFetchSize(2);

            if (resultSet != null) {
                while (resultSet.next()) {
                    job = produceJob(subSystem, dao.getLibraryName(), resultSet);
                }
            }

        } finally {
            dao.closeStatement(preparedStatement);
            dao.closeResultSet(resultSet);
        }

        return job;
    }

    private IRapidFireJobResource produceJob(IRapidFireSubSystem subSystem, String dataLibrary, ResultSet resultSet) throws SQLException {

        String name = resultSet.getString(JOB).trim();

        IRapidFireJobResource jobResource = createJobInstance(subSystem, dataLibrary, name);

        String description = resultSet.getString(DESCRIPTION).trim();
        String createEnvironment = resultSet.getString(CREATE_ENVIRONMENT).trim();
        String jobQueueLibrary = resultSet.getString(JOB_QUEUE_LIBRARY).trim();
        String jobQueueName = resultSet.getString(JOB_QUEUE).trim();
        String cancelASPThresholdExceeds = resultSet.getString(CANCEL_ASP_THRESHOLD_EXCEEDS).trim();
        String status = resultSet.getString(STATUS).trim();
        String phase = resultSet.getString(PHASE).trim();
        String isError = resultSet.getString(ERROR).trim();
        String errorText = resultSet.getString(ERROR_TEXT).trim();
        String batchJob = resultSet.getString(BATCH_JOB).trim();
        String batchUser = resultSet.getString(BATCH_USER).trim();
        String batchNumber = resultSet.getString(BATCH_NUMBER).trim();
        String isStopApplyChanges = resultSet.getString(STOP_APPLY_CHANGES).trim();
        String cmoneFormNumber = resultSet.getString(CMONE_FORM).trim();

        if (rapidFireLibraryVersion != null &&
        		rapidFireLibraryVersion.compareTo("050011") >= 0) {
        	if (status.equals("*RUN") &&
        			!isJobActive(batchJob, batchUser, batchNumber)) {
        		status = "*ABORT";
        		phase = "*NONE";
        		isError = "N";
        	}
        }
        
        jobResource.setDescription(description);
        jobResource.setDoCreateEnvironment(dao.convertYesNo(createEnvironment));
        jobResource.setJobQueueName(jobQueueName);
        jobResource.setJobQueueLibrary(jobQueueLibrary);
        jobResource.setDoCancelASPThresholdExceeds(dao.convertYesNo(cancelASPThresholdExceeds));
        jobResource.setStatus(Status.find(status));
        jobResource.setPhase(Phase.find(phase));
        jobResource.setError(dao.convertYesNo(isError));
        jobResource.setErrorText(errorText);
        jobResource.setBatchJob(new JobName(batchJob, batchUser, batchNumber));
        jobResource.setStopApplyChanges(dao.convertYesNo(isStopApplyChanges));
        jobResource.setCmoneFormNumber(cmoneFormNumber);

        return jobResource;
    }

    protected abstract IRapidFireJobResource createJobInstance(IRapidFireSubSystem subSystem, String library, String name);

    private String getSqlStatement() throws Exception {

        // @formatter:off
        String sqlStatement = 
        "SELECT " +
            "JOB, " +
            "DESCRIPTION, " +
            "CREATE_ENVIRONMENT, " +
            "JOB_QUEUE_LIBRARY, " +
            "JOB_QUEUE, " +
            "CANCEL_ASP_THRESHOLD_EXCEEDS, " +
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
            IJDBCConnection.LIBRARY +
            "JOBS";
        // @formatter:on

        return dao.insertLibraryQualifier(sqlStatement);
    }
    
    private boolean isJobActive(String batchJob, String batchUser, String batchNumber) {

    	boolean active = true;
    	
		try {
	        CallableStatement statement = dao.prepareCall(dao
	                .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"JOB_isJobActiveSP\"(?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

	        statement.setString(1, batchJob);
	        statement.setString(2, batchUser);
	        statement.setString(3, batchNumber);
	        statement.setString(4, "");

	        statement.registerOutParameter(4, Types.CHAR);

	        statement.execute();

	        String _active = statement.getString(4).trim();
	        
	        if (_active.equals("N")) {
	        	active = false;
	        }
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return active;
		
    }

}
