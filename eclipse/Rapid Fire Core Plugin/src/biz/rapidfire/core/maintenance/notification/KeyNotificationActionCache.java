/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.notification;

import biz.rapidfire.core.maintenance.AbstractKeyResourceActionCache;
import biz.rapidfire.core.model.IRapidFireNotificationResource;

/**
 * This class produces the key value for the NotificationActionCache.
 * 
 * <pre>
 * Form of the key:    [jobStatus], [createEnvironment], [position_isZero]
 * Example key value:  RDY, true, IS_ZERO
 * </pre>
 * 
 * The key is composed from the attributes of the job ('status' and 'create
 * environment') plus the notification ('position'). The position of the
 * notification is translated to IS_ZERO or IS_NOT_ZERO, because the actual
 * position is not relevant.
 */
public class KeyNotificationActionCache extends AbstractKeyResourceActionCache {

    public KeyNotificationActionCache(IRapidFireNotificationResource notification) {
        super(notification.getParentJob(), isNumericValueZero(notification.getPosition()));
    }
}
