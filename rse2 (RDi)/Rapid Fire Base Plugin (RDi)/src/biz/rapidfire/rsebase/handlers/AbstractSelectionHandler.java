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
import org.eclipse.rse.core.RSECorePlugin;
import org.eclipse.rse.core.events.ISystemRemoteChangeEvents;
import org.eclipse.rse.core.model.ISystemRegistry;
import org.eclipse.rse.core.subsystems.ISubSystem;
import org.eclipse.ui.handlers.HandlerUtil;

public abstract class AbstractSelectionHandler extends AbstractHandler {

    protected ISelection getCurrentSelection(ExecutionEvent event) throws ExecutionException {

        return HandlerUtil.getCurrentSelection(event);
    }

    protected void refreshUI(Object resource) {

        if (resource != null) {

            ISystemRegistry sr = RSECorePlugin.getTheSystemRegistry();

            Object parent = null;
            ISubSystem subsystem = null;
            String[] oldNames = null;

            if (isDeleteMode()) {
                // Tested: OK
                sr.fireRemoteResourceChangeEvent(ISystemRemoteChangeEvents.SYSTEM_REMOTE_RESOURCE_DELETED, resource, parent, subsystem, oldNames);
            } else {
                // sr.fireRemoteResourceChangeEvent(ISystemRemoteChangeEvents.SYSTEM_REMOTE_RESOURCE_CHANGED,
                // resource, null, null, null, null);
                // sr.fireRemoteResourceChangeEvent(ISystemRemoteChangeEvents.SYSTEM_REMOTE_RESOURCE_CREATED,
                // resource, null, null, null, null);
                // sr.fireRemoteResourceChangeEvent(ISystemRemoteChangeEvents.SYSTEM_REMOTE_RESOURCE_DOWNLOADED,
                // resource, null, null, null, null);
                // NPE
                // sr.fireRemoteResourceChangeEvent(ISystemRemoteChangeEvents.SYSTEM_REMOTE_RESOURCE_RENAMED,
                // resource, null, null, null, null);
                // sr.fireRemoteResourceChangeEvent(ISystemRemoteChangeEvents.SYSTEM_REMOTE_RESOURCE_UPLOADED,
                // resource, null, null, null, null);
            }
        }
    }

    protected abstract boolean isDeleteMode();
}
