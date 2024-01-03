/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.job.wizard;

import org.eclipse.jface.dialogs.MessageDialog;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.dialogs.MessageDialogAsync;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.maintenance.Result;
import biz.rapidfire.core.maintenance.job.JobManager;
import biz.rapidfire.core.maintenance.job.JobValues;
import biz.rapidfire.core.maintenance.job.shared.JobKey;
import biz.rapidfire.core.maintenance.job.wizard.model.JobWizardDataModel;
import biz.rapidfire.core.maintenance.library.LibraryManager;
import biz.rapidfire.core.maintenance.library.LibraryValues;
import biz.rapidfire.core.maintenance.library.shared.LibraryKey;
import biz.rapidfire.core.maintenance.librarylist.LibraryListManager;
import biz.rapidfire.core.maintenance.librarylist.LibraryListValues;
import biz.rapidfire.core.maintenance.librarylist.shared.LibraryListKey;
import biz.rapidfire.core.maintenance.wizard.AbstractNewWizard;
import biz.rapidfire.core.maintenance.wizard.AbstractWizardPage;
import biz.rapidfire.core.maintenance.wizard.DataLibraryPage;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.dao.IJDBCConnection;
import biz.rapidfire.core.model.dao.JDBCConnectionManager;
import biz.rapidfire.core.preferences.Preferences;
import biz.rapidfire.core.subsystem.IRapidFireSubSystem;
import biz.rapidfire.rsebase.helpers.SystemConnectionHelper;

public class NewJobWizard extends AbstractNewWizard<JobWizardDataModel> {

    public NewJobWizard() {
        super(JobWizardDataModel.createInitialized());

        setWindowTitle(Messages.Wizard_Title_New_Job_wizard);
        setNeedsProgressMonitor(false);
    }

    @Override
    public void addPages() {
        super.addPages(); // Adds the data library page, if necessary

        addPage(new JobPage(model));
        addPage(new LibraryPage(model));
        addPage(new LibraryListPage(model));
    }

    @Override
    protected void updatePageEnablement(AbstractWizardPage page) {

        if (model.isCreateEnvironment()) {
            setPageEnablement(LibraryListPage.NAME, true);
        } else {
            setPageEnablement(LibraryListPage.NAME, false);
        }
    }

    @Override
    public boolean canFinish() {

        for (int i = 0; i < getPageCount(); i++) {
            AbstractWizardPage page = (AbstractWizardPage)getPages()[i];
            if (page.isEnabled() && !page.isPageComplete()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean performFinish() {

        JobManager jobManager = null;
        LibraryManager libraryManager = null;
        LibraryListManager libraryListManager = null;

        IJDBCConnection connection = null;

        try {

            storePreferences();

            Result result;

            result = validateDataLibrary();
            if (result.isError()) {
                setActivePage(getDataLibraryPage());
                displayError(result, DataLibraryPage.NAME);
                return false;
            }

            String connectionName = model.getConnectionName();
            String dataLibrary = model.getDataLibraryName();

            /*
             * Get JDBC connection with manual commit control (auto commit
             * disabled).
             */
            connection = JDBCConnectionManager.getInstance().getConnectionForUpdateNoAutoCommit(connectionName, dataLibrary);

            jobManager = new JobManager(connection);
            libraryManager = new LibraryManager(connection);

            AbstractWizardPage libraryListPage = getLibraryListPage();

            if (libraryListPage.isEnabled()) {
                libraryListManager = new LibraryListManager(connection);
            }

            result = validateJob(jobManager);
            if (result.isError()) {
                setActivePage(getJobPage());
                displayError(result, JobPage.NAME);
                return false;
            }

            result = validateLibrary(libraryManager);
            if (result.isError()) {
                setActivePage(getLibraryPage());
                displayError(result, LibraryPage.NAME);
                return false;
            }

            if (libraryListManager != null) {
                result = validateLibraryList(libraryListManager);
                if (result.isError()) {
                    setActivePage(getLibraryListPage());
                    displayError(result, LibraryPage.NAME);
                    return false;
                }
            }

            jobManager.book();
            libraryManager.book();

            if (libraryListManager != null) {
                libraryListManager.book();
            }

            JDBCConnectionManager.getInstance().commit(connection);

            IRapidFireSubSystem subSystem = (IRapidFireSubSystem)SystemConnectionHelper.getSubSystem(connection.getConnectionName(),
                IRapidFireSubSystem.class);
            if (subSystem != null) {
                IRapidFireJobResource newJob = subSystem.getJob(dataLibrary, model.getJobName(), getShell());
                if (newJob != null) {
                    if (newJob.getParentFilters() != null) {
                        boolean isSlowConnection = Preferences.getInstance().isSlowConnection();
                        // TODO: try to figure out why that does not work
                        SystemConnectionHelper.refreshUICreated(isSlowConnection, subSystem, newJob, newJob.getParentFilters());
                    }
                }
            }

            MessageDialog.openInformation(getShell(), Messages.Wizard_Title_New_Job_wizard,
                Messages.bindParameters(Messages.NewJobWizard_Rapid_Fire_job_A_created, model.getJobName()));

            return true;

        } catch (Exception e) {

            if (connection != null) {
                try {
                    JDBCConnectionManager.getInstance().rollback(connection);
                } catch (Exception e2) {
                    RapidFireCorePlugin.logError("*** Could not rollback connection '" + connection.getConnectionName() + "' ***", e); //$NON-NLS-1$ //$NON-NLS-2$                
                }
            }

            RapidFireCorePlugin.logError("*** Failed to execute Job wizard ***", e); //$NON-NLS-1$
            MessageDialogAsync.displayError(getShell(), ExceptionHelper.getLocalizedMessage(e));

        } finally {

            closeFilesOfManager(jobManager);
            closeFilesOfManager(libraryManager);
            closeFilesOfManager(libraryListManager);
        }

        return false;
    }

    private JobPage getJobPage() {

        JobPage jobPage = (JobPage)getPage(JobPage.NAME);

        return jobPage;
    }

    private LibraryPage getLibraryPage() {

        LibraryPage libraryPage = (LibraryPage)getPage(LibraryPage.NAME);

        return libraryPage;
    }

    private LibraryListPage getLibraryListPage() {

        LibraryListPage libraryListPage = (LibraryListPage)getPage(LibraryListPage.NAME);

        return libraryListPage;
    }

    private JobValues getJobValues() {

        JobValues jobValues = JobValues.createInitialized();
        jobValues.setKey(new JobKey(model.getJobName()));
        jobValues.setDescription(model.getJobDescription());
        jobValues.setCreateEnvironment(model.isCreateEnvironment());
        jobValues.setJobQueueName(model.getJobQueueName());
        jobValues.setJobQueueLibraryName(model.getJobQueueLibraryName());
        jobValues.setCancelASPThresholdExceeds(model.isCancelASPThresholdExceeds());

        return jobValues;
    }

    private LibraryValues getLibraryValues() {

        JobValues jobValues = getJobValues();

        LibraryValues libraryValues = LibraryValues.createInitialized();
        libraryValues.setKey(new LibraryKey(jobValues.getKey(), model.getLibraryName()));
        libraryValues.setShadowLibrary(model.getShadowLibraryName());

        return libraryValues;
    }

    private LibraryListValues getLibraryListValues() {

        JobValues jobValues = getJobValues();

        LibraryListValues libraryListValues = LibraryListValues.createInitialized();
        libraryListValues.setKey(new LibraryListKey(jobValues.getKey(), model.getLibraryListName()));
        libraryListValues.setDescription(model.getLibraryListDescription());
        libraryListValues.setLibraryList(model.getLibraryListEntriesForUI());

        return libraryListValues;
    }

    private Result validateJob(JobManager jobManager) throws Exception {

        JobValues jobValues = getJobValues();

        jobManager.openFiles();
        jobManager.initialize(MaintenanceMode.CREATE, new JobKey(jobValues.getKey().getJobName()));
        jobManager.setValues(jobValues);
        Result result = jobManager.check();

        return result;
    }

    private Result validateLibrary(LibraryManager libraryManager) throws Exception {

        LibraryValues libraryValues = getLibraryValues();

        libraryManager.openFiles();
        libraryManager.initialize(MaintenanceMode.CREATE, new LibraryKey(new JobKey(libraryValues.getKey().getJobName()), libraryValues.getKey()
            .getLibrary()));
        libraryManager.setValues(libraryValues);
        Result result = libraryManager.check();

        return result;
    }

    private Result validateLibraryList(LibraryListManager libraryListManager) throws Exception {

        LibraryListValues libraryListValues = getLibraryListValues();

        libraryListManager.openFiles();
        libraryListManager.initialize(MaintenanceMode.CREATE, new LibraryListKey(new JobKey(libraryListValues.getKey().getJobName()),
            libraryListValues.getKey().getLibraryList()));
        libraryListManager.setValues(libraryListValues);
        Result result = libraryListManager.check();

        return result;
    }
}
