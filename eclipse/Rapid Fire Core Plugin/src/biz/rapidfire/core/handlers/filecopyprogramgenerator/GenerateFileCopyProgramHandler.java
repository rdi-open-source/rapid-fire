/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.filecopyprogramgenerator;

import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.Dialog;

import biz.rapidfire.core.dialogs.maintenance.filecopyprogramgenerator.FileCopyProgramGeneratorMaintenanceDialog;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.filecopyprogramgenerator.shared.FileCopyProgramGeneratorAction;
import biz.rapidfire.core.model.IRapidFireFileResource;

public class GenerateFileCopyProgramHandler extends AbstractProgramGeneratorHandler implements IHandler {

    public GenerateFileCopyProgramHandler() {
        super(MaintenanceMode.CREATE, FileCopyProgramGeneratorAction.CREATE);
    }

    @Override
    protected void performAction(IRapidFireFileResource file) throws Exception {

        FileCopyProgramGeneratorMaintenanceDialog dialog = FileCopyProgramGeneratorMaintenanceDialog.getCreateDialog(getShell(), getManager());
        dialog.setConnectionName(file.getParentSubSystem().getConnectionName());

        if (dialog.open() == Dialog.OK) {
            getManager().book();
            refreshUI(file);
        }
    }
}
