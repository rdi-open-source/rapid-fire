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

import biz.rapidfire.core.dialogs.maintenance.command.CommandMaintenanceDialog;
import biz.rapidfire.core.model.IRapidFireCommandResource;
import biz.rapidfire.core.model.maintenance.MaintenanceMode;
import biz.rapidfire.core.model.maintenance.command.CommandValues;
import biz.rapidfire.core.model.maintenance.command.shared.CommandAction;

public class NewCommandHandler extends AbstractCommandMaintenanceHandler implements IHandler {

    public NewCommandHandler() {
        super(MaintenanceMode.MODE_CREATE, CommandAction.CREATE);
    }

    @Override
    protected void performAction(IRapidFireCommandResource command) throws Exception {

        CommandValues values = getManager().getValues();

        CommandMaintenanceDialog dialog = CommandMaintenanceDialog.getCreateDialog(getShell(), getManager());
        dialog.setValue(values);

        if (dialog.open() == Dialog.OK) {
            getManager().book();
            refreshUI(command);
        }
    }
}
