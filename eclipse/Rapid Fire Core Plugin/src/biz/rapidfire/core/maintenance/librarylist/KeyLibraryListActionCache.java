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
 * Form of the key:    [dataLibrary], [jobName], [jobStatus], [createEnvironment], [libraryListName_isEmpty]
 * Example key value:  RFPRI, CUSTUPD, RDY, true, IS_EMPTY
 * </pre>
 */
public class KeyLibraryListActionCache extends AbstractKeyResourceActionCache {

    public KeyLibraryListActionCache(IRapidFireLibraryListResource libraryList) {
        super(libraryList.getParentJob(), Boolean.toString(libraryList.getParentJob().isDoCreateEnvironment()), isStringValueEmpty(libraryList
            .getName()));
    }
}
