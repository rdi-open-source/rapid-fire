/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.activity.shared;

import java.util.HashMap;
import java.util.Map;

import biz.rapidfire.core.maintenance.shared.IResourceAction;

public enum ActivityAction implements IResourceAction {
    CREATE ("*CREATE"),
    CHANGE ("*CHANGE"),
    DISPLAY ("*DISPLAY");

    private String label;

    private static Map<String, ActivityAction> actions;

    static {
        actions = new HashMap<String, ActivityAction>();
        for (ActivityAction status : ActivityAction.values()) {
            actions.put(status.label, status);
        }
    }

    private ActivityAction(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

    public static String[] labels() {
        return actions.keySet().toArray(new String[actions.size()]);
    }

    public static ActivityAction find(String label) {
        // TODO: remove debug code
        if (label.length() != label.trim().length()) {
            throw new IllegalArgumentException("Expect to see a trimmed value here."); //$NON-NLS-1$
        }
        return actions.get(label);
    }
}
