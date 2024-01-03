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
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.maintenance.activity.shared.Active;
import biz.rapidfire.core.model.IRapidFireActivityResource;
import biz.rapidfire.core.model.IRapidFireJobResource;

public abstract class AbstractActivitiesDAO {

    public static final String JOB = "JOB"; //$NON-NLS-1$
    public static final String START_TIME = "START_TIME"; //$NON-NLS-1$
    public static final String END_TIME = "END_TIME"; //$NON-NLS-1$
    public static final String ACTIVITY = "ACTIVITY"; //$NON-NLS-1$

    private IJDBCConnection dao;

    public AbstractActivitiesDAO(IJDBCConnection dao) {

        this.dao = dao;
    }

    public List<IRapidFireActivityResource> load(IRapidFireJobResource job, Shell shell) throws Exception {

        final List<IRapidFireActivityResource> activities = new ArrayList<IRapidFireActivityResource>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        if (!dao.checkRapidFireLibrary(shell)) {
            return activities;
        }

        try {

            String sqlStatement = getSqlStatement();
            preparedStatement = dao.prepareStatement(sqlStatement);
            preparedStatement.setString(1, job.getName());
            resultSet = preparedStatement.executeQuery();
            resultSet.setFetchSize(50);

            if (resultSet != null) {
                while (resultSet.next()) {
                    activities.add(produceActivity(resultSet, job));
                }
            }
        } finally {
            dao.closeStatement(preparedStatement);
            dao.closeResultSet(resultSet);
        }

        return activities;
    }

    private IRapidFireActivityResource produceActivity(ResultSet resultSet, IRapidFireJobResource job) throws SQLException {

        // String job = resultSet.getString(JOB).trim();
        Time startTime = resultSet.getTime(START_TIME);

        IRapidFireActivityResource activityResource = createActivityInstance(job, startTime);

        Time endTime = resultSet.getTime(END_TIME);
        String activity = resultSet.getString(ACTIVITY).trim();

        activityResource.setEndTime(endTime);
        if (Active.TRUE.label().equals(activity)) {
            activityResource.setActivity(true);
        } else {
            activityResource.setActivity(false);
        }

        return activityResource;
    }

    protected abstract IRapidFireActivityResource createActivityInstance(IRapidFireJobResource job, Time startTime);

    private String getSqlStatement() throws Exception {

        // @formatter:off
        String sqlStatement = 
        "SELECT " +
            "JOB, " +
            "START_TIME, " +
            "END_TIME, " +
            "ACTIVITY " +
        "FROM " +
            IJDBCConnection.LIBRARY +
            "ACTSCHD " +
        "WHERE " +
            "JOB = ?";
        // @formatter:on

        return dao.insertLibraryQualifier(sqlStatement);
    }
}
