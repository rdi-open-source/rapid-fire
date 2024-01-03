/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.librarylist;

import org.eclipse.jface.dialogs.MessageDialog;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.handlers.AbstractResourceMaintenanceHandler;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.Result;
import biz.rapidfire.core.maintenance.job.shared.JobKey;
import biz.rapidfire.core.maintenance.librarylist.LibraryListManager;
import biz.rapidfire.core.maintenance.librarylist.shared.LibraryListAction;
import biz.rapidfire.core.maintenance.librarylist.shared.LibraryListKey;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.IRapidFireLibraryListResource;

public abstract class AbstractLibraryListMaintenanceHandler extends
    AbstractResourceMaintenanceHandler<IRapidFireLibraryListResource, LibraryListAction> {

    private LibraryListManager manager;
    private LibraryListAction libraryListAction;

    public AbstractLibraryListMaintenanceHandler(MaintenanceMode mode, LibraryListAction libraryListAction) {
        super(mode, libraryListAction);

        this.libraryListAction = libraryListAction;
    }

    protected LibraryListManager getManager() {
        return manager;
    }

    protected LibraryListManager getOrCreateManager(IRapidFireJobResource job) throws Exception {

        if (manager == null) {
            String connectionName = job.getParentSubSystem().getConnectionName();
            String dataLibrary = job.getDataLibrary();
            manager = new LibraryListManager(getJdbcConnection(connectionName, dataLibrary));
        }

        return manager;
    }

    @Override
    protected boolean isValidAction(IRapidFireLibraryListResource libraryList) throws Exception {
        return getOrCreateManager(libraryList.getParentJob()).isValidAction(libraryList, libraryListAction);
    }

    @Override
    protected boolean isInstanceOf(Object object) {

        if (object instanceof IRapidFireLibraryListResource) {
            return true;
        }

        return false;
    }

    @Override
    protected boolean canExecuteAction(IRapidFireLibraryListResource libraryList, LibraryListAction libraryListAction) {

        String message = null;

        try {

            Result result;
            if (libraryListAction == LibraryListAction.CREATE) {
                result = getOrCreateManager(libraryList.getParentJob()).checkAction(
                    LibraryListKey.createNew(libraryList.getParentResource().getKey()), libraryListAction);
            } else {
                result = getOrCreateManager(libraryList.getParentJob()).checkAction(libraryList.getKey(), libraryListAction);
            }

            if (result.isSuccessfull()) {
                return true;
            } else {
                message = Messages.bindParameters(Messages.The_requested_operation_is_invalid_for_job_status_A, libraryList.getParentJob()
                    .getStatus().label());
            }

        } catch (Exception e) {
            logError(e);
        }

        if (message != null) {
            MessageDialog.openError(getShell(), Messages.E_R_R_O_R, message);
        }

        return false;
    }

    @Override
    protected Result initialize(IRapidFireLibraryListResource file) throws Exception {

        manager.openFiles();

        Result result = manager.initialize(getMaintenanceMode(), new LibraryListKey(new JobKey(file.getJob()), file.getName()));

        return result;
    }

    @Override
    protected void terminate(boolean closeConnection) throws Exception {

        if (manager != null) {
            manager.closeFiles();
            if (closeConnection) {
                manager.recoverError();
            }
            manager = null;
        }
    }
}
