/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.IRapidFireResource;
import biz.rapidfire.core.model.dao.JDBCConnectionManager;
import biz.rapidfire.core.model.maintenance.IMaintenance;
import biz.rapidfire.core.model.maintenance.Result;
import biz.rapidfire.core.model.maintenance.job.JobKey;
import biz.rapidfire.core.model.maintenance.job.JobManager;

public abstract class AbstractJobMaintenanceHandler extends AbstractResourceHandler {

    private JobManager manager;

    public AbstractJobMaintenanceHandler(String mode) {
        super(mode);
    }

    protected JobManager getManager() {
        return manager;
    }

    @Override
    protected Object executeWithResource(IRapidFireResource resource) throws ExecutionException {

        if (!(resource instanceof IRapidFireJobResource)) {
            return null;
        }

        try {

            IRapidFireJobResource job = (IRapidFireJobResource)resource;

            String message = initialize(job);
            if (message != null) {
                MessageDialog.openError(getShell(), Messages.E_R_R_O_R, message);
            } else {
                performAction(job);
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

    protected String initialize(IRapidFireJobResource job) throws Exception {

        String connectionName = job.getParentSubSystem().getConnectionName();

        manager = new JobManager(JDBCConnectionManager.getInstance().getConnection(connectionName, job.getDataLibrary(), isCommitControl()));
        manager.openFiles();

        Result status = manager.initialize(getMode(), new JobKey(job.getName()));
        if (status.isError()) {
            return status.getMessage();
        }

        return null;
    }

    protected abstract void performAction(IRapidFireJobResource job) throws Exception;

    protected void terminate() throws Exception {

        if (manager != null) {
            manager.closeFiles();
            manager = null;
        }
    }

    private boolean isCommitControl() {

        String mode = getMode();
        if (IMaintenance.MODE_CHANGE.equals(mode) || IMaintenance.MODE_DELETE.equals(mode)) {
            return true;
        }

        return false;
    }

    private void logError(Throwable e) {
        RapidFireCorePlugin.logError("*** Could not handle Rapid Fire job resource request ***", e); //$NON-NLS-1$
        MessageDialog.openError(getShell(), Messages.E_R_R_O_R, ExceptionHelper.getLocalizedMessage(e));
    }
}
