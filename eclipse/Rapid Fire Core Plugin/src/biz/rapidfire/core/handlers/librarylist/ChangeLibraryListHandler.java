/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.librarylist;

import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.Dialog;

import biz.rapidfire.core.dialogs.maintenance.librarylist.LibraryListMaintenanceDialog;
import biz.rapidfire.core.model.IRapidFireLibraryListResource;
import biz.rapidfire.core.model.maintenance.IMaintenance;
import biz.rapidfire.core.model.maintenance.librarylist.LibraryListValues;

public class ChangeLibraryListHandler extends AbstractLibraryListMaintenanceHandler implements IHandler {

    public ChangeLibraryListHandler() {
        super(IMaintenance.MODE_CHANGE);
    }

    @Override
    protected void performAction(IRapidFireLibraryListResource library) throws Exception {

        LibraryListValues values = getManager().getValues();

        LibraryListMaintenanceDialog dialog = LibraryListMaintenanceDialog.getChangeDialog(getShell(), getManager());
        dialog.setValue(values);

        if (dialog.open() == Dialog.OK) {
            getManager().book();
            refreshUI(library);
        }
    }
}
