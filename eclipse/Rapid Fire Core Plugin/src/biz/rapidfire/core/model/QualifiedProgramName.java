/*******************************************************************************
 * Copyright (c) 2017-2018 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model;

import biz.rapidfire.core.swt.widgets.viewers.stringlist.IStringListItem;

public class QualifiedProgramName implements IStringListItem {

    private String name;
    private String library;

    public QualifiedProgramName(String library, String name) {
        this.library = library;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String programName) {
        this.name = programName;
    }

    public String getLibrary() {
        return library;
    }

    public void setLibrary(String libraryName) {
        this.library = libraryName;
    }

    public String getLabel() {
        return getLibrary() + "/" + getName(); //$NON-NLS-1$
    }

}
