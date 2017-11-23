/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.library;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.handlers.AbstractResourceMaintenanceHandler;
import biz.rapidfire.core.model.IRapidFireLibraryResource;
import biz.rapidfire.core.model.IRapidFireResource;
import biz.rapidfire.core.model.dao.JDBCConnectionManager;
import biz.rapidfire.core.model.maintenance.Result;
import biz.rapidfire.core.model.maintenance.job.JobKey;
import biz.rapidfire.core.model.maintenance.library.LibraryKey;
import biz.rapidfire.core.model.maintenance.library.LibraryManager;

public abstract class AbstractLibraryMaintenanceHandler extends AbstractResourceMaintenanceHandler {

    private LibraryManager manager;

    public AbstractLibraryMaintenanceHandler(String mode) {
        super(mode);
    }

    protected LibraryManager getManager() {
        return manager;
    }

    @Override
    protected Object executeWithResource(IRapidFireResource resource) throws ExecutionException {

        if (!(resource instanceof IRapidFireLibraryResource)) {
            return null;
        }

        try {

            IRapidFireLibraryResource library = (IRapidFireLibraryResource)resource;

            String message = initialize(library);
            if (message != null) {
                MessageDialog.openError(getShell(), Messages.E_R_R_O_R, message);
            } else {
                performAction(library);
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

    private String initialize(IRapidFireLibraryResource file) throws Exception {

        String connectionName = file.getParentSubSystem().getConnectionName();
        String dataLibrary = file.getDataLibrary();
        boolean commitControl = isCommitControl();

        manager = new LibraryManager(JDBCConnectionManager.getInstance().getConnection(connectionName, dataLibrary, commitControl));
        manager.openFiles();

        Result status = manager.initialize(getMode(), new LibraryKey(new JobKey(file.getJob()), file.getName()));
        if (status.isError()) {
            return status.getMessage();
        }

        return null;
    }

    protected abstract void performAction(IRapidFireLibraryResource file) throws Exception;

    private void terminate() throws Exception {

        if (manager != null) {
            manager.closeFiles();
            manager = null;
        }
    }

    private void logError(Throwable e) {
        logError("*** Could not handle Rapid Fire library resource request ***", e); //$NON-NLS-1$
    }
}
