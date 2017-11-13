/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.dialogs.maintenance;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.jface.dialogs.Size;
import biz.rapidfire.core.jface.dialogs.XDialog;
import biz.rapidfire.core.model.maintenance.IMaintenance;
import biz.rapidfire.core.model.maintenance.Result;
import biz.rapidfire.core.model.maintenance.job.IJobCheck;
import biz.rapidfire.core.model.maintenance.job.JobManager;
import biz.rapidfire.core.model.maintenance.job.JobValues;
import biz.rapidfire.core.swt.widgets.WidgetFactory;

public class JobMaintenanceDialog extends XDialog {

    private String mode;
    private JobManager manager;

    private JobValues values;

    private Text textJobName;
    private Text textDescription;
    private Button buttonCreateEnvironment;
    private Text textJobQueueName;
    private Text textJobQueueLibraryName;

    private boolean enableKeyFields;
    private boolean enableFields;

    public static JobMaintenanceDialog getCopyDialog(Shell shell, JobManager manager) {
        return new JobMaintenanceDialog(shell, IMaintenance.MODE_COPY, manager);
    }

    public static JobMaintenanceDialog getChangeDialog(Shell shell, JobManager manager) {
        return new JobMaintenanceDialog(shell, IMaintenance.MODE_CHANGE, manager);
    }

    public static JobMaintenanceDialog getDeleteDialog(Shell shell, JobManager manager) {
        return new JobMaintenanceDialog(shell, IMaintenance.MODE_DELETE, manager);
    }

    public static JobMaintenanceDialog getDisplayDialog(Shell shell, JobManager manager) {
        return new JobMaintenanceDialog(shell, IMaintenance.MODE_DISPLAY, manager);
    }

    public void setValue(JobValues values) {
        this.values = values;
    }

    private JobMaintenanceDialog(Shell shell, String mode, JobManager manager) {
        super(shell);

        this.mode = mode;
        this.manager = manager;

        if (IMaintenance.MODE_CREATE.equals(mode) || IMaintenance.MODE_COPY.equals(mode)) {
            enableKeyFields = true;
            enableFields = true;
        } else if (IMaintenance.MODE_CHANGE.equals(mode)) {
            enableKeyFields = false;
            enableFields = true;
        } else {
            enableKeyFields = false;
            enableFields = false;
        }
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);

        newShell.setText(Messages.DialogTitle_Job);
    }

    @Override
    protected Control createDialogArea(Composite parent) {

        Composite container = (Composite)super.createDialogArea(parent);
        container.setLayout(new GridLayout(2, false));

        WidgetFactory.createDialogSubTitle(container, mode);

        Label labelJobName = new Label(container, SWT.NONE);
        labelJobName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        labelJobName.setText(Messages.Label_Job_colon);
        labelJobName.setToolTipText(Messages.Tooltip_Job);

        textJobName = WidgetFactory.createNameText(container);
        textJobName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textJobName.setToolTipText(Messages.Tooltip_Job);
        textJobName.setEnabled(enableKeyFields);

        Label labelDescription = new Label(container, SWT.NONE);
        labelDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        labelDescription.setText(Messages.Label_Description_colon);
        labelDescription.setToolTipText(Messages.Tooltip_Description);

        textDescription = WidgetFactory.createDescriptionText(container);
        textDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textDescription.setToolTipText(Messages.Tooltip_Description);
        textDescription.setEnabled(enableFields);

        Label labelCreateEnvironment = new Label(container, SWT.NONE);
        labelCreateEnvironment.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        labelCreateEnvironment.setText(Messages.Label_Create_environment_colon);
        labelCreateEnvironment.setToolTipText(Messages.Tooltip_Create_environment);

        buttonCreateEnvironment = WidgetFactory.createCheckbox(container);
        buttonCreateEnvironment.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        buttonCreateEnvironment.setToolTipText(Messages.Tooltip_Create_environment);
        buttonCreateEnvironment.setEnabled(enableFields);

        Label labelJobQueueName = new Label(container, SWT.NONE);
        labelJobQueueName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        labelJobQueueName.setText(Messages.Label_Job_queue_name_colon);
        labelJobQueueName.setToolTipText(Messages.Tooltip_Job_queue_name);

        textJobQueueName = WidgetFactory.createNameText(container);
        textJobQueueName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textJobQueueName.setToolTipText(Messages.Tooltip_Job_queue_name);
        textJobQueueName.setEnabled(enableFields);

        Label labelJobQueueLibraryName = new Label(container, SWT.NONE);
        labelJobQueueLibraryName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        labelJobQueueLibraryName.setText(Messages.Label_Job_queue_library_name_colon);
        labelJobQueueLibraryName.setToolTipText(Messages.Tooltip_Job_queue_library_name);

        textJobQueueLibraryName = WidgetFactory.createNameText(container);
        textJobQueueLibraryName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        textJobQueueLibraryName.setToolTipText(Messages.Tooltip_Job_queue_library_name);
        textJobQueueLibraryName.setEnabled(enableFields);

        createStatusLine(container);

        setScreenValues();

        return container;
    }

    private void setScreenValues() {

        textJobName.setText(values.getKey().getJobName());
        textDescription.setText(values.getDescription());
        buttonCreateEnvironment.setSelection(values.isCreateEnvironment());
        textJobQueueName.setText(values.getJobQueueName());
        textJobQueueLibraryName.setText(values.getJobQueueLibraryName());
    }

    @Override
    protected void okPressed() {

        JobValues newValues = values.clone();
        newValues.getKey().setJobName(textJobName.getText());
        newValues.setDescription(textDescription.getText());
        newValues.setCreateEnvironment(buttonCreateEnvironment.getSelection());
        newValues.setJobQueueName(textJobQueueName.getText());
        newValues.setJobQueueLibraryName(textJobQueueLibraryName.getText());

        if (!IMaintenance.MODE_DISPLAY.equals(mode)) {
            try {
                manager.setValues(newValues);
                Result result = manager.check();
                if (result.isError()) {
                    setErrorFocus(result.getFieldName());
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

    private void setErrorFocus(String fieldName) {

        if (IJobCheck.FIELD_JOB.equals(fieldName)) {
            textJobName.setFocus();
            setErrorMessage(Messages.bind(Messages.Job_name_A_is_not_valid, textJobName.getText()));
        } else if (IJobCheck.FIELD_DESCRIPTION.equals(fieldName)) {
            textDescription.setFocus();
            setErrorMessage(Messages.bind(Messages.Description_A_is_not_valid, textDescription.getText()));
        }
        if (IJobCheck.FIELD_CREATE_ENVIRONMENT.equals(fieldName)) {
            buttonCreateEnvironment.setFocus();
            setErrorMessage(Messages.Create_environment_value_has_been_rejected);
        }
        if (IJobCheck.FIELD_JOB_QUEUE_NAME.equals(fieldName)) {
            textJobQueueName.setFocus();
            setErrorMessage(Messages.bind(Messages.Job_queue_name_A_is_not_valid, textJobQueueName.getText()));
        }
        if (IJobCheck.FIELD_JOB_QUEUE_LIBRARY_NAME.equals(fieldName)) {
            textJobQueueLibraryName.setFocus();
            setErrorMessage(Messages.bind(Messages.Library_name_A_is_not_valid, textJobQueueLibraryName.getText()));
        }
    }

    /**
     * Overridden to make this dialog resizable.
     */
    @Override
    protected boolean isResizable() {
        return true;
    }

    /**
     * Overridden to provide a default size to {@link XDialog}.
     */
    @Override
    protected Point getDefaultSize() {
        return getShell().computeSize(Size.getSize(510), SWT.DEFAULT, true);
    }

    /**
     * Overridden to let {@link XDialog} store the state of this dialog in a
     * separate section of the dialog settings file.
     */
    @Override
    protected IDialogSettings getDialogBoundsSettings() {
        return super.getDialogBoundsSettings(RapidFireCorePlugin.getDefault().getDialogSettings());
    }
}
