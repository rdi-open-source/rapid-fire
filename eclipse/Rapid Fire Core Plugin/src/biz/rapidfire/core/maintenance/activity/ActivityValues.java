/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.activity;

import java.sql.Time;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.maintenance.IResourceValues;
import biz.rapidfire.core.maintenance.activity.shared.ActivityKey;

public class ActivityValues implements IResourceValues {

    private ActivityKey key;
    private Time endTime;
    private boolean isActive;

    public ActivityKey getKey() {
        ensureKey();
        return key;
    }

    public void setKey(ActivityKey key) {
        this.key = key;
    }

    public String getJobName() {
        return key.getJobName();
    }

    public Time getStartTime() {
        return key.getStartTime();
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActivity(boolean active) {
        this.isActive = active;
    }

    private void ensureKey() {

        if (key == null) {
            key = new ActivityKey(null, null);
        }
    }

    @Override
    public ActivityValues clone() {

        try {

            ActivityValues activityValues = (ActivityValues)super.clone();
            activityValues.key = (ActivityKey)getKey().clone();

            return activityValues;

        } catch (CloneNotSupportedException e) {
            RapidFireCorePlugin.logError("*** Clone not supported. ***", e); //$NON-NLS-1$
            throw new biz.rapidfire.core.exceptions.CloneNotSupportedException(ExceptionHelper.getLocalizedMessage(e), e);
        }
    }

    @Override
    public String toString() {
        return getStartTime() + " (" + Boolean.toString(isActive) + ")";
    }
}
