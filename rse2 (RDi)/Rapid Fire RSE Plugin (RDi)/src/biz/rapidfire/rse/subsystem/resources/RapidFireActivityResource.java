/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem.resources;

import java.sql.Time;

import org.eclipse.rse.core.subsystems.AbstractResource;
import org.eclipse.rse.core.subsystems.ISubSystem;

import biz.rapidfire.core.exceptions.IllegalParameterException;
import biz.rapidfire.core.maintenance.wizard.shared.IWizardSupporter;
import biz.rapidfire.core.model.IRapidFireActivityResource;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.subsystem.IRapidFireSubSystem;
import biz.rapidfire.core.subsystem.resources.RapidFireActivityResourceDelegate;

public class RapidFireActivityResource extends AbstractResource implements IWizardSupporter, IRapidFireActivityResource<IRapidFireJobResource>,
    Comparable<IRapidFireActivityResource> {

    private IRapidFireJobResource parentJob;
    private RapidFireActivityResourceDelegate delegate;

    public static RapidFireActivityResource createEmptyInstance(IRapidFireJobResource job) {
        return new RapidFireActivityResource(job, null);
    }

    public RapidFireActivityResource(IRapidFireJobResource job, Time time) {

        if (job == null) {
            throw new IllegalParameterException("job", null); //$NON-NLS-1$
        }

        this.parentJob = job;
        this.delegate = new RapidFireActivityResourceDelegate(job.getDataLibrary(), job.getName(), time);
        super.setSubSystem((ISubSystem)job.getParentSubSystem());
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

    public IRapidFireJobResource getParent() {
        return this.parentJob;
    }

    /*
     * IRapidFireActivityResource methods
     */

    public String getJob() {
        return delegate.getJob();
    }

    public Time getStartTime() {
        return delegate.getStartTime();
    }

    public void setStartTime(Time startTime) {
        delegate.setStartTime(startTime);
    }

    public Time getEndTime() {
        return delegate.getEndTime();
    }

    public void setEndTime(Time endTime) {
        delegate.setEndTime(endTime);
    }

    public boolean isActive() {
        return delegate.isActive();
    }

    public void setActivity(boolean active) {
        delegate.setActivity(active);
    }

    public int compareTo(IRapidFireActivityResource resource) {
        return delegate.compareTo(resource);
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

}
