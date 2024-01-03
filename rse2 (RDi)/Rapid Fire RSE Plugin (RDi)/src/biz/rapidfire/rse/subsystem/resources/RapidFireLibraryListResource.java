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
import biz.rapidfire.core.maintenance.librarylist.shared.LibraryListKey;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.IRapidFireLibraryListResource;
import biz.rapidfire.core.model.IRapidFireNodeResource;
import biz.rapidfire.core.subsystem.IRapidFireSubSystem;
import biz.rapidfire.core.subsystem.resources.RapidFireLibraryListResourceDelegate;
import biz.rapidfire.core.subsystem.resources.RapidFireLibraryListResourceDelegate.LibraryListEntry;

public class RapidFireLibraryListResource extends AbstractResource implements IRapidFireLibraryListResource,
    Comparable<IRapidFireLibraryListResource> {

    private IRapidFireNodeResource parentNode;
    private IRapidFireJobResource parentJob;
    private RapidFireLibraryListResourceDelegate delegate;

    public static RapidFireLibraryListResource createEmptyInstance(IRapidFireJobResource job) {
        return new RapidFireLibraryListResource(job, ""); //$NON-NLS-1$
    }

    public RapidFireLibraryListResource(IRapidFireJobResource job, String library) {

        if (job == null) {
            throw new IllegalParameterException("job", null); //$NON-NLS-1$
        }

        if (library == null) {
            throw new IllegalParameterException("library", library); //$NON-NLS-1$
        }

        this.parentJob = job;
        this.delegate = new RapidFireLibraryListResourceDelegate(job.getDataLibrary(), job.getName(), library);
        super.setSubSystem((ISubSystem)job.getParentSubSystem());
    }

    public LibraryListKey getKey() {
        return new LibraryListKey(parentJob.getKey(), delegate.getName());
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

    public String getDescription() {
        return delegate.getDescription();
    }

    public void setDescription(String description) {
        delegate.setDescription(description);
    }

    public LibraryListEntry[] getLibraryListEntries() {
        return delegate.getLibraryListEntries();
    }

    public void addLibraryListEntry(int sequence, String libraryName) {
        delegate.addLibraryListEntry(sequence, libraryName);
    }

    public void reload(Shell shell) throws Exception {

        IRapidFireLibraryListResource libraryList = getParentSubSystem().getLibraryList(getParentResource(), getName(), shell);

        delegate.setDescription(libraryList.getDescription());
        delegate.clearLibraryList();
        LibraryListEntry[] libraryListEntries = libraryList.getLibraryListEntries();
        for (LibraryListEntry libraryListEntry : libraryListEntries) {
            delegate.addLibraryListEntry(libraryListEntry.getSequenceNumber(), libraryListEntry.getLibraryName());
        }
    }

    public int compareTo(IRapidFireLibraryListResource resource) {
        return delegate.compareTo(resource);
    }

    @Override
    public String toString() {
        return delegate.toString();
    }
}
