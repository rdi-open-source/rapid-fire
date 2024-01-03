/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.job.wizard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.dialogs.maintenance.job.JobMaintenanceControl;
import biz.rapidfire.core.helpers.StringHelper;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.job.wizard.model.JobWizardDataModel;
import biz.rapidfire.core.maintenance.wizard.AbstractWizardPage;
import biz.rapidfire.core.validators.Validator;

public class JobPage extends AbstractWizardPage {

    public static final String NAME = "JOB_PAGE"; //$NON-NLS-1$

    private JobWizardDataModel model;

    private Validator nameValidator;
    private Validator libraryValidator;

    private JobMaintenanceControl jobMaintenanceControl;

    public JobPage(JobWizardDataModel model) {
        super(NAME);

        this.model = model;

        this.nameValidator = Validator.getNameInstance();
        this.libraryValidator = Validator.getLibraryNameInstance(Validator.LIBRARY_LIBL, Validator.LIBRARY_CURLIB);

        setTitle(Messages.Wizard_Page_Job);

        updateMode();
    }

    @Override
    public void updateMode() {

        setDescription(Messages.Wizard_Page_Job_description);

        if (jobMaintenanceControl != null) {
            jobMaintenanceControl.setMode(MaintenanceMode.CREATE);
        }
    }

    @Override
    public void setFocus() {

        if (StringHelper.isNullOrEmpty(jobMaintenanceControl.getJobName())) {
            jobMaintenanceControl.setFocusJobName();
        } else if (StringHelper.isNullOrEmpty(jobMaintenanceControl.getDescription())) {
            jobMaintenanceControl.setFocusDescription();
        } else if (StringHelper.isNullOrEmpty(jobMaintenanceControl.getJobQueueName())) {
            jobMaintenanceControl.setFocusJobQueueName();
        } else if (StringHelper.isNullOrEmpty(jobMaintenanceControl.getJobQueueLibraryName())) {
            jobMaintenanceControl.setFocusJobQueueLibraryName();
        } else {
            jobMaintenanceControl.setFocusJobName();
        }
    }

    @Override
    public void createContent(Composite parent) {

        jobMaintenanceControl = new JobMaintenanceControl(parent, SWT.NONE);
        jobMaintenanceControl.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1));

        updateMode();
    }

    @Override
    protected void setInputData() {

        jobMaintenanceControl.setJobName(model.getJobName());
        jobMaintenanceControl.setDescription(model.getJobDescription());
        jobMaintenanceControl.setCreateEnvironment(model.isCreateEnvironment());
        jobMaintenanceControl.setJobQueueName(model.getJobQueueName());
        jobMaintenanceControl.setJobQueueLibraryName(model.getJobQueueLibraryName());
        jobMaintenanceControl.setCancelASPThresholdExceeds(model.isCancelASPThresholdExceeds());
    }

    @Override
    protected void addControlListeners() {

        jobMaintenanceControl.addModifyListener(this);
        jobMaintenanceControl.addSelectionListener(this);
    }

    @Override
    protected void updatePageComplete(Object source) {

        String message = null;

        if (!nameValidator.validate(jobMaintenanceControl.getJobName())) {
            // jobMaintenanceControl.setFocusJobName();
            message = Messages.bindParameters(Messages.Job_name_A_is_not_valid, jobMaintenanceControl.getJobName());
        } else if (StringHelper.isNullOrEmpty(jobMaintenanceControl.getDescription())) {
            // jobMaintenanceControl.setFocusDescription();
            message = Messages.bind(Messages.Description_A_is_not_valid, jobMaintenanceControl.getDescription());
        } else if (!nameValidator.validate(jobMaintenanceControl.getJobQueueName())) {
            // jobMaintenanceControl.setFocusJobQueueName();
            message = Messages.bindParameters(Messages.Job_queue_name_A_is_not_valid, jobMaintenanceControl.getJobQueueName());
        } else if (!libraryValidator.validate(jobMaintenanceControl.getJobQueueLibraryName())) {
            // jobMaintenanceControl.setFocusJobQueueLibraryName();
            message = Messages.bind(Messages.Library_name_A_is_not_valid, jobMaintenanceControl.getJobQueueLibraryName());
        }

        updateValues();

        if (message == null) {
            setPageComplete(true);
        } else {
            setPageComplete(false);
        }

        setErrorMessage(message);
    }

    private void updateValues() {

        model.setJobName(jobMaintenanceControl.getJobName());
        model.setJobDescription(jobMaintenanceControl.getDescription());
        model.setCreateEnvironment(jobMaintenanceControl.isCreateEnvironment());
        model.setJobQueueName(jobMaintenanceControl.getJobQueueName());
        model.setJobQueueLibraryName(jobMaintenanceControl.getJobQueueLibraryName());
        model.setCancelASPThresholdExceeds(jobMaintenanceControl.isCancelASPThresholdExceeds());
    }

    @Override
    protected void storePreferences() {
    }
}
