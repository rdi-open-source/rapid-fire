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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.dialogs.MessageDialogAsync;
import biz.rapidfire.core.handlers.shared.IMaintenanceHandler;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.maintenance.IResourceAction;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.Result;
import biz.rapidfire.core.model.IRapidFireResource;
import biz.rapidfire.rsebase.handlers.AbstractSelectionHandler;
import biz.rapidfire.rsebase.helpers.ExpressionsHelper;

public abstract class AbstractResourceMaintenanceHandler<R extends IRapidFireResource, A extends IResourceAction> extends AbstractSelectionHandler
    implements IMaintenanceHandler {

    private String message;
    private MaintenanceMode initialMode;
    private MaintenanceMode currentMode;
    private boolean isEnabled;
    private A action;

    public AbstractResourceMaintenanceHandler(MaintenanceMode mode, A action) {
        this.initialMode = mode;
        this.currentMode = this.initialMode;
        this.action = action;
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

    protected Object executeWithResource(R notification) throws ExecutionException {

        boolean isError = false;

        try {

            if (canExecuteAction(notification, action)) {
                Result result = initialize(notification);
                if (result != null && result.isError()) {
                    MessageDialog.openError(getShell(), Messages.E_R_R_O_R, result.getMessage());
                } else {
                    performAction(notification);
                }
            }

        } catch (Throwable e) {
            logError(e);
            isError = true;
        } finally {
            terminateInternally(isError);
        }

        return null;
    }

    protected void terminateInternally(boolean isError) {

        try {

            terminate(isError);

        } catch (Throwable e) {
            logError(e);
        }
    }

    protected abstract boolean isInstanceOf(Object object);

    protected abstract boolean isValidAction(R resource) throws Exception;

    protected abstract boolean canExecuteAction(R resource, A action);

    protected abstract Result initialize(R resource) throws Exception;

    protected abstract void performAction(R resource) throws Exception;

    protected abstract void terminate(boolean isError) throws Exception;

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

    protected void logError(Throwable e) {

        String logMessage = "*** Could not handle Rapid Fire resource request in: " + getClass().getSimpleName() + " ***"; //$NON-NLS-1$ //$NON-NLS-2$
        RapidFireCorePlugin.logError(logMessage, e);
        setErrorMessage(ExceptionHelper.getLocalizedMessage(e));
        displayError();
    }

    private void displayError() {

        if (message != null) {
            MessageDialogAsync.displayError(getShell(), message);
            message = null;
        }
    }
}
