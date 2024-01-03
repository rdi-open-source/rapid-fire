/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.file;

import org.eclipse.core.commands.IHandler;

import biz.rapidfire.core.dialogs.maintenance.file.FileMaintenanceDialog;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.file.FileValues;
import biz.rapidfire.core.maintenance.file.shared.FileAction;
import biz.rapidfire.core.model.IRapidFireFileResource;

public class DisplayFileHandler extends AbstractFileMaintenanceHandler implements IHandler {

    public DisplayFileHandler() {
        super(MaintenanceMode.DISPLAY, FileAction.DISPLAY);
    }

    @Override
    protected void performAction(IRapidFireFileResource file) throws Exception {

        FileValues values = getManager().getValues();

        FileMaintenanceDialog dialog = FileMaintenanceDialog.getDisplayDialog(getShell(), getManager());
        dialog.setValue(values);

        dialog.open();
    }
}
