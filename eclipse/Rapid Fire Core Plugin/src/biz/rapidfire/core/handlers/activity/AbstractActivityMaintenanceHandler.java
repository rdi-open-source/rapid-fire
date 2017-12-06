/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.activity;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.handlers.AbstractResourceMaintenanceHandler;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.IRapidFireResource;
import biz.rapidfire.core.model.dao.JDBCConnectionManager;
import biz.rapidfire.core.model.maintenance.MaintenanceMode;
import biz.rapidfire.core.model.maintenance.Result;
import biz.rapidfire.core.model.maintenance.activity.ActivityManager;
import biz.rapidfire.core.model.maintenance.activity.shared.ActivityAction;
import biz.rapidfire.core.model.maintenance.job.JobManager;
import biz.rapidfire.core.model.maintenance.job.shared.JobAction;
import biz.rapidfire.core.model.maintenance.job.shared.JobKey;

public abstract class AbstractActivityMaintenanceHandler extends AbstractResourceMaintenanceHandler<IRapidFireJobResource, ActivityAction> {

    private ActivityManager manager;
    private ActivityAction initialActivityAction;
    private ActivityAction currentActivityAction;

    public AbstractActivityMaintenanceHandler(MaintenanceMode mode, ActivityAction activityAction) {
        super(mode);

        this.initialActivityAction = activityAction;
        this.currentActivityAction = this.initialActivityAction;
    }

    protected ActivityManager getManager() {
        return manager;
    }

    @Override
    protected Object executeWithResource(IRapidFireJobResource job) throws ExecutionException {

        try {

            currentActivityAction = initialActivityAction;
            if (canExecuteAction(job, currentActivityAction)) {
                Result result = initialize(job);
                if (result != null && result.isError()) {
                    MessageDialog.openError(getShell(), Messages.E_R_R_O_R, result.getMessage());
                } else {
                    performAction(job);
                }
            }

        } catch (Throwable e) {
            logError(e);
        } finally {
            try {
                terminate();
            } catch (Throwable e) {
                logError(e);
            }
        }

        return null;
    }

    protected ActivityManager getOrCreateManager(IRapidFireJobResource job) throws Exception {

        if (manager == null) {
            String connectionName = job.getParentSubSystem().getConnectionName();
            String dataLibrary = job.getDataLibrary();
            boolean commitControl = isCommitControl();
            manager = new ActivityManager(JDBCConnectionManager.getInstance().getConnection(connectionName, dataLibrary, commitControl));
        }

        return manager;
    }

    @Override
    protected boolean isValidAction(IRapidFireJobResource job) throws Exception {
        return true;
    }

    @Override
    protected boolean isInstanceOf(Object object) {

        if (object instanceof IRapidFireJobResource) {
            return true;
        }

        return false;
    }

    @Override
    protected boolean canExecuteAction(IRapidFireJobResource job, ActivityAction activityAction) {

        try {

            // Create activity manager for maintaining activities
            getOrCreateManager(job);

            // Create job manager for checking, whether or not the job is
            // allowed to be changed. Only when the job is allowed to be
            // changed, activities can be changed, too.
            String connectionName = job.getParentSubSystem().getConnectionName();
            String dataLibrary = job.getDataLibrary();
            JobManager jobManager = new JobManager(JDBCConnectionManager.getInstance().getConnection(connectionName, dataLibrary, false));
            Result result = jobManager.checkAction(job.getKey(), JobAction.CHANGE);
            if (result.isError()) {
                // Fall back to display mode
                if (getMaintenanceMode() != MaintenanceMode.DISPLAY) {
                    changeMaintenanceMode(job, MaintenanceMode.DISPLAY, ActivityAction.DISPLAY);
                    result = jobManager.checkAction(job.getKey(), JobAction.DISPLAY);
                }
            }

            if (result.isError()) {
                setErrorMessage(Messages.bindParameters(Messages.The_requested_operation_is_invalid_for_job_status_A, job.getStatus().label));
            }

            return true;

        } catch (Exception e) {
            logError("*** Could not check job action. Failed creating the job manager ***", e); //$NON-NLS-1$
        }

        return false;
    }

    private Result initialize(IRapidFireJobResource job) throws Exception {

        JobKey jobKey = job.getKey();
        Result result = getOrCreateManager(job).initialize(getMaintenanceMode(), jobKey);

        return result;
    }

    protected abstract void performAction(IRapidFireJobResource job) throws Exception;

    private void terminate() throws Exception {

        if (manager != null) {
            manager = null;
        }
    }

    private Result changeMaintenanceMode(IRapidFireResource resource, MaintenanceMode mode, ActivityAction activityAction) throws Exception {

        terminate();

        super.changeMaintenanceMode(mode);
        this.currentActivityAction = activityAction;

        return initialize((IRapidFireJobResource)resource);
    }

    private void logError(Throwable e) {
        logError("*** Could not handle Rapid Fire activity resource request ***", e); //$NON-NLS-1$
    }
}
