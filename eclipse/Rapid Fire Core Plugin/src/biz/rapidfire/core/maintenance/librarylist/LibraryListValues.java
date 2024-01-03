/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.librarylist;

import java.util.ArrayList;
import java.util.List;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.helpers.IntHelper;
import biz.rapidfire.core.helpers.StringHelper;
import biz.rapidfire.core.maintenance.IResourceValues;
import biz.rapidfire.core.maintenance.job.shared.JobKey;
import biz.rapidfire.core.maintenance.librarylist.shared.LibraryListKey;
import biz.rapidfire.core.model.IRapidFireLibraryListResource;

public class LibraryListValues implements IResourceValues {

    private static final String EMPTY = ""; //$NON-NLS-1$

    private LibraryListKey key;
    private String description;
    private LibraryListEntries libraryListEntries;

    private String libraryListString;
    private String sequenceNumbersString;

    public static LibraryListValues createInitialized() {

        LibraryListValues libraryListValues = new LibraryListValues();
        libraryListValues.setKey(new LibraryListKey(new JobKey(EMPTY), EMPTY));
        libraryListValues.setDescription(EMPTY);

        return libraryListValues;
    }

    public LibraryListValues() {

        this.libraryListEntries = new LibraryListEntries();

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
        return libraryListEntries.getLibraryListEntries();
    }

    public String getLibraryListAsString() {
        return libraryListString;
        // return StringHelper.getFixLength(libraryListString,
        // LENGTH_LIBRARIES);
    }

    public String getSequenceNumberAsString() {
        return StringHelper.getFixLength(sequenceNumbersString, IRapidFireLibraryListResource.LENGTH_SEQUENCE_NUMBERS).replaceAll(" ", "0"); //$NON-NLS-1$ //$NON-NLS-2$ 
    }

    public void setLibraryList(LibraryListEntry[] libraryList) {
        this.libraryListEntries.setLibraryListEntries(libraryList);

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

        LibraryListEntry[] libraryListEntries = produceLibraryList();

        this.libraryListEntries.setLibraryListEntries(libraryListEntries);
    }

    public LibraryListEntry[] produceLibraryList() {

        int libOffs = 0;
        int seqNbrOffs = 0;
        String sequenceNumberStr;
        int sequenceNumber;
        String library;

        List<LibraryListEntry> libraryList = new ArrayList<LibraryListEntry>();

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

        LibraryListEntry[] libraryListEntries = libraryList.toArray(new LibraryListEntry[libraryList.size()]);

        return libraryListEntries;
    }

    public void clear() {
        setDescription(null);
    }

    private void ensureKey() {

        if (key == null) {
            key = new LibraryListKey(null, null);
        }
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
