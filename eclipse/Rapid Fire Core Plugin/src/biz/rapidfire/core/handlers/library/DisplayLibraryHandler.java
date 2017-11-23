/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.library;

import org.eclipse.core.commands.IHandler;

import biz.rapidfire.core.dialogs.maintenance.library.LibraryMaintenanceDialog;
import biz.rapidfire.core.model.IRapidFireLibraryResource;
import biz.rapidfire.core.model.maintenance.IMaintenance;
import biz.rapidfire.core.model.maintenance.library.LibraryValues;

public class DisplayLibraryHandler extends AbstractLibraryMaintenanceHandler implements IHandler {

    public DisplayLibraryHandler() {
        super(IMaintenance.MODE_DISPLAY);
    }

    @Override
    protected void performAction(IRapidFireLibraryResource library) throws Exception {

        LibraryValues values = getManager().getValues();

        LibraryMaintenanceDialog dialog = LibraryMaintenanceDialog.getDisplayDialog(getShell(), getManager());
        dialog.setValue(values);

        dialog.open();
    }
}
