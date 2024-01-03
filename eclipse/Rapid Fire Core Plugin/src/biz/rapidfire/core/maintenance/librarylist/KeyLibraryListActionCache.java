/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.librarylist;

import biz.rapidfire.core.maintenance.AbstractKeyResourceActionCache;
import biz.rapidfire.core.model.IRapidFireLibraryListResource;

/**
 * This class produces the key value for the LibraryListActionCache.
 * 
 * <pre>
 * Form of the key:    [jobStatus], [createEnvironment], [libraryListName_isEmpty]
 * Example key value:  RDY, true, IS_EMPTY
 * </pre>
 * 
 * The key is composed from the attributes of the job ('status' and 'create
 * environment') plus the name of the library list ('name'). The name of the
 * library list is translated to IS_EMPTY or IS_NOT_EMPTY, because the actual
 * name is not relevant.
 */
public class KeyLibraryListActionCache extends AbstractKeyResourceActionCache {

    public KeyLibraryListActionCache(IRapidFireLibraryListResource libraryList) {
        super(libraryList.getParentJob(), isStringValueEmpty(libraryList.getName()));
    }
}
