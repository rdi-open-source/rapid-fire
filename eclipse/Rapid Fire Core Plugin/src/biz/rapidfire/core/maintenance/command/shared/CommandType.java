/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.command.shared;

import java.util.HashMap;
import java.util.Map;

public enum CommandType {
    PRERUN ("*PRERUN"), //$NON-NLS-1$
    COMPILE ("*COMPILE"), //$NON-NLS-1$
    POSTRUN ("*POSTRUN"); //$NON-NLS-1$

    private String label;

    private static Map<String, CommandType> commandTypes;

    static {
        commandTypes = new HashMap<String, CommandType>();
        for (CommandType status : CommandType.values()) {
            commandTypes.put(status.label, status);
        }
    }

    private CommandType(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

    public static String[] labels() {
        return commandTypes.keySet().toArray(new String[commandTypes.size()]);
    }

    public static CommandType find(String label) {
        // TODO: remove debug code
        if (label.length() != label.trim().length()) {
            throw new IllegalArgumentException("Expect to see a trimmed value here."); //$NON-NLS-1$
        }
        return commandTypes.get(label);
    }

}
