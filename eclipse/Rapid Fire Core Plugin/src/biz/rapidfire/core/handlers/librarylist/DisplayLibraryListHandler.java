/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.librarylist;

import org.eclipse.core.commands.IHandler;

import biz.rapidfire.core.dialogs.maintenance.librarylist.LibraryListMaintenanceDialog;
import biz.rapidfire.core.model.IRapidFireLibraryListResource;
import biz.rapidfire.core.model.maintenance.IMaintenance;
import biz.rapidfire.core.model.maintenance.librarylist.LibraryListValues;

public class DisplayLibraryListHandler extends AbstractLibraryListMaintenanceHandler implements IHandler {

    public DisplayLibraryListHandler() {
        super(IMaintenance.MODE_DISPLAY);
    }

    @Override
    protected void performAction(IRapidFireLibraryListResource libraryList) throws Exception {

        LibraryListValues values = getManager().getValues();

        LibraryListMaintenanceDialog dialog = LibraryListMaintenanceDialog.getDisplayDialog(getShell(), getManager());
        dialog.setValue(values);

        dialog.open();
    }
}
