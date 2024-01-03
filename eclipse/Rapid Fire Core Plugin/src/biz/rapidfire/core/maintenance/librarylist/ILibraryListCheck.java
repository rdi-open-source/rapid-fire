/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.librarylist;

public interface ILibraryListCheck {

    public static final int SUCCESS = 1;
    public static final int FIELD_NAME = 2;
    public static final int RECORD = 3;
    public static final int MESSAGE = 4;

    public static final String FIELD_JOB = "JOB"; //$NON-NLS-1$
    public static final String FIELD_SEQUENCE = "SEQ"; //$NON-NLS-1$
    public static final String FIELD_DUPLICATE = "DUP"; //$NON-NLS-1$
    public static final String FIELD_LIBRARY_LIST = "LIBL"; //$NON-NLS-1$
    public static final String FIELD_DESCRIPTION = "DSCR"; //$NON-NLS-1$
}
