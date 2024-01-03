/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.subsystem.resources;

import java.sql.Time;

import biz.rapidfire.core.model.IRapidFireActivityResource;

public class RapidFireActivityResourceDelegate implements Comparable<IRapidFireActivityResource> {

    private String dataLibrary;
    private String job;
    private Time startTime;
    private Time endTime;
    private boolean isActive;

    public RapidFireActivityResourceDelegate(String dataLibrary, String job, Time startTime) {

        this.dataLibrary = dataLibrary;
        this.job = job;
        this.startTime = startTime;
    }

    /*
     * IRapidFireResource methods
     */

    public String getDataLibrary() {
        return dataLibrary;
    }

    /*
     * IRapidFireActivityResource methods
     */

    public String getJob() {
        return job;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
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
        isActive = active;
    }

    public int compareTo(IRapidFireActivityResource resource) {

        if (resource == null) {
            return 1;
        }

        int result = resource.getDataLibrary().compareTo(getDataLibrary());
        if (result != 0) {
            return result;
        }

        result = resource.getJob().compareTo(getJob());
        if (result != 0) {
            return result;
        }

        return getStartTime().compareTo(resource.getStartTime());
    }

    @Override
    public String toString() {
        return getStartTime().toString() + " to " + getEndTime(); //$NON-NLS-1$
    }

}
