/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.notification;

import biz.rapidfire.core.maintenance.ICheck;

public interface INotificationCheck extends ICheck {

    public static final String FIELD_JOB = "JOB"; //$NON-NLS-1$
    public static final String FIELD_POSITION = "POS"; //$NON-NLS-1$
    public static final String FIELD_TYPE = "TYPE"; //$NON-NLS-1$
    public static final String FIELD_USER = "USER"; //$NON-NLS-1$
    public static final String FIELD_MESSAGE_QUEUE_NAME = "MSGQ"; //$NON-NLS-1$
    public static final String FIELD_MESSAGE_QUEUE_LIBRARY_NAME = "MSGQL"; //$NON-NLS-1$
}
