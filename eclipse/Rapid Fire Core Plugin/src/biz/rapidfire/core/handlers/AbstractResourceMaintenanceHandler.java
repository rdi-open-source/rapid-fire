/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
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

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.dialogs.MessageDialogAsync;
import biz.rapidfire.core.model.IRapidFireResource;
import biz.rapidfire.core.model.maintenance.MaintenanceMode;
import biz.rapidfire.rsebase.handlers.AbstractSelectionHandler;

public abstract class AbstractResourceMaintenanceHandler<M, A> extends AbstractSelectionHandler {

    private String message;
    private MaintenanceMode initialMode;
    private MaintenanceMode currentMode;

    public AbstractResourceMaintenanceHandler(MaintenanceMode mode) {
        this.initialMode = mode;
        this.currentMode = this.initialMode;
    }

    protected MaintenanceMode getMaintenanceMode() {
        return currentMode;
    }

    protected void changeMaintenanceMode(MaintenanceMode mode) {
        this.currentMode = mode;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands
     * .ExecutionEvent)
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
                currentMode = initialMode;
                setErrorMessage(null);
                executeWithResource(iterator.next());
                if (isError()) {
                    displayError();
                }
            }
        }

        return null;
    }

    public Shell getShell() {
        return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
    }

    @Override
    protected boolean isDeleteMode() {
        return MaintenanceMode.DELETE.equals(initialMode);
    }

    protected abstract Object executeWithResource(IRapidFireResource resource) throws ExecutionException;

    protected abstract boolean canExecuteAction(IRapidFireResource rapidFireResource, A resourceAction);

    protected boolean isCommitControl() {

        MaintenanceMode mode = getMaintenanceMode();
        if (MaintenanceMode.CHANGE == mode || MaintenanceMode.DELETE == mode) {
            return true;
        }

        return false;
    }

    protected void setErrorMessage(String message) {
        this.message = message;
    }

    private boolean isError() {

        if (message != null) {
            return true;
        }

        return false;
    }

    private void displayError() {

        if (message != null) {
            MessageDialogAsync.displayError(getShell(), message);
            message = null;
        }
    }

    protected void logError(String message, Throwable e) {

        RapidFireCorePlugin.logError(message, e);
        setErrorMessage(message);
        displayError();
    }
}
