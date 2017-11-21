/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.job;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

import biz.rapidfire.core.handlers.AbstractResourceHandler;
import biz.rapidfire.core.model.IRapidFireResource;
import biz.rapidfire.core.model.maintenance.IMaintenance;

public class EndJobHandler extends AbstractResourceHandler implements IHandler {

    public EndJobHandler() {
        super(IMaintenance.MODE_CHANGE);
    }

    @Override
    protected Object executeWithResource(IRapidFireResource job) throws ExecutionException {

        System.out.println("Ending Rapid Fire job ... " + job);

        return null;
    }

}
