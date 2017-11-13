/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model.maintenance;

public interface IMaintenance {

    public static final String MODE_CREATE = "*CREATE"; //$NON-NLS-1$
    public static final String MODE_COPY = "*COPY"; //$NON-NLS-1$
    public static final String MODE_CHANGE = "*CHANGE"; //$NON-NLS-1$
    public static final String MODE_DELETE = "*DELETE"; //$NON-NLS-1$
    public static final String MODE_DISPLAY = "*DISPLAY"; //$NON-NLS-1$
}
