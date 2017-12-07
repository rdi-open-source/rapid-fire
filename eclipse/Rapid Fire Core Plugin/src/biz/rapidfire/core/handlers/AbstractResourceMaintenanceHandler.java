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
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.dialogs.MessageDialogAsync;
import biz.rapidfire.core.handlers.shared.IMaintenanceHandler;
import biz.rapidfire.core.model.IRapidFireResource;
import biz.rapidfire.core.model.maintenance.IResourceAction;
import biz.rapidfire.core.model.maintenance.MaintenanceMode;
import biz.rapidfire.rsebase.handlers.AbstractSelectionHandler;
import biz.rapidfire.rsebase.helpers.ExpressionsHelper;

public abstract class AbstractResourceMaintenanceHandler<R extends IRapidFireResource, A extends IResourceAction> extends AbstractSelectionHandler
    implements IMaintenanceHandler {

    private String message;
    private MaintenanceMode initialMode;
    private MaintenanceMode currentMode;
    private boolean isEnabled;

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

    private void selectionChanged(Object selection) {

        isEnabled = false;

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
                if (isInstanceOf(object)) {
                    try {
                        isEnabled = isValidAction((R)object);
                    } catch (Exception e) {
                        isEnabled = false;
                    }
                } else {
                    break;
                }

                if (!isEnabled) {
                    break;
                }
            }
        }
    }

    public void setEnabled(Object evaluationContext) {
        Object selection = ExpressionsHelper.getSelection(evaluationContext);
        selectionChanged(selection);
    }

    public void setEnabledWDSCi(ISelection selection) {
        selectionChanged(selection);
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public Object execute(ExecutionEvent event) throws ExecutionException {

        ISelection selection = getCurrentSelection(event);

        return executeWithSelection(selection);
    }

    public void executeWDSCi(ExecutionEvent event) throws ExecutionException {
        execute(event);
    }

    public Object executeWithSelection(ISelection selection) throws ExecutionException {

        if (selection instanceof IStructuredSelection) {
            IStructuredSelection structuredSelection = (IStructuredSelection)selection;
            Iterator<IRapidFireResource> iterator = structuredSelection.iterator();
            while (iterator.hasNext()) {
                currentMode = initialMode;
                setErrorMessage(null);
                executeWithResource((R)iterator.next());
                if (isError()) {
                    displayError();
                }
            }
        }

        return null;
    }

    protected Shell getShell() {
        return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
    }

    @Override
    protected boolean isDeleteMode() {
        return MaintenanceMode.DELETE.equals(initialMode);
    }

    protected abstract boolean isInstanceOf(Object object);

    protected abstract boolean isValidAction(R resource) throws Exception;

    protected abstract Object executeWithResource(R resource) throws ExecutionException;

    protected abstract boolean canExecuteAction(R resource, A action);

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
