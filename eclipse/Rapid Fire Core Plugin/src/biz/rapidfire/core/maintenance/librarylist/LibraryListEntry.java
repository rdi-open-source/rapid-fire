/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.librarylist;

import biz.rapidfire.core.helpers.StringHelper;

public class LibraryListEntry implements Comparable<LibraryListEntry> {

    static final int LENGTH_SEQUENCE_NUMBER = 4;
    static final int LENGTH_LIBRARY = 10;

    private int sequenceNumber;
    private String sequenceNumberFixLengthString;
    private String library;
    private String libraryFixLengthString;

    public LibraryListEntry(int sequenceNumber, String library) {
        setSequenceNumber(sequenceNumber);
        setLibrary(library);
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public String getSequenceNumberAsFixLengthString() {
        return sequenceNumberFixLengthString;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
        this.sequenceNumberFixLengthString = StringHelper.getFixLengthLeading(Integer.toString(sequenceNumber), LENGTH_SEQUENCE_NUMBER).replace(
            " ", "0"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public String getLibrary() {
        return library;
    }

    public String getLibraryAsFixLengthString() {
        return libraryFixLengthString;
    }

    public void setLibrary(String library) {
        this.library = library;
        this.libraryFixLengthString = StringHelper.getFixLength(library, LENGTH_LIBRARY);
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
        return Integer.toString(sequenceNumber) + ": " + library; //$NON-NLS-1$
    }
}
