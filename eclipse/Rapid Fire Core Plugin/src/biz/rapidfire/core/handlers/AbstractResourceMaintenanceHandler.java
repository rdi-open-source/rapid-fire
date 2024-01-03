/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
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
import biz.rapidfire.core.exceptions.AutoReconnectErrorException;
import biz.rapidfire.core.handlers.shared.IMaintenanceHandler;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.Result;
import biz.rapidfire.core.maintenance.shared.IResourceAction;
import biz.rapidfire.core.model.IRapidFireResource;
import biz.rapidfire.core.model.dao.IJDBCConnection;
import biz.rapidfire.core.model.dao.JDBCConnectionManager;
import biz.rapidfire.core.preferences.Preferences;
import biz.rapidfire.rsebase.helpers.ExpressionsHelper;
import biz.rapidfire.rsebase.helpers.SystemConnectionHelper;

public abstract class AbstractResourceMaintenanceHandler<R extends IRapidFireResource, A extends IResourceAction> extends AbstractHandler implements
    IMaintenanceHandler {

    private String message;
    private MaintenanceMode mode;
    private boolean isEnabled;
    private boolean isCanceled;
    private A action;

    public AbstractResourceMaintenanceHandler(MaintenanceMode mode, A action) {
        this.mode = mode;
        this.action = action;
    }

    protected MaintenanceMode getMaintenanceMode() {
        return mode;
    }

    protected IJDBCConnection getJdbcConnection(String connectionName, String dataLibrary) throws Exception {

        if (isCommitControl()) {
            return JDBCConnectionManager.getInstance().getConnectionForUpdate(connectionName, dataLibrary);
        } else {
            return JDBCConnectionManager.getInstance().getConnectionForRead(connectionName, dataLibrary);
        }
    }

    @SuppressWarnings("unchecked")
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
                    setEnabledByResource((R)object);
                } else {
                    break;
                }

                if (!isEnabled()) {
                    break;
                }
            }
        }
    }

    /**
     * Set the enabled state of the handler. When this method is called by the
     * Eclipse framework, the framework passes an
     * 'org.eclipse.core.expressions.EvaluationContext'. When the method is
     * called by one of the "NewResourceAction" classes of the
     * RapidFireRSEPlugin, it gets a resource object.
     */
    public void setEnabled(Object object) {

        if (isInstanceOf(object)) {
            setEnabledByResource((R)object);
        } else if (object instanceof ISelection) {
            selectionChanged(object);
        } else {
            Object selection = ExpressionsHelper.getSelection(object);
            selectionChanged(selection);
        }
    }

    /**
     * Set the enabled state of the handler. It is called by the WDSCi version
     * of the plug-in.
     */
    public void setEnabledWDSCi(ISelection selection) {
        selectionChanged(selection);
    }

    private void setEnabledByResource(R resource) {

        try {
            isEnabled = isValidAction(resource);
        } catch (Exception e) {
            isEnabled = false;
        }
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public Object execute(ExecutionEvent event) throws ExecutionException {

        ISelection selection = SystemConnectionHelper.getCurrentSelection(event);

        return executeWithSelection(selection);
    }

    public void executeWDSCi(ExecutionEvent event) throws ExecutionException {
        execute(event);
    }

    @SuppressWarnings("unchecked")
    public Object executeWithSelection(ISelection selection) throws ExecutionException {

        initializeHandler();

        if (selection instanceof IStructuredSelection) {
            IStructuredSelection structuredSelection = (IStructuredSelection)selection;
            Iterator<?> iterator = structuredSelection.iterator();
            while (!isCanceled && iterator.hasNext()) {
                setErrorMessage(null);
                Object object = iterator.next();
                if (isInstanceOf(object)) {
                    executeWithResource((R)object);
                }
                if (isError()) {
                    displayError();
                }
            }
        }

        return null;
    }

    protected void initializeHandler() {
        isCanceled = false;
    }

    public void cancel() {
        isCanceled = true;
    }

    protected Shell getShell() {
        return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
    }

    private Object executeWithResource(R resource) {

        boolean isError = false;

        try {

            if (canExecuteAction(resource, action)) {
                Result result = initialize(resource);
                if (result != null && result.isError()) {
                    MessageDialog.openError(getShell(), Messages.E_R_R_O_R, result.getMessage());
                } else {
                    performAction(resource);
                }
            }

        } catch (AutoReconnectErrorException e) {
            MessageDialogAsync.displayError(e.getLocalizedMessage());
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
        if (MaintenanceMode.DISPLAY == mode) {
            return false;
        }

        return true;
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

    public void refreshUICreated(Object subSystem, Object resource, Object... parents) {

        SystemConnectionHelper.refreshUICreated(isSlowConnection(), subSystem, resource, parents);
    }

    public void refreshUIChanged(Object subSystem, Object resource, Object... parents) {

        SystemConnectionHelper.refreshUIChanged(isSlowConnection(), subSystem, resource, parents);
    }

    public void refreshUIDeleted(Object subSystem, Object resource, Object... parents) {

        SystemConnectionHelper.refreshUIDeleted(isSlowConnection(), subSystem, resource, parents);
    }

    private boolean isSlowConnection() {
        return Preferences.getInstance().isSlowConnection();
    }
}
