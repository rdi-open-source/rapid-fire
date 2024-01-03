/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.job;

import org.eclipse.core.commands.IHandler;

import biz.rapidfire.core.dialogs.maintenance.job.JobMaintenanceDialog;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.job.JobValues;
import biz.rapidfire.core.maintenance.job.shared.JobAction;
import biz.rapidfire.core.model.IRapidFireJobResource;

public class DisplayJobHandler extends AbstractJobMaintenanceHandler implements IHandler {

    public DisplayJobHandler() {
        super(MaintenanceMode.DISPLAY, JobAction.DISPLAY);
    }

    @Override
    protected void performAction(IRapidFireJobResource job) throws Exception {

        JobValues values = getManager().getValues();

        JobMaintenanceDialog dialog = JobMaintenanceDialog.getDisplayDialog(getShell(), getManager());
        dialog.setValue(values);

        dialog.open();
    }
}
