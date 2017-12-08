/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.command;

import biz.rapidfire.core.model.maintenance.ICheck;

public interface ICommandCheck extends ICheck {

    public static final String FIELD_TYPE = "TYPE"; //$NON-NLS-1$
    public static final String FIELD_SEQUENCE = "SEQ"; //$NON-NLS-1$
    public static final String FIELD_EXIST = "TYPESEQ"; //$NON-NLS-1$
    public static final String FIELD_COMMAND = "CMD"; //$NON-NLS-1$
}