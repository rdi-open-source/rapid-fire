/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.filecopyprogramgenerator.shared;

import java.util.HashMap;
import java.util.Map;

public enum Replace {
    YES ("*YES"), //$NON-NLS-1$
    NO ("*NO"); //$NON-NLS-1$

    private String label;

    private static Map<String, Replace> replace;

    static {
        replace = new HashMap<String, Replace>();
        for (Replace status : Replace.values()) {
            replace.put(status.label, status);
        }
    }

    private Replace(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

    public static String[] labels() {
        return replace.keySet().toArray(new String[replace.size()]);
    }

    public static Replace find(String label) {
        // TODO: remove debug code
        if (label.length() != label.trim().length()) {
            throw new IllegalArgumentException("Expect to see a trimmed value here."); //$NON-NLS-1$
        }
        return replace.get(label);
    }
}
