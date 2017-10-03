/*******************************************************************************
 * Copyright (c) 2012-2017 iSphere Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem;

import biz.rapidfire.core.model.IRapidFireInstanceResource;
import biz.rapidfire.core.subsystem.AbstractRapidFireSubSystemAttributes;
import biz.rapidfire.rse.model.RapidFireInstanceResource;

public class RapidFireSubSystemAttributes extends AbstractRapidFireSubSystemAttributes {

    private RapidFireSubSystem subSystem;

    public RapidFireSubSystemAttributes(RapidFireSubSystem subSystem) {
        super();

        this.subSystem = subSystem;
    }

    protected IRapidFireInstanceResource createRapidFireInstanceResource(String name, String library) {

        IRapidFireInstanceResource resource = new RapidFireInstanceResource(subSystem, name, library);

        return resource;
    }

    protected void saveSubSystem() throws Exception {

        subSystem.getSubSystemConfiguration().saveSubSystem(subSystem);
    }

    protected String getResourceKey(IRapidFireInstanceResource resource) {

        return getResourceKey(resource.getLibrary());
    }

    protected String getResourceKey(String library) {

        return subSystem.getHostAliasName() + "." + library; //$NON-NLS-1$
    }

    protected String getVendorAttribute(String key) {
        return subSystem.getVendorAttribute(key);
    }

    protected void setVendorAttribute(String key, String value) {
        subSystem.setVendorAttribute(key, value);
    }

    protected void removeVendorAttribute(String key) {
        subSystem.removeVendorAttribute(key);
    }
}
