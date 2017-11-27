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
import biz.rapidfire.core.model.IRapidFireCommandResource;
import biz.rapidfire.core.model.maintenance.IMaintenance;
import biz.rapidfire.core.model.maintenance.command.CommandValues;

public class DisplayCommandHandler extends AbstractCommandMaintenanceHandler implements IHandler {

    public DisplayCommandHandler() {
        super(IMaintenance.MODE_DISPLAY);
    }

    @Override
    protected void performAction(IRapidFireCommandResource command) throws Exception {

        CommandValues values = getManager().getValues();

        CommandMaintenanceDialog dialog = CommandMaintenanceDialog.getDisplayDialog(getShell(), getManager());
        dialog.setValue(values);

        dialog.open();
    }
}
