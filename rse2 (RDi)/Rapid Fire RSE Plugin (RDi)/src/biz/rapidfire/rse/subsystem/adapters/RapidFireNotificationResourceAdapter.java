/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem.adapters;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.rse.ui.view.ISystemRemoteElementAdapter;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import biz.rapidfire.core.maintenance.notification.shared.NotificationType;
import biz.rapidfire.core.model.IRapidFireNotificationResource;
import biz.rapidfire.rse.Messages;
import biz.rapidfire.rse.RapidFireRSEPlugin;
import biz.rapidfire.rse.subsystem.resources.RapidFireNotificationResource;

public class RapidFireNotificationResourceAdapter extends AbstractResourceAdapter<IRapidFireNotificationResource> implements
    ISystemRemoteElementAdapter {

    private static final String DATA_LIBRARY = "DATA_LIBRARY"; //$NON-NLS-1$
    private static final String JOB = "JOB"; //$NON-NLS-1$
    private static final String POSITION = "POSITION"; //$NON-NLS-1$
    private static final String NOTIFICATION_TYPE = "NOTIFICATION_TYPE"; //$NON-NLS-1$
    private static final String USER = "USER"; //$NON-NLS-1$
    private static final String MESSAGE_QUEUE_NAME = "MESSAGE_QUEUE_NAME"; //$NON-NLS-1$
    private static final String MESSAGE_QUEUE_LIBRARY = "MESSAGE_QUEUE_LIBRARY"; //$NON-NLS-1$

    public RapidFireNotificationResourceAdapter() {
        super();
    }

    @Override
    public ImageDescriptor getImageDescriptor(Object element) {

        RapidFireNotificationResource resource = getResource(element);

        if (resource.getNotificationType() == NotificationType.MSGQ) {
            return RapidFireRSEPlugin.getDefault().getImageRegistry().getDescriptor(RapidFireRSEPlugin.IMAGE_MESSAGE_QUEUE);
        } else {
            return RapidFireRSEPlugin.getDefault().getImageRegistry().getDescriptor(RapidFireRSEPlugin.IMAGE_USER);
        }
    }

    @Override
    public boolean handleDoubleClick(Object object) {
        return false;
    }

    /**
     * Returns the name of the resource, e.g. for showing the name in the status
     * line.
     */
    @Override
    public String getText(Object element) {

        RapidFireNotificationResource resource = getResource(element);

        StringBuilder text = new StringBuilder();

        if (NotificationType.USR.equals(resource.getNotificationType())) {
            text.append(resource.getUser());
        } else {
            text.append(resource.getMessageQueueLibrary());
            text.append("/"); //$NON-NLS-1$
            text.append(resource.getMessageQueueName());
        }

        return text.toString();
    }

    /**
     * Returns the absolute name of the resource for building hash values.
     */
    @Override
    public String getAbsoluteName(Object element) {

        RapidFireNotificationResource resource = getResource(element);

        String name = getAbsoluteNamePrefix() + resource.getDataLibrary() + "." + resource.getJob() + "." + resource.getPosition(); //$NON-NLS-1$ //$NON-NLS-2$

        return name;
    }

    @Override
    public String getAbsoluteNamePrefix() {
        return "RapidFireNotification."; //$NON-NLS-1$
    }

    /**
     * Returns the type of the resource. Used for qualifying the name of the
     * resource, when displayed on the status line.
     */
    @Override
    public String getType(Object element) {
        return Messages.Resource_Rapid_Fire_Notification;
    }

    @Override
    public String getRemoteType(Object element) {
        return "notification";
    }

    @Override
    public Object getParent(Object element) {
        return null;
    }

    @Override
    public boolean hasChildren(IAdaptable element) {
        return false;
    }

    @Override
    public Object[] getChildren(IAdaptable element, IProgressMonitor progressMonitor) {
        return new Object[0];
    }

    @Override
    protected IPropertyDescriptor[] internalGetPropertyDescriptors() {

        PropertyDescriptor[] ourPDs = new PropertyDescriptor[7];
        ourPDs[0] = new PropertyDescriptor(DATA_LIBRARY, Messages.DataLibrary_name);
        ourPDs[0].setDescription(Messages.Tooltip_DataLibrary_name);
        ourPDs[1] = new PropertyDescriptor(JOB, Messages.Job_name);
        ourPDs[1].setDescription(Messages.Tooltip_Job_name);
        ourPDs[2] = new PropertyDescriptor(POSITION, Messages.Position);
        ourPDs[2].setDescription(Messages.Tooltip_Position);
        ourPDs[3] = new PropertyDescriptor(USER, Messages.User_name);
        ourPDs[3].setDescription(Messages.Tooltip_User_name);
        ourPDs[4] = new PropertyDescriptor(NOTIFICATION_TYPE, Messages.NotificationType);
        ourPDs[4].setDescription(Messages.Tooltip_NotificationType);
        ourPDs[5] = new PropertyDescriptor(MESSAGE_QUEUE_NAME, Messages.Copy_program_name);
        ourPDs[5].setDescription(Messages.Tooltip_Copy_program_name);
        ourPDs[6] = new PropertyDescriptor(MESSAGE_QUEUE_LIBRARY, Messages.Copy_program_library);
        ourPDs[6].setDescription(Messages.Tooltip_Copy_program_library);

        return ourPDs;
    }

    @Override
    public Object internalGetPropertyValue(Object propKey) {

        final IRapidFireNotificationResource resource = (IRapidFireNotificationResource)propertySourceInput;

        if (propKey.equals(DATA_LIBRARY)) {
            return resource.getDataLibrary();
        } else if (propKey.equals(JOB)) {
            return resource.getJob();
        } else if (propKey.equals(POSITION)) {
            return resource.getPosition();
        } else if (propKey.equals(USER)) {
            return resource.getUser();
        } else if (propKey.equals(NOTIFICATION_TYPE)) {
            return resource.getNotificationType().label();
        } else if (propKey.equals(MESSAGE_QUEUE_NAME)) {
            return resource.getMessageQueueName();
        } else if (propKey.equals(MESSAGE_QUEUE_LIBRARY)) {
            return resource.getMessageQueueLibrary();
        }
        return null;
    }

    private RapidFireNotificationResource getResource(Object element) {
        return (RapidFireNotificationResource)element;
    }
}