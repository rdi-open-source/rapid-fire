/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem.resources;

import biz.rapidfire.core.exceptions.IllegalParameterException;
import biz.rapidfire.core.helpers.StringHelper;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.JobName;
import biz.rapidfire.core.model.Phase;
import biz.rapidfire.core.model.Status;
import biz.rapidfire.core.subsystem.IRapidFireSubSystem;
import biz.rapidfire.core.subsystem.resources.RapidFireJobResourceDelegate;

import com.ibm.etools.systems.subsystems.SubSystem;
import com.ibm.etools.systems.subsystems.impl.AbstractResource;

public class RapidFireJobResource extends AbstractResource implements IRapidFireJobResource, Comparable<IRapidFireJobResource> {

    private RapidFireJobResourceDelegate delegate;

    public static RapidFireJobResource createEmptyInstance(IRapidFireSubSystem subSystem, String dataLibrary) {
        return new RapidFireJobResource(subSystem, dataLibrary, ""); //$NON-NLS-1$
    }

    public RapidFireJobResource(IRapidFireSubSystem subSystem, String dataLibrary, String job) {

        if (subSystem == null) {
            throw new IllegalParameterException("subSystem", null); //$NON-NLS-1$
        }

        if (StringHelper.isNullOrEmpty(dataLibrary)) {
            throw new IllegalParameterException("dataLibrary", dataLibrary); //$NON-NLS-1$
        }

        if (job == null) {
            throw new IllegalParameterException("job", job); //$NON-NLS-1$
        }

        this.delegate = new RapidFireJobResourceDelegate(dataLibrary, job);
        super.setSubSystem((SubSystem)subSystem);
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

    /*
     * IRapidFireJobResource methods
     */

    public String getName() {
        return delegate.getName();
    }

    public String getDescription() {
        return delegate.getDescription();
    }

    public void setDescription(String description) {
        delegate.setDescription(description);
    }

    public boolean isDoCreateEnvironment() {
        return delegate.isDoCreateEnvironment();
    }

    public void setDoCreateEnvironment(boolean doCreateEnvironment) {
        delegate.setDoCreateEnvironment(doCreateEnvironment);
    }

    public String getJobQueueName() {
        return delegate.getJobQueueName();
    }

    public void setJobQueueName(String jobQueueName) {
        delegate.setJobQueueName(jobQueueName);
    }

    public String getJobQueueLibrary() {
        return delegate.getJobQueueLibrary();
    }

    public void setJobQueueLibrary(String jobQueueLibrary) {
        delegate.setJobQueueLibrary(jobQueueLibrary);
    }

    public Status getStatus() {
        return delegate.getStatus();
    }

    public void setStatus(Status status) {
        delegate.setStatus(status);
    }

    public Phase getPhase() {
        return delegate.getPhase();
    }

    public void setPhase(Phase phase) {
        delegate.setPhase(phase);
    }

    public boolean isError() {
        return delegate.isError();
    }

    public void setError(boolean isError) {
        delegate.setError(isError);
    }

    public String getErrorText() {
        return delegate.getErrorText();
    }

    public void setErrorText(String errorText) {
        delegate.setErrorText(errorText);
    }

    public boolean isStopApplyChanges() {
        return delegate.isStopApplyChanges();
    }

    public void setStopApplyChanges(boolean isStopApplyChanges) {
        delegate.setStopApplyChanges(isStopApplyChanges);
    }

    public String getCmoneFormNumber() {
        return delegate.getCmoneFormNumber();
    }

    public void setCmoneFormNumber(String cmoneFormNumber) {
        delegate.setCmoneFormNumber(cmoneFormNumber);
    }

    public JobName getBatchJob() {
        return delegate.getBatchJob();
    }

    public void setBatchJob(JobName job) {
        delegate.setBatchJob(job);
    }

    public int compareTo(IRapidFireJobResource resource) {
        return delegate.compareTo(resource);
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

}
