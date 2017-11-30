/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.librarylist;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.handlers.AbstractResourceMaintenanceHandler;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.IRapidFireLibraryListResource;
import biz.rapidfire.core.model.IRapidFireResource;
import biz.rapidfire.core.model.dao.JDBCConnectionManager;
import biz.rapidfire.core.model.maintenance.MaintenanceMode;
import biz.rapidfire.core.model.maintenance.Result;
import biz.rapidfire.core.model.maintenance.job.shared.JobKey;
import biz.rapidfire.core.model.maintenance.librarylist.LibraryListManager;
import biz.rapidfire.core.model.maintenance.librarylist.shared.LibraryListAction;
import biz.rapidfire.core.model.maintenance.librarylist.shared.LibraryListKey;

public abstract class AbstractLibraryListMaintenanceHandler extends
    AbstractResourceMaintenanceHandler<IRapidFireLibraryListResource, LibraryListAction> {

    private LibraryListManager manager;
    private LibraryListAction libraryListAction;

    public AbstractLibraryListMaintenanceHandler(MaintenanceMode mode, LibraryListAction libraryListAction) {
        super(mode);

        this.libraryListAction = libraryListAction;
    }

    protected LibraryListManager getManager() {
        return manager;
    }

    @Override
    protected Object executeWithResource(IRapidFireResource resource) throws ExecutionException {

        if (!(resource instanceof IRapidFireLibraryListResource)) {
            return null;
        }

        try {

            IRapidFireLibraryListResource libraryList = (IRapidFireLibraryListResource)resource;

            if (canExecuteAction(libraryList, libraryListAction)) {
                Result result = initialize(libraryList);
                if (result != null && result.isError()) {
                    MessageDialog.openError(getShell(), Messages.E_R_R_O_R, result.getMessage());
                } else {
                    performAction(libraryList);
                }
            }

        } catch (Throwable e) {
            logError(e);
        } finally {
            try {
                terminate();
            } catch (Throwable e) {
                logError(e);
            }
        }

        return null;
    }

    private LibraryListManager getOrCreateManager(IRapidFireJobResource job) throws Exception {

        if (manager == null) {
            String connectionName = job.getParentSubSystem().getConnectionName();
            String dataLibrary = job.getDataLibrary();
            boolean commitControl = isCommitControl();
            manager = new LibraryListManager(JDBCConnectionManager.getInstance().getConnection(connectionName, dataLibrary, commitControl));
        }

        return manager;
    }

    @Override
    protected boolean canExecuteAction(IRapidFireResource resource, LibraryListAction libraryListAction) {

        if (!(resource instanceof IRapidFireLibraryListResource)) {
            return false;
        }

        String message = null;

        try {

            // TODO: check action!
            IRapidFireLibraryListResource libraryList = (IRapidFireLibraryListResource)resource;
            Result result = getOrCreateManager(libraryList.getParentJob()).checkAction(libraryList.getKey(), libraryListAction);
            if (result.isSuccessfull()) {
                return true;
            } else {
                // TODO: fix message
                message = Messages.bindParameters(Messages.The_requested_operation_is_invalid_for_job_status_A, libraryList.getParentJob()
                    .getStatus().label);
            }

        } catch (Exception e) {
            logError("*** Could not check job action. Failed creating the job manager ***", e); //$NON-NLS-1$
        }

        if (message != null) {
            MessageDialog.openError(getShell(), Messages.E_R_R_O_R, message);
        }

        return false;
    }

    private Result initialize(IRapidFireLibraryListResource file) throws Exception {

        manager.openFiles();

        Result result = manager.initialize(getMaintenanceMode(), new LibraryListKey(new JobKey(file.getJob()), file.getName()));

        return result;
    }

    protected abstract void performAction(IRapidFireLibraryListResource file) throws Exception;

    private void terminate() throws Exception {

        if (manager != null) {
            manager.closeFiles();
            manager = null;
        }
    }

    private void logError(Throwable e) {
        logError("*** Could not handle Rapid Fire library list resource request ***", e); //$NON-NLS-1$
    }
}
