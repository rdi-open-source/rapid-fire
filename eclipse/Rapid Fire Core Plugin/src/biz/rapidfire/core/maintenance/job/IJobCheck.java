/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.job;

import biz.rapidfire.core.maintenance.ICheck;

public interface IJobCheck extends ICheck {

    public static final String FIELD_JOB = "JOB"; //$NON-NLS-1$
    public static final String FIELD_DESCRIPTION = "DSCR"; //$NON-NLS-1$
    public static final String FIELD_CREATE_ENVIRONMENT = "CRTE"; //$NON-NLS-1$
    public static final String FIELD_JOB_QUEUE_NAME = "JQ"; //$NON-NLS-1$
    public static final String FIELD_JOB_QUEUE_LIBRARY_NAME = "JQL"; //$NON-NLS-1$
    public static final String FIELD_CANCEL_ASP_THRESHOLD_EXCEEDS = "CASPTE"; //$NON-NLS-1$
}
