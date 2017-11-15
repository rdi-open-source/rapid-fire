/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem.resources;

import biz.rapidfire.core.model.IRapidFireLibraryResource;
import biz.rapidfire.core.subsystem.IRapidFireSubSystem;
import biz.rapidfire.core.subsystem.resources.RapidFireLibraryResourceDelegate;

import com.ibm.etools.systems.subsystems.SubSystem;
import com.ibm.etools.systems.subsystems.impl.AbstractResource;

public class RapidFireLibraryResource extends AbstractResource implements IRapidFireLibraryResource, Comparable<IRapidFireLibraryResource> {

    private RapidFireLibraryResourceDelegate delegate;

    public RapidFireLibraryResource(String dataLibrary, String job, String library) {
        this.delegate = new RapidFireLibraryResourceDelegate(dataLibrary, job, library);
    }

    /*
     * IRapidFireResource methods
     */

    public String getDataLibrary() {
        return delegate.getDataLibrary();
    }

    public IRapidFireSubSystem getParentSubSystem() {
        return (IRapidFireSubSystem)super.getSubSystem();
    }

    public void setParentSubSystem(IRapidFireSubSystem subSystem) {
        super.setSubSystem((SubSystem)subSystem);
    }

    /*
     * IRapidFireFileResource methods
     */

    public String getJob() {
        return delegate.getJob();
    }

    public String getName() {
        return delegate.getName();
    }

    public String getShadowLibrary() {
        return delegate.getShadowLibrary();
    }

    public void setShadowLibrary(String shadowLibrary) {
        delegate.setShadowLibrary(shadowLibrary);
    }

    public int compareTo(IRapidFireLibraryResource resource) {
        return delegate.compareTo(resource);
    }

    @Override
    public String toString() {
        return delegate.toString();
    }
}
