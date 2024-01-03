/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model;

import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.maintenance.librarylist.shared.LibraryListKey;
import biz.rapidfire.core.subsystem.resources.RapidFireLibraryListResourceDelegate.LibraryListEntry;

public interface IRapidFireLibraryListResource extends IRapidFireChildResource<IRapidFireJobResource> {

    public static final int DESCRIPTION_MAX_LENGTH = 35;
    public static final int LENGTH_SEQUENCE_NUMBERS = 1000;
    public static final int LENGTH_LIBRARIES = 2500;

    public LibraryListKey getKey();

    /*
     * Key attributes
     */

    public String getJob();

    public String getName();

    /*
     * Other attributes
     */

    public String getDescription();

    public void setDescription(String description);

    public void reload(Shell shell) throws Exception;

    public LibraryListEntry[] getLibraryListEntries();

    public void addLibraryListEntry(int sequence, String libraryName);
}
