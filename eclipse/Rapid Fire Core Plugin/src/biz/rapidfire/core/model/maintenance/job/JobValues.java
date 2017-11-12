/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model.maintenance.job;

public class JobValues {

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
        this.description = description;
    }

    public String getCreateEnvironment() {
        return createEnvironment;
    }

    public void setCreateEnvironment(String createEnvironment) {
        this.createEnvironment = createEnvironment;
    }

    public String getJobQueueName() {
        return jobQueueName;
    }

    public void setJobQueueName(String jobQueueName) {
        this.jobQueueName = jobQueueName;
    }

    public String getJobQueueLibraryName() {
        return jobQueueLibraryName;
    }

    public void setJobQueueLibraryName(String jobQueueLibraryName) {
        this.jobQueueLibraryName = jobQueueLibraryName;
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
}
