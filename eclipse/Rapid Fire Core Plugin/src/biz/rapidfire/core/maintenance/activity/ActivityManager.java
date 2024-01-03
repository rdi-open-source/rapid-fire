/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.activity;

import java.sql.CallableStatement;
import java.sql.Types;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.maintenance.AbstractManager;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.Result;
import biz.rapidfire.core.maintenance.Success;
import biz.rapidfire.core.maintenance.activity.shared.ActivityAction;
import biz.rapidfire.core.maintenance.activity.shared.ActivityKey;
import biz.rapidfire.core.maintenance.job.shared.JobKey;
import biz.rapidfire.core.model.IRapidFireActivityResource;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.dao.IJDBCConnection;
import biz.rapidfire.core.model.dao.JDBCConnectionManager;

public class ActivityManager extends AbstractManager<IRapidFireActivityResource, ActivityKey, ActivityValues, ActivityAction> {

    private static final String EMPTY_STRING = ""; //$NON-NLS-1$

    private IJDBCConnection dao;
    private ActivityValues[] currentValues;
    private ActivityValues[] newValues;

    public ActivityManager(IJDBCConnection dao) {
        this.dao = dao;
    }

    public ActivityValues[] getValues(IRapidFireJobResource job, Shell shell) throws Exception {

        IRapidFireActivityResource[] activities = job.getParentSubSystem().getActivities(job, shell);
        currentValues = new ActivityValues[activities.length];

        List<ActivityValues> currentActivities = new LinkedList<ActivityValues>();
        for (int i = 0; i < activities.length; i++) {
            IRapidFireActivityResource currentActivity = activities[i];
            ActivityValues value = new ActivityValues();
            value.setKey(new ActivityKey(new JobKey(currentActivity.getJob()), currentActivity.getStartTime()));
            value.setEndTime(currentActivity.getEndTime());
            value.setActivity(currentActivity.isActive());
            currentActivities.add(value);
            currentValues[i] = value.clone();
        }

        return currentActivities.toArray(new ActivityValues[currentActivities.size()]);
    }

    public void setValues(ActivityValues[] values) throws Exception {
        this.newValues = values;
    }

    @Override
    public Result book() throws Exception {

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

        return null;
    }

    @Override
    public Result checkAction(ActivityKey key, ActivityAction activityAction) throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTAS_checkAction\"(?, ?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IActivityCheckAction.ACTION, activityAction.label());
        statement.setString(IActivityCheckAction.JOB, key.getJobName());
        statement.setTime(IActivityCheckAction.START_TIME, key.getStartTime());
        statement.setString(IActivityCheckAction.SUCCESS, Success.NO.label());
        statement.setString(IActivityCheckAction.MESSAGE, EMPTY_STRING);

        statement.registerOutParameter(IActivityCheckAction.SUCCESS, Types.CHAR);
        statement.registerOutParameter(IActivityCheckAction.MESSAGE, Types.CHAR);

        statement.execute();

        String success = statement.getString(IActivityCheckAction.SUCCESS);
        String message = statement.getString(IActivityCheckAction.MESSAGE);

        return new Result(null, message, success);
    }

    protected ActivityAction[] getValidActions(ActivityKey activityKey) throws Exception {

        CallableStatement statement = dao.prepareCall(dao
            .insertLibraryQualifier("{CALL " + IJDBCConnection.LIBRARY + "\"MNTAS_getValidActions\"(?, ?, ?, ?)}")); //$NON-NLS-1$ //$NON-NLS-2$

        statement.setString(IActivityGetValidActions.JOB, activityKey.getJobName());
        statement.setTime(IActivityGetValidActions.START_TIME, activityKey.getStartTime());
        statement.setInt(IActivityGetValidActions.NUMBER_ACTIONS, 0);
        statement.setString(IActivityGetValidActions.ACTIONS, EMPTY_STRING);

        statement.registerOutParameter(IActivityGetValidActions.NUMBER_ACTIONS, Types.DECIMAL);
        statement.registerOutParameter(IActivityGetValidActions.ACTIONS, Types.CHAR);

        statement.execute();

        int numberActions = statement.getBigDecimal(IActivityGetValidActions.NUMBER_ACTIONS).intValue();
        String[] actions = splitActions(statement.getString(IActivityGetValidActions.ACTIONS), numberActions);

        Set<ActivityAction> activityActions = new HashSet<ActivityAction>();
        for (String action : actions) {
            activityActions.add(ActivityAction.find(action.trim()));
        }

        Result result = checkAction(ActivityKey.createNew(new JobKey(activityKey.getJobName())), ActivityAction.CREATE);
        if (result.isSuccessfull()) {
            activityActions.add(ActivityAction.CREATE);
        }

        return activityActions.toArray(new ActivityAction[activityActions.size()]);
    }

    @Override
    public boolean isValidAction(IRapidFireActivityResource activity, ActivityAction action) throws Exception {

        if (isActionCacheEnabled()) {
            return isValidCachedAction(activity, action);
        } else {
            return isValidUncachedAction(activity, action);
        }
    }

    private boolean isValidUncachedAction(IRapidFireActivityResource activity, ActivityAction action) throws Exception {

        Result result = checkAction(activity.getKey(), action);

        return result.isSuccessfull();
    }

    private boolean isValidCachedAction(IRapidFireActivityResource activity, ActivityAction action) throws Exception {

        KeyActivityActionCache activityActionsKey = new KeyActivityActionCache(activity);

        Set<ActivityAction> actionsSet = ActivityActionCache.getInstance().getActions(activityActionsKey);
        if (actionsSet == null) {
            ActivityAction[] activityActions = getValidActions(activity.getKey());
            ActivityActionCache.getInstance().putActions(activityActionsKey, activityActions);
            actionsSet = ActivityActionCache.getInstance().getActions(activityActionsKey);
        }

        return actionsSet.contains(action);
    }

    @Override
    public void recoverError() {
        JDBCConnectionManager.getInstance().close(dao);
    }

    @Override
    public void openFiles() throws Exception {
        throw new IllegalAccessError("Method openFiles() of ActivityManager must not be called.");
    }

    @Override
    public Result initialize(MaintenanceMode mode, ActivityKey key) throws Exception {
        throw new IllegalAccessError("Method initialize() of ActivityManager must not be called.");
    }

    @Override
    public ActivityValues getValues() throws Exception {
        throw new IllegalAccessError("Method ActivityValues() of ActivityManager must not be called.");
    }

    @Override
    public void setValues(ActivityValues values) throws Exception {
        throw new IllegalAccessError("Method setValues() of ActivityManager must not be called.");
    }

    @Override
    public Result check() throws Exception {
        throw new IllegalAccessError("Method check() of ActivityManager must not be called.");
    }

    @Override
    public void closeFiles() throws Exception {
        throw new IllegalAccessError("Method closeFiles() of ActivityManager must not be called.");
    }
}
