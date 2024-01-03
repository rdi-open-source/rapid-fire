/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.conversion;

import biz.rapidfire.core.maintenance.AbstractResourceActionsCache;
import biz.rapidfire.core.maintenance.conversion.shared.ConversionAction;

public final class ConversionActionCache extends AbstractResourceActionsCache<KeyConversionActionCache, ConversionAction> {

    /**
     * The instance of this Singleton class.
     */
    protected static ConversionActionCache instance;

    /**
     * Thread-safe method that returns the instance of this Singleton class.
     */
    public synchronized static ConversionActionCache getInstance() {
        if (instance == null) {
            instance = new ConversionActionCache();
        }
        return instance;
    }

}
