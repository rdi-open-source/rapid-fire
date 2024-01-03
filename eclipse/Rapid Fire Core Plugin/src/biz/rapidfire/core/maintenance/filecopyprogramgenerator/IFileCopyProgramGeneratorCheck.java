/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.filecopyprogramgenerator;

import biz.rapidfire.core.maintenance.ICheck;

public interface IFileCopyProgramGeneratorCheck extends ICheck {

    public static final String FIELD_JOB = "JOB"; //$NON-NLS-1$
    public static final String FIELD_POSITION = "POS"; //$NON-NLS-1$
    public static final String FIELD_FILE = "FILE"; //$NON-NLS-1$
    public static final String FIELD_TYPE = "TYPE"; //$NON-NLS-1$
    public static final String FIELD_COPY_PROGRAM_NAME = "CP"; //$NON-NLS-1$
    public static final String FIELD_COPY_PROGRAM_LIBRARY_NAME = "CPL"; //$NON-NLS-1$
    public static final String FIELD_CONVERSION_PROGRAM_NAME = "VP"; //$NON-NLS-1$
    public static final String FIELD_CONVERSION_PROGRAM_LIBRARY_NAME = "VPL"; //$NON-NLS-1$
}
