/*******************************************************************************
 * Copyright (c) 2012-2017 iSphere Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.model;

import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.JobName;
import biz.rapidfire.core.model.Phase;
import biz.rapidfire.core.model.Status;
import biz.rapidfire.core.subsystem.IRapidFireSubSystem;

import com.ibm.as400.access.QSYSObjectPathName;
import com.ibm.etools.systems.subsystems.SubSystem;
import com.ibm.etools.systems.subsystems.impl.AbstractResource;


public class RapidFireJobResource extends AbstractResource implements IRapidFireJobResource, Comparable<RapidFireJobResource> {

    private String name;
    private String description;
    private boolean doCreateEnvironment;
    private QSYSObjectPathName jobQueue;
    private Status status;
    private Phase phase;
    private boolean isError;
    private String errorText;
    private JobName batchJob;
    private boolean isStopApplyChanges;
    private String cmoneFormNumber;
    private String library;

    public RapidFireJobResource(String library, String name, String description, boolean doCreateEnvironment, QSYSObjectPathName jobQueue) {

        this.library = library;
        this.name = name;
        this.description = description;
        this.doCreateEnvironment = doCreateEnvironment;
        this.jobQueue = jobQueue;
    }

    /*
     * IRapidFireJobResource methods
     */

    public String getParent() {
        return library;
    }

    public String getLibrary() {
        return library;
    }

    public String getName() {
        return name;
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

    public QSYSObjectPathName getJobQueue() {
        return jobQueue;
    }

    public void setJobQueue(QSYSObjectPathName jobQueue) {
        this.jobQueue = jobQueue;
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

    public int compareTo(RapidFireJobResource resource) {

        if (resource == null || resource.getName() == null) {
            return 1;
        } else if (getName() == null) {
            return -1;
        }

        return getName().compareTo(resource.getName());
    }

    public void setParentSubSystem(IRapidFireSubSystem subSystem) {
        super.setSubSystem((SubSystem)subSystem);
    }

    public IRapidFireSubSystem getParentSubSystem() {
        return (IRapidFireSubSystem)super.getSubSystem();
    }

    @Override
    public String toString() {
        return getName();
    }

}
