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

import biz.rapidfire.core.dialogs.maintenance.library.LibraryMaintenanceDialog;
import biz.rapidfire.core.model.IRapidFireLibraryResource;
import biz.rapidfire.core.model.maintenance.IMaintenance;
import biz.rapidfire.core.model.maintenance.library.LibraryValues;
import biz.rapidfire.core.model.maintenance.library.shared.LibraryAction;

public class NewLibraryHandler extends AbstractLibraryMaintenanceHandler implements IHandler {

    public NewLibraryHandler() {
        super(IMaintenance.MODE_CREATE, LibraryAction.CREATE);
    }

    @Override
    protected void performAction(IRapidFireLibraryResource file) throws Exception {

        LibraryValues values = getManager().getValues();

        LibraryMaintenanceDialog dialog = LibraryMaintenanceDialog.getCreateDialog(getShell(), getManager());
        dialog.setValue(values);

        if (dialog.open() == Dialog.OK) {
            getManager().book();
            refreshUI(file);
        }
    }
}
