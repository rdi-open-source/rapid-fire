/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.activity;

import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.Dialog;

import biz.rapidfire.core.dialogs.maintenance.activity.ActivityMaintenanceDialog;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.activity.ActivityValues;
import biz.rapidfire.core.maintenance.activity.shared.ActivityAction;
import biz.rapidfire.core.model.IRapidFireJobResource;

public class ChangeActivitiesHandler extends AbstractActivityMaintenanceHandler implements IHandler {

    public ChangeActivitiesHandler() {
        super(MaintenanceMode.CHANGE, ActivityAction.CHANGE);
    }

    @Override
    protected void performAction(IRapidFireJobResource job) throws Exception {

        ActivityValues[] values = getManager().getValues(job, getShell());

        ActivityMaintenanceDialog dialog;
        if (getMaintenanceMode() == MaintenanceMode.CHANGE) {
            dialog = ActivityMaintenanceDialog.getChangeDialog(getShell(), getManager());
            dialog.setValue(values);
            openDialog(dialog, job, true); // Book changes.
        } else {
            dialog = ActivityMaintenanceDialog.getDisplayDialog(getShell(), getManager());
            dialog.setValue(values);
            openDialog(dialog, job, false); // Nothing to update here.
        }
    }

    private void openDialog(ActivityMaintenanceDialog dialog, IRapidFireJobResource job, boolean isBookingEnabled) throws Exception {

        if (dialog.open() == Dialog.OK) {
            if (isBookingEnabled) {
                getManager().book();
            }
            refreshUI(job);
        }
    }
}
