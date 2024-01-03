/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.notification.shared;

import java.util.HashMap;
import java.util.Map;

public enum NotificationType {
    USR ("*USR"), //$NON-NLS-1$
    MSGQ ("*MSGQ"); //$NON-NLS-1$

    private String label;

    private static Map<String, NotificationType> notificationsTypes;

    static {
        notificationsTypes = new HashMap<String, NotificationType>();
        for (NotificationType status : NotificationType.values()) {
            notificationsTypes.put(status.label, status);
        }
    }

    private NotificationType(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

    public String[] labels() {
        return notificationsTypes.keySet().toArray(new String[notificationsTypes.size()]);
    }

    public static NotificationType find(String label) {
        // TODO: remove debug code
        if (label.length() != label.trim().length()) {
            throw new IllegalArgumentException("Expect to see a trimmed value here."); //$NON-NLS-1$
        }
        return notificationsTypes.get(label);
    }
}
