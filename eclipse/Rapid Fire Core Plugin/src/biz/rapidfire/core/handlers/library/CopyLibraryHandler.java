/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.library;

import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.Dialog;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.dialogs.MessageDialogAsync;
import biz.rapidfire.core.dialogs.maintenance.library.LibraryMaintenanceDialog;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.library.LibraryValues;
import biz.rapidfire.core.maintenance.library.shared.LibraryAction;
import biz.rapidfire.core.model.IRapidFireLibraryResource;

public class CopyLibraryHandler extends AbstractLibraryMaintenanceHandler implements IHandler {

    public CopyLibraryHandler() {
        super(MaintenanceMode.COPY, LibraryAction.COPY);
    }

    @Override
    protected void performAction(IRapidFireLibraryResource library) throws Exception {

        LibraryValues values = getManager().getValues();

        LibraryMaintenanceDialog dialog = LibraryMaintenanceDialog.getCopyDialog(getShell(), getManager());
        dialog.setValue(values);

        if (dialog.open() == Dialog.OK) {
            getManager().book();

            values = dialog.getValue();
            IRapidFireLibraryResource newLibrary = library.getParentSubSystem().getLibrary(library.getParentResource(), values.getKey().getLibrary(),
                getShell());
            if (newLibrary != null) {
                newLibrary.setParentNode(library.getParentNode());
                refreshUICreated(newLibrary.getParentSubSystem(), newLibrary, newLibrary.getParentNode());
            } else {
                MessageDialogAsync.displayError(Messages.Could_not_copy_resource_Resource_not_found);
            }
        }
    }
}
