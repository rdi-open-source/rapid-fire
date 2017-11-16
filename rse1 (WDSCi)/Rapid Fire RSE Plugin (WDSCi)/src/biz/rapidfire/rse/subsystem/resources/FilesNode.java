/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem.resources;

import biz.rapidfire.core.model.IRapidFireJobResource;

import com.ibm.etools.systems.subsystems.impl.AbstractResource;

public class FilesNode extends AbstractResource {

    private String label;
    private IRapidFireJobResource job;

    public FilesNode(IRapidFireJobResource job) {
        this.label = "Files";
        this.job = job;
    }

    public String getLabel() {
        return label;
    }

    public IRapidFireJobResource getJob() {
        return job;
    }

}
