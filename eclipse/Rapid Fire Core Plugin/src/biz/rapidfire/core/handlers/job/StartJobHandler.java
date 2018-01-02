/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.job;

import biz.rapidfire.core.dialogs.action.ConfirmActionDialog;
import biz.rapidfire.core.maintenance.job.shared.JobAction;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.rsebase.helpers.SystemConnectionHelper;

public class StartJobHandler extends AbstractJobActionHandler {

    public StartJobHandler() {
        super(JobAction.STRJOB);
    }

    @Override
    protected void performAction(IRapidFireJobResource job) throws Exception {

        ConfirmActionDialog dialog = ConfirmActionDialog.open(getShell(), getJobAction(), job.getName());
        if (dialog.isConfirmed()) {
            getManager().testJob(job.getKey());

            job.reload(getShell());
            SystemConnectionHelper.refreshUIChanged(job.getParentSubSystem(), job, job.getParentFilters());
        }
    }
}
