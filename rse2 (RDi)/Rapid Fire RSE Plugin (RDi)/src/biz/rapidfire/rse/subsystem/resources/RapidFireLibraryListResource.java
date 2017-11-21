/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem.resources;

import org.eclipse.rse.core.subsystems.AbstractResource;
import org.eclipse.rse.core.subsystems.SubSystem;

import biz.rapidfire.core.exceptions.IllegalParameterException;
import biz.rapidfire.core.helpers.StringHelper;
import biz.rapidfire.core.model.IRapidFireLibraryListResource;
import biz.rapidfire.core.subsystem.IRapidFireSubSystem;
import biz.rapidfire.core.subsystem.resources.RapidFireLibraryListResourceDelegate;

public class RapidFireLibraryListResource extends AbstractResource implements IRapidFireLibraryListResource,
    Comparable<IRapidFireLibraryListResource> {

    private RapidFireLibraryListResourceDelegate delegate;

    public static RapidFireLibraryListResource createEmptyInstance(String dataLibrary, String job) {
        return new RapidFireLibraryListResource(dataLibrary, job, ""); //$NON-NLS-1$
    }

    public RapidFireLibraryListResource(String dataLibrary, String job, String library) {

        if (StringHelper.isNullOrEmpty(dataLibrary)) {
            throw new IllegalParameterException("dataLibrary", dataLibrary); //$NON-NLS-1$
        }

        if (StringHelper.isNullOrEmpty(job)) {
            throw new IllegalParameterException("job", job); //$NON-NLS-1$
        }

        if (library == null) {
            throw new IllegalParameterException("library", library); //$NON-NLS-1$
        }

        this.delegate = new RapidFireLibraryListResourceDelegate(dataLibrary, job, library);
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

    public String getDescription() {
        return delegate.getDescription();
    }

    public void setDescription(String description) {
        delegate.setDescription(description);
    }

    public int compareTo(IRapidFireLibraryListResource resource) {
        return delegate.compareTo(resource);
    }

    @Override
    public String toString() {
        return delegate.toString();
    }
}
