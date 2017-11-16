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

public class LibrariesNode extends AbstractResource {

    private String label;
    private IRapidFireJobResource job;

    public LibrariesNode(IRapidFireJobResource job) {
        this.label = "Libraries";
        this.job = job;
    }

    public String getLabel() {
        return label;
    }

    public IRapidFireJobResource getJob() {
        return job;
    }

}
