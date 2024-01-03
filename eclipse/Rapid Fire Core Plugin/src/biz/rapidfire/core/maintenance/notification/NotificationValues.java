/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.notification;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.maintenance.IResourceValues;
import biz.rapidfire.core.maintenance.notification.shared.MessageQueueLibrary;
import biz.rapidfire.core.maintenance.notification.shared.NotificationKey;
import biz.rapidfire.core.maintenance.notification.shared.NotificationType;
import biz.rapidfire.core.maintenance.notification.shared.User;

public class NotificationValues implements IResourceValues {

    private NotificationKey key;
    private NotificationType notificationType;
    private String user;
    private String messageQueueLibraryName;
    private String messageQueueName;

    public static String[] getTypeLabels() {

        String[] labels = new String[2];

        labels[0] = NotificationType.USR.label();
        labels[1] = NotificationType.MSGQ.label();

        return labels;
    }

    public static String[] getUserSpecialValues() {
        return User.labels();
    }

    public static String[] getMessageQueueLibrarySpecialValues() {
        return MessageQueueLibrary.labels();
    }

    public NotificationKey getKey() {
        ensureKey();
        return key;
    }

    public void setKey(NotificationKey key) {
        ensureKey();
        this.key = key;
    }

    public String getNotificationType() {

        if (notificationType == null) {
            return ""; //$NON-NLS-1$
        } else {
            return notificationType.label();
        }
    }

    public void setNotificationType(String type) {

        if (type == null || type.trim().length() == 0) {
            this.notificationType = null;
        } else {
            this.notificationType = NotificationType.find(type.trim());
        }
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user.trim();
    }

    public String getMessageQueueLibraryName() {
        return messageQueueLibraryName;
    }

    public void setMessageQueueLibraryName(String copyProgramLibraryName) {
        this.messageQueueLibraryName = copyProgramLibraryName.trim();
    }

    public String getMessageQueueName() {
        return messageQueueName;
    }

    public void setMessageQueueName(String copyProgramName) {
        this.messageQueueName = copyProgramName.trim();
    }

    public void clear() {
        setNotificationType(null);
        setUser(null);
        setMessageQueueLibraryName(null);
        setMessageQueueName(null);
    }

    private void ensureKey() {

        if (key == null) {
            key = new NotificationKey(null, 0);
        }
    }

    @Override
    public NotificationValues clone() {

        try {

            NotificationValues notificationValues = (NotificationValues)super.clone();
            notificationValues.setKey((NotificationKey)getKey().clone());

            return notificationValues;

        } catch (CloneNotSupportedException e) {
            RapidFireCorePlugin.logError("*** Clone not supported. ***", e); //$NON-NLS-1$
            throw new biz.rapidfire.core.exceptions.CloneNotSupportedException(ExceptionHelper.getLocalizedMessage(e), e);
        }
    }
}
