/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.notification.shared;

import java.util.HashMap;
import java.util.Map;

public enum MessageQueueLibrary {
    LIBL ("*LIBL"), //$NON-NLS-1$
    CURLIB ("*CURLIB"); //$NON-NLS-1$

    private String label;

    private static Map<String, MessageQueueLibrary> messageQueue;

    static {
        messageQueue = new HashMap<String, MessageQueueLibrary>();
        for (MessageQueueLibrary status : MessageQueueLibrary.values()) {
            messageQueue.put(status.label, status);
        }
    }

    private MessageQueueLibrary(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

    public static String[] labels() {
        return messageQueue.keySet().toArray(new String[messageQueue.size()]);
    }

    public static MessageQueueLibrary find(String label) {
        // TODO: remove debug code
        if (label.length() != label.trim().length()) {
            throw new IllegalArgumentException("Expect to see a trimmed value here."); //$NON-NLS-1$
        }
        return messageQueue.get(label);
    }

}
