/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem.resources;

import org.eclipse.rse.core.subsystems.AbstractResource;

import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.subsystem.IRapidFireSubSystem;

public abstract class AbstractNodeResource extends AbstractResource {

    private IRapidFireJobResource job;
    private String label;

    public AbstractNodeResource(IRapidFireJobResource job, String label) {

        this.job = job;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public IRapidFireJobResource getJob() {
        return job;
    }

    public String getDataLibrary() {
        return job.getDataLibrary();
    }

    public IRapidFireSubSystem getParentSubSystem() {
        return job.getParentSubSystem();
    }
}
