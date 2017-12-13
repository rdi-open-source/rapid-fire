/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.library;

import biz.rapidfire.core.maintenance.AbstractKeyResourceActionCache;
import biz.rapidfire.core.model.IRapidFireLibraryResource;

/**
 * This class produces the key value for the LibraryActionCache.
 * 
 * <pre>
 * Form of the key:    [dataLibrary] + [jobName], [jobStatus], [libraryName_isEmpty]
 * Example key value:  RFPRI, CUSTUPD, RDY, IS_EMPTY
 * </pre>
 */
public class KeyLibraryActionCache extends AbstractKeyResourceActionCache {

    public KeyLibraryActionCache(IRapidFireLibraryResource library) {
        super(library.getParentJob(), isStringValueEmpty(library.getName()));
    }
}
