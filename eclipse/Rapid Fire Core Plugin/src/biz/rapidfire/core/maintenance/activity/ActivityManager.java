/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.activity;

import java.sql.CallableStatement;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.maintenance.job.shared.JobKey;
import biz.rapidfire.core.model.IRapidFireActivityResource;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.dao.IJDBCConnection;
import biz.rapidfire.core.model.maintenance.MaintenanceMode;
import biz.rapidfire.core.model.maintenance.Result;

public class ActivityManager {

    private IJDBCConnection dao;
    private JobKey jobKey;
    private ActivityValues[] currentValues;
    private ActivityValues[] newValues;

    public ActivityManager(IJDBCConnection dao) {
        this.dao = dao;
    }

    public Result initialize(MaintenanceMode mode, JobKey key) throws Exception {

        jobKey = key;

        Result result = Result.createSuccessResult();

        return result;
    }

    public ActivityValues[] getValues(IRapidFireJobResource job, Shell shell) throws Exception {

        IRapidFireActivityResource[] activities = job.getParentSubSystem().getActivities(job, shell);
        currentValues = new ActivityValues[activities.length];

        List<ActivityValues> currentActivities = new LinkedList<ActivityValues>();
        for (int i = 0; i < activities.length; i++) {
            IRapidFireActivityResource activity = activities[i];
            ActivityValues value = new ActivityValues();
            value.setKey(new ActivityKey(new JobKey(job.getName()), activity.getStartTime()));
            value.setEndTime(activity.getEndTime());
            value.setActivity(activity.isActive());
            currentActivities.add(value);
            currentValues[i] = value.clone();
        }

        return currentActivities.toArray(new ActivityValues[currentActivities.size()]);
    }

    public void setValues(ActivityValues[] values) throws Exception {
        this.newValues = values;
    }

    public void book() throws Exception {

        CallableStatement statement = dao
            .prepareCall(dao.insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTAS_setActivity\"(?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        for (int i = 0; i < newValues.length; i++) {
            ActivityValues newActivity = newValues[i];
            if (newActivity.isActive() != currentValues[i].isActive()) {

                statement.setString(IActivitySetActivity.JOB, newActivity.getJobName());
                statement.setTime(IActivitySetActivity.TIME, newActivity.getStartTime());

                statement.execute();
            }
        }
    }
}
