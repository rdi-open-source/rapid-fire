/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.area;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.maintenance.IResourceValues;
import biz.rapidfire.core.maintenance.area.shared.Area;
import biz.rapidfire.core.maintenance.area.shared.AreaKey;
import biz.rapidfire.core.maintenance.area.shared.Ccsid;
import biz.rapidfire.core.maintenance.area.shared.LibraryList;
import biz.rapidfire.core.maintenance.file.shared.FileKey;
import biz.rapidfire.core.maintenance.job.shared.JobKey;

public class AreaValues implements IResourceValues {

    private static final String EMPTY = ""; //$NON-NLS-1$

    private AreaKey key;
    private String library;
    private String libraryList;
    private String libraryCcsid;
    private String commandExtension;

    public static String[] getAreaLabels() {
        return Area.labels();
    }

    public static String[] getLibraryListSpecialValues() {
        return LibraryList.labels();
    }

    public static String[] getCcsidSpecialValues() {
        return Ccsid.labels();
    }

    public static AreaValues createInitialized() {

        AreaValues areaValues = new AreaValues();
        areaValues.setKey(new AreaKey(new FileKey(new JobKey(EMPTY), 0), EMPTY));
        areaValues.setLibrary(EMPTY);
        areaValues.setLibraryList(EMPTY);
        areaValues.setLibraryCcsid(EMPTY);
        areaValues.setCommandExtension(EMPTY);

        return areaValues;
    }

    public AreaKey getKey() {
        ensureKey();
        return key;
    }

    public void setKey(AreaKey key) {
        ensureKey();
        this.key = key;
    }

    public String getLibrary() {
        return library;
    }

    public void setLibrary(String library) {
        this.library = library;
    }

    public String getLibraryList() {
        return libraryList;
    }

    public void setLibraryList(String libraryList) {
        this.libraryList = libraryList;
    }

    public String getLibraryCcsid() {
        return libraryCcsid;
    }

    public void setLibraryCcsid(String libraryCcsid) {
        this.libraryCcsid = libraryCcsid;
    }

    public String getCommandExtension() {
        return commandExtension;
    }

    public void setCommandExtension(String commandExtension) {
        this.commandExtension = commandExtension;
    }

    public void clear() {
        setLibrary(null);
        setLibraryList(null);
        setLibraryCcsid(null);
        setCommandExtension(null);
    }

    private void ensureKey() {

        if (key == null) {
            key = new AreaKey(null, null);
        }
    }

    @Override
    public AreaValues clone() {

        try {

            AreaValues areaValues = (AreaValues)super.clone();
            areaValues.setKey((AreaKey)getKey().clone());

            return areaValues;

        } catch (CloneNotSupportedException e) {
            RapidFireCorePlugin.logError("*** Clone not supported. ***", e); //$NON-NLS-1$
            throw new biz.rapidfire.core.exceptions.CloneNotSupportedException(ExceptionHelper.getLocalizedMessage(e), e);
        }
    }
}
