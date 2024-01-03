/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rsebase.host;

public enum SystemFileType {
    SRC (2),
    DTA (4),
    SRC_OR_DTA (8),
    DSPF (16),
    PRTF (32),
    ANY (256);

    private int fileType;

    private SystemFileType(int fileType) {
        this.fileType = fileType;
    }

    public int intValue() {
        return fileType;
    }
}
