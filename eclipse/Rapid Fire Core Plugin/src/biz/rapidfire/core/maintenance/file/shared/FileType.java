/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.file.shared;

import java.util.HashMap;
import java.util.Map;

public enum FileType {
    PHYSICAL ("*PHYSICAL"), //$NON-NLS-1$
    LOGICAL ("*LOGICAL"); //$NON-NLS-1$

    private String label;

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

    public String label() {
        return label;
    }

    public static String[] labels() {
        return fileTypes.keySet().toArray(new String[fileTypes.size()]);
    }

    public static FileType find(String label) {
        // TODO: remove debug code
        if (label.length() != label.trim().length()) {
            throw new IllegalArgumentException("Expect to see a trimmed value here."); //$NON-NLS-1$
        }
        return fileTypes.get(label);
    }
}
