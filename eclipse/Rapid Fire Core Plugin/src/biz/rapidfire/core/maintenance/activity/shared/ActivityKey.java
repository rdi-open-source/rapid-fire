/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.activity.shared;

import java.sql.Time;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.maintenance.IResourceKey;
import biz.rapidfire.core.maintenance.job.shared.JobKey;

public class ActivityKey implements IResourceKey {

    private JobKey jobKey;
    private Time startTime;

    public static ActivityKey createNew(JobKey jobKey) {

        ActivityKey key = new ActivityKey(jobKey, null);

        return key;
    }

    public ActivityKey(JobKey jobKey, Time startTime) {

        this.jobKey = jobKey;
        this.startTime = startTime;
    }

    public String getJobName() {
        return jobKey.getJobName();
    }

    public Time getStartTime() {
        return startTime;
    }

    @Override
    public Object clone() {
        try {

            ActivityKey jobKey = (ActivityKey)super.clone();

            return jobKey;

        } catch (CloneNotSupportedException e) {
            RapidFireCorePlugin.logError("*** Clone not supported. ***", e); //$NON-NLS-1$
            throw new biz.rapidfire.core.exceptions.CloneNotSupportedException(ExceptionHelper.getLocalizedMessage(e), e);
        }
    }
}
