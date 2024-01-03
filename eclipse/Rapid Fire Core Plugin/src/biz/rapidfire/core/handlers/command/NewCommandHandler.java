/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.command;

import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.Dialog;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.dialogs.MessageDialogAsync;
import biz.rapidfire.core.dialogs.maintenance.command.CommandMaintenanceDialog;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.command.CommandValues;
import biz.rapidfire.core.maintenance.command.shared.CommandAction;
import biz.rapidfire.core.maintenance.command.shared.CommandType;
import biz.rapidfire.core.model.IRapidFireCommandResource;

public class NewCommandHandler extends AbstractCommandMaintenanceHandler implements IHandler {

    public NewCommandHandler() {
        super(MaintenanceMode.CREATE, CommandAction.CREATE);
    }

    @Override
    protected void performAction(IRapidFireCommandResource command) throws Exception {

        CommandValues values = getManager().getValues();

        CommandMaintenanceDialog dialog = CommandMaintenanceDialog.getCreateDialog(getShell(), getManager());
        dialog.setValue(values);

        if (dialog.open() == Dialog.OK) {
            getManager().book();

            values = dialog.getValue();
            CommandType commandType = CommandType.find(values.getKey().getCommandType());
            IRapidFireCommandResource newCommand = command.getParentSubSystem().getCommand(command.getParentResource(), commandType,
                values.getKey().getSequence(), getShell());
            if (newCommand != null) {
                newCommand.setParentNode(command.getParentNode());
                refreshUICreated(newCommand.getParentSubSystem(), newCommand, newCommand.getParentNode());
            } else {
                MessageDialogAsync.displayError(Messages.Could_not_create_resource_Resource_not_found);
            }
        }
    }
}
