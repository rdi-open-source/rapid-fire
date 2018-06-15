/*******************************************************************************
 * Copyright (c) 2012-2018 iSphere Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rsebase.ui.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISources;

/**
 * Partial implementation of org.eclipse.ui.handlers.HandlerUtil, which is
 * available since 3.3.
 * 
 */
public class HandlerUtil {

    /**
     * Return the current selection.
     * 
     * @param event The execution event that contains the application context
     * @return the current selection, or <code>null</code>.
     */
    public static ISelection getCurrentSelection(ExecutionEvent event) throws ExecutionException {

        Object object = event.getParameters().get(ISources.ACTIVE_CURRENT_SELECTION_NAME);
        if (!(object instanceof ISelection)) {
            throw new ExecutionException("*** Incorrect type of current selection ***"); //$NON-NLS-1$
        }
        return (ISelection)object;
    }

}
