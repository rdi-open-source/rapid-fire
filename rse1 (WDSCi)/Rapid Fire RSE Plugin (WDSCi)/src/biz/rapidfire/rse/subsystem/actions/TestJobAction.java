/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem.actions;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import biz.rapidfire.core.handlers.TestJobHandler;

public class TestJobAction extends AbstractJobAction {

    private TestJobHandler handler = new TestJobHandler();

    @Override
    public void execute(ExecutionEvent event) throws ExecutionException {

        System.out.println("Calling handler: Testing Rapid Fire job ...");
        handler.execute(event);
    }

}