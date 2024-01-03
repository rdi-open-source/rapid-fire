/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.command;

import org.eclipse.jface.dialogs.MessageDialog;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.handlers.AbstractResourceMaintenanceHandler;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.Result;
import biz.rapidfire.core.maintenance.command.CommandManager;
import biz.rapidfire.core.maintenance.command.shared.CommandAction;
import biz.rapidfire.core.maintenance.command.shared.CommandKey;
import biz.rapidfire.core.maintenance.file.shared.FileKey;
import biz.rapidfire.core.maintenance.job.shared.JobKey;
import biz.rapidfire.core.model.IRapidFireCommandResource;
import biz.rapidfire.core.model.IRapidFireJobResource;

public abstract class AbstractCommandMaintenanceHandler extends AbstractResourceMaintenanceHandler<IRapidFireCommandResource, CommandAction> {

    private CommandManager manager;
    private CommandAction commandAction;

    public AbstractCommandMaintenanceHandler(MaintenanceMode mode, CommandAction commandAction) {
        super(mode, commandAction);

        this.commandAction = commandAction;
    }

    protected CommandManager getManager() {
        return manager;
    }

    protected CommandManager getOrCreateManager(IRapidFireJobResource job) throws Exception {

        if (manager == null) {
            String connectionName = job.getParentSubSystem().getConnectionName();
            String dataLibrary = job.getDataLibrary();
            manager = new CommandManager(getJdbcConnection(connectionName, dataLibrary));
        }

        return manager;
    }

    @Override
    protected boolean isValidAction(IRapidFireCommandResource command) throws Exception {
        return getOrCreateManager(command.getParentJob()).isValidAction(command, commandAction);
    }

    @Override
    protected boolean isInstanceOf(Object object) {

        if (object instanceof IRapidFireCommandResource) {
            return true;
        }

        return false;
    }

    @Override
    protected boolean canExecuteAction(IRapidFireCommandResource command, CommandAction commandAction) {

        String message = null;

        try {

            Result result;
            if (commandAction == CommandAction.CREATE) {
                result = getOrCreateManager(command.getParentJob()).checkAction(CommandKey.createNew(command.getParentResource().getKey()),
                    commandAction);
            } else {
                result = getOrCreateManager(command.getParentJob()).checkAction(command.getKey(), commandAction);
            }

            if (result.isSuccessfull()) {
                return true;
            } else {
                message = Messages.bindParameters(Messages.The_requested_operation_is_invalid_for_job_status_A, command.getParentJob().getStatus()
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
    protected Result initialize(IRapidFireCommandResource command) throws Exception {

        manager.openFiles();

        JobKey jobKey = new JobKey(command.getJob());
        Result result = manager.initialize(getMaintenanceMode(), new CommandKey(new FileKey(jobKey, command.getPosition()), command.getCommandType(),
            command.getSequence()));

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
