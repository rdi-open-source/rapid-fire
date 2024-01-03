/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.dialogs.maintenance.job;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.dialogs.maintenance.AbstractMaintenanceDialog;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.Result;
import biz.rapidfire.core.maintenance.job.IJobCheck;
import biz.rapidfire.core.maintenance.job.JobManager;
import biz.rapidfire.core.maintenance.job.JobValues;

public class JobMaintenanceDialog extends AbstractMaintenanceDialog {

    private JobManager manager;

    private JobValues values;
    private JobMaintenanceControl jobMaintenanceControl;

    public static JobMaintenanceDialog getCreateDialog(Shell shell, JobManager manager) {
        return new JobMaintenanceDialog(shell, MaintenanceMode.CREATE, manager);
    }

    public static JobMaintenanceDialog getCopyDialog(Shell shell, JobManager manager) {
        return new JobMaintenanceDialog(shell, MaintenanceMode.COPY, manager);
    }

    public static JobMaintenanceDialog getChangeDialog(Shell shell, JobManager manager) {
        return new JobMaintenanceDialog(shell, MaintenanceMode.CHANGE, manager);
    }

    public static JobMaintenanceDialog getDeleteDialog(Shell shell, JobManager manager) {
        return new JobMaintenanceDialog(shell, MaintenanceMode.DELETE, manager);
    }

    public static JobMaintenanceDialog getDisplayDialog(Shell shell, JobManager manager) {
        return new JobMaintenanceDialog(shell, MaintenanceMode.DISPLAY, manager);
    }

    public void setValue(JobValues values) {
        this.values = values;
    }

    public JobValues getValue() {
        return values;
    }

    private JobMaintenanceDialog(Shell shell, MaintenanceMode mode, JobManager manager) {
        super(shell, mode);

        this.manager = manager;
    }

    @Override
    protected void createEditorAreaContent(Composite parent) {

        jobMaintenanceControl = new JobMaintenanceControl(parent, SWT.NONE);
        jobMaintenanceControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        jobMaintenanceControl.setMode(getMode());
    }

    @Override
    protected String getDialogTitle() {
        return Messages.DialogTitle_Job;
    }

    @Override
    protected void setScreenValues() {

        jobMaintenanceControl.setJobName(values.getKey().getJobName());
        jobMaintenanceControl.setDescription(values.getDescription());
        jobMaintenanceControl.setCreateEnvironment(values.isCreateEnvironment());
        jobMaintenanceControl.setJobQueueName(values.getJobQueueName());
        jobMaintenanceControl.setJobQueueLibraryName(values.getJobQueueLibraryName());
        jobMaintenanceControl.setCancelASPThresholdExceeds(values.isCancelASPThresholdExceeds());
    }

    @Override
    protected void okPressed() {

        JobValues newValues = values.clone();
        newValues.getKey().setJobName(jobMaintenanceControl.getJobName());
        newValues.setDescription(jobMaintenanceControl.getDescription());
        newValues.setCreateEnvironment(jobMaintenanceControl.isCreateEnvironment());
        newValues.setJobQueueName(jobMaintenanceControl.getJobQueueName());
        newValues.setJobQueueLibraryName(jobMaintenanceControl.getJobQueueLibraryName());
        newValues.setCancelASPThresholdExceeds(jobMaintenanceControl.isCancelASPThresholdExceeds());

        if (!isDisplayMode()) {
            try {
                manager.setValues(newValues);
                Result result = manager.check();
                if (result.isError()) {
                    setErrorFocus(result);
                    return;
                }
            } catch (Exception e) {
                MessageDialog.openError(getShell(), Messages.E_R_R_O_R, ExceptionHelper.getLocalizedMessage(e));
                return;
            }
        }

        values = newValues;

        super.okPressed();
    }

    private void setErrorFocus(Result result) {

        String fieldName = result.getFieldName();
        String message = null;

        if (IJobCheck.FIELD_JOB.equals(fieldName)) {
            jobMaintenanceControl.setFocusJobName();
            message = Messages.bind(Messages.Job_name_A_is_not_valid, jobMaintenanceControl.getJobName());
        } else if (IJobCheck.FIELD_DESCRIPTION.equals(fieldName)) {
            jobMaintenanceControl.setFocusDescription();
            message = Messages.bind(Messages.Description_A_is_not_valid, jobMaintenanceControl.getDescription());
        } else if (IJobCheck.FIELD_CREATE_ENVIRONMENT.equals(fieldName)) {
            jobMaintenanceControl.setFocusCreateEnvironment();
            message = Messages.Create_environment_value_has_been_rejected;
        } else if (IJobCheck.FIELD_JOB_QUEUE_NAME.equals(fieldName)) {
            jobMaintenanceControl.setFocusJobQueueName();
            message = Messages.bind(Messages.Job_queue_name_A_is_not_valid, jobMaintenanceControl.getJobQueueName());
        } else if (IJobCheck.FIELD_JOB_QUEUE_LIBRARY_NAME.equals(fieldName)) {
            jobMaintenanceControl.setFocusJobQueueLibraryName();
            message = Messages.bind(Messages.Library_name_A_is_not_valid, jobMaintenanceControl.getJobQueueLibraryName());
        } else if (IJobCheck.FIELD_CANCEL_ASP_THRESHOLD_EXCEEDS.equals(fieldName)) {
            jobMaintenanceControl.setFocusCancelASPThresholdExceeds();
            message = Messages.Cancel_ASP_threshold_exceeds_value_has_been_rejected;
        }

        setErrorMessage(message, result);
    }
}
