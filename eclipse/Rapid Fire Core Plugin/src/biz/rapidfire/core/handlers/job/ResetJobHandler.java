/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.job;

import biz.rapidfire.core.dialogs.action.ConfirmActionDialog;
import biz.rapidfire.core.maintenance.job.shared.DeleteShadowLibraries;
import biz.rapidfire.core.maintenance.job.shared.JobAction;
import biz.rapidfire.core.model.IRapidFireJobResource;

public class ResetJobHandler extends AbstractJobActionHandler {

    public ResetJobHandler() {
        super(JobAction.RESETJOB);
    }

    @Override
    protected void performAction(IRapidFireJobResource job) throws Exception {

        ConfirmActionDialog dialog = ConfirmActionDialog.open(getShell(), getJobAction(), job.getName());

        if (dialog.isConfirmed()) {
            if (dialog.isDeleteShadowLibrary()) {
                getManager().resetJob(job.getKey(), DeleteShadowLibraries.YES);
            } else {
                getManager().resetJob(job.getKey(), DeleteShadowLibraries.NO);
            }

            job.reload(getShell());
            refreshUIChanged(job.getParentSubSystem(), job, job.getParentFilters());
        }
    }
}
