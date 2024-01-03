/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.job.shared;

import java.util.HashMap;
import java.util.Map;

import biz.rapidfire.core.maintenance.shared.IResourceAction;

public enum JobAction implements IResourceAction {
    SELECT ("*SELECT"), //$NON-NLS-1$
    CREATE ("*CREATE"), //$NON-NLS-1$
    COPY ("*COPY"), //$NON-NLS-1$
    CHANGE ("*CHANGE"), //$NON-NLS-1$
    DELETE ("*DELETE"), //$NON-NLS-1$
    DISPLAY ("*DISPLAY"), //$NON-NLS-1$
    CHKACT ("*CHKACT"), //$NON-NLS-1$
    MNTLIB ("*MNTLIB"), //$NON-NLS-1$
    MNTLIBL ("*MNTLIBL"), //$NON-NLS-1$
    MNTFILE ("*MNTFILE"), //$NON-NLS-1$
    MNTSTBN ("*MNTSTBN"), //$NON-NLS-1$
    MNTAS ("*MNTAS"), //$NON-NLS-1$
    TSTJOB ("*TSTJOB"), //$NON-NLS-1$
    STRJOB ("*STRJOB"), //$NON-NLS-1$
    ENDJOB ("*ENDJOB"), //$NON-NLS-1$
    RESETJOB ("*RESETJOB"), //$NON-NLS-1$
    RESETJOBA ("*RESETJOBA"), //$NON-NLS-1$
    DSPSTS ("*DSPSTS"), //$NON-NLS-1$
    DSPERR ("*DSPERR"), //$NON-NLS-1$
    RFRJOBSTS ("*RFRJOBSTS"); //$NON-NLS-1$

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
