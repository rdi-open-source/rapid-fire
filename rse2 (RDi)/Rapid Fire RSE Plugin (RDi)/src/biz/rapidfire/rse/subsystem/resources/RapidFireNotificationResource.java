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

import biz.rapidfire.core.exceptions.IllegalParameterException;
import biz.rapidfire.core.helpers.StringHelper;
import biz.rapidfire.core.model.IRapidFireNotificationResource;
import biz.rapidfire.core.model.maintenance.notification.shared.NotificationType;
import biz.rapidfire.core.subsystem.IRapidFireSubSystem;
import biz.rapidfire.core.subsystem.resources.RapidFireNotificationResourceDelegate;

public class RapidFireNotificationResource extends AbstractResource implements IRapidFireNotificationResource,
    Comparable<IRapidFireNotificationResource> {

    private RapidFireNotificationResourceDelegate delegate;

    public static RapidFireNotificationResource createEmptyInstance(String dataLibrary, String job) {
        return new RapidFireNotificationResource(dataLibrary, job, 0);
    }

    public RapidFireNotificationResource(String dataLibrary, String job, int position) {

        if (StringHelper.isNullOrEmpty(dataLibrary)) {
            throw new IllegalParameterException("dataLibrary", dataLibrary); //$NON-NLS-1$
        }

        if (StringHelper.isNullOrEmpty(job)) {
            throw new IllegalParameterException("job", job); //$NON-NLS-1$
        }

        this.delegate = new RapidFireNotificationResourceDelegate(dataLibrary, job, position);
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

    public void setParentSubSystem(IRapidFireSubSystem subSystem) {
        super.setSubSystem((ISubSystem)subSystem);
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

    public int compareTo(IRapidFireNotificationResource resource) {
        return delegate.compareTo(resource);
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

}
