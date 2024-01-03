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
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.job.shared.JobAction;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.view.FileCopyStatusView;

public class DisplayCopyStatusHandler extends AbstractJobMaintenanceHandler implements IHandler {

    public DisplayCopyStatusHandler() {
        super(MaintenanceMode.DISPLAY, JobAction.DSPSTS);
    }

    @Override
    protected void performAction(IRapidFireJobResource job) throws Exception {

        try {

            FileCopyStatusView view = (FileCopyStatusView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                .showView(FileCopyStatusView.ID);
            view.setInput(job);

        } catch (PartInitException e) {
            RapidFireCorePlugin.logError("*** Could not open job status view ***", e);
            MessageDialog.openError(getShell(), Messages.E_R_R_O_R, ExceptionHelper.getLocalizedMessage(e));
        } catch (Exception e) {
            RapidFireCorePlugin.logError("*** Could load job statuses ***", e);
            MessageDialog.openError(getShell(), Messages.E_R_R_O_R, ExceptionHelper.getLocalizedMessage(e));
        }
    }
}
