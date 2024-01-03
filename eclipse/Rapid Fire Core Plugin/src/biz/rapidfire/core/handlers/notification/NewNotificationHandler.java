/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.notification;

import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.Dialog;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.dialogs.MessageDialogAsync;
import biz.rapidfire.core.dialogs.maintenance.notification.NotificationMaintenanceDialog;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.notification.NotificationValues;
import biz.rapidfire.core.maintenance.notification.shared.NotificationAction;
import biz.rapidfire.core.model.IRapidFireNotificationResource;

public class NewNotificationHandler extends AbstractNotificationMaintenanceHandler implements IHandler {

    public NewNotificationHandler() {
        super(MaintenanceMode.CREATE, NotificationAction.CREATE);
    }

    @Override
    protected void performAction(IRapidFireNotificationResource notification) throws Exception {

        NotificationValues values = getManager().getValues();

        NotificationMaintenanceDialog dialog = NotificationMaintenanceDialog.getCreateDialog(getShell(), getManager());
        dialog.setValue(values);

        if (dialog.open() == Dialog.OK) {
            getManager().book();

            values = dialog.getValue();
            IRapidFireNotificationResource newNotification = notification.getParentSubSystem().getNotification(notification.getParentResource(),
                values.getKey().getPosition(), getShell());
            if (newNotification != null) {
                newNotification.setParentNode(notification.getParentNode());
                refreshUICreated(newNotification.getParentSubSystem(), newNotification, newNotification.getParentNode());
            } else {
                MessageDialogAsync.displayError(Messages.Could_not_create_resource_Resource_not_found);
            }
        }
    }
}
