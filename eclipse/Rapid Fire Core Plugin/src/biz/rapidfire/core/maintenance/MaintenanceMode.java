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

import biz.rapidfire.core.maintenance.activity.shared.ActivityAction;
import biz.rapidfire.core.maintenance.area.shared.AreaAction;
import biz.rapidfire.core.maintenance.command.shared.CommandAction;
import biz.rapidfire.core.maintenance.conversion.shared.ConversionAction;
import biz.rapidfire.core.maintenance.file.shared.FileAction;
import biz.rapidfire.core.maintenance.job.shared.JobAction;
import biz.rapidfire.core.maintenance.library.shared.LibraryAction;
import biz.rapidfire.core.maintenance.librarylist.shared.LibraryListAction;

/**
 * Specifies the maintenance modes as used in the GUI. The stored procedure
 * action modes are defined separately for each entity.
 * 
 * @see ActivityAction
 * @see AreaAction
 * @see CommandAction
 * @see ConversionAction
 * @see FileAction
 * @see JobAction
 * @see LibraryAction
 * @see LibraryListAction
 * @see NotificcationAction
 */
public enum MaintenanceMode {
    CREATE ("*CREATE"), //$NON-NLS-1$
    COPY ("*COPY"), //$NON-NLS-1$
    CHANGE ("*CHANGE"), //$NON-NLS-1$
    DELETE ("*DELETE"), //$NON-NLS-1$
    DISPLAY ("*DISPLAY"); //$NON-NLS-1$

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
