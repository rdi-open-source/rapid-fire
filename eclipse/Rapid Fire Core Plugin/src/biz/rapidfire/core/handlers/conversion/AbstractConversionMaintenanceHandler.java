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
import biz.rapidfire.core.handlers.AbstractResourceHandler;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.model.IRapidFireConversionResource;
import biz.rapidfire.core.model.IRapidFireResource;
import biz.rapidfire.core.model.dao.JDBCConnectionManager;
import biz.rapidfire.core.model.maintenance.IMaintenance;
import biz.rapidfire.core.model.maintenance.Result;
import biz.rapidfire.core.model.maintenance.conversion.ConversionKey;
import biz.rapidfire.core.model.maintenance.conversion.ConversionManager;
import biz.rapidfire.core.model.maintenance.file.FileKey;
import biz.rapidfire.core.model.maintenance.job.JobKey;

public abstract class AbstractConversionMaintenanceHandler extends AbstractResourceHandler {

    private ConversionManager manager;

    public AbstractConversionMaintenanceHandler(String mode) {
        super(mode);
    }

    protected ConversionManager getManager() {
        return manager;
    }

    @Override
    protected Object executeWithResource(IRapidFireResource resource) throws ExecutionException {

        if (!(resource instanceof IRapidFireConversionResource)) {
            return null;
        }

        try {

            IRapidFireConversionResource conversion = (IRapidFireConversionResource)resource;

            Result result = initialize(conversion);
            if (result != null && result.isError()) {
                MessageDialog.openError(getShell(), Messages.E_R_R_O_R, result.getMessage());
            } else {
                performAction(conversion);
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

    private Result initialize(IRapidFireConversionResource conversion) throws Exception {

        String connectionName = conversion.getParentSubSystem().getConnectionName();
        String dataLibrary = conversion.getDataLibrary();
        boolean commitControl = isCommitControl();

        manager = new ConversionManager(JDBCConnectionManager.getInstance().getConnection(connectionName, dataLibrary, commitControl));
        manager.openFiles();

        JobKey jobKey = new JobKey(conversion.getJob());
        Result result = manager.initialize(getMode(),
            new ConversionKey(new FileKey(jobKey, conversion.getPosition()), conversion.getFieldToConvert()));
        if (result.isError()) {
            return result;
        }

        return null;
    }

    protected abstract void performAction(IRapidFireConversionResource conversion) throws Exception;

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
        RapidFireCorePlugin.logError("*** Could not handle Rapid Fire conversion resource request ***", e); //$NON-NLS-1$
        MessageDialog.openError(getShell(), Messages.E_R_R_O_R, ExceptionHelper.getLocalizedMessage(e));
    }
}
