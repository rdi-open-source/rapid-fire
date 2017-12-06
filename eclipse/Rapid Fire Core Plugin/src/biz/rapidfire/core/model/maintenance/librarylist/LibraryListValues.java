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
import biz.rapidfire.core.helpers.StringHelper;
import biz.rapidfire.core.model.maintenance.IResourceValues;
import biz.rapidfire.core.model.maintenance.librarylist.shared.LibraryListKey;

public class LibraryListValues implements IResourceValues {

    static final int LENGTH_SEQUENCE_NUMBERS = 1000;
    static final int LENGTH_LIBRARIES = 2500;

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
        // return StringHelper.getFixLength(libraryListString,
        // LENGTH_LIBRARIES);
    }

    public String getSequenceNumberAsString() {
        return StringHelper.getFixLength(sequenceNumbersString, LENGTH_SEQUENCE_NUMBERS).replaceAll(" ", "0"); //$NON-NLS-1$ 
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

        this.libraryListString = libraries.trim();
        this.sequenceNumbersString = sequenceNumbers.trim();

        libraryList.clear();

        int libOffs = 0;
        int seqNbrOffs = 0;
        String sequenceNumberStr;
        int sequenceNumber;
        String library;

        while (seqNbrOffs < sequenceNumbersString.length() && libOffs < libraryListString.length()) {

            if (seqNbrOffs + LibraryListEntry.LENGTH_SEQUENCE_NUMBER < sequenceNumbersString.length()) {
                sequenceNumberStr = sequenceNumbersString.substring(seqNbrOffs, seqNbrOffs + LibraryListEntry.LENGTH_SEQUENCE_NUMBER);
            } else {
                sequenceNumberStr = sequenceNumbersString.substring(seqNbrOffs);
            }

            sequenceNumber = IntHelper.tryParseInt(sequenceNumberStr, -1);

            if (libOffs + LibraryListEntry.LENGTH_LIBRARY < libraryListString.length()) {
                library = libraryListString.substring(libOffs, libOffs + LibraryListEntry.LENGTH_LIBRARY);
            } else {
                library = libraryListString.substring(libOffs);
            }

            libraryList.add(new LibraryListEntry(sequenceNumber, library));

            seqNbrOffs += LibraryListEntry.LENGTH_SEQUENCE_NUMBER;
            libOffs += LibraryListEntry.LENGTH_LIBRARY;
        }

        LibraryListEntry[] tempArray = libraryList.toArray(new LibraryListEntry[libraryList.size()]);
        Arrays.sort(tempArray);

        libraryList.clear();
        for (LibraryListEntry entry : tempArray) {
            libraryList.add(entry);
        }
    }

    public void clear() {
        setDescription(null);
    }

    private void ensureKey() {

        if (key == null) {
            key = new LibraryListKey(null, null);
        }
    }

    private List<LibraryListEntry> createLibraryList(LibraryListEntry[] libraries) {

        List<LibraryListEntry> libraryList = new LinkedList<LibraryListEntry>();

        if (libraries != null) {
            for (LibraryListEntry library : libraries) {
                libraryList.add(library);
            }
        }

        return libraryList;
    }

    @Override
    public LibraryListValues clone() {

        try {

            LibraryListValues libraryListValues = (LibraryListValues)super.clone();
            libraryListValues.setKey((LibraryListKey)getKey().clone());
            libraryListValues.setLibraryList(getSequenceNumberAsString(), getLibraryListAsString());

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
