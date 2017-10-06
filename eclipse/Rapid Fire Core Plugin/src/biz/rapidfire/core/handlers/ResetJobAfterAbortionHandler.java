/*******************************************************************************
 * Copyright (c) 2012-2014 iSphere Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

import biz.rapidfire.rsebase.model.IRapidFireResource;

public class ResetJobAfterAbortionHandler extends AbstractJobHandler implements IHandler {

    public ResetJobAfterAbortionHandler() {
        super();
    }

    protected Object executeWithResource(IRapidFireResource job) throws ExecutionException {

        System.out.println("Reseting Rapid Fire job after abortion ... " + job.getName());

        return null;
    }

}
