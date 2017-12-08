/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.job;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.maintenance.IResourceValues;
import biz.rapidfire.core.maintenance.job.shared.JobKey;

public class JobValues implements IResourceValues {

    private JobKey key;
    private String description;
    private String createEnvironment;
    private String jobQueueName;
    private String jobQueueLibraryName;

    public JobKey getKey() {
        ensureKey();
        return key;
    }

    public void setKey(JobKey key) {
        ensureKey();
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description.trim();
    }

    public String getCreateEnvironment() {
        return createEnvironment;
    }

    public boolean isCreateEnvironment() {
        return "*YES".equals(this.createEnvironment);
    }

    public void setCreateEnvironment(String createEnvironment) {
        this.createEnvironment = createEnvironment.trim();
    }

    public void setCreateEnvironment(boolean createEnvironment) {
        if (createEnvironment) {
            this.createEnvironment = "*YES";
        } else {
            this.createEnvironment = "*NO";
        }
    }

    public String getJobQueueName() {
        return jobQueueName;
    }

    public void setJobQueueName(String jobQueueName) {
        this.jobQueueName = jobQueueName.trim();
    }

    public String getJobQueueLibraryName() {
        return jobQueueLibraryName;
    }

    public void setJobQueueLibraryName(String jobQueueLibraryName) {
        this.jobQueueLibraryName = jobQueueLibraryName.trim();
    }

    public void clear() {
        setDescription(null);
        setCreateEnvironment(null);
        setJobQueueName(null);
        setJobQueueLibraryName(null);
    }

    private void ensureKey() {

        if (key == null) {
            key = new JobKey(null);
        }
    }

    @Override
    public JobValues clone() {

        try {

            JobValues jobValues = (JobValues)super.clone();
            jobValues.setKey((JobKey)getKey().clone());

            return jobValues;

        } catch (CloneNotSupportedException e) {
            RapidFireCorePlugin.logError("*** Clone not supported. ***", e); //$NON-NLS-1$
            throw new biz.rapidfire.core.exceptions.CloneNotSupportedException(ExceptionHelper.getLocalizedMessage(e), e);
        }
    }
}
