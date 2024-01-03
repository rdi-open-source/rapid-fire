/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.conversion;

import biz.rapidfire.core.maintenance.ICheck;

public interface IConversionCheck extends ICheck {

    public static final String FIELD_FIELD_TO_CONVERT = "FTC"; //$NON-NLS-1$
    public static final String FIELD_NEW_FIELD_NAME = "RFT"; //$NON-NLS-1$
    public static final String FIELD_SAME_FIELD_NAMES = "FTCRFT"; //$NON-NLS-1$
    public static final String FIELD_STATEMENT = "STM"; //$NON-NLS-1$
}
