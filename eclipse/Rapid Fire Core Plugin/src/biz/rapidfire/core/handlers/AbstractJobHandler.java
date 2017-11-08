/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers;

import java.util.Iterator;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import biz.rapidfire.core.model.IRapidFireResource;
import biz.rapidfire.rsebase.handlers.AbstractSelectionHandler;

public abstract class AbstractJobHandler extends AbstractSelectionHandler {

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.
     * ExecutionEvent)
     */
    public Object execute(ExecutionEvent event) throws ExecutionException {

        ISelection selection = getCurrentSelection(event);

        return executeWithSelection(selection);
    }

    public Object executeWithSelection(ISelection selection) throws ExecutionException {

        if (selection instanceof IStructuredSelection) {
            IStructuredSelection structuredSelection = (IStructuredSelection)selection;
            Iterator<IRapidFireResource> iterator = structuredSelection.iterator();
            while (iterator.hasNext()) {
                executeWithResource(iterator.next());
            }
        }

        return null;
    }

    public Shell getShell() {
        return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
    }

    protected abstract Object executeWithResource(IRapidFireResource resource) throws ExecutionException;

}
