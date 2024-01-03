/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.job;

import biz.rapidfire.core.maintenance.AbstractKeyResourceActionCache;
import biz.rapidfire.core.model.IRapidFireJobResource;

/**
 * This class produces the key value for the JobActionCache.
 * 
 * <pre>
 * Form of the key:    [jobStatus], [createEnvironment]
 * Example key value:  RDY, true
 * </pre>
 * 
 * The key is composed from the job attributes 'status' and 'create
 * environment'.
 */
public class KeyJobActionCache extends AbstractKeyResourceActionCache {

    public KeyJobActionCache(IRapidFireJobResource job) {
        super(job);
    }
}
