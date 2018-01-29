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
 * Form of the key:    [dataLibrary], [jobName], [jobStatus], [position_isZero]
 * Example key value:  RFPRI, CUSTUPD, RDY, IS_ZERO
 * </pre>
 */
public class KeyNotificationActionCache extends AbstractKeyResourceActionCache {

    public KeyNotificationActionCache(IRapidFireNotificationResource notification) {
        super(notification.getParentJob(), isNumericValueZero(notification.getPosition()));
    }
}
