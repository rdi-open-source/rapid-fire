/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.reapplychanges;

import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.reapplychanges.ReapplyChangesAction;
import biz.rapidfire.core.maintenance.reapplychanges.ReapplyChangesValues;
import biz.rapidfire.core.model.IFileCopyStatus;

public class ReapplyChangesHandler extends AbstractReapplyChangesHandler implements IHandler {

    private boolean isYesToAll;

    public ReapplyChangesHandler() {
        super(MaintenanceMode.CHANGE, ReapplyChangesAction.REAPYCHG);
    }

    @Override
    protected void initializeHandler() {
        super.initializeHandler();

        isYesToAll = false;
    }

    @Override
    protected void performAction(IFileCopyStatus fileCopyStatus) throws Exception {

        final int YES = 0;
        final int YES_TO_ALL = 1;
        final int NO = 2;
        final int CANCEL = 3;

        int rc;
        if (!isYesToAll) {
            String title = Messages.DialogTitle_Reapply_all_changes;
            String question = Messages.bindParameters(Messages.Question_Do_you_want_to_reapply_all_changes_to_file_B_of_copy_job_A, fileCopyStatus
                .getJob().getName(), fileCopyStatus.getFile());
            String[] buttonLabels = new String[] { IDialogConstants.YES_LABEL, IDialogConstants.YES_TO_ALL_LABEL, IDialogConstants.NO_LABEL,
                IDialogConstants.CANCEL_LABEL };

            MessageDialog dialog = new MessageDialog(getShell(), title, null, question, MessageDialog.QUESTION, buttonLabels, CANCEL);
            rc = dialog.open();
        } else {
            rc = YES_TO_ALL;
        }

        if (rc == YES || rc == YES_TO_ALL) {

            ReapplyChangesValues values = new ReapplyChangesValues();
            values.setJob(fileCopyStatus.getJob().getName());
            values.setPosition(fileCopyStatus.getPosition());
            values.setArea(fileCopyStatus.getArea());

            getManager().setValues(values);
            getManager().book();

            if (rc == YES_TO_ALL) {
                isYesToAll = true;
            }

        } else if (rc == CANCEL) {
            cancel();
        }
    }
}
