/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem.resources;

import org.eclipse.rse.core.subsystems.AbstractResource;
import org.eclipse.rse.core.subsystems.ISubSystem;
import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.exceptions.IllegalParameterException;
import biz.rapidfire.core.maintenance.notification.shared.NotificationKey;
import biz.rapidfire.core.maintenance.notification.shared.NotificationType;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.IRapidFireNodeResource;
import biz.rapidfire.core.model.IRapidFireNotificationResource;
import biz.rapidfire.core.subsystem.IRapidFireSubSystem;
import biz.rapidfire.core.subsystem.resources.RapidFireNotificationResourceDelegate;

public class RapidFireNotificationResource extends AbstractResource implements IRapidFireNotificationResource,
    Comparable<IRapidFireNotificationResource> {

    private IRapidFireNodeResource parentNode;
    private IRapidFireJobResource parentJob;
    private RapidFireNotificationResourceDelegate delegate;

    public static RapidFireNotificationResource createEmptyInstance(IRapidFireJobResource job) {
        return new RapidFireNotificationResource(job, 0);
    }

    public RapidFireNotificationResource(IRapidFireJobResource job, int position) {

        if (job == null) {
            throw new IllegalParameterException("job", null); //$NON-NLS-1$
        }

        this.parentJob = job;
        this.delegate = new RapidFireNotificationResourceDelegate(job.getDataLibrary(), job.getName(), position);
        super.setSubSystem((ISubSystem)job.getParentSubSystem());
    }

    public NotificationKey getKey() {
        return new NotificationKey(parentJob.getKey(), delegate.getPosition());
    }

    /*
     * IRapidFireResource methods
     */

    public String getDataLibrary() {
        return delegate.getDataLibrary();
    }

    public IRapidFireSubSystem getParentSubSystem() {
        return (IRapidFireSubSystem)super.getSubSystem();
    }

    public IRapidFireJobResource getParentJob() {
        return this.parentJob;
    }

    public IRapidFireJobResource getParentResource() {
        return this.parentJob;
    }

    public IRapidFireNodeResource getParentNode() {
        return parentNode;
    }

    public void setParentNode(IRapidFireNodeResource parentNode) {
        this.parentNode = parentNode;
    }

    /*
     * IRapidFireNotificationResource methods
     */

    public String getJob() {
        return delegate.getJob();
    }

    public int getPosition() {
        return delegate.getPosition();
    }

    public NotificationType getNotificationType() {
        return delegate.getNotificationType();
    }

    public void setNotificationType(NotificationType notificationType) {
        delegate.setNotificationType(notificationType);
    }

    public String getUser() {
        return delegate.getUser();
    }

    public void setUser(String user) {
        delegate.setUser(user);
    }

    public String getMessageQueueName() {
        return delegate.getMessageQueueName();
    }

    public void setMessageQueueName(String messageQueueName) {
        delegate.setMessageQueueName(messageQueueName);
    }

    public String getMessageQueueLibrary() {
        return delegate.getMessageQueueLibrary();
    }

    public void setMessageQueueLibrary(String messageQueueLibrary) {
        delegate.setMessageQueueLibrary(messageQueueLibrary);
    }

    public void reload(Shell shell) throws Exception {

        IRapidFireNotificationResource notification = getParentSubSystem().getNotification(getParentResource(), getPosition(), shell);

        delegate.setNotificationType(notification.getNotificationType());
        delegate.setUser(notification.getUser());
        delegate.setMessageQueueName(notification.getMessageQueueName());
        delegate.setMessageQueueLibrary(notification.getMessageQueueLibrary());
    }

    public int compareTo(IRapidFireNotificationResource resource) {
        return delegate.compareTo(resource);
    }

    @Override
    public String toString() {
        return delegate.toString();
    }
}
