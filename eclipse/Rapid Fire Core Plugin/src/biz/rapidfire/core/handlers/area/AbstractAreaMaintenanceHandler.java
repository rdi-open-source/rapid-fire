/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.area;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.handlers.AbstractResourceMaintenanceHandler;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.maintenance.area.AreaManager;
import biz.rapidfire.core.maintenance.area.shared.AreaAction;
import biz.rapidfire.core.maintenance.area.shared.AreaKey;
import biz.rapidfire.core.maintenance.file.shared.FileKey;
import biz.rapidfire.core.maintenance.job.shared.JobKey;
import biz.rapidfire.core.model.IRapidFireAreaResource;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.dao.JDBCConnectionManager;
import biz.rapidfire.core.model.maintenance.MaintenanceMode;
import biz.rapidfire.core.model.maintenance.Result;

public abstract class AbstractAreaMaintenanceHandler extends AbstractResourceMaintenanceHandler<IRapidFireAreaResource, AreaAction> {

    private AreaManager manager;
    private AreaAction areaAction;

    public AbstractAreaMaintenanceHandler(MaintenanceMode mode, AreaAction areaAction) {
        super(mode);

        this.areaAction = areaAction;
    }

    protected AreaManager getManager() {
        return manager;
    }

    @Override
    protected Object executeWithResource(IRapidFireAreaResource area) throws ExecutionException {

        try {

            if (canExecuteAction(area, areaAction)) {
                Result result = initialize(area);
                if (result != null && result.isError()) {
                    MessageDialog.openError(getShell(), Messages.E_R_R_O_R, result.getMessage());
                } else {
                    performAction(area);
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

    protected AreaManager getOrCreateManager(IRapidFireJobResource job) throws Exception {

        if (manager == null) {
            String connectionName = job.getParentSubSystem().getConnectionName();
            String dataLibrary = job.getDataLibrary();
            boolean commitControl = isCommitControl();
            manager = new AreaManager(JDBCConnectionManager.getInstance().getConnection(connectionName, dataLibrary, commitControl));
        }

        return manager;
    }

    @Override
    protected boolean isValidAction(IRapidFireAreaResource job) throws Exception {
        return true;
    }

    @Override
    protected boolean isInstanceOf(Object object) {

        if (object instanceof IRapidFireAreaResource) {
            return true;
        }

        return false;
    }

    @Override
    protected boolean canExecuteAction(IRapidFireAreaResource area, AreaAction areaAction) {

        String message = null;

        try {

            // TODO: check action!
            Result result = getOrCreateManager(area.getParentJob()).checkAction(area.getKey(), areaAction);
            if (result.isSuccessfull()) {
                return true;
            } else {
                // TODO: fix message
                message = Messages
                    .bindParameters(Messages.The_requested_operation_is_invalid_for_job_status_A, area.getParentJob().getStatus().label);
            }

        } catch (Exception e) {
            logError("*** Could not check job action. Failed creating the job manager ***", e); //$NON-NLS-1$
        }

        if (message != null) {
            MessageDialog.openError(getShell(), Messages.E_R_R_O_R, message);
        }

        return false;
    }

    private Result initialize(IRapidFireAreaResource area) throws Exception {

        manager.openFiles();

        JobKey jobKey = new JobKey(area.getJob());
        Result result = manager.initialize(getMaintenanceMode(), new AreaKey(new FileKey(jobKey, area.getPosition()), area.getName()));

        return result;
    }

    protected abstract void performAction(IRapidFireAreaResource area) throws Exception;

    private void terminate() throws Exception {

        if (manager != null) {
            manager.closeFiles();
            manager = null;
        }
    }

    private void logError(Throwable e) {
        RapidFireCorePlugin.logError("*** Could not handle Rapid Fire area resource request ***", e); //$NON-NLS-1$
        MessageDialog.openError(getShell(), Messages.E_R_R_O_R, ExceptionHelper.getLocalizedMessage(e));
    }
}
