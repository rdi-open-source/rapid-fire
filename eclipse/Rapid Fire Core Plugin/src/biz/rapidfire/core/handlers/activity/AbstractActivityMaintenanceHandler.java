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
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.handlers.AbstractResourceMaintenanceHandler;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.IRapidFireResource;
import biz.rapidfire.core.model.dao.JDBCConnectionManager;
import biz.rapidfire.core.model.maintenance.Result;
import biz.rapidfire.core.model.maintenance.activity.ActivityManager;
import biz.rapidfire.core.model.maintenance.job.JobKey;
import biz.rapidfire.core.model.maintenance.job.JobManager;
import biz.rapidfire.core.model.maintenance.job.shared.JobAction;

public abstract class AbstractActivityMaintenanceHandler extends AbstractResourceMaintenanceHandler<IRapidFireJobResource> {

    private ActivityManager manager;
    private JobAction jobAction;

    public AbstractActivityMaintenanceHandler(String mode, JobAction jobAction) {
        super(mode);

        this.jobAction = jobAction;
    }

    protected ActivityManager getManager() {
        return manager;
    }

    @Override
    protected Object executeWithResource(IRapidFireResource resource) throws ExecutionException {

        if (!(resource instanceof IRapidFireJobResource)) {
            return null;
        }

        try {

            IRapidFireJobResource job = (IRapidFireJobResource)resource;
            manager = getOrCreateManager(job);

            if (canExecuteAction(job, jobAction)) {
                initialize(job);
                performAction(job);
            }

        } catch (Throwable e) {
            logError(e);
        } finally {
        }

        return null;
    }

    private ActivityManager getOrCreateManager(IRapidFireJobResource job) throws Exception {

        if (manager == null) {
            String connectionName = job.getParentSubSystem().getConnectionName();
            String dataLibrary = job.getDataLibrary();
            boolean commitControl = isCommitControl();
            manager = new ActivityManager(JDBCConnectionManager.getInstance().getConnection(connectionName, dataLibrary, commitControl));
        }

        return manager;
    }

    protected boolean canExecuteAction(IRapidFireJobResource job, JobAction jobAction) {

        String connectionName = job.getParentSubSystem().getConnectionName();
        String dataLibrary = job.getDataLibrary();
        String message = null;

        try {

            JobManager jobManager = new JobManager(JDBCConnectionManager.getInstance().getConnection(connectionName, dataLibrary, false));
            Result result = jobManager.checkAction(new JobKey(job.getName()), jobAction);

            if (result.isSuccessfull()) {
                return true;
            } else {
                message = Messages.bindParameters(Messages.The_requested_operation_is_invalid_for_job_status_A, job.getStatus().label);
            }

        } catch (Exception e) {
            message = "*** Could not check job action. Failed creating the job manager ***";
            RapidFireCorePlugin.logError(message, e); //$NON-NLS-1$
        }

        if (message != null) {
            MessageDialog.openError(getShell(), Messages.E_R_R_O_R, message);
        }

        return false;
    }

    private void initialize(IRapidFireJobResource job) throws Exception {

        String connectionName = job.getParentSubSystem().getConnectionName();
        String dataLibrary = job.getDataLibrary();
        boolean commitControl = isCommitControl();

        manager = new ActivityManager(JDBCConnectionManager.getInstance().getConnection(connectionName, dataLibrary, commitControl));

        manager.initialize(getMode(), new JobKey(job.getName()));
    }

    protected abstract void performAction(IRapidFireJobResource job) throws Exception;

    private void logError(Throwable e) {
        logError("*** Could not handle Rapid Fire activity resource request ***", e); //$NON-NLS-1$
    }
}
