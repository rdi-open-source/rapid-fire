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

public class JobPage extends AbstractWizardPage {

    public static final String NAME = "JOB_PAGE"; //$NON-NLS-1$

    private JobValues jobValues;

    private JobMaintenanceControl jobMaintenanceControl;

    public JobPage(JobValues jobValues) {
        super(NAME);

        this.jobValues = jobValues;

        setTitle(Messages.Wizard_Page_Job);
        setDescription(Messages.Wizard_Page_Job_description);
    }

    public JobValues getValues() {
        return jobValues;
    }

    public void createContent(Composite parent) {

        jobMaintenanceControl = new JobMaintenanceControl(parent, SWT.NONE);
        jobMaintenanceControl.addModifyListener(this);
        jobMaintenanceControl.addSelectionListener(this);
        jobMaintenanceControl.setMode(MaintenanceMode.CREATE);
        jobMaintenanceControl.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1));

        jobMaintenanceControl.setJobName(jobValues.getKey().getJobName());
        jobMaintenanceControl.setDescription(jobValues.getDescription());
        jobMaintenanceControl.setCreateEnvironment(jobValues.isCreateEnvironment());
        jobMaintenanceControl.setJobQueueName(jobValues.getJobQueueName());
        jobMaintenanceControl.setJobQueueLibraryName(jobValues.getJobQueueLibraryName());

        // updateSkipLibraryListPage();
    }

    protected void updatePageComplete() {

        String message = null;

        if (StringHelper.isNullOrEmpty(jobMaintenanceControl.getJobName())) {
            // jobMaintenanceControl.setFocusJobName();
            message = Messages.bind(Messages.Job_name_A_is_not_valid, jobMaintenanceControl.getJobName());
        } else if (StringHelper.isNullOrEmpty(jobMaintenanceControl.getDescription())) {
            // jobMaintenanceControl.setFocusDescription();
            message = Messages.bind(Messages.Description_A_is_not_valid, jobMaintenanceControl.getDescription());
        } else if (StringHelper.isNullOrEmpty(jobMaintenanceControl.getJobQueueName())) {
            // jobMaintenanceControl.setFocusJobQueueName();
            message = Messages.bind(Messages.Job_queue_name_A_is_not_valid, jobMaintenanceControl.getJobQueueName());
        } else if (StringHelper.isNullOrEmpty(jobMaintenanceControl.getJobQueueLibraryName())) {
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

        updateSkipLibraryListPage();
    }

    private void updateSkipLibraryListPage() {

        NewJobWizard wizard = (NewJobWizard)getWizard();
        wizard.setSkipLibraryListPage(!jobValues.isCreateEnvironment());
    }
}
