/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rsebase.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISources;

import com.ibm.etools.systems.core.SystemPlugin;
import com.ibm.etools.systems.model.ISystemRemoteChangeEvents;
import com.ibm.etools.systems.model.SystemRegistry;

public abstract class AbstractSelectionHandler extends AbstractHandler {

    protected ISelection getCurrentSelection(ExecutionEvent event) throws ExecutionException {

        Object object = event.getParameters().get(ISources.ACTIVE_CURRENT_SELECTION_NAME);
        if (!(object instanceof ISelection)) {
            throw new ExecutionException("*** Incorrect type of current selection ***"); //$NON-NLS-1$
        }
        return (ISelection)object;
    }

//    protected void refreshUI(Object resource) {
//        if (resource != null) {
//            SystemRegistry sr = SystemPlugin.getDefault().getSystemRegistry();
//            if (isDeleteMode()) {
//                sr.fireRemoteResourceChangeEvent(ISystemRemoteChangeEvents.SYSTEM_REMOTE_RESOURCE_DELETED, resource, null, null, null, null);
//            } else {
//                sr.fireRemoteResourceChangeEvent(ISystemRemoteChangeEvents.SYSTEM_REMOTE_RESOURCE_CREATED, resource, null, null, null, null);
//            }
//        }
//    }

    protected abstract boolean isDeleteMode();
}
