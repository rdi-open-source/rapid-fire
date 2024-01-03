/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.area;

import org.eclipse.jface.dialogs.MessageDialog;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.handlers.AbstractResourceMaintenanceHandler;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.Result;
import biz.rapidfire.core.maintenance.area.AreaManager;
import biz.rapidfire.core.maintenance.area.shared.AreaAction;
import biz.rapidfire.core.maintenance.area.shared.AreaKey;
import biz.rapidfire.core.maintenance.file.shared.FileKey;
import biz.rapidfire.core.maintenance.job.shared.JobKey;
import biz.rapidfire.core.model.IRapidFireAreaResource;
import biz.rapidfire.core.model.IRapidFireJobResource;

public abstract class AbstractAreaMaintenanceHandler extends AbstractResourceMaintenanceHandler<IRapidFireAreaResource, AreaAction> {

    private AreaManager manager;
    private AreaAction areaAction;

    public AbstractAreaMaintenanceHandler(MaintenanceMode mode, AreaAction areaAction) {
        super(mode, areaAction);

        this.areaAction = areaAction;
    }

    protected AreaManager getManager() {
        return manager;
    }

    protected AreaManager getOrCreateManager(IRapidFireJobResource job) throws Exception {

        if (manager == null) {
            String connectionName = job.getParentSubSystem().getConnectionName();
            String dataLibrary = job.getDataLibrary();
            manager = new AreaManager(getJdbcConnection(connectionName, dataLibrary));
        }

        return manager;
    }

    @Override
    protected boolean isValidAction(IRapidFireAreaResource area) throws Exception {
        return getOrCreateManager(area.getParentJob()).isValidAction(area, areaAction);
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

            Result result;
            if (areaAction == AreaAction.CREATE) {
                result = getOrCreateManager(area.getParentJob()).checkAction(AreaKey.createNew(area.getParentResource().getKey()), areaAction);
            } else {
                result = getOrCreateManager(area.getParentJob()).checkAction(area.getKey(), areaAction);
            }

            if (result.isSuccessfull()) {
                return true;
            } else {
                message = Messages.bindParameters(Messages.The_requested_operation_is_invalid_for_job_status_A, area.getParentJob().getStatus()
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
    protected Result initialize(IRapidFireAreaResource area) throws Exception {

        manager.openFiles();

        JobKey jobKey = new JobKey(area.getJob());
        Result result = manager.initialize(getMaintenanceMode(), new AreaKey(new FileKey(jobKey, area.getPosition()), area.getName()));

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
