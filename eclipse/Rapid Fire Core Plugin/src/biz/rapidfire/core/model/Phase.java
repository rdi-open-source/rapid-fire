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

public enum Phase {
    NONE ("*NONE"), //$NON-NLS-1$
    COPY_RECORDS ("*CPY-RCD"), //$NON-NLS-1$
    APPLY_CHANGES ("*APY-CHG"), //$NON-NLS-1$
    READY_PRODUCTION ("*RDY-PRD"), //$NON-NLS-1$
    ABORT ("*ABORT"); //$NON-NLS-1$

    private String label;

    private static Map<String, Phase> phases;

    static {
        phases = new HashMap<String, Phase>();
        for (Phase phase : Phase.values()) {
            phases.put(phase.label, phase);
        }
    }

    private Phase(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

    public String[] labels() {
        return phases.keySet().toArray(new String[phases.size()]);
    }

    public static Phase find(String label) {
        // TODO: remove debug code
        if (label.length() != label.trim().length()) {
            throw new IllegalArgumentException("Expect to see a trimmed value here."); //$NON-NLS-1$
        }
        return phases.get(label);
    }
}
