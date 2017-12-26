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
import biz.rapidfire.core.maintenance.job.JobValues;
import biz.rapidfire.core.maintenance.wizard.AbstractWizardPage;
import biz.rapidfire.core.validators.Validator;

public class JobPage extends AbstractWizardPage {

    public static final String NAME = "JOB_PAGE"; //$NON-NLS-1$

    private JobValues jobValues;

    private Validator nameValidator;
    private Validator libraryValidator;

    private JobMaintenanceControl jobMaintenanceControl;

    public JobPage(JobValues jobValues) {
        super(NAME);

        this.jobValues = jobValues;

        this.nameValidator = Validator.getNameInstance();
        this.libraryValidator = Validator.getLibraryNameInstance(Validator.LIBRARY_LIBL, Validator.LIBRARY_CURLIB);

        setTitle(Messages.Wizard_Page_Job);
        setDescription(Messages.Wizard_Page_Job_description);
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

    public JobValues getValues() {
        return jobValues;
    }

    @Override
    public void createContent(Composite parent) {

        jobMaintenanceControl = new JobMaintenanceControl(parent, SWT.NONE);
        jobMaintenanceControl.setMode(MaintenanceMode.CREATE);
        jobMaintenanceControl.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1));
    }

    @Override
    protected void setInputData() {

        jobMaintenanceControl.setJobName(jobValues.getKey().getJobName());
        jobMaintenanceControl.setDescription(jobValues.getDescription());
        jobMaintenanceControl.setCreateEnvironment(jobValues.isCreateEnvironment());
        jobMaintenanceControl.setJobQueueName(jobValues.getJobQueueName());
        jobMaintenanceControl.setJobQueueLibraryName(jobValues.getJobQueueLibraryName());

        updatePageEnablement();
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

        jobValues.getKey().setJobName(jobMaintenanceControl.getJobName());
        jobValues.setDescription(jobMaintenanceControl.getDescription());
        jobValues.setCreateEnvironment(jobMaintenanceControl.isCreateEnvironment());
        jobValues.setJobQueueName(jobMaintenanceControl.getJobQueueName());
        jobValues.setJobQueueLibraryName(jobMaintenanceControl.getJobQueueLibraryName());

        updatePageEnablement();
    }

    private void updatePageEnablement() {

        NewJobWizard wizard = (NewJobWizard)getWizard();
        if (jobValues.isCreateEnvironment()) {
            wizard.setPageEnablement(LibraryListPage.NAME, true);
        } else {
            wizard.setPageEnablement(LibraryListPage.NAME, false);
        }
    }

    @Override
    protected void storePreferences() {

        // getPreferences().setConnectionName(connectionName);
        // getPreferences().setRapidFireLibrary(dataLibraryName);
    }
}
