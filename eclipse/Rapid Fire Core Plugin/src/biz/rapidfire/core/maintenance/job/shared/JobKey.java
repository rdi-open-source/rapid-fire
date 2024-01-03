/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.job.shared;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.maintenance.IResourceKey;

public class JobKey implements IResourceKey {

    private String jobName;

    public static JobKey createNew() {

        JobKey key = new JobKey(""); //$NON-NLS-1$

        return key;
    }

    public JobKey(String jobName) {
        if (jobName != null) {
            this.jobName = jobName.trim();
        }
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName.trim();
    }

    @Override
    public String toString() {

        StringBuilder buffer = new StringBuilder();
        buffer.append(getJobName());

        return buffer.toString();
    }

    @Override
    public Object clone() {
        try {

            JobKey jobKey = (JobKey)super.clone();

            return jobKey;

        } catch (CloneNotSupportedException e) {
            RapidFireCorePlugin.logError("*** Clone not supported. ***", e); //$NON-NLS-1$
            throw new biz.rapidfire.core.exceptions.CloneNotSupportedException(ExceptionHelper.getLocalizedMessage(e), e);
        }
    }
}
