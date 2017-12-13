/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.conversion;

import org.eclipse.jface.dialogs.MessageDialog;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.handlers.AbstractResourceMaintenanceHandler;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.Result;
import biz.rapidfire.core.maintenance.conversion.ConversionManager;
import biz.rapidfire.core.maintenance.conversion.shared.ConversionAction;
import biz.rapidfire.core.maintenance.conversion.shared.ConversionKey;
import biz.rapidfire.core.maintenance.file.shared.FileKey;
import biz.rapidfire.core.maintenance.job.shared.JobKey;
import biz.rapidfire.core.model.IRapidFireConversionResource;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.dao.JDBCConnectionManager;

public abstract class AbstractConversionMaintenanceHandler extends AbstractResourceMaintenanceHandler<IRapidFireConversionResource, ConversionAction> {

    private ConversionManager manager;
    private ConversionAction conversionAction;

    public AbstractConversionMaintenanceHandler(MaintenanceMode mode, ConversionAction conversionAction) {
        super(mode, conversionAction);

        this.conversionAction = conversionAction;
    }

    protected ConversionManager getManager() {
        return manager;
    }

    protected ConversionManager getOrCreateManager(IRapidFireJobResource job) throws Exception {

        if (manager == null) {
            String connectionName = job.getParentSubSystem().getConnectionName();
            String dataLibrary = job.getDataLibrary();
            boolean commitControl = isCommitControl();
            manager = new ConversionManager(JDBCConnectionManager.getInstance().getConnection(connectionName, dataLibrary, commitControl));
        }

        return manager;
    }

    @Override
    protected boolean isValidAction(IRapidFireConversionResource conversion) throws Exception {
        return getOrCreateManager(conversion.getParentJob()).isValidAction(conversion, conversionAction);
    }

    @Override
    protected boolean isInstanceOf(Object object) {

        if (object instanceof IRapidFireConversionResource) {
            return true;
        }

        return false;
    }

    @Override
    protected boolean canExecuteAction(IRapidFireConversionResource conversion, ConversionAction conversionAction) {

        String message = null;

        try {

            Result result = getOrCreateManager(conversion.getParentJob()).checkAction(conversion.getKey(), conversionAction);
            if (result.isSuccessfull()) {
                return true;
            } else {
                message = Messages.bindParameters(Messages.The_requested_operation_is_invalid_for_job_status_A,
                    conversion.getParentJob().getStatus().label);
            }

        } catch (Exception e) {
            logError(e);
        }

        if (message != null) {
            MessageDialog.openError(getShell(), Messages.E_R_R_O_R, message);
        }

        return false;
    }

    protected Result initialize(IRapidFireConversionResource conversion) throws Exception {

        manager.openFiles();

        JobKey jobKey = new JobKey(conversion.getJob());
        Result result = manager.initialize(getMaintenanceMode(),
            new ConversionKey(new FileKey(jobKey, conversion.getPosition()), conversion.getFieldToConvert()));

        return result;
    }

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
