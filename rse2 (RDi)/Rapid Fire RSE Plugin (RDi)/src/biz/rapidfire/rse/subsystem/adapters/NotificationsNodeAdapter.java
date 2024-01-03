/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem.adapters;

import java.util.Arrays;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.rse.ui.SystemMenuManager;
import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.dialogs.MessageDialogAsync;
import biz.rapidfire.core.exceptions.AutoReconnectErrorException;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.IRapidFireNotificationResource;
import biz.rapidfire.rse.Messages;
import biz.rapidfire.rse.RapidFireRSEPlugin;
import biz.rapidfire.rse.subsystem.actions.NewNotificationAction;
import biz.rapidfire.rse.subsystem.resources.NotificationsNode;

public class NotificationsNodeAdapter extends AbstractNodeAdapter<NotificationsNode> {

    @Override
    public final boolean hasChildren(IAdaptable element) {
        return true;
    }

    @Override
    public String getText(Object element) {
        return Messages.NodeText_Notifications;
    }

    @Override
    protected String getAbsoluteNamePrefix() {
        return "node.notifications.";
    }

    @Override
    public void addActions(SystemMenuManager menuManager, IStructuredSelection selection, Shell shell, String menuGroup) {
        menuManager.add(null, new NewNotificationAction(shell));
    }

    @Override
    public ImageDescriptor getImageDescriptor(Object element) {
        return RapidFireRSEPlugin.getDefault().getImageRegistry().getDescriptor(RapidFireRSEPlugin.IMAGE_NOTIFICATION);
    }

    @Override
    public Object[] getChildren(IAdaptable element, IProgressMonitor progressMonitor) {

        try {

            NotificationsNode notificationsNode = (NotificationsNode)element;
            IRapidFireJobResource jobResource = notificationsNode.getParentResource();

            IRapidFireNotificationResource[] notifications = jobResource.getParentSubSystem().getNotifications(jobResource, getShell());
            for (IRapidFireNotificationResource notification : notifications) {
                notification.setParentNode(notificationsNode);
            }

            Arrays.sort(notifications);

            return notifications;

        } catch (AutoReconnectErrorException e) {
            MessageDialogAsync.displayError(e.getLocalizedMessage());
        } catch (Exception e) {
            RapidFireCorePlugin.logError("*** Could resolve filter string and load notifications ***", e); //$NON-NLS-1$
            MessageDialogAsync.displayError(e.getLocalizedMessage());
        }

        return new Object[0];
    }

    @Override
    public String getType(Object element) {
        return Messages.NodeType_Notifications;
    }

    @Override
    public String getRemoteType(Object element) {
        return "node.notifications"; //$NON-NLS-1$
    }

}