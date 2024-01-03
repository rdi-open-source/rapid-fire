/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.area;

import biz.rapidfire.core.maintenance.ICheck;

public interface IAreaCheck extends ICheck {

    public static final String FIELD_AREA = "ARA"; //$NON-NLS-1$
    public static final String FIELD_LIBRARY = "LIB"; //$NON-NLS-1$
    public static final String FIELD_LIBRARY_LIST = "LIBL"; //$NON-NLS-1$
    public static final String FIELD_LIBRARY_CCSID = "CCSID"; //$NON-NLS-1$
    public static final String FIELD_COMMAND_EXTENSION = "CEXT"; //$NON-NLS-1$
}
