/*******************************************************************************
 * Copyright (c) 2012-2014 iSphere Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.subsystem.IRapidFireSubSystem;

public class DeleteJobHandler extends AbstractHandler implements IHandler {

    public DeleteJobHandler() {
        super();
    }

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.
     * ExecutionEvent)
     */
    public Object execute(ExecutionEvent event) throws ExecutionException {

        try {

            ISelection selection = HandlerUtil.getCurrentSelection(event);
            if (selection instanceof IStructuredSelection) {
                IStructuredSelection structuredSelection = (IStructuredSelection)selection;
                Iterator<IRapidFireJobResource> iterator = structuredSelection.iterator();
                while (iterator.hasNext()) {
                    IRapidFireJobResource job = iterator.next();
                    IRapidFireSubSystem subSystem = job.getParentSubSystem();

                    System.out.println("Deleting Rapid Fire job ... " + job.getName() + " (" + subSystem.getClass() + ")");
                }
            }

        } catch (Exception e) {
            RapidFireCorePlugin.logError("*** Error in DeleteJobHandler. ***", e); //$NON-NLS-1$
        }
        return null;
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }

}
