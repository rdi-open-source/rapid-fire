/*******************************************************************************
 * Copyright (c) 2012-2017 iSphere Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model;

import biz.rapidfire.core.subsystem.IRapidFireSubSystem;

import com.ibm.as400.access.QSYSObjectPathName;

public interface IRapidFireJobResource extends IRapidFireResource {

    public String getParent();

    public String getLibrary();

    public String getDescription();

    public void setDescription(String description);

    public boolean isDoCreateEnvironment();

    public void setDoCreateEnvironment(boolean doCreateEnvironment);

    public QSYSObjectPathName getJobQueue();

    public void setJobQueue(QSYSObjectPathName jobQueue);

    public Status getStatus();

    public void setStatus(Status status);

    public Phase getPhase();

    public void setPhase(Phase phase);

    public boolean isError();

    public void setError(boolean isError);

    public String getErrorText();

    public void setErrorText(String errorText);

    public boolean isStopApplyChanges();

    public void setStopApplyChanges(boolean isStopApplyChanges);

    public String getCmoneFormNumber();

    public void setCmoneFormNumber(String cmoneFormNumber);

    public void setBatchJob(JobName job);

    public JobName getBatchJob();

    public void setParentSubSystem(IRapidFireSubSystem subSystem);

    public IRapidFireSubSystem getParentSubSystem();
}
