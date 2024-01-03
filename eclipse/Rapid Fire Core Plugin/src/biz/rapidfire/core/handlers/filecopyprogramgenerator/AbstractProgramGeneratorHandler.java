/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.handlers.filecopyprogramgenerator;

import org.eclipse.jface.dialogs.MessageDialog;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.handlers.AbstractResourceMaintenanceHandler;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.Result;
import biz.rapidfire.core.maintenance.file.shared.FileKey;
import biz.rapidfire.core.maintenance.filecopyprogramgenerator.FileCopyProgramGeneratorManager;
import biz.rapidfire.core.maintenance.filecopyprogramgenerator.shared.FileCopyProgramGeneratorAction;
import biz.rapidfire.core.maintenance.job.shared.JobKey;
import biz.rapidfire.core.model.IRapidFireFileResource;
import biz.rapidfire.core.model.IRapidFireJobResource;

public abstract class AbstractProgramGeneratorHandler extends
    AbstractResourceMaintenanceHandler<IRapidFireFileResource, FileCopyProgramGeneratorAction> {

    private FileCopyProgramGeneratorManager manager;
    private FileCopyProgramGeneratorAction fileCopyProgramGeneratorAction;

    public AbstractProgramGeneratorHandler(MaintenanceMode mode, FileCopyProgramGeneratorAction fileCopyProgramGeneratorAction) {
        super(mode, fileCopyProgramGeneratorAction);

        this.fileCopyProgramGeneratorAction = fileCopyProgramGeneratorAction;
    }

    protected FileCopyProgramGeneratorManager getManager() {
        return manager;
    }

    protected FileCopyProgramGeneratorManager getOrCreateManager(IRapidFireJobResource job) throws Exception {

        if (manager == null) {
            String connectionName = job.getParentSubSystem().getConnectionName();
            String dataLibrary = job.getDataLibrary();
            manager = new FileCopyProgramGeneratorManager(getJdbcConnection(connectionName, dataLibrary));
        }

        return manager;
    }

    @Override
    protected boolean isValidAction(IRapidFireFileResource file) throws Exception {
        return getOrCreateManager(file.getParentJob()).isValidAction(file, fileCopyProgramGeneratorAction);
    }

    @Override
    protected boolean isInstanceOf(Object object) {

        if (object instanceof IRapidFireFileResource) {
            return true;
        }

        return false;
    }

    @Override
    protected boolean canExecuteAction(IRapidFireFileResource file, FileCopyProgramGeneratorAction fileCopyProgramGeneratorAction) {

        String message = null;

        try {

            Result result;
            if (fileCopyProgramGeneratorAction == FileCopyProgramGeneratorAction.CREATE) {
                result = getOrCreateManager(file.getParentJob()).checkAction(FileKey.createNew(file.getParentResource().getKey()),
                    fileCopyProgramGeneratorAction);
            } else {
                result = getOrCreateManager(file.getParentJob()).checkAction(file.getKey(), fileCopyProgramGeneratorAction);
            }

            if (result.isSuccessfull()) {
                return true;
            } else {
                message = Messages.bindParameters(Messages.The_requested_operation_is_invalid_for_job_status_A, file.getParentJob().getStatus()
                    .label());
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
    protected Result initialize(IRapidFireFileResource file) throws Exception {

        manager.openFiles();

        Result result = manager.initialize(getMaintenanceMode(), new FileKey(new JobKey(file.getJob()), file.getPosition()));

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
