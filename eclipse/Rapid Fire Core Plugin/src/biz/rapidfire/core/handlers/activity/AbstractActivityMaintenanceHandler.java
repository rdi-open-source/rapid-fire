/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.activity;

import org.eclipse.core.commands.ExecutionException;

import biz.rapidfire.core.handlers.AbstractResourceMaintenanceHandler;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.IRapidFireResource;
import biz.rapidfire.core.model.dao.JDBCConnectionManager;
import biz.rapidfire.core.model.maintenance.activity.ActivityManager;
import biz.rapidfire.core.model.maintenance.job.JobKey;

public abstract class AbstractActivityMaintenanceHandler extends AbstractResourceMaintenanceHandler {

    private ActivityManager manager;

    public AbstractActivityMaintenanceHandler(String mode) {
        super(mode);
    }

    protected ActivityManager getManager() {
        return manager;
    }

    @Override
    protected Object executeWithResource(IRapidFireResource resource) throws ExecutionException {

        if (!(resource instanceof IRapidFireJobResource)) {
            return null;
        }

        try {

            IRapidFireJobResource job = (IRapidFireJobResource)resource;

            initialize(job);
            performAction(job);

        } catch (Throwable e) {
            logError(e);
        } finally {
        }

        return null;
    }

    private void initialize(IRapidFireJobResource job) throws Exception {

        String connectionName = job.getParentSubSystem().getConnectionName();
        String dataLibrary = job.getDataLibrary();
        boolean commitControl = isCommitControl();

        manager = new ActivityManager(JDBCConnectionManager.getInstance().getConnection(connectionName, dataLibrary, commitControl));

        manager.initialize(getMode(), new JobKey(job.getName()));
    }

    protected abstract void performAction(IRapidFireJobResource job) throws Exception;

    private void logError(Throwable e) {
        logError("*** Could not handle Rapid Fire activity resource request ***", e); //$NON-NLS-1$
    }
}
