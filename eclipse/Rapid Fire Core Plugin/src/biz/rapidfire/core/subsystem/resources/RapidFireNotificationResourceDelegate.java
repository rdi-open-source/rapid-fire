/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.subsystem.resources;

import biz.rapidfire.core.maintenance.notification.shared.NotificationType;
import biz.rapidfire.core.model.IRapidFireNotificationResource;

public class RapidFireNotificationResourceDelegate implements Comparable<IRapidFireNotificationResource> {

    private String dataLibrary;
    private String job;
    private int position;
    private NotificationType notificationType;
    private String user;
    private String messageQueueName;
    private String messageQueueLibrary;

    public RapidFireNotificationResourceDelegate(String dataLibrary, String job, int position) {

        this.dataLibrary = dataLibrary;
        this.job = job;
        this.position = position;
    }

    /*
     * IRapidFireResource methods
     */

    public String getDataLibrary() {
        return dataLibrary;
    }

    /*
     * IRapidFireNotificationResource methods
     */

    public String getJob() {
        return job;
    }

    public int getPosition() {
        return position;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public String getMessageQueueName() {
        return messageQueueName;
    }

    public void setMessageQueueName(String messageQueueName) {
        this.messageQueueName = messageQueueName;
    }

    public String getMessageQueueLibrary() {
        return messageQueueLibrary;
    }

    public void setMessageQueueLibrary(String messageQueueLibrary) {
        this.messageQueueLibrary = messageQueueLibrary;
    }

    public int compareTo(IRapidFireNotificationResource resource) {

        if (resource == null) {
            return 1;
        }

        int result = resource.getDataLibrary().compareTo(getDataLibrary());
        if (result != 0) {
            return result;
        }

        result = resource.getJob().compareTo(getJob());
        if (result != 0) {
            return result;
        }

        if (getPosition() > resource.getPosition()) {
            return 1;
        } else if (getPosition() < resource.getPosition()) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return getUser();
    }

}
