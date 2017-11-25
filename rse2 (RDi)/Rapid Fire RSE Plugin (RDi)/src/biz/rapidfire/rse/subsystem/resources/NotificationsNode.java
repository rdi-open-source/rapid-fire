/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem.resources;

import org.eclipse.rse.core.subsystems.AbstractResource;

import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.rse.Messages;

public class NotificationsNode extends AbstractResource {

    private String label;
    private IRapidFireJobResource job;

    public NotificationsNode(IRapidFireJobResource job) {
        this.label = Messages.NodeText_Files;
        this.job = job;
    }

    public String getLabel() {
        return label;
    }

    public IRapidFireJobResource getJob() {
        return job;
    }

}
