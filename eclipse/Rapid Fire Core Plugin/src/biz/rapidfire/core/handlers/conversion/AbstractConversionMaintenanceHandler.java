/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.conversion;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.handlers.AbstractResourceMaintenanceHandler;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.model.IRapidFireConversionResource;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.dao.JDBCConnectionManager;
import biz.rapidfire.core.model.maintenance.MaintenanceMode;
import biz.rapidfire.core.model.maintenance.Result;
import biz.rapidfire.core.model.maintenance.conversion.ConversionManager;
import biz.rapidfire.core.model.maintenance.conversion.shared.ConversionAction;
import biz.rapidfire.core.model.maintenance.conversion.shared.ConversionKey;
import biz.rapidfire.core.model.maintenance.file.shared.FileKey;
import biz.rapidfire.core.model.maintenance.job.shared.JobKey;

public abstract class AbstractConversionMaintenanceHandler extends AbstractResourceMaintenanceHandler<IRapidFireConversionResource, ConversionAction> {

    private ConversionManager manager;
    private ConversionAction conversionAction;

    public AbstractConversionMaintenanceHandler(MaintenanceMode mode, ConversionAction conversionAction) {
        super(mode);

        this.conversionAction = conversionAction;
    }

    protected ConversionManager getManager() {
        return manager;
    }

    @Override
    protected Object executeWithResource(IRapidFireConversionResource conversion) throws ExecutionException {

        try {

            if (canExecuteAction(conversion, conversionAction)) {
                Result result = initialize(conversion);
                if (result != null && result.isError()) {
                    MessageDialog.openError(getShell(), Messages.E_R_R_O_R, result.getMessage());
                } else {
                    performAction(conversion);
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
        return true;
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
                // TODO: fix message
                message = Messages.bindParameters(Messages.The_requested_operation_is_invalid_for_job_status_A,
                    conversion.getParentJob().getStatus().label);
            }

        } catch (Exception e) {
            logError("*** Could not check job action. Failed creating the job manager ***", e); //$NON-NLS-1$
        }

        if (message != null) {
            MessageDialog.openError(getShell(), Messages.E_R_R_O_R, message);
        }

        return false;
    }

    private Result initialize(IRapidFireConversionResource conversion) throws Exception {

        manager.openFiles();

        JobKey jobKey = new JobKey(conversion.getJob());
        Result result = manager.initialize(getMaintenanceMode(),
            new ConversionKey(new FileKey(jobKey, conversion.getPosition()), conversion.getFieldToConvert()));

        return result;
    }

    protected abstract void performAction(IRapidFireConversionResource conversion) throws Exception;

    private void terminate() throws Exception {

        if (manager != null) {
            manager.closeFiles();
            manager = null;
        }
    }

    private void logError(Throwable e) {
        RapidFireCorePlugin.logError("*** Could not handle Rapid Fire conversion resource request ***", e); //$NON-NLS-1$
        MessageDialog.openError(getShell(), Messages.E_R_R_O_R, ExceptionHelper.getLocalizedMessage(e));
    }
}
