/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.job;

import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.maintenance.Result;
import biz.rapidfire.core.model.maintenance.job.shared.JobAction;

public abstract class AbstractJobActionHandler extends AbstractJobMaintenanceHandler {

    public AbstractJobActionHandler(JobAction jobAction) {
        super(null, jobAction);
    }

    protected Result initialize(IRapidFireJobResource job) throws Exception {
        return Result.createSuccessResult();
    }

    protected abstract void performAction(IRapidFireJobResource job) throws Exception;

    protected void terminate() throws Exception {
    }
}
