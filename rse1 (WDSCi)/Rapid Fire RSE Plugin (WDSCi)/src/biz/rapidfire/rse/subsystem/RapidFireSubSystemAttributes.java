/*******************************************************************************
 * Copyright (c) 2012-2017 iSphere Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem;

import biz.rapidfire.core.subsystem.AbstractRapidFireSubSystemAttributes;

public class RapidFireSubSystemAttributes extends AbstractRapidFireSubSystemAttributes {

    private RapidFireSubSystem subSystem;

    public RapidFireSubSystemAttributes(RapidFireSubSystem subSystem) {
        super();

        this.subSystem = subSystem;
    }

    protected void saveSubSystem() throws Exception {
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
