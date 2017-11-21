/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model.maintenance.library;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.helpers.ExceptionHelper;

public class LibraryValues implements Cloneable {

    private LibraryKey key;
    private String shadowLibrary;

    public LibraryKey getKey() {
        ensureKey();
        return key;
    }

    public void setKey(LibraryKey key) {
        ensureKey();
        this.key = key;
    }

    public String getShadowLibrary() {
        return shadowLibrary;
    }

    public void setShadowLibrary(String shadowLibrary) {
        this.shadowLibrary = shadowLibrary.trim();
    }

    public void clear() {
        setShadowLibrary(null);
    }

    private void ensureKey() {

        if (key == null) {
            key = new LibraryKey(null, null); //$NON-NLS-1$
        }
    }

    @Override
    public LibraryValues clone() {

        try {

            LibraryValues libraryValues = (LibraryValues)super.clone();
            libraryValues.setKey((LibraryKey)getKey().clone());

            return libraryValues;

        } catch (CloneNotSupportedException e) {
            RapidFireCorePlugin.logError("*** Clone not supported. ***", e); //$NON-NLS-1$
            throw new biz.rapidfire.core.exceptions.CloneNotSupportedException(ExceptionHelper.getLocalizedMessage(e), e);
        }
    }
}