/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model;

import biz.rapidfire.core.model.maintenance.notification.shared.NotificationType;

public interface IRapidFireNotificationResource extends IRapidFireChildResource {

    /*
     * Key attributes
     */

    public String getJob();

    public int getPosition();

    /*
     * Data attributes
     */

    public NotificationType getNotificationType();

    public void setNotificationType(NotificationType notificationType);

    public String getUser();

    public void setUser(String user);

    public String getMessageQueueName();

    public void setMessageQueueName(String messageQueueName);

    public String getMessageQueueLibrary();

    public void setMessageQueueLibrary(String messageQueueLibrary);
}