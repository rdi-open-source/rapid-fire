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
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.librarylist.LibraryListValues;
import biz.rapidfire.core.maintenance.librarylist.shared.LibraryListAction;
import biz.rapidfire.core.model.IRapidFireLibraryListResource;

public class DisplayLibraryListHandler extends AbstractLibraryListMaintenanceHandler implements IHandler {

    public DisplayLibraryListHandler() {
        super(MaintenanceMode.DISPLAY, LibraryListAction.DISPLAY);
    }

    @Override
    protected void performAction(IRapidFireLibraryListResource libraryList) throws Exception {

        LibraryListValues values = getManager().getValues();

        LibraryListMaintenanceDialog dialog = LibraryListMaintenanceDialog.getDisplayDialog(getShell(), getManager());
        dialog.setValue(values);

        dialog.open();
    }
}
