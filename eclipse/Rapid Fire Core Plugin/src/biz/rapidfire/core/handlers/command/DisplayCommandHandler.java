/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.command;

import org.eclipse.core.commands.IHandler;

import biz.rapidfire.core.dialogs.maintenance.command.CommandMaintenanceDialog;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.command.CommandValues;
import biz.rapidfire.core.maintenance.command.shared.CommandAction;
import biz.rapidfire.core.model.IRapidFireCommandResource;

public class DisplayCommandHandler extends AbstractCommandMaintenanceHandler implements IHandler {

    public DisplayCommandHandler() {
        super(MaintenanceMode.DISPLAY, CommandAction.DISPLAY);
    }

    @Override
    protected void performAction(IRapidFireCommandResource command) throws Exception {

        CommandValues values = getManager().getValues();

        CommandMaintenanceDialog dialog = CommandMaintenanceDialog.getDisplayDialog(getShell(), getManager());
        dialog.setValue(values);

        dialog.open();
    }
}
