/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.job;

import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.MessageDialog;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.maintenance.MaintenanceMode;
import biz.rapidfire.core.model.maintenance.job.shared.JobAction;

public class DisplayErrorHandler extends AbstractJobMaintenanceHandler implements IHandler {

    public DisplayErrorHandler() {
        super(MaintenanceMode.DISPLAY, JobAction.DSPERR);
    }

    @Override
    protected void performAction(IRapidFireJobResource job) throws Exception {

        if (job.isError()) {
            // TODO: get and display current error message
            MessageDialog.openError(getShell(), Messages.DialogTitle_Job_Error_Message, job.getErrorText());
        } else {
            MessageDialog.openInformation(getShell(), Messages.DialogTitle_Job_Error_Message, Messages.No_errors);
        }
    }
}
