/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.job;

import org.eclipse.ui.PlatformUI;

import biz.rapidfire.core.maintenance.job.shared.JobAction;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.view.FileCopyStatusView;

public class RefreshJobStatusHandler extends AbstractJobActionHandler {

    public RefreshJobStatusHandler() {
        super(JobAction.RFRJOBSTS);
    }

    @Override
    protected void performAction(IRapidFireJobResource job) throws Exception {
        job.reload(getShell());
        refreshUIChanged(job.getParentSubSystem(), job, job.getParentFilters());

        FileCopyStatusView view = (FileCopyStatusView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
            .findView(FileCopyStatusView.ID);
        if (view != null) {
            view.setInput(job);
        }

    }
}
