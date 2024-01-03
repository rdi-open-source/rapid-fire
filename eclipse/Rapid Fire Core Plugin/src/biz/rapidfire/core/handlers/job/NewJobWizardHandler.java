/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.job;

import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;

import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.job.shared.JobAction;
import biz.rapidfire.core.maintenance.job.wizard.NewJobWizard;
import biz.rapidfire.core.model.IRapidFireJobResource;

public class NewJobWizardHandler extends AbstractJobMaintenanceHandler implements IHandler {

    public NewJobWizardHandler() {
        super(MaintenanceMode.CREATE, JobAction.CREATE);
    }

    @Override
    protected void performAction(IRapidFireJobResource job) throws Exception {

        WizardDialog wizardDialog = new WizardDialog(getShell(), new NewJobWizard());
        if (wizardDialog.open() == Window.OK) {
            System.out.println("Ok pressed");
        } else {
            System.out.println("Cancel pressed");
        }
    }
}
