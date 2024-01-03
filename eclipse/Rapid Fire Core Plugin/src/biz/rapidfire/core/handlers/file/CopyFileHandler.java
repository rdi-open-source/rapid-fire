/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.file;

import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.Dialog;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.dialogs.MessageDialogAsync;
import biz.rapidfire.core.dialogs.maintenance.file.FileMaintenanceDialog;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.file.FileValues;
import biz.rapidfire.core.maintenance.file.shared.FileAction;
import biz.rapidfire.core.model.IRapidFireFileResource;

public class CopyFileHandler extends AbstractFileMaintenanceHandler implements IHandler {

    public CopyFileHandler() {
        super(MaintenanceMode.COPY, FileAction.COPY);
    }

    @Override
    protected void performAction(IRapidFireFileResource file) throws Exception {

        FileValues values = getManager().getValues();

        FileMaintenanceDialog dialog = FileMaintenanceDialog.getCopyDialog(getShell(), getManager());
        dialog.setValue(values);

        if (dialog.open() == Dialog.OK) {
            getManager().book();

            values = dialog.getValue();
            IRapidFireFileResource newFile = file.getParentSubSystem().getFile(file.getParentResource(), values.getKey().getPosition(), getShell());
            if (newFile != null) {
                newFile.setParentNode(file.getParentNode());
                refreshUICreated(newFile.getParentSubSystem(), newFile, newFile.getParentNode());
            } else {
                MessageDialogAsync.displayError(Messages.Could_not_copy_resource_Resource_not_found);
            }
        }
    }
}
