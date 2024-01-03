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

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.dialogs.MessageDialogAsync;
import biz.rapidfire.core.dialogs.maintenance.librarylist.LibraryListMaintenanceDialog;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.librarylist.LibraryListValues;
import biz.rapidfire.core.maintenance.librarylist.shared.LibraryListAction;
import biz.rapidfire.core.model.IRapidFireLibraryListResource;

public class NewLibraryListHandler extends AbstractLibraryListMaintenanceHandler implements IHandler {

    public NewLibraryListHandler() {
        super(MaintenanceMode.CREATE, LibraryListAction.CREATE);
    }

    @Override
    protected void performAction(IRapidFireLibraryListResource libraryList) throws Exception {

        LibraryListValues values = getManager().getValues();

        LibraryListMaintenanceDialog dialog = LibraryListMaintenanceDialog.getCreateDialog(getShell(), getManager());
        dialog.setValue(values);

        if (dialog.open() == Dialog.OK) {
            getManager().book();

            values = dialog.getValue();
            IRapidFireLibraryListResource newLibraryList = libraryList.getParentSubSystem().getLibraryList(libraryList.getParentJob(),
                values.getKey().getLibraryList(), getShell());
            if (newLibraryList != null) {
                newLibraryList.setParentNode(libraryList.getParentNode());
                refreshUICreated(newLibraryList.getParentSubSystem(), newLibraryList, newLibraryList.getParentNode());
            } else {
                MessageDialogAsync.displayError(Messages.Could_not_create_resource_Resource_not_found);
            }
        }
    }
}
