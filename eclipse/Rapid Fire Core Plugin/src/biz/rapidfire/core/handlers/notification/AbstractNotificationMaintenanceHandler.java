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
import biz.rapidfire.core.handlers.AbstractResourceMaintenanceHandler;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.IRapidFireNotificationResource;
import biz.rapidfire.core.model.IRapidFireResource;
import biz.rapidfire.core.model.dao.JDBCConnectionManager;
import biz.rapidfire.core.model.maintenance.MaintenanceMode;
import biz.rapidfire.core.model.maintenance.Result;
import biz.rapidfire.core.model.maintenance.job.shared.JobKey;
import biz.rapidfire.core.model.maintenance.notification.NotificationManager;
import biz.rapidfire.core.model.maintenance.notification.shared.NotificationAction;
import biz.rapidfire.core.model.maintenance.notification.shared.NotificationKey;

public abstract class AbstractNotificationMaintenanceHandler extends
    AbstractResourceMaintenanceHandler<IRapidFireNotificationResource, NotificationAction> {

    private NotificationManager manager;
    private NotificationAction notificationAction;

    public AbstractNotificationMaintenanceHandler(MaintenanceMode mode, NotificationAction notificationAction) {
        super(mode);

        this.notificationAction = notificationAction;
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
            manager = getOrCreateManager(notification.getParentJob());

            if (canExecuteAction(notification, notificationAction)) {
                String message = initialize(notification);
                if (message != null) {
                    MessageDialog.openError(getShell(), Messages.E_R_R_O_R, message);
                } else {
                    performAction(notification);
                }
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

    private NotificationManager getOrCreateManager(IRapidFireJobResource job) throws Exception {

        if (manager == null) {
            String connectionName = job.getParentSubSystem().getConnectionName();
            String dataLibrary = job.getDataLibrary();
            boolean commitControl = isCommitControl();
            manager = new NotificationManager(JDBCConnectionManager.getInstance().getConnection(connectionName, dataLibrary, commitControl));
        }

        return manager;
    }

    protected boolean canExecuteAction(IRapidFireNotificationResource notification, NotificationAction notificationAction) {

        String message = null;

        try {

            // TODO: check action!
            Result result = manager.checkAction(notification.getKey(), notificationAction);
            if (result.isSuccessfull()) {
                return true;
            } else {
                // TODO: fix message
                message = Messages.bindParameters(Messages.The_requested_operation_is_invalid_for_job_status_A, notification.getParentJob()
                    .getStatus().label);
            }

        } catch (Exception e) {
            message = "*** Could not check job action. Failed creating the job manager ***";
            RapidFireCorePlugin.logError(message, e); //$NON-NLS-1$
        }

        if (message != null) {
            MessageDialog.openError(getShell(), Messages.E_R_R_O_R, message);
        }

        return false;
    }

    private String initialize(IRapidFireNotificationResource notification) throws Exception {

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

    private void logError(Throwable e) {
        RapidFireCorePlugin.logError("*** Could not handle Rapid Fire notification resource request ***", e); //$NON-NLS-1$
        MessageDialog.openError(getShell(), Messages.E_R_R_O_R, ExceptionHelper.getLocalizedMessage(e));
    }
}
