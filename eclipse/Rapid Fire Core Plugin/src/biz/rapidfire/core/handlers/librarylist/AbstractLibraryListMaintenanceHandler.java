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
import biz.rapidfire.core.model.IRapidFireLibraryListResource;
import biz.rapidfire.core.model.IRapidFireResource;
import biz.rapidfire.core.model.dao.JDBCConnectionManager;
import biz.rapidfire.core.model.maintenance.Result;
import biz.rapidfire.core.model.maintenance.job.JobKey;
import biz.rapidfire.core.model.maintenance.librarylist.LibraryListKey;
import biz.rapidfire.core.model.maintenance.librarylist.LibraryListManager;

public abstract class AbstractLibraryListMaintenanceHandler extends AbstractResourceMaintenanceHandler {

    private LibraryListManager manager;

    public AbstractLibraryListMaintenanceHandler(String mode) {
        super(mode);
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

            Result result = initialize(libraryList);
            if (result != null && result.isError()) {
                MessageDialog.openError(getShell(), Messages.E_R_R_O_R, result.getMessage());
            } else {
                performAction(libraryList);
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

    private Result initialize(IRapidFireLibraryListResource file) throws Exception {

        String connectionName = file.getParentSubSystem().getConnectionName();
        String dataLibrary = file.getDataLibrary();
        boolean commitControl = isCommitControl();

        manager = new LibraryListManager(JDBCConnectionManager.getInstance().getConnection(connectionName, dataLibrary, commitControl));
        manager.openFiles();

        Result result = manager.initialize(getMode(), new LibraryListKey(new JobKey(file.getJob()), file.getName()));
        if (result.isError()) {
            return result;
        }

        return null;
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
