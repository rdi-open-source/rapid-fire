/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.notification;

import org.eclipse.jface.dialogs.MessageDialog;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.handlers.AbstractResourceMaintenanceHandler;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.Result;
import biz.rapidfire.core.maintenance.job.shared.JobKey;
import biz.rapidfire.core.maintenance.notification.NotificationManager;
import biz.rapidfire.core.maintenance.notification.shared.NotificationAction;
import biz.rapidfire.core.maintenance.notification.shared.NotificationKey;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.IRapidFireNotificationResource;

public abstract class AbstractNotificationMaintenanceHandler extends
    AbstractResourceMaintenanceHandler<IRapidFireNotificationResource, NotificationAction> {

    private NotificationManager manager;
    private NotificationAction notificationAction;

    public AbstractNotificationMaintenanceHandler(MaintenanceMode mode, NotificationAction notificationAction) {
        super(mode, notificationAction);

        this.notificationAction = notificationAction;
    }

    protected NotificationManager getManager() {
        return manager;
    }

    protected NotificationManager getOrCreateManager(IRapidFireJobResource job) throws Exception {

        if (manager == null) {
            String connectionName = job.getParentSubSystem().getConnectionName();
            String dataLibrary = job.getDataLibrary();
            manager = new NotificationManager(getJdbcConnection(connectionName, dataLibrary));
        }

        return manager;
    }

    @Override
    protected boolean isValidAction(IRapidFireNotificationResource notification) throws Exception {
        return getOrCreateManager(notification.getParentJob()).isValidAction(notification, notificationAction);
    }

    @Override
    protected boolean isInstanceOf(Object object) {

        if (object instanceof IRapidFireNotificationResource) {
            return true;
        }

        return false;
    }

    @Override
    protected boolean canExecuteAction(IRapidFireNotificationResource notification, NotificationAction notificationAction) {

        String message = null;

        try {

            Result result;
            if (notificationAction == NotificationAction.CREATE) {
                result = getOrCreateManager(notification.getParentJob()).checkAction(
                    NotificationKey.createNew(notification.getParentResource().getKey()), notificationAction);
            } else {
                result = getOrCreateManager(notification.getParentJob()).checkAction(notification.getKey(), notificationAction);
            }
            if (result.isSuccessfull()) {
                return true;
            } else {
                message = Messages.bindParameters(Messages.The_requested_operation_is_invalid_for_job_status_A, notification.getParentJob()
                    .getStatus().label());
            }

        } catch (Exception e) {
            logError(e);
        }

        if (message != null) {
            MessageDialog.openError(getShell(), Messages.E_R_R_O_R, message);
        }

        return false;
    }

    @Override
    protected Result initialize(IRapidFireNotificationResource notification) throws Exception {

        manager.openFiles();

        Result result = manager.initialize(getMaintenanceMode(), new NotificationKey(new JobKey(notification.getJob()), notification.getPosition()));

        return result;
    }

    @Override
    protected void terminate(boolean closeConnection) throws Exception {

        if (manager != null) {
            manager.closeFiles();
            if (closeConnection) {
                manager.recoverError();
            }
            manager = null;
        }
    }
}
