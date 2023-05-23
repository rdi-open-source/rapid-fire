/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance;

import java.util.HashMap;
import java.util.Map;

public enum Contains {
    YES ("Y"),
    NO ("N");

    private String label;

    private static Map<String, Contains> contains;

    static {
        contains = new HashMap<String, Contains>();
        for (Contains containsItem : Contains.values()) {
            contains.put(containsItem.label, containsItem);
        }
    }

    private Contains(String value) {
        this.label = value;
    }

    public String label() {
        return label;
    }

    public static String[] labels() {
        return contains.keySet().toArray(new String[contains.size()]);
    }

    public static Contains find(String label) {
        // TODO: remove debug code
        if (label.length() != label.trim().length()) {
            throw new IllegalArgumentException("Expect to see a trimmed value here."); //$NON-NLS-1$
        }
        return contains.get(label);
    }
}
