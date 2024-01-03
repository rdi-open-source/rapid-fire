/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.subsystem.resources;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import biz.rapidfire.core.model.IRapidFireLibraryListResource;

public class RapidFireLibraryListResourceDelegate implements Comparable<IRapidFireLibraryListResource> {

    private String dataLibrary;
    private String job;
    private String libraryListName;
    private String description;
    private List<LibraryListEntry> libraryListEntries;

    public RapidFireLibraryListResourceDelegate(String dataLibrary, String job, String libraryListName) {

        this.dataLibrary = dataLibrary;
        this.job = job;
        this.libraryListName = libraryListName;
        this.libraryListEntries = new LinkedList<LibraryListEntry>();
    }

    /*
     * IRapidFireResource methods
     */

    public String getDataLibrary() {
        return dataLibrary;
    }

    /*
     * IRapidFireLibraryResource methods
     */

    public String getJob() {
        return job;
    }

    public String getName() {
        return libraryListName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LibraryListEntry[] getLibraryListEntries() {

        LibraryListEntry[] libraryListEntries = this.libraryListEntries.toArray(new LibraryListEntry[this.libraryListEntries.size()]);

        Arrays.sort(libraryListEntries);

        return libraryListEntries;
    }

    public void addLibraryListEntry(int sequence, String libraryName) {

        libraryListEntries.add(new LibraryListEntry(sequence, libraryName));
    }

    public void clearLibraryList() {
        libraryListEntries.clear();
    }

    public int compareTo(IRapidFireLibraryListResource resource) {

        if (resource == null) {
            return 1;
        }

        int result = resource.getDataLibrary().compareTo(getDataLibrary());
        if (result != 0) {
            return result;
        }

        result = resource.getJob().compareTo(getJob());
        if (result != 0) {
            return result;
        }

        return getName().compareTo(resource.getName());
    }

    @Override
    public String toString() {
        return getName();
    }

    public class LibraryListEntry implements Comparable<LibraryListEntry> {

        int sequenceNumber;
        String libraryName;

        public LibraryListEntry(int sequenceNumber, String libraryName) {

            this.sequenceNumber = sequenceNumber;
            this.libraryName = libraryName;
        }

        public int getSequenceNumber() {
            return sequenceNumber;
        }

        public String getLibraryName() {
            return libraryName;
        }

        public int compareTo(LibraryListEntry element) {

            if (element == null) {
                return 1;
            }

            if (getSequenceNumber() > element.getSequenceNumber()) {
                return 1;
            } else if (getSequenceNumber() < element.getSequenceNumber()) {
                return -1;
            } else {
                return 0;
            }
        }

        @Override
        public String toString() {
            return Integer.toString(sequenceNumber) + ": " + libraryName; //$NON-NLS-1$
        }
    }
}
