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
import org.eclipse.swt.custom.BusyIndicator;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.dialogs.maintenance.filecopyprogramgenerator.FileCopyProgramGeneratorMaintenanceDialog;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.Result;
import biz.rapidfire.core.maintenance.Success;
import biz.rapidfire.core.maintenance.filecopyprogramgenerator.shared.FileCopyProgramGeneratorAction;
import biz.rapidfire.core.model.IRapidFireFileResource;
import biz.rapidfire.rsebase.helpers.SystemConnectionHelper;

public class GenerateFileCopyProgramHandler extends AbstractProgramGeneratorHandler implements IHandler {

    public GenerateFileCopyProgramHandler() {
        super(MaintenanceMode.CREATE, FileCopyProgramGeneratorAction.CREATE);
    }

    @Override
    protected void performAction(IRapidFireFileResource file) throws Exception {

        int rc = 0;

        final BusyResult busyResult = new BusyResult();

        do {

            FileCopyProgramGeneratorMaintenanceDialog dialog = FileCopyProgramGeneratorMaintenanceDialog.getCreateDialog(getShell(), getManager());
            dialog.setConnectionName(file.getParentSubSystem().getConnectionName());
            dialog.setAreas(file.getParentSubSystem().getAreas(file, getShell()));
            dialog.setConversionProgramName(file.getConversionProgramLibrary(), file.getConversionProgramName());

            rc = dialog.open();
            if (rc == Dialog.OK) {

                BusyIndicator.showWhile(getShell().getDisplay(), new Runnable() {

                    public void run() {
                        try {

                            busyResult.setResult(getManager().book());

                        } catch (Exception e) {
                            RapidFireCorePlugin.logError("*** Unexpected exception when generating the copy program ***", e); //$NON-NLS-1$
                            String message = ExceptionHelper.getLocalizedMessage(e);
                            busyResult.setResult(new Result(Success.NO.label(), message));
                        }
                    }
                });

                if (busyResult.isError()) {
                    if (!MessageDialog.openQuestion(getShell(), Messages.E_R_R_O_R,
                        Messages.Question_Could_not_generate_copy_program_Do_you_want_to_try_again)) {
                        rc = Dialog.CANCEL;
                    }
                } else {
                    if (dialog.isOpenMember()) {
                        String connectionName = dialog.getConnectionName();
                        String sourceFile = dialog.getSourceFile();
                        String sourceFileLibrary = dialog.getSourceFileLibrary();
                        String sourceMember = dialog.getSourceMember();
                        SystemConnectionHelper.openMember(connectionName, sourceFileLibrary, sourceFile, sourceMember);
                    }
                    MessageDialog.openInformation(getShell(), Messages.DialogFile_Copy_Program_Generator,
                        Messages.Copy_program_successfully_generated);
                }
            }

        } while (busyResult.isError() && rc == Dialog.OK);

    }

    private class BusyResult {

        private Result result;

        public BusyResult() {
            this.result = Result.createSuccessResult();
        }

        public boolean isError() {
            return result.isError();
        }

        public void setResult(Result result) {
            this.result = result;
        }
    }
}
