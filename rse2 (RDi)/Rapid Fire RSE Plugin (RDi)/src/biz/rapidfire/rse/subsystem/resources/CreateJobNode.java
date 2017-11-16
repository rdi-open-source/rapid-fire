/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem.resources;

import org.eclipse.rse.core.subsystems.AbstractResource;
import org.eclipse.rse.core.subsystems.ISubSystem;

import biz.rapidfire.core.model.IRapidFireResource;
import biz.rapidfire.core.subsystem.IRapidFireSubSystem;

public class CreateJobNode extends AbstractResource implements IRapidFireResource {

    private String dataLibrary;

    public CreateJobNode(String dataLibrary) {
        this.dataLibrary = dataLibrary;
    }

    public String getDataLibrary() {
        return dataLibrary;
    }

    public void setParentSubSystem(IRapidFireSubSystem subSystem) {
        this.setSubSystem((ISubSystem)subSystem);
    }

    public IRapidFireSubSystem getParentSubSystem() {
        return (IRapidFireSubSystem)getSubSystem();
    }

}
