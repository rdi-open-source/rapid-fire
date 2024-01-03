/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem.actions;

import java.util.Iterator;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.rse.ui.actions.SystemBaseAction;
import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.handlers.AbstractResourceMaintenanceHandler;

public abstract class AbstractNewNodePopupMenuAction<N, R> extends SystemBaseAction {

    private AbstractResourceMaintenanceHandler<?, ?> handler;

    public AbstractNewNodePopupMenuAction(String label, String tooltip, Shell shell, AbstractResourceMaintenanceHandler<?, ?> handler) {
        super(label, tooltip, shell);

        this.handler = handler;
    }

    public AbstractResourceMaintenanceHandler<?, ?> getHandler() {
        return handler;
    }

    @Override
    public boolean isEnabled() {
        return handler.isEnabled();
    }

    @Override
    public void setSelection(ISelection selection) {
        super.setSelection(selection);

        Iterator<?> iterator = null;
        if (selection instanceof StructuredSelection) {
            StructuredSelection structuredSelection = (StructuredSelection)selection;
            iterator = structuredSelection.iterator();

        } else if (selection instanceof TreeSelection) {
            TreeSelection treeSelection = (TreeSelection)selection;
            iterator = treeSelection.iterator();
        }

        if (iterator != null) {

            while (iterator.hasNext()) {
                Object object = iterator.next();
                N resource = getResource(object);
                if (resource != null) {
                    handler.setEnabled(createNewResource(resource));
                } else {
                    setEnabled(false);
                }

                if (!handler.isEnabled()) {
                    break;
                }
            }
        }
    }

    protected abstract N getResource(Object object);

    protected abstract R createNewResource(N object);
}
