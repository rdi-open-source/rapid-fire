/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.library;

import org.eclipse.jface.dialogs.MessageDialog;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.handlers.AbstractResourceMaintenanceHandler;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.Result;
import biz.rapidfire.core.maintenance.job.shared.JobKey;
import biz.rapidfire.core.maintenance.library.LibraryManager;
import biz.rapidfire.core.maintenance.library.shared.LibraryAction;
import biz.rapidfire.core.maintenance.library.shared.LibraryKey;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.IRapidFireLibraryResource;
import biz.rapidfire.core.model.dao.JDBCConnectionManager;

public abstract class AbstractLibraryMaintenanceHandler extends AbstractResourceMaintenanceHandler<IRapidFireLibraryResource, LibraryAction> {

    private LibraryManager manager;
    private LibraryAction libraryAction;

    public AbstractLibraryMaintenanceHandler(MaintenanceMode mode, LibraryAction libraryAction) {
        super(mode, libraryAction);

        this.libraryAction = libraryAction;
    }

    protected LibraryManager getManager() {
        return manager;
    }

    protected LibraryManager getOrCreateManager(IRapidFireJobResource job) throws Exception {

        if (manager == null) {
            String connectionName = job.getParentSubSystem().getConnectionName();
            String dataLibrary = job.getDataLibrary();
            boolean commitControl = isCommitControl();
            manager = new LibraryManager(JDBCConnectionManager.getInstance().getConnection(connectionName, dataLibrary, commitControl));
        }

        return manager;
    }

    @Override
    protected boolean isValidAction(IRapidFireLibraryResource library) throws Exception {
        return getOrCreateManager(library.getParentJob()).isValidAction(library, libraryAction);
    }

    @Override
    protected boolean isInstanceOf(Object object) {

        if (object instanceof IRapidFireLibraryResource) {
            return true;
        }

        return false;
    }

    @Override
    protected boolean canExecuteAction(IRapidFireLibraryResource library, LibraryAction libraryAction) {

        String message = null;

        try {

            Result result = getOrCreateManager(library.getParentJob()).checkAction(library.getKey(), libraryAction);
            if (result.isSuccessfull()) {
                return true;
            } else {
                message = Messages.bindParameters(Messages.The_requested_operation_is_invalid_for_job_status_A,
                    library.getParentJob().getStatus().label);
            }

        } catch (Exception e) {
            logError(e);
        }

        if (message != null) {
            MessageDialog.openError(getShell(), Messages.E_R_R_O_R, message);
        }

        return false;
    }

    protected Result initialize(IRapidFireLibraryResource file) throws Exception {

        manager.openFiles();

        Result result = manager.initialize(getMaintenanceMode(), new LibraryKey(new JobKey(file.getJob()), file.getName()));
        if (result.isError()) {
            return result;
        }

        return null;
    }

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
