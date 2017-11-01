/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model;

import java.util.HashMap;
import java.util.Map;

public enum FileType {
    PHYSICAL ("*PHYSICAL"), //$NON-NLS-1$
    LOGICAL ("*LOGICAL"); //$NON-NLS-1$

    public String label;

    private static Map<String, FileType> fileTypes;

    static {
        fileTypes = new HashMap<String, FileType>();
        for (FileType status : FileType.values()) {
            fileTypes.put(status.label, status);
        }
    }

    private FileType(String label) {
        this.label = label;
    }

    public static FileType find(String label) {
        return fileTypes.get(label);
    }
}
