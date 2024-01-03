/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.swt.widgets.listeditors.librarylist;

public class LibraryListItem implements Comparable<LibraryListItem> {

    static final int LENGTH_SEQUENCE_NUMBER = 4;
    static final int LENGTH_LIBRARY = 10;

    private int sequenceNumber;
    private String library;

    public LibraryListItem(int sequenceNumber, String library) {
        setSequenceNumber(sequenceNumber);
        setLibrary(library);
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getLibrary() {
        return library;
    }

    public void setLibrary(String library) {
        this.library = library;
    }

    public int compareTo(LibraryListItem element) {

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
