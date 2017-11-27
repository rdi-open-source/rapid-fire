/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.command;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.handlers.AbstractResourceHandler;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.model.IRapidFireCommandResource;
import biz.rapidfire.core.model.IRapidFireResource;
import biz.rapidfire.core.model.dao.JDBCConnectionManager;
import biz.rapidfire.core.model.maintenance.IMaintenance;
import biz.rapidfire.core.model.maintenance.Result;
import biz.rapidfire.core.model.maintenance.command.CommandKey;
import biz.rapidfire.core.model.maintenance.command.CommandManager;
import biz.rapidfire.core.model.maintenance.file.FileKey;
import biz.rapidfire.core.model.maintenance.job.JobKey;

public abstract class AbstractCommandMaintenanceHandler extends AbstractResourceHandler {

    private CommandManager manager;

    public AbstractCommandMaintenanceHandler(String mode) {
        super(mode);
    }

    protected CommandManager getManager() {
        return manager;
    }

    @Override
    protected Object executeWithResource(IRapidFireResource resource) throws ExecutionException {

        if (!(resource instanceof IRapidFireCommandResource)) {
            return null;
        }

        try {

            IRapidFireCommandResource command = (IRapidFireCommandResource)resource;

            Result result = initialize(command);
            if (result != null && result.isError()) {
                MessageDialog.openError(getShell(), Messages.E_R_R_O_R, result.getMessage());
            } else {
                performAction(command);
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

    private Result initialize(IRapidFireCommandResource command) throws Exception {

        String connectionName = command.getParentSubSystem().getConnectionName();
        String dataLibrary = command.getDataLibrary();
        boolean commitControl = isCommitControl();

        manager = new CommandManager(JDBCConnectionManager.getInstance().getConnection(connectionName, dataLibrary, commitControl));
        manager.openFiles();

        JobKey jobKey = new JobKey(command.getJob());
        Result result = manager.initialize(getMode(),
            new CommandKey(new FileKey(jobKey, command.getPosition()), command.getCommandType(), command.getSequence()));
        if (result.isError()) {
            return result;
        }

        return null;
    }

    protected abstract void performAction(IRapidFireCommandResource command) throws Exception;

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
        RapidFireCorePlugin.logError("*** Could not handle Rapid Fire command resource request ***", e); //$NON-NLS-1$
        MessageDialog.openError(getShell(), Messages.E_R_R_O_R, ExceptionHelper.getLocalizedMessage(e));
    }
}
