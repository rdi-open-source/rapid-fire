/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.job;

import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.Dialog;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.dialogs.MessageDialogAsync;
import biz.rapidfire.core.dialogs.maintenance.job.JobMaintenanceDialog;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.job.JobValues;
import biz.rapidfire.core.maintenance.job.shared.JobAction;
import biz.rapidfire.core.model.IRapidFireJobResource;

public class NewJobHandler extends AbstractJobMaintenanceHandler implements IHandler {

    public NewJobHandler() {
        super(MaintenanceMode.CREATE, JobAction.CREATE);
    }

    @Override
    protected void performAction(IRapidFireJobResource job) throws Exception {

        JobValues values = getManager().getValues();

        JobMaintenanceDialog dialog = JobMaintenanceDialog.getCreateDialog(getShell(), getManager());
        dialog.setValue(values);

        if (dialog.open() == Dialog.OK) {
            getManager().book();

            values = dialog.getValue();
            IRapidFireJobResource newJob = job.getParentSubSystem().getJob(job.getDataLibrary(), values.getKey().getJobName(), getShell());
            if (newJob != null) {
                newJob.setFilter(job.getFilter());
                refreshUICreated(newJob.getParentSubSystem(), newJob, newJob.getParentFilters());
            } else {
                MessageDialogAsync.displayError(Messages.Could_not_create_resource_Resource_not_found);
            }
        }
    }
}
