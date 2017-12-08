/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model;

import biz.rapidfire.core.maintenance.job.shared.JobKey;
import biz.rapidfire.core.subsystem.RapidFireFilter;

public interface IRapidFireJobResource extends IRapidFireResource {

    /*
     * Key attributes
     */

    public JobKey getKey();

    public String getName();

    /*
     * Data attributes
     */

    public String getDescription();

    public void setDescription(String description);

    public boolean isDoCreateEnvironment();

    public void setDoCreateEnvironment(boolean doCreateEnvironment);

    public String getJobQueueName();

    public void setJobQueueName(String jobQueueName);

    public String getJobQueueLibrary();

    public void setJobQueueLibrary(String jobQueueLibrary);

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

    public void setFilter(RapidFireFilter filter);

    public RapidFireFilter getFilter();

    public JobName getBatchJob();
}
