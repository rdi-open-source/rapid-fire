/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem.actions;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.rse.core.subsystems.ISubSystem;
import org.eclipse.rse.ui.actions.SystemBaseAction;
import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.handlers.notification.NewNotificationHandler;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.rse.Messages;
import biz.rapidfire.rse.RapidFireRSEPlugin;
import biz.rapidfire.rse.subsystem.resources.NotificationsNode;
import biz.rapidfire.rse.subsystem.resources.RapidFireNotificationResource;

public class NewNotificationAction extends SystemBaseAction {

    public NewNotificationAction(Shell shell) {
        super(Messages.ActionLabel_New_Notification, Messages.ActionTooltip_New_Notification, shell);

        setImageDescriptor(RapidFireRSEPlugin.getDefault().getImageRegistry().getDescriptor(RapidFireRSEPlugin.IMAGE_NEW_NOTIFICATION));
        setContextMenuGroup("group.new");
    }

    @Override
    public void run() {

        try {

            Object element = getFirstSelection();
            NotificationsNode notificationNode = (NotificationsNode)element;
            IRapidFireJobResource job = notificationNode.getJob();

            RapidFireNotificationResource notification = RapidFireNotificationResource.createEmptyInstance(job);
            notification.setSubSystem((ISubSystem)job.getParentSubSystem());

            NewNotificationHandler handler = new NewNotificationHandler();
            IStructuredSelection selection = new StructuredSelection(notification);
            handler.executeWithSelection(selection);

        } catch (Exception e) {
            RapidFireCorePlugin.logError("*** Could not execute 'New Notification' handler ***", e); //$NON-NLS-1$
        }

    }
}
