/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.job;

import biz.rapidfire.core.maintenance.Result;
import biz.rapidfire.core.maintenance.job.shared.JobAction;
import biz.rapidfire.core.model.IRapidFireJobResource;

public abstract class AbstractJobActionHandler extends AbstractJobMaintenanceHandler {

    public AbstractJobActionHandler(JobAction jobAction) {
        super(null, jobAction);
    }

    @Override
    protected Result initialize(IRapidFireJobResource job) throws Exception {
        return Result.createSuccessResult();
    }

    @Override
    protected abstract void performAction(IRapidFireJobResource job) throws Exception;

    protected void terminate() throws Exception {
    }
}
