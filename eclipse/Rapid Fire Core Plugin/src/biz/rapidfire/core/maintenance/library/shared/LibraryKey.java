/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.library.shared;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.maintenance.IResourceKey;
import biz.rapidfire.core.maintenance.job.shared.JobKey;

public class LibraryKey implements IResourceKey {

    private JobKey jobKey;
    private String library;

    public static LibraryKey createNew(JobKey jobKey) {

        LibraryKey key = new LibraryKey(jobKey, ""); //$NON-NLS-1$

        return key;
    }

    public LibraryKey(JobKey jobKey, String library) {

        this.jobKey = jobKey;
        if (library != null) {
            this.library = library.trim();
        }
    }

    public String getJobName() {
        return jobKey.getJobName();
    }

    public String getLibrary() {
        return library;
    }

    public void setLibrary(String library) {
        this.library = library.trim();
    }

    @Override
    public String toString() {

        StringBuilder buffer = new StringBuilder();
        buffer.append(jobKey.toString());
        buffer.append(DELIMITER);
        buffer.append(getLibrary());

        return buffer.toString();
    }

    @Override
    public Object clone() {
        try {

            LibraryKey libraryKey = (LibraryKey)super.clone();
            libraryKey.jobKey = (JobKey)jobKey.clone();

            return libraryKey;

        } catch (CloneNotSupportedException e) {
            RapidFireCorePlugin.logError("*** Clone not supported. ***", e); //$NON-NLS-1$
            throw new biz.rapidfire.core.exceptions.CloneNotSupportedException(ExceptionHelper.getLocalizedMessage(e), e);
        }
    }
}
