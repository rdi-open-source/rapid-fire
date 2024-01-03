/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem.resources;

import org.eclipse.rse.core.subsystems.AbstractResource;
import org.eclipse.rse.core.subsystems.ISubSystem;
import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.exceptions.IllegalParameterException;
import biz.rapidfire.core.maintenance.library.shared.LibraryKey;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.IRapidFireLibraryResource;
import biz.rapidfire.core.model.IRapidFireNodeResource;
import biz.rapidfire.core.subsystem.IRapidFireSubSystem;
import biz.rapidfire.core.subsystem.resources.RapidFireLibraryResourceDelegate;

public class RapidFireLibraryResource extends AbstractResource implements IRapidFireLibraryResource, Comparable<IRapidFireLibraryResource> {

    private IRapidFireNodeResource parentNode;
    private IRapidFireJobResource parentJob;
    private RapidFireLibraryResourceDelegate delegate;

    public static RapidFireLibraryResource createEmptyInstance(IRapidFireJobResource job) {
        return new RapidFireLibraryResource(job, ""); //$NON-NLS-1$
    }

    public RapidFireLibraryResource(IRapidFireJobResource job, String library) {

        if (job == null) {
            throw new IllegalParameterException("job", null); //$NON-NLS-1$
        }

        if (library == null) {
            throw new IllegalParameterException("library", null); //$NON-NLS-1$
        }

        this.parentJob = job;
        this.delegate = new RapidFireLibraryResourceDelegate(job.getDataLibrary(), job.getName(), library);
        super.setSubSystem((ISubSystem)job.getParentSubSystem());
    }

    public LibraryKey getKey() {
        return new LibraryKey(parentJob.getKey(), delegate.getName());
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

    public IRapidFireJobResource getParentJob() {
        return this.parentJob;
    }

    public IRapidFireJobResource getParentResource() {
        return this.parentJob;
    }

    public IRapidFireNodeResource getParentNode() {
        return parentNode;
    }

    public void setParentNode(IRapidFireNodeResource parentNode) {
        this.parentNode = parentNode;
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

    public void reload(Shell shell) throws Exception {

        IRapidFireLibraryResource library = getParentSubSystem().getLibrary(getParentResource(), getName(), shell);

        delegate.setShadowLibrary(library.getShadowLibrary());
    }

    public int compareTo(IRapidFireLibraryResource resource) {
        return delegate.compareTo(resource);
    }

    @Override
    public String toString() {
        return delegate.toString();
    }
}
