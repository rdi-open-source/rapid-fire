/*******************************************************************************
 * Copyright (c) 2017-2018 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.librarylist;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class LibraryListEntries {

    private List<LibraryListEntry> libraryList;

    public LibraryListEntries() {

        createLibraryList(null);
    }

    public LibraryListEntry[] getLibraryListEntries() {
        return libraryList.toArray(new LibraryListEntry[libraryList.size()]);
    }

    public void setLibraryListEntries(LibraryListEntry[] libraryList) {
        this.libraryList = createLibraryList(libraryList);
    }

    private List<LibraryListEntry> createLibraryList(LibraryListEntry[] libraryListEntries) {

        libraryList = new LinkedList<LibraryListEntry>();

        if (libraryListEntries != null) {
            Arrays.sort(libraryListEntries);
            for (LibraryListEntry libraryListEntry : libraryListEntries) {
                libraryList.add(libraryListEntry);
            }
        }

        return libraryList;
    }
}
