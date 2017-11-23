/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model.maintenance.librarylist;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.helpers.IntHelper;

public class LibraryListValues implements Cloneable {

    private LibraryListKey key;
    private String description;
    private List<LibraryListEntry> libraryList;

    private String libraryListString;
    private String sequenceNumbersString;

    public LibraryListValues() {

        this.libraryList = createLibraryList(null);

        setLibraryList(new LibraryListEntry[0]);
    }

    public LibraryListKey getKey() {
        ensureKey();
        return key;
    }

    public void setKey(LibraryListKey key) {
        ensureKey();
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String Description) {
        this.description = Description.trim();
    }

    public LibraryListEntry[] getLibraryList() {
        return libraryList.toArray(new LibraryListEntry[libraryList.size()]);
    }

    public String getLibraryListAsString() {
        return libraryListString;
    }

    public String getSequenceNumberAsString() {
        return sequenceNumbersString;
    }

    public void setLibraryList(LibraryListEntry[] libraryList) {
        this.libraryList = createLibraryList(libraryList);

        StringBuilder libraries = new StringBuilder();
        StringBuilder sequenceNumbers = new StringBuilder();
        for (LibraryListEntry libraryListEntry : libraryList) {
            libraries.append(libraryListEntry.getLibraryAsFixLengthString());
            sequenceNumbers.append(libraryListEntry.getSequenceNumberAsFixLengthString());
        }

        libraryListString = libraries.toString();
        sequenceNumbersString = sequenceNumbers.toString();
    }

    void setLibraryList(String sequenceNumbers, String libraries) {

        this.libraryListString = libraries;
        this.sequenceNumbersString = sequenceNumbers;

        libraryList.clear();

        int libOffs = 0;
        int seqNbrOffs = 0;
        String sequenceNumberStr;
        int sequenceNumber;
        String library;
        while (libOffs + LibraryListEntry.LENGTH_LIBRARY <= libraries.length()
            && seqNbrOffs + LibraryListEntry.LENGTH_SEQUENCE_NUMBER <= sequenceNumbers.length()) {
            sequenceNumberStr = sequenceNumbers.substring(libOffs, libOffs + LibraryListEntry.LENGTH_SEQUENCE_NUMBER);
            sequenceNumber = IntHelper.tryParseInt(sequenceNumberStr, -1);
            library = libraries.substring(libOffs, libOffs + LibraryListEntry.LENGTH_LIBRARY);

            libraryList.add(new LibraryListEntry(sequenceNumber, library));

            libOffs += LibraryListEntry.LENGTH_SEQUENCE_NUMBER;
            seqNbrOffs += LibraryListEntry.LENGTH_LIBRARY;
        }

        LibraryListEntry[] tempArray = libraryList.toArray(new LibraryListEntry[libraryList.size()]);
        Arrays.sort(tempArray);

        libraryList = Arrays.asList(tempArray);
    }

    public void clear() {
        setDescription(null);
    }

    private void ensureKey() {

        if (key == null) {
            key = new LibraryListKey(null, null); //$NON-NLS-1$
        }
    }

    private List<LibraryListEntry> createLibraryList(LibraryListEntry[] libraries) {

        List<LibraryListEntry> libraryList;

        if (libraries != null) {
            libraryList = new LinkedList<LibraryListEntry>(Arrays.asList(libraries));
        } else {
            libraryList = new LinkedList<LibraryListEntry>();
        }

        return libraryList;
    }

    @Override
    public LibraryListValues clone() {

        try {

            LibraryListValues libraryListValues = (LibraryListValues)super.clone();
            libraryListValues.setKey((LibraryListKey)getKey().clone());

            return libraryListValues;

        } catch (CloneNotSupportedException e) {
            RapidFireCorePlugin.logError("*** Clone not supported. ***", e); //$NON-NLS-1$
            throw new biz.rapidfire.core.exceptions.CloneNotSupportedException(ExceptionHelper.getLocalizedMessage(e), e);
        }
    }

    @Override
    public String toString() {
        return key + ": " + description;
    }
}
