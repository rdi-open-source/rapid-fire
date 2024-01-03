/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.filecopyprogramgenerator;

import biz.rapidfire.core.maintenance.AbstractResourceActionsCache;
import biz.rapidfire.core.maintenance.filecopyprogramgenerator.shared.FileCopyProgramGeneratorAction;

public final class FileCopyProgramGeneratorActionCache extends
    AbstractResourceActionsCache<KeyFileCopyProgramGeneratorActionCache, FileCopyProgramGeneratorAction> {

    /**
     * The instance of this Singleton class.
     */
    protected static FileCopyProgramGeneratorActionCache instance;

    /**
     * Thread-safe method that returns the instance of this Singleton class.
     */
    public synchronized static FileCopyProgramGeneratorActionCache getInstance() {
        if (instance == null) {
            instance = new FileCopyProgramGeneratorActionCache();
        }
        return instance;
    }

}
