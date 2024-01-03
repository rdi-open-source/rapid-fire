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
import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.handlers.notification.NewNotificationHandler;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.IRapidFireNotificationResource;
import biz.rapidfire.rse.Messages;
import biz.rapidfire.rse.RapidFireRSEPlugin;
import biz.rapidfire.rse.subsystem.resources.NotificationsNode;
import biz.rapidfire.rse.subsystem.resources.RapidFireNotificationResource;

public class NewNotificationAction extends AbstractNewNodePopupMenuAction<NotificationsNode, IRapidFireNotificationResource> {

    public NewNotificationAction(Shell shell) {
        super(Messages.ActionLabel_New_Notification, Messages.ActionTooltip_New_Notification, shell, new NewNotificationHandler());

        setImageDescriptor(RapidFireRSEPlugin.getDefault().getImageRegistry().getDescriptor(RapidFireRSEPlugin.IMAGE_NEW_NOTIFICATION));
        setContextMenuGroup("group.new");
    }

    @Override
    public void run() {

        try {

            Object element = getFirstSelection();
            NotificationsNode notificationNode = (NotificationsNode)element;
            IRapidFireJobResource job = notificationNode.getParentResource();

            RapidFireNotificationResource notification = RapidFireNotificationResource.createEmptyInstance(job);
            notification.setParentNode(notificationNode);

            IStructuredSelection selection = new StructuredSelection(notification);
            getHandler().executeWithSelection(selection);

        } catch (Exception e) {
            RapidFireCorePlugin.logError("*** Could not execute 'New Notification' handler ***", e); //$NON-NLS-1$
        }

    }

    @Override
    protected IRapidFireNotificationResource createNewResource(NotificationsNode node) {
        return RapidFireNotificationResource.createEmptyInstance(node.getParentResource());
    }

    @Override
    protected NotificationsNode getResource(Object object) {

        if (object instanceof NotificationsNode) {
            return (NotificationsNode)object;
        }

        return null;
    }
}
