/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.MessageDialog;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.IRapidFireResource;
import biz.rapidfire.core.model.maintenance.CheckStatus;
import biz.rapidfire.core.model.maintenance.job.JobKey;
import biz.rapidfire.core.model.maintenance.job.JobManager;
import biz.rapidfire.core.model.maintenance.job.JobValues;

public class ChangeJobHandler extends AbstractJobHandler implements IHandler {

    private static final String MODE = JobManager.MODE_CHANGE;

    private JobManager manager;

    public ChangeJobHandler() {
        super();
    }

    @Override
    protected Object executeWithResource(IRapidFireResource resource) throws ExecutionException {

        if (!(resource instanceof IRapidFireJobResource)) {
            return null;
        }

        CheckStatus status = null;
        IRapidFireJobResource job = (IRapidFireJobResource)resource;

        try {

            String message = initialize(job);
            if (message != null) {
                MessageDialog.openError(getShell(), Messages.E_R_R_O_R, message);
            } else {

                JobValues values = manager.getValues();

                // TODO: open dialog and get values

                manager.setValues(values);
                status = manager.check();
                if (status.isSuccessfull()) {
                    manager.book();
                }
            }

        } catch (Exception e) {
            RapidFireCorePlugin.logError("*** Could not change Rapid Fire job resource ***", e);
        } finally {
            terminate();
            if (status != null && status.isError()) {
                MessageDialog.openError(getShell(), Messages.E_R_R_O_R, status.getMessage());
            }
        }

        return null;
    }

    private String initialize(IRapidFireJobResource job) {

        String connectionName = job.getParentSubSystem().getConnectionName();

        try {

            manager = job.getParentSubSystem().getJobManager(connectionName, job.getDataLibrary(), true);
            manager.openFiles();

        } catch (Exception e) {
            RapidFireCorePlugin.logError("*** Could not open files of 'Job' resource ***", e);
            return "Could not open files of 'Job' resource.";
        }

        try {

            CheckStatus status = manager.initialize(MODE, new JobKey(job.getName()));
            if (status.isError()) {
                return status.getMessage();
            }

        } catch (Exception e) {
            RapidFireCorePlugin.logError("*** Could not initialize 'Job Manager' for mode '" + MODE + "' ***", e);
            return Messages.bind("Could not initialize 'Job Manager' for mode ''{0}''.", MODE);
        }

        return null;
    }

    private void terminate() {

        try {

            if (manager != null) {
                manager.closeFiles();
            }

        } catch (Exception e) {
            RapidFireCorePlugin.logError("*** Could not close files of job resource ***", e);
        }
    }
}
