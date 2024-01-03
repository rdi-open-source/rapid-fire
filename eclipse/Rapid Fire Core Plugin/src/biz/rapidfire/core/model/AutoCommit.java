/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model;

import java.util.HashMap;
import java.util.Map;

public enum AutoCommit {
    YES ("Y"),
    NO ("N");

    private String label;

    private static Map<String, AutoCommit> success;

    static {
        success = new HashMap<String, AutoCommit>();
        for (AutoCommit successItem : AutoCommit.values()) {
            success.put(successItem.label, successItem);
        }
    }

    private AutoCommit(String value) {
        this.label = value;
    }

    public String label() {
        return label;
    }

    public static String[] labels() {
        return success.keySet().toArray(new String[success.size()]);
    }

    public static AutoCommit find(String label) {
        // TODO: remove debug code
        if (label.length() != label.trim().length()) {
            throw new IllegalArgumentException("Expect to see a trimmed value here."); //$NON-NLS-1$
        }
        return success.get(label);
    }
}
