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
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.maintenance.IMaintenance;
import biz.rapidfire.core.model.maintenance.activity.ActivityValues;

public class ChangeActivitiesHandler extends AbstractActivityMaintenanceHandler implements IHandler {

    public ChangeActivitiesHandler() {
        super(IMaintenance.MODE_CHANGE);
    }

    @Override
    protected void performAction(IRapidFireJobResource job) throws Exception {

        ActivityValues[] values = getManager().getValues(job, getShell());

        ActivityMaintenanceDialog dialog = ActivityMaintenanceDialog.getChangeDialog(getShell(), getManager());
        dialog.setValue(values);

        if (dialog.open() == Dialog.OK) {
            getManager().book();
            refreshUI(job);
        }
    }
}
