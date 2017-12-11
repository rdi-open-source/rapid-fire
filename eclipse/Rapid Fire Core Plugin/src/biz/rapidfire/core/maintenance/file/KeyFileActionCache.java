/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.file;

import biz.rapidfire.core.maintenance.IKeyResourceActionCache;
import biz.rapidfire.core.maintenance.file.shared.FileType;
import biz.rapidfire.core.model.Status;

public class KeyFileActionCache implements IKeyResourceActionCache {

    private Status status;
    private boolean doCreateEnvironment;
    private FileType fileType;

    public KeyFileActionCache(Status status, boolean doCreateEnvironment, FileType fileType) {
        this.status = status;
        this.doCreateEnvironment = doCreateEnvironment;
        this.fileType = fileType;
    }

    public String getValue() {
        return status + ", " + doCreateEnvironment + ", " + fileType.label(); //$NON-NLS-1$
    }

    @Override
    public String toString() {
        return getValue();
    }
}
