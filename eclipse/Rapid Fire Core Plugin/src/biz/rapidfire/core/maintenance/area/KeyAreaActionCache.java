/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.area;

import biz.rapidfire.core.maintenance.AbstractKeyResourceActionCache;
import biz.rapidfire.core.model.IRapidFireAreaResource;

/**
 * This class produces the key value for the AreaActionCache.
 * 
 * <pre>
 * Form of the key:    [jobStatus], [createEnvironment], [position], [area_isEmpty]
 * Example key value:  RDY, true, 10, IS_EMPTY
 * </pre>
 * 
 * The key is composed from the attributes of the job ('status' and 'create
 * environment') plus the file ('position') and the area ('name'). The name is
 * translated to IS_EMPTY or IS_NOT_EMPTY, because the actual name is not
 * relevant.
 */
public class KeyAreaActionCache extends AbstractKeyResourceActionCache {

    public KeyAreaActionCache(IRapidFireAreaResource area) {
        super(area.getParentJob(), Integer.toString(area.getPosition()), isStringValueEmpty(area.getName()));
    }
}
