/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model.maintenance;

import java.util.HashMap;
import java.util.Map;

public enum MaintenanceMode {
    MODE_CREATE ("*CREATE"), //$NON-NLS-1$
    MODE_COPY ("*COPY"), //$NON-NLS-1$
    MODE_CHANGE ("*CHANGE"), //$NON-NLS-1$
    MODE_DELETE ("*DELETE"), //$NON-NLS-1$
    MODE_DISPLAY ("*DISPLAY"); //$NON-NLS-1$

    private String label;

    private static Map<String, MaintenanceMode> maintenanceModes;

    static {
        maintenanceModes = new HashMap<String, MaintenanceMode>();
        for (MaintenanceMode mode : MaintenanceMode.values()) {
            maintenanceModes.put(mode.label, mode);
        }
    }

    public String label() {
        return label;
    }

    private MaintenanceMode(String label) {
        this.label = label;
    }

}
