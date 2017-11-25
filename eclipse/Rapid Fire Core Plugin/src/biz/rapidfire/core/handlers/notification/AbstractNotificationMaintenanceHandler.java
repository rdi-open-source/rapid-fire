/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.notification;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.handlers.AbstractResourceHandler;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.model.IRapidFireNotificationResource;
import biz.rapidfire.core.model.IRapidFireResource;
import biz.rapidfire.core.model.dao.JDBCConnectionManager;
import biz.rapidfire.core.model.maintenance.IMaintenance;
import biz.rapidfire.core.model.maintenance.Result;
import biz.rapidfire.core.model.maintenance.job.JobKey;
import biz.rapidfire.core.model.maintenance.notification.NotificationKey;
import biz.rapidfire.core.model.maintenance.notification.NotificationManager;

public abstract class AbstractNotificationMaintenanceHandler extends AbstractResourceHandler {

    private NotificationManager manager;

    public AbstractNotificationMaintenanceHandler(String mode) {
        super(mode);
    }

    protected NotificationManager getManager() {
        return manager;
    }

    @Override
    protected Object executeWithResource(IRapidFireResource resource) throws ExecutionException {

        if (!(resource instanceof IRapidFireNotificationResource)) {
            return null;
        }

        try {

            IRapidFireNotificationResource notification = (IRapidFireNotificationResource)resource;

            String message = initialize(notification);
            if (message != null) {
                MessageDialog.openError(getShell(), Messages.E_R_R_O_R, message);
            } else {
                performAction(notification);
            }

        } catch (Throwable e) {
            logError(e);
        } finally {
            try {
                terminate();
            } catch (Throwable e) {
                logError(e);
            }
        }

        return null;
    }

    private String initialize(IRapidFireNotificationResource notification) throws Exception {

        String connectionName = notification.getParentSubSystem().getConnectionName();
        String dataLibrary = notification.getDataLibrary();
        boolean commitControl = isCommitControl();

        manager = new NotificationManager(JDBCConnectionManager.getInstance().getConnection(connectionName, dataLibrary, commitControl));
        manager.openFiles();

        Result status = manager.initialize(getMode(), new NotificationKey(new JobKey(notification.getJob()), notification.getPosition()));
        if (status.isError()) {
            return status.getMessage();
        }

        return null;
    }

    protected abstract void performAction(IRapidFireNotificationResource notification) throws Exception;

    private void terminate() throws Exception {

        if (manager != null) {
            manager.closeFiles();
            manager = null;
        }
    }

    private boolean isCommitControl() {

        String mode = getMode();
        if (IMaintenance.MODE_CHANGE.equals(mode) || IMaintenance.MODE_DELETE.equals(mode)) {
            return true;
        }

        return false;
    }

    private void logError(Throwable e) {
        RapidFireCorePlugin.logError("*** Could not handle Rapid Fire notification resource request ***", e); //$NON-NLS-1$
        MessageDialog.openError(getShell(), Messages.E_R_R_O_R, ExceptionHelper.getLocalizedMessage(e));
    }
}
