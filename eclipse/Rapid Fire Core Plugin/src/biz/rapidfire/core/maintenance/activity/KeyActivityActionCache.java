/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.activity;

import biz.rapidfire.core.maintenance.AbstractKeyResourceActionCache;
import biz.rapidfire.core.model.IRapidFireActivityResource;

/**
 * This class produces the key value for the ActivityActionCache.
 * 
 * <pre>
 * Form of the key:    [jobStatus], [createEnvironment]
 * Example key value:  RDY, true
 * </pre>
 * 
 * These are the same attributes that are used to determine the valid job
 * actions. Therefore no more attributes are passed to the constructor of the
 * super class.
 */
public class KeyActivityActionCache extends AbstractKeyResourceActionCache {

    public KeyActivityActionCache(IRapidFireActivityResource activity) {
        super(activity.getParentJob());
    }
}
