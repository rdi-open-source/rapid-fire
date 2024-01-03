/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.job;

import org.eclipse.jface.dialogs.MessageDialog;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.handlers.AbstractResourceMaintenanceHandler;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.Result;
import biz.rapidfire.core.maintenance.job.JobManager;
import biz.rapidfire.core.maintenance.job.shared.JobAction;
import biz.rapidfire.core.maintenance.job.shared.JobKey;
import biz.rapidfire.core.model.IRapidFireJobResource;

public abstract class AbstractJobMaintenanceHandler extends AbstractResourceMaintenanceHandler<IRapidFireJobResource, JobAction> {

    private JobManager manager;
    private JobAction jobAction;

    public AbstractJobMaintenanceHandler(MaintenanceMode mode, JobAction jobAction) {
        super(mode, jobAction);

        this.jobAction = jobAction;
    }

    protected JobManager getManager() {
        return manager;
    }

    public JobAction getJobAction() {
        return jobAction;
    }

    protected JobManager getOrCreateManager(IRapidFireJobResource job) throws Exception {

        if (manager == null) {
            String connectionName = job.getParentSubSystem().getConnectionName();
            String dataLibrary = job.getDataLibrary();
            manager = new JobManager(getJdbcConnection(connectionName, dataLibrary));
        }

        return manager;
    }

    @Override
    protected boolean isValidAction(IRapidFireJobResource job) throws Exception {
        return getOrCreateManager(job).isValidAction(job, jobAction);
    }

    @Override
    protected boolean isInstanceOf(Object object) {

        if (object instanceof IRapidFireJobResource) {
            return true;
        }

        return false;
    }

    @Override
    protected boolean canExecuteAction(IRapidFireJobResource job, JobAction action) {

        String message = null;

        try {

            Result result;
            if (action == JobAction.CREATE) {
                result = getOrCreateManager(job).checkAction(JobKey.createNew(), action);
            } else {
                result = getOrCreateManager(job).checkAction(new JobKey(job.getName()), action);
            }

            if (result.isSuccessfull()) {
                return true;
            } else {
                message = Messages.bindParameters(Messages.The_requested_operation_is_invalid_for_job_status_A, job.getStatus().label());
            }

        } catch (Exception e) {
            logError(e);
        }

        if (message != null) {
            MessageDialog.openError(getShell(), Messages.E_R_R_O_R, message);
        }

        return false;
    }

    @Override
    protected Result initialize(IRapidFireJobResource job) throws Exception {

        manager.openFiles();

        Result result = manager.initialize(getMaintenanceMode(), new JobKey(job.getName()));

        return result;
    }

    @Override
    protected void terminate(boolean closeConnection) throws Exception {

        if (manager != null) {
            manager.closeFiles();
            if (closeConnection) {
                manager.recoverError();
            }
            manager = null;
        }
    }
}
