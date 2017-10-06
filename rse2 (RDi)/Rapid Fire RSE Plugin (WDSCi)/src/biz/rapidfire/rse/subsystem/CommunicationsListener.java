/*******************************************************************************
 * Copyright (c) 2012-2016 iSphere Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     iSphere Project Owners - Maintenance and enhancements
 *******************************************************************************/
package biz.rapidfire.rse.subsystem;

import org.eclipse.rse.core.subsystems.CommunicationsEvent;
import org.eclipse.rse.core.subsystems.ICommunicationsListener;

public class CommunicationsListener implements ICommunicationsListener {

    private RapidFireSubSystem queuedMessageSubSystem;

    public CommunicationsListener(RapidFireSubSystem queuedMessageSubSystem) {
        super();

        this.queuedMessageSubSystem = queuedMessageSubSystem;
    }

    public void communicationsStateChange(CommunicationsEvent ce) {

        if (ce.getState() == CommunicationsEvent.AFTER_CONNECT) {
            queuedMessageSubSystem.setParentConnected(true);
        } else if (ce.getState() == CommunicationsEvent.BEFORE_DISCONNECT) {
            queuedMessageSubSystem.setParentConnected(false);
        }
    }

    public boolean isPassiveCommunicationsListener() {
        return true;
    }
}
