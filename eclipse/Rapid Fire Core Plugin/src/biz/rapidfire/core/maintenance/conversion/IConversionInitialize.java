/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.conversion;

public interface IConversionInitialize {

    public static final int MODE = 1;
    public static final int JOB = 2;
    public static final int POSITION = 3;
    public static final int FIELD_TO_CONVERT = 4;
    public static final int SUCCESS = 5;
    public static final int ERROR_CODE = 6; // TODO: rename to MESSAGE
}
