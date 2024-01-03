/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.model;

import java.sql.Time;

import biz.rapidfire.core.maintenance.activity.shared.ActivityKey;

public interface IRapidFireActivityResource extends IRapidFireChildResource<IRapidFireJobResource> {

    public ActivityKey getKey();

    /*
     * Key attributes
     */

    public String getJob();

    public Time getStartTime();

    /*
     * Other attributes
     */

    public void setStartTime(Time startTime);

    public Time getEndTime();

    public void setEndTime(Time endTime);

    public boolean isActive();

    public void setActivity(boolean active);
}
