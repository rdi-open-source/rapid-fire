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
import org.eclipse.jface.dialogs.MessageDialog;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.dialogs.maintenance.filecopyprogramgenerator.FileCopyProgramGeneratorMaintenanceDialog;
import biz.rapidfire.core.helpers.RapidFireHelper;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.Result;
import biz.rapidfire.core.maintenance.filecopyprogramgenerator.shared.FileCopyProgramGeneratorAction;
import biz.rapidfire.core.model.IRapidFireFileResource;

public class GenerateFileCopyProgramHandler extends AbstractProgramGeneratorHandler implements IHandler {

    public GenerateFileCopyProgramHandler() {
        super(MaintenanceMode.CREATE, FileCopyProgramGeneratorAction.CREATE);
    }

    @Override
    protected void performAction(IRapidFireFileResource file) throws Exception {

        int rc = 0;

        Result result = null;

        do {

            FileCopyProgramGeneratorMaintenanceDialog dialog = FileCopyProgramGeneratorMaintenanceDialog.getCreateDialog(getShell(), getManager());
            dialog.setConnectionName(file.getParentSubSystem().getConnectionName());

            rc = dialog.open();
            if (rc == Dialog.OK) {

                // TODO: implement busy indicator
                // BusyIndicator.showWhile(getShell().getDisplay(), new
                // Runnable() {
                //
                // public void run() {
                result = getManager().book();
                // }
                // });

                if (result.isError()) {
                    if (!MessageDialog
                        .openQuestion(getShell(), Messages.E_R_R_O_R, Messages.Could_not_generate_copy_program_Do_you_want_to_try_again)) {
                        rc = Dialog.CANCEL;
                    }
                } else {
                    if (dialog.isOpenMember()) {
                        String connectionName = dialog.getConnectionName();
                        String sourceFile = dialog.getSourceFile();
                        String sourceFileLibrary = dialog.getSourceFileLibrary();
                        String sourceMember = dialog.getSourceMember();
                        RapidFireHelper.openMember(connectionName, sourceFileLibrary, sourceFile, sourceMember);
                    }
                }

                MessageDialog.openInformation(getShell(), Messages.Dialog_File_Copy_Program_Generator, Messages.Copy_program_successfully_generated);
            }

        } while (result != null && result.isError() && rc == Dialog.OK);

    }
}
