/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.subsystem.resources;

import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.JobName;
import biz.rapidfire.core.model.Phase;
import biz.rapidfire.core.model.Status;
import biz.rapidfire.core.subsystem.RapidFireFilter;

public class RapidFireJobResourceDelegate implements Comparable<IRapidFireJobResource> {

    private String dataLibrary;
    private String job;
    private String description;
    private boolean doCreateEnvironment;
    private String jobQueueName;
    private String jobQueueLibrary;
    private boolean doCancelASPThresholdExceeds;
    private Status status;
    private Phase phase;
    private boolean isError;
    private String errorText;
    private JobName batchJob;
    private boolean isStopApplyChanges;
    private String cmoneFormNumber;
    private RapidFireFilter filter;

    public RapidFireJobResourceDelegate(String dataLibrary, String job) {

        this.dataLibrary = dataLibrary;
        this.job = job;
    }

    /*
     * IRapidFireResource methods
     */

    public String getDataLibrary() {
        return dataLibrary;
    }

    /*
     * IRapidFireJobResource methods
     */

    public String getName() {
        return job;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDoCreateEnvironment() {
        return doCreateEnvironment;
    }

    public void setDoCreateEnvironment(boolean doCreateEnvironment) {
        this.doCreateEnvironment = doCreateEnvironment;
    }

    public String getJobQueueName() {
        return jobQueueName;
    }

    public void setJobQueueName(String jobQueueName) {
        this.jobQueueName = jobQueueName;
    }

    public String getJobQueueLibrary() {
        return jobQueueLibrary;
    }

    public void setJobQueueLibrary(String jobQueueLibrary) {
        this.jobQueueLibrary = jobQueueLibrary;
    }

    public boolean isDoCancelASPThresholdExceeds() {
        return doCancelASPThresholdExceeds;
    }

    public void setDoCancelASPThresholdExceeds(boolean doCancelASPThresholdExceeds) {
        this.doCancelASPThresholdExceeds = doCancelASPThresholdExceeds;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean isError) {
        this.isError = isError;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }

    public boolean isStopApplyChanges() {
        return isStopApplyChanges;
    }

    public void setStopApplyChanges(boolean isStopApplyChanges) {
        this.isStopApplyChanges = isStopApplyChanges;
    }

    public String getCmoneFormNumber() {
        return cmoneFormNumber;
    }

    public void setCmoneFormNumber(String cmoneFormNumber) {
        this.cmoneFormNumber = cmoneFormNumber;
    }

    public void setBatchJob(JobName job) {
        this.batchJob = job;
    }

    public JobName getBatchJob() {
        return batchJob;
    }

    public RapidFireFilter getFilter() {
        return this.filter;
    }

    public void setFilter(RapidFireFilter filter) {
        this.filter = filter;
    }

    public int compareTo(IRapidFireJobResource resource) {

        int result = resource.getDataLibrary().compareTo(getDataLibrary());
        if (result != 0) {
            return result;
        }

        return getName().compareTo(resource.getName());
    }

    @Override
    public String toString() {

        StringBuilder buffer = new StringBuilder();

        buffer.append(getName());
        buffer.append(" ("); //$NON-NLS-1$
        buffer.append(getStatus().label());
        buffer.append(")"); //$NON-NLS-1$

        return buffer.toString();
    }

}
