/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.job;

import biz.rapidfire.core.model.Status;
import biz.rapidfire.core.model.maintenance.IKeyResourceActionCache;

public class KeyJobActionCache implements IKeyResourceActionCache {

    private Status status;
    private boolean doCreateEnvironment;

    public KeyJobActionCache(Status status, boolean doCreateEnvironment) {
        this.status = status;
        this.doCreateEnvironment = doCreateEnvironment;
    }

    public String getValue() {
        return status + ", " + doCreateEnvironment; //$NON-NLS-1$
    }

    @Override
    public String toString() {
        return getValue();
    }
}
