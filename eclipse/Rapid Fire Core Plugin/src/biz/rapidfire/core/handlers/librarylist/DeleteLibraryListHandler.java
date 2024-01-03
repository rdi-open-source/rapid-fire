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
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.librarylist.LibraryListValues;
import biz.rapidfire.core.maintenance.librarylist.shared.LibraryListAction;
import biz.rapidfire.core.model.IRapidFireLibraryListResource;

public class DeleteLibraryListHandler extends AbstractLibraryListMaintenanceHandler implements IHandler {

    public DeleteLibraryListHandler() {
        super(MaintenanceMode.DELETE, LibraryListAction.DELETE);
    }

    @Override
    protected void performAction(IRapidFireLibraryListResource libraryList) throws Exception {

        LibraryListValues values = getManager().getValues();

        LibraryListMaintenanceDialog dialog = LibraryListMaintenanceDialog.getDeleteDialog(getShell(), getManager());
        dialog.setValue(values);

        if (dialog.open() == Dialog.OK) {
            getManager().book();

            refreshUIDeleted(libraryList.getParentSubSystem(), libraryList, libraryList.getParentNode());
        }
    }
}
