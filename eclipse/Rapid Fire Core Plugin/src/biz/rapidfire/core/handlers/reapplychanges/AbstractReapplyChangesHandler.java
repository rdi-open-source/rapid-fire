/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.reapplychanges;

import org.eclipse.jface.dialogs.MessageDialog;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.handlers.AbstractResourceMaintenanceHandler;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.Result;
import biz.rapidfire.core.maintenance.area.shared.AreaKey;
import biz.rapidfire.core.maintenance.file.shared.FileKey;
import biz.rapidfire.core.maintenance.job.shared.JobKey;
import biz.rapidfire.core.maintenance.reapplychanges.ReapplyChangesAction;
import biz.rapidfire.core.maintenance.reapplychanges.ReapplyChangesManager;
import biz.rapidfire.core.model.IFileCopyStatus;
import biz.rapidfire.core.model.IRapidFireJobResource;

public abstract class AbstractReapplyChangesHandler extends AbstractResourceMaintenanceHandler<IFileCopyStatus, ReapplyChangesAction> {

    private ReapplyChangesManager manager;
    private ReapplyChangesAction reapplyChangesAction;

    public AbstractReapplyChangesHandler(MaintenanceMode mode, ReapplyChangesAction reapplyChangesAction) {
        super(mode, reapplyChangesAction);

        this.reapplyChangesAction = reapplyChangesAction;
    }

    protected ReapplyChangesManager getManager() {
        return manager;
    }

    protected ReapplyChangesManager getOrCreateManager(IRapidFireJobResource job) throws Exception {

        if (manager == null) {
            String connectionName = job.getParentSubSystem().getConnectionName();
            String dataLibrary = job.getDataLibrary();
            manager = new ReapplyChangesManager(getJdbcConnection(connectionName, dataLibrary));
        }

        return manager;
    }

    @Override
    protected boolean isValidAction(IFileCopyStatus fileCopyStatus) throws Exception {
        return getOrCreateManager(fileCopyStatus.getJob()).isValidAction(fileCopyStatus, reapplyChangesAction);
    }

    @Override
    protected boolean isInstanceOf(Object object) {

        if (object instanceof IFileCopyStatus) {
            return true;
        }

        return false;
    }

    @Override
    protected boolean canExecuteAction(IFileCopyStatus fileCopyStatus, ReapplyChangesAction reapplyChangesAction) {

        String message = null;

        try {

            Result result;
            if (reapplyChangesAction == ReapplyChangesAction.REAPYCHG) {
                result = getOrCreateManager(fileCopyStatus.getJob()).checkAction(fileCopyStatus.getKey(), reapplyChangesAction);
            } else {
                throw new RuntimeException("Invalid action: " + reapplyChangesAction.label()); //$NON-NLS-1$
            }

            if (result.isSuccessfull()) {
                return true;
            } else {
                message = Messages.bindParameters(Messages.The_requested_operation_is_invalid_for_job_status_A, fileCopyStatus.getJob().getStatus()
                    .label());
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
    protected Result initialize(IFileCopyStatus fileCopyStatus) throws Exception {

        manager.openFiles();

        JobKey jobKey = fileCopyStatus.getJob().getKey();
        Result result = manager.initialize(getMaintenanceMode(),
            new AreaKey(new FileKey(jobKey, fileCopyStatus.getPosition()), fileCopyStatus.getArea()));

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
