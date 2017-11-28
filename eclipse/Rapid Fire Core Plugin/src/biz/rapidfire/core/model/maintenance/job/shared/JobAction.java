/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model.maintenance.job.shared;

import java.util.HashMap;
import java.util.Map;

public enum JobAction {
    CREATE ("*CREATE"),
    COPY ("*COPY"),
    CHANGE ("*CHANGE"),
    DELETE ("*DELETE"),
    DISPLAY ("*DISPLAY"),
    MNTLIB ("*MNTLIB"),
    MNTLIBL ("*MNTLIBL"),
    MNTFILE ("*MNTFILE"),
    MNTSTBN ("*MNTSTBN"),
    MNTSCDE ("*MNTSCDE"),
    TSTJOB ("*TSTJOB"),
    STRJOB ("*STRJOB"),
    ENDJOB ("*ENDJOB"),
    RESETJOB ("*RESETJOB"),
    RESETJOBA ("*RESETJOBA"),
    DSPSTS ("*DSPSTS");

    private String label;

    private static Map<String, JobAction> actions;

    static {
        actions = new HashMap<String, JobAction>();
        for (JobAction status : JobAction.values()) {
            actions.put(status.label, status);
        }
    }

    private JobAction(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

    public static String[] labels() {
        return actions.keySet().toArray(new String[actions.size()]);
    }

    public static JobAction find(String label) {
        // TODO: remove debug code
        if (label.length() != label.trim().length()) {
            throw new IllegalArgumentException("Expect to see a trimmed value here."); //$NON-NLS-1$
        }
        return actions.get(label);
    }
}
