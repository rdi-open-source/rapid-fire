/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.reapplychanges;

import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.MessageDialog;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.reapplychanges.ReapplyChangesAction;
import biz.rapidfire.core.maintenance.reapplychanges.ReapplyChangesValues;
import biz.rapidfire.core.model.IFileCopyStatus;

public class ReapplyChangesHandler extends AbstractReapplyChangesHandler implements IHandler {

    public ReapplyChangesHandler() {
        super(MaintenanceMode.CHANGE, ReapplyChangesAction.REAPYCHG);
    }

    @Override
    protected void performAction(IFileCopyStatus fileCopyStatus) throws Exception {

        if (MessageDialog.openConfirm(getShell(), Messages.DialogTitle_Reapply_all_changes,
            Messages.bindParameters(Messages.Question_Do_you_want_to_reapply_all_changes_to_file_B_of_copy_job_A, fileCopyStatus.getJob().getName(),
                fileCopyStatus.getFile()))) {

            ReapplyChangesValues values = new ReapplyChangesValues();
            values.setJob(fileCopyStatus.getJob().getName());
            values.setPosition(fileCopyStatus.getPosition());
            values.setArea(fileCopyStatus.getArea());

            getManager().setValues(values);
            getManager().book();
        }
    }
}
