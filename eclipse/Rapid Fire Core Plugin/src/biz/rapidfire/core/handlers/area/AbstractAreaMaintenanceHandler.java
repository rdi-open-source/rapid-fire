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
import biz.rapidfire.core.handlers.AbstractResourceHandler;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.model.IRapidFireAreaResource;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.IRapidFireResource;
import biz.rapidfire.core.model.dao.JDBCConnectionManager;
import biz.rapidfire.core.model.maintenance.IMaintenance;
import biz.rapidfire.core.model.maintenance.Result;
import biz.rapidfire.core.model.maintenance.Success;
import biz.rapidfire.core.model.maintenance.area.AreaKey;
import biz.rapidfire.core.model.maintenance.area.AreaManager;
import biz.rapidfire.core.model.maintenance.area.shared.AreaAction;
import biz.rapidfire.core.model.maintenance.file.FileKey;
import biz.rapidfire.core.model.maintenance.job.JobKey;

public abstract class AbstractAreaMaintenanceHandler extends AbstractResourceHandler {

    private AreaManager manager;
    private AreaAction areaAction;

    public AbstractAreaMaintenanceHandler(String mode, AreaAction areaAction) {
        super(mode);

        this.areaAction = areaAction;
    }

    protected AreaManager getManager() {
        return manager;
    }

    @Override
    protected Object executeWithResource(IRapidFireResource resource) throws ExecutionException {

        if (!(resource instanceof IRapidFireAreaResource)) {
            return null;
        }

        try {

            IRapidFireAreaResource area = (IRapidFireAreaResource)resource;
            manager = getOrCreateManager(area.getParentJob());

            if (canExecuteAction(area.getParentJob(), areaAction)) {
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

    private AreaManager getOrCreateManager(IRapidFireJobResource job) throws Exception {

        if (manager == null) {
            String connectionName = job.getParentSubSystem().getConnectionName();
            String dataLibrary = job.getDataLibrary();
            boolean commitControl = isCommitControl();
            manager = new AreaManager(JDBCConnectionManager.getInstance().getConnection(connectionName, dataLibrary, commitControl));
        }

        return manager;
    }

    protected boolean canExecuteAction(IRapidFireJobResource job, AreaAction areaAction) {

        String message = null;

        try {

            // TODO: check action!
            // Result result = libraryManager.checkAction(new
            // JobKey(job.getName()),
            // libraryAction);
            Result result = new Result(Success.YES.label(), null);

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

    private Result initialize(IRapidFireAreaResource area) throws Exception {

        String connectionName = area.getParentSubSystem().getConnectionName();
        String dataLibrary = area.getDataLibrary();
        boolean commitControl = isCommitControl();

        manager = new AreaManager(JDBCConnectionManager.getInstance().getConnection(connectionName, dataLibrary, commitControl));
        manager.openFiles();

        JobKey jobKey = new JobKey(area.getJob());
        Result result = manager.initialize(getMode(), new AreaKey(new FileKey(jobKey, area.getPosition()), area.getName()));
        if (result.isError()) {
            return result;
        }

        return null;
    }

    protected abstract void performAction(IRapidFireAreaResource area) throws Exception;

    private void terminate() throws Exception {

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
        RapidFireCorePlugin.logError("*** Could not handle Rapid Fire area resource request ***", e); //$NON-NLS-1$
        MessageDialog.openError(getShell(), Messages.E_R_R_O_R, ExceptionHelper.getLocalizedMessage(e));
    }
}
