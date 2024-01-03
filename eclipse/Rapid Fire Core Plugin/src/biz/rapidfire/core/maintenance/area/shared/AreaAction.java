/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.area.shared;

import java.util.HashMap;
import java.util.Map;

import biz.rapidfire.core.maintenance.shared.IResourceAction;

public enum AreaAction implements IResourceAction {
    CREATE ("*CREATE"),
    COPY ("*COPY"),
    CHANGE ("*CHANGE"),
    DELETE ("*DELETE"),
    DISPLAY ("*DISPLAY");

    private String label;

    private static Map<String, AreaAction> actions;

    static {
        actions = new HashMap<String, AreaAction>();
        for (AreaAction status : AreaAction.values()) {
            actions.put(status.label, status);
        }
    }

    private AreaAction(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

    public static String[] labels() {
        return actions.keySet().toArray(new String[actions.size()]);
    }

    public static AreaAction find(String label) {
        // TODO: remove debug code
        if (label.length() != label.trim().length()) {
            throw new IllegalArgumentException("Expect to see a trimmed value here."); //$NON-NLS-1$
        }
        return actions.get(label);
    }
}
